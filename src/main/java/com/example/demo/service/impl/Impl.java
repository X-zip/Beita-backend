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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.TaskInfo;
import javax.annotation.PostConstruct;  // 需导入该注解
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
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@PostConstruct
	public void initMeilisearch() {
		// 设置搜索字段为title和content
		String[] SearchableAttributes = new String[]{"content", "title"};
		String[] filterableAttributes = new String[]{"is_delete", "is_complaint"};
		index.updateFilterableAttributesSettings(filterableAttributes);
		index.updateSearchableAttributesSettings(SearchableAttributes);

		// 3. 等待任务完成（关键步骤）
		// 参数：任务ID、超时时间（毫秒）、轮询间隔（毫秒）
		int TOTAL_COUNT = getTaskCount();
		int batchSize = 10000;
		int offset = 0; // 起始位置，从0开始
		TOTAL_COUNT = 10;
		while (offset < TOTAL_COUNT) {
			// 计算当前批次的实际大小（最后一批可能不足BATCH_SIZE）
			int currentBatchSize = Math.min(batchSize, TOTAL_COUNT - offset);

			try {
				// 1. 获取当前批次数据
				List<Task> tasks = getallTaskbyBatch(offset, currentBatchSize);
				// 2. 转换为数组并添加到Meilisearch（Client会自动序列化字段）
				List<Map<String, Object>> documents = tasks.stream()
						.map(this::convertTaskToDocument)
						.collect(Collectors.toList());
				// 批量添加文档
				try {
					// 尝试转换为JSON
					String doc = listMapToJson(documents);
					index.addDocuments(doc,"id");
					System.out.println("初始数据同步完成，共同步" + offset +'-'+ (offset+currentBatchSize)+ "条任务");
				} catch (JsonProcessingException e) {
					// 异常处理逻辑（根据业务需求调整）
					System.err.println("JSON转换失败：" + e.getMessage());
					e.printStackTrace(); // 打印堆栈信息便于调试
				}

				// 3. 更新起始位置（准备取下一批）
				offset += currentBatchSize;

			} catch (Exception e) {
				// 异常处理：如重试当前批次、记录错误日志
				System.err.println("处理批次失败（offset=" + offset + "）：" + e.getMessage());
				// 可添加重试逻辑，避免单个批次失败导致整体中断
				// retryCurrentBatch(offset, currentBatchSize);
			}
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
	public  List<Task> gettaskbySearch(String search,int length) {
		// TODO Auto-generated method stub
		SearchRequest searchRequest = SearchRequest.builder()
				.q(search)
				.offset(length)
				.limit(20)
				.filter(new String[]{"is_delete=0", "is_complaint=0"})	//设置过滤器，只显示未被删除的结果；保留数据恢复的可能性
				.build();
		// 5. 执行搜索（传入SearchRequest）
		Searchable searchResult = index.search(searchRequest);
		// 3. 解析searchResult为List<Task>
		ObjectMapper objectMapper = new ObjectMapper();

		// 3.1 从搜索结果中获取hits（匹配的文档数组，Meilisearch默认字段为"hits"）
		// 注意：需根据实际返回的JSON结构调整字段名，通常为"hits"
		Object hits = searchResult.getHits(); // 假设Searchable有getHits()方法

		// 3.2 将hits转换为List<Task>
		List<Task> tasks = objectMapper.convertValue(
				hits,
				new TypeReference<List<Task>>() {} // 指定目标类型为List<Task>
		);

		return tasks;
//		return taskDao.gettaskbySearch(search,length);
	}

	@Override
	public int addTask(Task task) {
		// TODO Auto-generated method stub
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
		// 2. 转换为数组并添加到Meilisearch（Client会自动序列化字段）
		List<Map<String, Object>> documents = tasks.stream()
				.map(this::convertTaskToDocument)
				.collect(Collectors.toList());
		// 批量添加文档
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
		return taskDao.deleteTask(Id);
	}
	
	@Override
	public int recoverTask(int Id) {
		// TODO Auto-generated method stub
		return taskDao.recoverTask(Id);
	}
	
	@Override
	public int hideTask(int Id) {
		// TODO Auto-generated method stub
		return taskDao.hideTask(Id);
	}
	
	@Override
	public int recoverTaskHide(int Id) {
		// TODO Auto-generated method stub
		return taskDao.recoverTaskHide(Id);
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
