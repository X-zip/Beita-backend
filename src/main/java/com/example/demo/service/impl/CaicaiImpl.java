package com.example.demo.service.impl;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Pr;
import com.example.demo.model.QR;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.dao.CaicaiDao;
import com.example.demo.service.CaicaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service(value = "caicaiService")
public class CaicaiImpl implements CaicaiService{
	@Autowired
	CaicaiDao caicaiDao;
	@Override
	public List<Task> getallTask(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getallTask(length);
	}
	
	@Override
	public List<Task> getHotTask(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getHotTask(length);
	}

	@Override
	public  List<Task> gettaskbyId(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbyId(Id);
	}
	
	@Override
	public  List<Task> gettaskbyOpenId(String openid,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbyOpenId(openid,length);
	}
	
	@Override
	public  List<Task> gettaskbySearch(String search,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbySearch(search,length);
	}

	@Override
	public int addTask(Task task) {
		// TODO Auto-generated method stub
		return caicaiDao.addTask(task);
	}
	
	@Override
	public int addTaskB(Task task) {
		// TODO Auto-generated method stub
		return caicaiDao.addTaskB(task);
	}

	@Override
	public  List<Task> gettaskbyRadio(String radioGroup,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbyRadio(radioGroup,length);
	}
	
	@Override
	public  List<Task> gettaskbyRadioSecond(List<String> radioGroup,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbyRadioSecond(radioGroup,length);
	}
	
