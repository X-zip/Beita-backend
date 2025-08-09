package com.example.demo.service.impl;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.dao.TaskDao;
import com.example.demo.service.BeitaService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.SearchRequest;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
@Service(value = "beitaService")
public class Impl implements BeitaService{
	@Autowired
	TaskDao taskDao;

	@Autowired
	private Index index;
	// 自动注入Client（如需使用）
	@Autowired
	private Client client;
	// 初始化状态标志（线程安全）
	private final AtomicBoolean isInitialized = new AtomicBoolean(false);
	// 初始化失败标志
	private final AtomicBoolean initFailed = new AtomicBoolean(false);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Async("meilisearchExecutor")  // 指定专用线程池
	public void initMeilisearch() {
		// 避免重复初始化
		if (isInitialized.get()) {
			return;
		}

		try {
			System.out.println("开始Meilisearch初始化，线程: " + Thread.currentThread().getName());

			// 1. 配置索引
			configureIndex();

			// 2. 批量导入数据（降低批次大小适配4G内存）
			int totalCount = getTaskCount();
			int batchSize = 5000;  // 4G内存建议减小批次
			int offset = 0;

			while (offset < totalCount && !Thread.currentThread().isInterrupted()) {
				int currentBatchSize = Math.min(batchSize, totalCount - offset);
				try {
					List<Task> tasks = getallTaskbyBatch(offset, currentBatchSize);
					List<Map<String, Object>> documents = tasks.stream()
							.map(this::convertTaskToDocument)
							.collect(Collectors.toList());

					String doc = listMapToJson(documents);
					index.addDocuments(doc, "id");
					System.out.println("同步完成：" + offset + "-" + (offset + currentBatchSize) +
							"，进度：" + ((offset + currentBatchSize) * 100 / totalCount) + "%");

					offset += currentBatchSize;

					// 每批处理后短暂休眠，降低CPU占用
					Thread.sleep(100);
				} catch (Exception e) {
					System.err.println("批次处理失败（offset=" + offset + "）：" + e.getMessage());
					// 重试当前批次（最多3次）
					for (int retry = 1; retry <= 3; retry++) {
						try {
							Thread.sleep(1000 * retry);  // 指数退避
							// 重新获取数据并重试
							List<Task> tasks = getallTaskbyBatch(offset, currentBatchSize);
							List<Map<String, Object>> documents = tasks.stream()
									.map(this::convertTaskToDocument)
									.collect(Collectors.toList());
							index.addDocuments(listMapToJson(documents), "id");
							offset += currentBatchSize;
							break;
						} catch (Exception ex) {
							if (retry == 3) throw ex;  // 最后一次重试失败则抛出
						}
					}
				}
			}

			// 标记初始化完成
			isInitialized.set(true);
			System.out.println("Meilisearch初始化完成，共导入" + totalCount + "条数据");

		} catch (Exception e) {
			System.err.println("Meilisearch初始化失败：" + e.getMessage());
			initFailed.set(true);  // 标记初始化失败
		}
	}
	private void configureIndex() {
		try {
			String[] searchableAttributes = new String[]{"content", "title"};
			index.updateSearchableAttributesSettings(searchableAttributes);

			String[] sortableAttributes = new String[]{"c_time"};
			index.updateSortableAttributesSettings(sortableAttributes);

			String[] rankingRules = new String[]{
					"exactness", "words", "proximity", "c_time:desc", "typo"
			};
			index.updateRankingRulesSettings(rankingRules);
		} catch (Exception e) {
			throw new RuntimeException("索引配置失败", e);
		}
	}
	public String listMapToJson(List<Map<String, Object>> data) throws JsonProcessingException {
		return objectMapper.writeValueAsString(data);
	}
	private Map<String, Object> convertTaskToDocument(Task task) {
		Map<String, Object> doc = new HashMap<>();
		doc.put("id", task.getId());
		doc.put("ip", task.getIp());
		doc.put("content", task.getContent());
		doc.put("price", task.getPrice());
		doc.put("title", task.getTitle());
		doc.put("wechat", task.getWechat());
		doc.put("openid", task.getOpenid());
		doc.put("avatar", task.getAvatar());
		doc.put("campusGroup", task.getCampusGroup());
		doc.put("commentNum", task.getCommentNum());
		doc.put("watchNum", task.getWatchNum());
		doc.put("likeNum", task.getLikeNum());
		doc.put("radioGroup", task.getRadioGroup());
		doc.put("img", task.getImg());
		doc.put("cover", task.getCover());
		doc.put("is_delete", task.getIs_delete());
		doc.put("is_complaint", task.getIs_complaint());
		doc.put("region", task.getRegion());
		doc.put("userName", task.getUserName());
		doc.put("c_time", task.getC_time());
		doc.put("comment_time", task.getComment_time());
		doc.put("choose", task.getChoose());
		doc.put("hot", task.getHot());
		return doc;
	}
	@Override
	public List<Task> getallTaskbyBatch(int start, int limit) {
		// TODO Auto-generated method stub
		return taskDao.getallTaskbyBatch(start, limit);
	}
	public int getTaskCount(){
		return taskDao.getTaskCount();
	}
	@Override
	public List<Task> getallTask(int length) {
		return getallTask(length); // 调用原方法，传入默认limit
	}
	@Override
	public List<Task> getHotTask(int length) {
		// TODO Auto-generated method stub
		return taskDao.getHotTask(length);
	}

