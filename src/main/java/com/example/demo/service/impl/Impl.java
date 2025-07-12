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

@Service(value = "beitaService")
public class Impl implements BeitaService{
	@Autowired
	TaskDao taskDao;
	@Override
	public List<Task> getallTask(int length) {
		// TODO Auto-generated method stub
		return taskDao.getallTask(length);
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
		return taskDao.gettaskbySearch(search,length);
	}

	@Override
	public int addTask(Task task) {
		// TODO Auto-generated method stub
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