	@Override
	public  List<Task> gettaskbyType(List<String> radioGroup,String type,int length) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return caicaiDao.gettaskbyCtime(radioGroup,length);
		} else if (type.equals("1")) {
			return caicaiDao.gettaskbyComment(radioGroup,length);
		} else if (type.equals("2")) {
			return caicaiDao.gettaskbyHot(radioGroup,length);
		} else if (type.equals("3")) {
			return caicaiDao.gettaskbyChoose(radioGroup,length);
		} else if (type.equals("4")) {
			return caicaiDao.gettaskbyRadioSecond(radioGroup,length);
		} else if (type.equals("5")) {
			return caicaiDao.gettaskbyNUS(radioGroup,length);
		} else if (type.equals("6")) {
			return caicaiDao.gettaskbyNTU(radioGroup,length);
		} else if (type.equals("7")) {
			return caicaiDao.gettaskbySMU(radioGroup,length);
		} else if (type.equals("8")) {
			return caicaiDao.gettaskbySIT(radioGroup,length);
		} else {			
			return caicaiDao.gettaskbyRadioSecond(radioGroup,length);
		}
		
	}
	
	@Override
	public  List<Task> gettaskbyTypeCampus(List<String> radioGroup,String type,int length, String campus) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return caicaiDao.gettaskbyCtimeCampus(radioGroup,length,campus);
		} else if (type.equals("1")) {
			return caicaiDao.gettaskbyCommentCampus(radioGroup,length,campus);
		} else if (type.equals("2")) {
			return caicaiDao.gettaskbyHotCampus(radioGroup,length,campus);
		} else if (type.equals("3")) {
			return caicaiDao.gettaskbyChooseCampus(radioGroup,length,campus);
		} else {			
			return caicaiDao.gettaskbyRadioSecondCampus(radioGroup,length,campus);
		}
		
	}
	
	@Override
	public  List<Task> gettaskbyRadioSecondForWX(String radioGroup,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.gettaskbyRadioSecondForWX(radioGroup,length);
	}
	
	@Override
	public  int upDateTask(String c_time,int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.upDateTask(c_time,Id);
	}
	
	@Override
	public  int incWatch(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.incWatch(Id);
	}
	
	@Override
	public  int incLike(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.incLike(Id);
	}
	
	@Override
	public  int incCommentLike(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.incCommentLike(Id);
	}
	
	@Override
	public  int decCommentLike(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.decCommentLike(Id);
	}
	
	
	@Override
	public  int incComment(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.incComment(Id);
	}
	
	@Override
	public  int decLike(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.decLike(Id);
	}

	@Override
	public int deleteTask(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.deleteTask(Id);
	}
	
	@Override
	public int recoverTask(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.recoverTask(Id);
	}
	
	@Override
	public int hideTask(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.hideTask(Id);
	}
	
	@Override
	public int recoverTaskHide(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.recoverTaskHide(Id);
	}
	
	@Override
	public int topTask(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.topTask(Id);
	}
	
	@Override
	public int downTask(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.downTask(Id);
	}
	
	@Override
	public int addLike(Like like) {
		// TODO Auto-generated method stub
		return caicaiDao.addLike(like);
	}
	
	@Override
	public int addComment(Comment comment) {
		// TODO Auto-generated method stub
		return caicaiDao.addComment(comment) + caicaiDao.upDateTaskCommentTime(comment);
	}
	
	@Override
	public  List<Like> getlikeByPk(String openid,int pk) {
		// TODO Auto-generated method stub
		return caicaiDao.getlikeByPk(openid,pk);
	}
	
	@Override
	public  List<Comment> getCommentByPk(int pk) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByPk(pk);
	}
	
	@Override
	public  List<Comment> getCommentByLength(int pk,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByLength(pk,length);
	}
	
	@Override
	public  List<Comment> getCommentByType(int pk,int length,String type) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return caicaiDao.getCommentByLength(pk,length);
		} else if (type.equals("1")) {
			return caicaiDao.getCommentByReverse(pk,length);
		} else {
			return caicaiDao.getCommentByHot(pk,length);
		}
		
	}
	
	@Override
	public  List<Comment> getCommentByPid(int pid) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByPid(pid);
	}
	
	@Override
	public  List<Comment> getCommentByIdList(List<String> str) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByIdList(str);
	}
	
	@Override
	public int deleteLike(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.deleteLike(Id);
	}
	
	@Override
	public  List<Like> getlikeByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getlikeByOpenid(openid,length);
	}
	@Override
	public int deleteComment(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.deleteComment(Id);
	}
	@Override
	public  List<Comment> getCommentByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByOpenid(openid,length);
	}
	
	@Override
	public  List<Comment> getCommentByApplyto(String applyTo,int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getCommentByApplyto(applyTo,length);
	}
	
	@Override
	public  List<Member> getMember(String openid) {
		// TODO Auto-generated method stub
		return caicaiDao.getMember(openid);
	}
	
	@Override
	public int addMember(Member member) {
		// TODO Auto-generated method stub
		return caicaiDao.addMember(member);
	}
	
	@Override
	public  List<Member> getAllMember() {
		// TODO Auto-generated method stub
		return caicaiDao.getAllMember();
	}
	
	@Override
	public  int upDateWatch(int watchNum,int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.upDateWatch(watchNum,Id);
	}
	
	@Override
	public  int updateChoose(int Id,int choose) {
		// TODO Auto-generated method stub
		return caicaiDao.updateChoose(Id,choose);
	}
	
	@Override
	public  List<Comment> getAllComment(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getAllComment(length);
	}
	

	@Override
	public  List<Banner> getBanner() {
		// TODO Auto-generated method stub
		return caicaiDao.getBanner();
	}
	
	@Override
	public  List<Banner> getBanner2() {
		// TODO Auto-generated method stub
		return caicaiDao.getBanner2();
	}
	
	@Override
	public int deleteBanner(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.deleteBanner(Id);
	}
	
	@Override
	public int addBanner(String imgPath,String url,String weight) {
		// TODO Auto-generated method stub
		return caicaiDao.addBanner(imgPath,url,weight);
	}
	
	@Override
	public  List<BlackList> checkBlackList(String openid) {
		// TODO Auto-generated method stub
		return caicaiDao.checkBlackList(openid);
	}
	
	@Override
	public  List<BlackList> getBlackList(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getBlackList(length);
	}
	
	@Override
	public  List<Task> getOpenidbySearch(String search) {
		// TODO Auto-generated method stub
		return caicaiDao.getOpenidbySearch(search);
	}
	
	@Override
	public  List<Task> getAllOpenid(String search) {
		// TODO Auto-generated method stub
		return caicaiDao.getAllOpenid(search);
	}
	
	@Override
	public  List<Comment> getOpenidBySearchComment(String search) {
		// TODO Auto-generated method stub
		return caicaiDao.getOpenidBySearchComment(search);
	}
	
	@Override
	public  List<Comment> getOpenidBySearchAllComment(String search) {
		// TODO Auto-generated method stub
		return caicaiDao.getOpenidBySearchAllComment(search);
	}
	
	@Override
	public int addBlacklist(BlackList blacklist) {
		// TODO Auto-generated method stub
		return caicaiDao.addBlacklist(blacklist);
	}
	
	@Override
	public int deleteBlacklist(int Id) {
		// TODO Auto-generated method stub
		return caicaiDao.deleteBlacklist(Id);
	}
	
	@Override
	public int addSuggestion(Suggestion suggestion) {
		// TODO Auto-generated method stub
		return caicaiDao.addSuggestion(suggestion);
	}
	
	@Override
	public  List<Suggestion> getSuggestionByPk(int id) {
		// TODO Auto-generated method stub
		return caicaiDao.getSuggestionByPk(id);
	}
	
	@Override
	public  List<Suggestion> getSuggestion() {
		// TODO Auto-generated method stub
		return caicaiDao.getSuggestion();
	}
	
	@Override
	public List<BitRank> getRankList(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.getRankList(length);
	}
	
	@Override
	public List<BitRank> getRankListByOpenid(String openid) {
		// TODO Auto-generated method stub
		return caicaiDao.getRankListByOpenid(openid);
	}
	
	@Override
	public int addRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return caicaiDao.addRank(bitrank);
	}
	
	@Override
	public  int updateRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return caicaiDao.updateRank(bitrank);
	}
	
	@Override
	public  List<Task> getChooseBySearch(String search) {
		// TODO Auto-generated method stub
		return caicaiDao.getChooseBySearch(search);
	}
	
	@Override
	public List<Pr> caicaigetPR(int length) {
		// TODO Auto-generated method stub
		return caicaiDao.caicaigetPR(length);
	}
	
	@Override
	public  List<AccessCode> getCodeCtime(Long c_time) {
		// TODO Auto-generated method stub
		return caicaiDao.getCodeCtime(c_time);
	}
	
	@Override
	public  int saveCode(String code,Long c_time) {
		// TODO Auto-generated method stub
		return caicaiDao.saveCode(code,c_time);
	}
	
}