	@Override
	public  List<Task> gettaskbyId(int Id) {
		// TODO Auto-generated method stub
		return taskDao.gettaskbyId(Id);
	}

	@Override
	public  List<Task> gettaskbyOpenId(String openid,int length) {
		// TODO Auto-generated method stub
		return taskDao.gettaskbyOpenId(openid,length);
	}
	@Override
	public List<Task> gettaskbySearch(String search, int length) {
		if (!isInitialized.get() || initFailed.get()) {
			System.out.println("初始化未完成或失败时使用原有实现");
			return taskDao.gettaskbySearch(search, length);
		}
		System.out.println("初始化完成后使用新实现");
		return getTaskByMeiliSearch(search, length);
	}
	private List<Task> getTaskByMeiliSearch(String search, int length) {
		try {
			SearchRequest searchRequest = SearchRequest.builder()
					.q(search)
					.offset(length)
					.limit(20)
					.build();

			Searchable searchResult = index.search(searchRequest);
			return objectMapper.convertValue(
					searchResult.getHits(),
					new TypeReference<List<Task>>() {}
			);
		} catch (Exception e) {
			System.err.println("Meilisearch搜索失败，降级使用DAO：" + e.getMessage());
			return taskDao.gettaskbySearch(search, length);  // 搜索失败时降级
		}
	}

	@Override
	public int addTask(Task task) {
		// TODO Auto-generated method stub
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
		// 转换为数组并添加到Meilisearch
		List<Map<String, Object>> documents = tasks.stream()
				.map(this::convertTaskToDocument)
				.collect(Collectors.toList());
		// 尝试转换为JSON
		try{
			String doc = listMapToJson(documents);
			index.addDocuments(doc,"id");
			System.out.println("新增一条数据");
		} catch (JsonProcessingException e) {
			// 异常处理逻辑（根据业务需求调整）
			System.err.println("JSON转换失败：" + e.getMessage());
			e.printStackTrace(); // 打印堆栈信息便于调试
		}
		return taskDao.addTask(task);
	}

	@Override
	public  List<Task> gettaskbyRadio(String radioGroup,int length) {
		// TODO Auto-generated method stub
		return taskDao.gettaskbyRadio(radioGroup,length);
	}

	@Override
	public  List<Task> gettaskbyRadioSecond(List<String> radioGroup,int length) {
		// TODO Auto-generated method stub
		return taskDao.gettaskbyRadioSecond(radioGroup,length);
	}

	@Override
	public  List<Task> gettaskbyType(List<String> radioGroup,String type,int length) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return taskDao.gettaskbyCtime(radioGroup,length);
		} else if (type.equals("1")) {
			return taskDao.gettaskbyComment(radioGroup,length);
		} else if (type.equals("2")) {
			return taskDao.gettaskbyHot(radioGroup,length);
		} else if (type.equals("3")) {
			return taskDao.gettaskbyChoose(radioGroup,length);
		} else if (type.equals("4")) {
			return taskDao.gettaskbyRadioSecond(radioGroup,length);
		} else if (type.equals("5")) {
			return taskDao.gettaskbyZGC(radioGroup,length);
		} else if (type.equals("6")) {
			return taskDao.gettaskbyLX(radioGroup,length);
		} else if (type.equals("7")) {
			return taskDao.gettaskbyZH(radioGroup,length);
		} else{
			return taskDao.gettaskbyRadioSecond(radioGroup,length);
		}

	}

	@Override
	public  List<Task> gettaskbyTypeCampus(List<String> radioGroup,String type,int length, String campus) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return taskDao.gettaskbyCtimeCampus(radioGroup,length,campus);
		} else if (type.equals("1")) {
			return taskDao.gettaskbyCommentCampus(radioGroup,length,campus);
		} else if (type.equals("2")) {
			return taskDao.gettaskbyHotCampus(radioGroup,length,campus);
		} else if (type.equals("3")) {
			return taskDao.gettaskbyChooseCampus(radioGroup,length,campus);
		} else {
			return taskDao.gettaskbyRadioSecondCampus(radioGroup,length,campus);
		}
	}

	@Override
	public  List<Task> gettaskbyRadioSecondForWX(String radioGroup,int length) {
		// TODO Auto-generated method stub
		return taskDao.gettaskbyRadioSecondForWX(radioGroup,length);
	}

	@Override
	public  int upDateTask(String c_time,int Id) {
		// TODO Auto-generated method stub
		return taskDao.upDateTask(c_time,Id);
	}

	@Override
	public  int incWatch(int Id) {
		// TODO Auto-generated method stub
		return taskDao.incWatch(Id);
	}

	@Override
	public  int incLike(int Id) {
		// TODO Auto-generated method stub
		return taskDao.incLike(Id);
	}

	@Override
	public  int incCommentLike(int Id) {
		// TODO Auto-generated method stub
		return taskDao.incCommentLike(Id);
	}

	@Override
	public  int decCommentLike(int Id) {
		// TODO Auto-generated method stub
		return taskDao.decCommentLike(Id);
	}


	@Override
	public  int incComment(int Id) {
		// TODO Auto-generated method stub
		return taskDao.incComment(Id);
	}

	@Override
	public  int decLike(int Id) {
		// TODO Auto-generated method stub
		return taskDao.decLike(Id);
	}

	@Override
	public int deleteTask(int Id) {
		// TODO Auto-generated method stub
		// 同步在索引中删除
		index.deleteDocument(String.valueOf(Id));
		return taskDao.deleteTask(Id);
	}

	@Override
	public int recoverTask(int Id) {
		// TODO Auto-generated method stub
		// 恢复等价于在meilisearch新增这个帖子
		int ret = taskDao.recoverTask(Id);
		List<Task> recover_task = gettaskbyId(Id);
		List<Map<String, Object>> documents = recover_task.stream()
				.map(this::convertTaskToDocument)
				.collect(Collectors.toList());
		// 尝试转换为JSON
		try{
			String doc = listMapToJson(documents);
			index.addDocuments(doc,"id");
			System.out.println("恢复一条被删除数据");
		} catch (JsonProcessingException e) {
			// 异常处理逻辑（根据业务需求调整）
			System.err.println("JSON转换失败：" + e.getMessage());
			e.printStackTrace(); // 打印堆栈信息便于调试
		}
		return ret;
	}

	@Override
	public int hideTask(int Id) {
		// TODO Auto-generated method stub
		index.deleteDocument(String.valueOf(Id));
		return taskDao.hideTask(Id);
	}

	@Override
	public int recoverTaskHide(int Id) {
		// TODO Auto-generated method stub
		int ret = taskDao.recoverTaskHide(Id);
		List<Task> recover_task = gettaskbyId(Id);
		List<Map<String, Object>> documents = recover_task.stream()
				.map(this::convertTaskToDocument)
				.collect(Collectors.toList());
		// 尝试转换为JSON
		try{
			String doc = listMapToJson(documents);
			index.addDocuments(doc,"id");
			System.out.println("恢复一条被举报的数据");
		} catch (JsonProcessingException e) {
			// 异常处理逻辑（根据业务需求调整）
			System.err.println("JSON转换失败：" + e.getMessage());
			e.printStackTrace(); // 打印堆栈信息便于调试
		}
		return ret;
	}

	@Override
	public int topTask(int Id) {
		// TODO Auto-generated method stub
		return taskDao.topTask(Id);
	}

	@Override
	public int downTask(int Id) {
		// TODO Auto-generated method stub
		return taskDao.downTask(Id);
	}

	@Override
	public int addLike(Like like) {
		// TODO Auto-generated method stub
		return taskDao.addLike(like);
	}

	@Override
	public int addComment(Comment comment) {
		// TODO Auto-generated method stub
		return taskDao.addComment(comment) + taskDao.upDateTaskCommentTime(comment);
	}

	@Override
	public  List<Like> getlikeByPk(String openid,int pk) {
		// TODO Auto-generated method stub
		return taskDao.getlikeByPk(openid,pk);
	}

	@Override
	public  List<Comment> getCommentByPk(int pk) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByPk(pk);
	}

	@Override
	public  List<Comment> getCommentByLength(int pk,int length) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByLength(pk,length);
	}

	@Override
	public  List<Comment> getCommentByType(int pk,int length,String type) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return taskDao.getCommentByLength(pk,length);
		} else if (type.equals("1")) {
			return taskDao.getCommentByReverse(pk,length);
		} else {
			return taskDao.getCommentByHot(pk,length);
		}

	}

	@Override
	public  List<Comment> getCommentByPid(int pid) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByPid(pid);
	}

	@Override
	public  List<Comment> getFirstCommentByPid(int pid) {
		// TODO Auto-generated method stub
		return taskDao.getFirstCommentByPid(pid);
	}

	@Override
	public  List<Comment> getCommentByIdList(List<String> str) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByIdList(str);
	}

	@Override
	public int deleteLike(int Id) {
		// TODO Auto-generated method stub
		return taskDao.deleteLike(Id);
	}

	@Override
	public  List<Like> getlikeByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return taskDao.getlikeByOpenid(openid,length);
	}
	@Override
	public int deleteComment(int Id) {
		// TODO Auto-generated method stub
		return taskDao.deleteComment(Id);
	}
	@Override
	public  List<Comment> getCommentByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByOpenid(openid,length);
	}

	@Override
	public  List<Comment> getCommentByApplyto(String applyTo,int length) {
		// TODO Auto-generated method stub
		return taskDao.getCommentByApplyto(applyTo,length);
	}

	@Override
	public  List<Member> getMember(String openid) {
		// TODO Auto-generated method stub
		return taskDao.getMember(openid);
	}

	@Override
	public int addMember(Member member) {
		// TODO Auto-generated method stub
		return taskDao.addMember(member);
	}

	@Override
	public  List<Member> getAllMember() {
		// TODO Auto-generated method stub
		return taskDao.getAllMember();
	}

	@Override
	public  int upDateWatch(int watchNum,int Id) {
		// TODO Auto-generated method stub
		return taskDao.upDateWatch(watchNum,Id);
	}

	@Override
	public  int updateChoose(int Id,int choose) {
		// TODO Auto-generated method stub
		return taskDao.updateChoose(Id,choose);
	}

	@Override
	public  List<Comment> getAllComment(int length) {
		// TODO Auto-generated method stub
		return taskDao.getAllComment(length);
	}


	@Override
	public  List<Banner> getBanner() {
		// TODO Auto-generated method stub
		return taskDao.getBanner();
	}

	@Override
	public  List<Banner> getBanner2() {
		// TODO Auto-generated method stub
		return taskDao.getBanner2();
	}

	@Override
	public int deleteBanner(int Id) {
		// TODO Auto-generated method stub
		return taskDao.deleteBanner(Id);
	}

	@Override
	public int addBanner(String imgPath,String url,String weight) {
		// TODO Auto-generated method stub
		return taskDao.addBanner(imgPath,url,weight);
	}

	@Override
	public  List<BlackList> checkBlackList(String openid) {
		// TODO Auto-generated method stub
		return taskDao.checkBlackList(openid);
	}

	@Override
	public  List<BlackList> getBlackList(int length) {
		// TODO Auto-generated method stub
		return taskDao.getBlackList(length);
	}

	@Override
	public  List<Task> getOpenidbySearch(String search) {
		// TODO Auto-generated method stub
		return taskDao.getOpenidbySearch(search);
	}

	@Override
	public  List<Task> getAllOpenid(String search) {
		// TODO Auto-generated method stub
		return taskDao.getAllOpenid(search);
	}


	@Override
	public  List<Comment> getOpenidBySearchComment(String search) {
		// TODO Auto-generated method stub
		return taskDao.getOpenidBySearchComment(search);
	}

	@Override
	public  List<Comment> getOpenidBySearchAllComment(String search) {
		// TODO Auto-generated method stub
		return taskDao.getOpenidBySearchAllComment(search);
	}

	@Override
	public int addBlacklist(BlackList blacklist) {
		// TODO Auto-generated method stub
		return taskDao.addBlacklist(blacklist);
	}

	@Override
	public int deleteBlacklist(int Id) {
		// TODO Auto-generated method stub
		return taskDao.deleteBlacklist(Id);
	}

	@Override
	public int addSuggestion(Suggestion suggestion) {
		// TODO Auto-generated method stub
		return taskDao.addSuggestion(suggestion);
	}

	@Override
	public  List<Suggestion> getSuggestionByPk(int id) {
		// TODO Auto-generated method stub
		return taskDao.getSuggestionByPk(id);
	}

	@Override
	public  List<Suggestion> getSuggestion() {
		// TODO Auto-generated method stub
		return taskDao.getSuggestion();
	}

	@Override
	public List<BitRank> getRankList(int length) {
		// TODO Auto-generated method stub
		return taskDao.getRankList(length);
	}

	@Override
	public List<BitRank> getRankListByOpenid(String openid) {
		// TODO Auto-generated method stub
		return taskDao.getRankListByOpenid(openid);
	}

	@Override
	public int addRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return taskDao.addRank(bitrank);
	}

	@Override
	public  int updateRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return taskDao.updateRank(bitrank);
	}

	@Override
	public  List<Task> getChooseBySearch(String search) {
		// TODO Auto-generated method stub
		return taskDao.getChooseBySearch(search);
	}

	@Override
	public  List<AccessCode> getCodeCtime(Long c_time) {
		// TODO Auto-generated method stub
		return taskDao.getCodeCtime(c_time);
	}

	@Override
	public  int saveCode(String code,Long c_time) {
		// TODO Auto-generated method stub
		return taskDao.saveCode(code,c_time);
	}

//	@Override
//	public int downBannerTask(int Id) {
//		// TODO Auto-generated method stub
//		return taskDao.deleteBanner(Id);
//	}
//
//	@Override
//	public int addbannerTask(String imgPath,String url,String campus,String weight) {
//		// TODO Auto-generated method stub
//
//		return taskDao.addBanner(page,imgPath,url,campus,weight);
//	}
}