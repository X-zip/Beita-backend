package com.example.demo.service.impl;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Campus;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.PMChat;
import com.example.demo.model.PMChatDetail;
import com.example.demo.model.PMFriendList;
import com.example.demo.model.PMFriendMsgList;
import com.example.demo.model.PMStatus;
import com.example.demo.model.QR;
import com.example.demo.model.RadioGroupCategory;
import com.example.demo.model.Rider;
import com.example.demo.model.Secret;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Switch;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.model.VerifyUser;
import com.example.demo.model.VerifyUserIdentity;
import com.example.demo.dao.QuanziDao;
import com.example.demo.service.QuanziService;
import com.example.demo.model.WXTemplate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service(value = "quanziService")
public class QuanziImpl implements QuanziService{
	@Autowired
	QuanziDao quanziDao;
	@Override
	public List<Task> getallTask(int length) {
		// TODO Auto-generated method stub
		return quanziDao.getallTask(length);
	}
	
	@Override
	public List<Task> getHotTaskByRegion(String region, int length) {
		// TODO Auto-generated method stub
		return quanziDao.getHotTaskByRegion(region, length);
	}
	
	@Override
	public List<Task> getHotTaskByRegionCampus(String region, String campus, int length) {
		// TODO Auto-generated method stub
		return quanziDao.getHotTaskByRegionCampus(region, campus, length);
	}

	@Override
	public  List<Task> gettaskbyId(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.gettaskbyId(Id);
	}
	
	@Override
	public  List<Task> gettaskbyOpenId(String openid,int length) {
		// TODO Auto-generated method stub
		return quanziDao.gettaskbyOpenId(openid,length);
	}
	
	@Override
	public  List<Task> gettaskbySearch(String search,int length,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.gettaskbySearch(search,length,campus);
	}
	
	@Override
	public  List<Task> gettaskbySearchRegion(String search,int length,String region) {
		// TODO Auto-generated method stub
		return quanziDao.gettaskbySearchRegion(search,length,region);
	}

	@Override
	public int addTask(Task task,String status) {
		// TODO Auto-generated method stub
		int is_delete = 0;
		if ((status.equals("true")) || (task.getIs_delete()==1)) {
			is_delete = 1;
		}
		return quanziDao.addTask(task.getContent(),task.getPrice(),task.getTitle(),task.getWechat(),task.getOpenid(),task.getAvatar(),task.getCampusGroup(),
				task.getCommentNum(),task.getWatchNum(),task.getLikeNum(),task.getRadioGroup(),task.getImg(),task.getRegion(),task.getUserName(),task.getC_time(),
				task.getCover(),is_delete,task.getIp());
	}

	@Override
	public  List<Task> gettaskbyRadio(String radioGroup,int length) {
		// TODO Auto-generated method stub
		return quanziDao.gettaskbyRadio(radioGroup,length);
	}
	
	
	@Override
	public  List<Task> gettaskbyType(List<String> radioGroup,String type,int length,String campus,String region) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return quanziDao.gettaskbyCtime(radioGroup,length,campus,region);
		} else if (type.equals("1")) {
			return quanziDao.gettaskbyComment(radioGroup,length,campus,region);
		} else if (type.equals("2")) {
			return quanziDao.gettaskbyHot(radioGroup,length,campus,region);
		} else if (type.equals("3")) {
			return quanziDao.gettaskbyChoose(radioGroup,length,campus,region);
		} else{			
			return quanziDao.gettaskbyRadioSecond(radioGroup,length,campus,region);
		}
	}
	
	@Override
	public  List<Task> gettaskbyCampus(List<String> radioGroup,String type,int length,String campus,String region) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return quanziDao.gettaskbyCtimeCampus(radioGroup,length,campus,region);
		} else if (type.equals("1")) {
			return quanziDao.gettaskbyCommentCampus(radioGroup,length,campus,region);
		} else if (type.equals("2")) {
			return quanziDao.gettaskbyHotCampus(radioGroup,length,campus,region);
		} else if (type.equals("3")) {
			return quanziDao.gettaskbyChooseCampus(radioGroup,length,campus,region);
		} else{			
			return quanziDao.gettaskbyRadioSecondCampus(radioGroup,length,campus,region);
		}
	}
	
	@Override
	public  List<Task> getAllTaskForWX(String length,String campus,String start,String end,List<String> radioGroup,String order,String keyword) {
		// TODO Auto-generated method stub
		if(order.equals("time")) {
			if(campus.equals("beita")) {
				return quanziDao.getAllTaskForWXBeita(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			} else if (campus.equals("sg")) {
				return quanziDao.getAllTaskForWXSg(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			} else {
				return quanziDao.getAllTaskForWX(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			}		
		} else {
			if(campus.equals("beita")) {
				return quanziDao.getAllTaskForWXByHotBeita(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			} else if (campus.equals("sg")) {
				return quanziDao.getAllTaskForWXByHotSg(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			} else {
				return quanziDao.getAllTaskForWXByHot(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
			}	
		}

	}
	
	@Override
	public  List<Task> getAllTaskForWXByRegion(String length,String campus,String start,String end,List<String> radioGroup,String order,String keyword) {
		// TODO Auto-generated method stub
		if(order.equals("time")) {
			return quanziDao.getAllTaskForWXByRegion(keyword,Integer.parseInt(length),campus,start,end,radioGroup);		
		} else {
			return quanziDao.getAllTaskForWXByHotByRegion(keyword,Integer.parseInt(length),campus,start,end,radioGroup);
		}

	}
	
	@Override
	public  int upDateTask(String c_time,int Id) {
		// TODO Auto-generated method stub
		return quanziDao.upDateTask(c_time,Id);
	}
	
	@Override
	public  int incWatch(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.incWatch(Id);
	}
	
	@Override
	public  int incLike(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.incLike(Id);
	}
	
	@Override
	public  int incCommentLike(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.incCommentLike(Id);
	}
	
	@Override
	public  int decCommentLike(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.decCommentLike(Id);
	}
	
	
	@Override
	public  int incComment(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.incComment(Id);
	}
	
	@Override
	public  int decLike(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.decLike(Id);
	}

	@Override
	public int deleteTask(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteTask(Id);
	}
	
	@Override
	public int recoverTask(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.recoverTask(Id);
	}
	
	@Override
	public int recoverTaskHide(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.recoverTaskHide(Id);
	}
	
	@Override
	public int hideTask(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.hideTask(Id);
	}
	
	
	@Override
	public int topTask(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.topTask(Id);
	}
	
	@Override
	public int downTask(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.downTask(Id);
	}
	
	@Override
	public int addLike(Like like) {
		// TODO Auto-generated method stub
		return quanziDao.addLike(like);
	}
	
	@Override
	public int addComment(Comment comment) {
		// TODO Auto-generated method stub
		return quanziDao.addComment(comment) + quanziDao.upDateTaskCommentTime(comment);
	}
	
	@Override
	public  List<Like> getlikeByPk(String openid,int pk) {
		// TODO Auto-generated method stub
		return quanziDao.getlikeByPk(openid,pk);
	}
	
	@Override
	public  List<Comment> getCommentByPk(int pk) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByPk(pk);
	}
	
	@Override
	public  List<Comment> getCommentByLength(int pk,int length) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByLength(pk,length);
	}
	
	@Override
	public  List<Comment> getCommentByType(int pk,int length,String type) {
		// TODO Auto-generated method stub
		if(type.equals("0") ) {
			return quanziDao.getCommentByLength(pk,length);
		} else if (type.equals("1")) {
			return quanziDao.getCommentByReverse(pk,length);
		} else {
			return quanziDao.getCommentByHot(pk,length);
		}
		
	}
	
	@Override
	public  List<Comment> getCommentByPid(int pid) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByPid(pid);
	}
	
	@Override
	public  List<Comment> getFirstCommentByPid(int pid) {
		// TODO Auto-generated method stub
		return quanziDao.getFirstCommentByPid(pid);
	}
	
	@Override
	public  List<Comment> getCommentByIdList(List<String> str) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByIdList(str);
	}
	
	@Override
	public int deleteLike(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteLike(Id);
	}
	
	@Override
	public  List<Like> getlikeByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return quanziDao.getlikeByOpenid(openid,length);
	}
	@Override
	public int deleteComment(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteComment(Id);
	}
	@Override
	public  List<Comment> getCommentByOpenid(String openid,int length) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByOpenid(openid,length);
	}
	
	@Override
	public  List<Comment> getCommentByApplyto(String applyTo,int length) {
		// TODO Auto-generated method stub
		return quanziDao.getCommentByApplyto(applyTo,length);
	}
	
	@Override
	public  List<Member> getMember(String openid,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getMember(openid,campus);
	}
	
	@Override
	public int addMember(Member member,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.addMember(member.getName(),member.getOpenid(),campus);
	}
	
	@Override
	public int deleteMember(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteMember(Id);
	}
	
	@Override
	public  List<Member> getAllMember(String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getAllMember(campus);
	}
	
	@Override
	public  List<Member> getAllMemberWX() {
		// TODO Auto-generated method stub
		return quanziDao.getAllMemberWX();
	}
	
	@Override
	public  int upDateWatch(int watchNum,int Id) {
		// TODO Auto-generated method stub
		return quanziDao.upDateWatch(watchNum,Id);
	}
	
	@Override
	public  int updateChoose(int Id,int choose, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.updateChoose(Id,choose,campus);
	}
	
	@Override
	public  List<Comment> getAllComment(int length) {
		// TODO Auto-generated method stub
		return quanziDao.getAllComment(length);
	}
	

	@Override
	public  List<Banner> getBanner(String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getBanner(campus);
	}
	
	@Override
	public  List<Banner> getBannerXY(String region, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getBannerXY(region, campus);
	}
	
	@Override
	public int addBanner(String page,String imgPath,String url,String campus,String weight) {
		// TODO Auto-generated method stub
		return quanziDao.addBanner(page,imgPath,url,campus,weight);
	}
	
	@Override
	public int deleteBanner(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteBanner(Id);
	}
	
	@Override
	public  List<BlackList> checkBlackList(String openid) {
		// TODO Auto-generated method stub
		return quanziDao.checkBlackList(openid);
	}
	
	@Override
	public  List<BlackList> getBlackList(int length, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getBlackList(length,campus);
	}
	
	@Override
	public int addBlacklistXiaoyuan(String openid, String period, String campus) {
		return quanziDao.addBlacklistXiaoyuan(openid,period,campus);
	}
	
	@Override
	public  List<Task> getOpenidbySearch(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getOpenidbySearch(search,campus);
	}
	
	@Override
	public  List<Task> getOpenidbySearchByRegion(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getOpenidbySearchByRegion(search,campus);
	}
	
	@Override
	public  List<Task> getAllOpenid(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getAllOpenid(search,campus);
	}
	
	
	@Override
	public  List<Comment> getOpenidBySearchComment(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getOpenidBySearchComment(search,campus);
	}
	
	@Override
	public  List<Comment> getOpenidBySearchCommentByRegion(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getOpenidBySearchCommentByRegion(search,campus);
	}
	
	@Override
	public  List<Comment> getOpenidBySearchAllComment(String search, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getOpenidBySearchAllComment(search,campus);
	}
	
	@Override
	public int addBlacklist(BlackList blacklist, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.addBlacklist(blacklist.getOpenid(),blacklist.getPeriod(),blacklist.getDescription(),campus);
	}
	
	@Override
	public int deleteBlacklist(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteBlacklist(Id);
	}
	
	@Override
	public int addSuggestion(Suggestion suggestion) {
		// TODO Auto-generated method stub
		return quanziDao.addSuggestion(suggestion);
	}
	
	@Override
	public  List<Suggestion> getSuggestionByPk(int id) {
		// TODO Auto-generated method stub
		return quanziDao.getSuggestionByPk(id);
	}
	
	@Override
	public  List<Suggestion> getSuggestion() {
		// TODO Auto-generated method stub
		return quanziDao.getSuggestion();
	}
	
	@Override
	public List<BitRank> getRankList(int length) {
		// TODO Auto-generated method stub
		return quanziDao.getRankList(length);
	}
	
	@Override
	public List<BitRank> getRankListByOpenid(String openid) {
		// TODO Auto-generated method stub
		return quanziDao.getRankListByOpenid(openid);
	}
	
	@Override
	public int addRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return quanziDao.addRank(bitrank);
	}
	
	@Override
	public  int updateRank(BitRank bitrank) {
		// TODO Auto-generated method stub
		return quanziDao.updateRank(bitrank);
	}
	
	@Override
	public  List<Task> getChooseBySearch(String search,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getChooseBySearch(search,campus);
	}
	
	@Override
	public  List<Task> getChooseBySearchByRegion(String search,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getChooseBySearchByRegion(search,campus);
	}
	
	@Override
	public  List<AccessCode> getCodeCtime(Long c_time,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getCodeCtime(c_time,campus);
	}
	
	@Override
	public  int saveCode(String code,Long c_time,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.saveCode(code,c_time,campus);
	}

	
	@Override
	public  List<Secret> getSecret(String appid) {
		// TODO Auto-generated method stub
		return quanziDao.getSecret(appid);
	}
	
	@Override
	public  List<Secret> getSecretByCampus(String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getSecretByCampus(campus);
	}
	
	@Override
	public  List<User> getUser(String account,String password) {
		// TODO Auto-generated method stub
		return quanziDao.getUser(account,password);
	}
	
	@Override
	public  List<Task> taskManagement(String length,String campus) {
		// TODO Auto-generated method stub
		if(campus.equals("beita")) {
			return quanziDao.taskManagementBeita(Integer.parseInt(length));
		} else if (campus.equals("sg")) {
			return quanziDao.taskManagementSg(Integer.parseInt(length));
		} else {
			return quanziDao.taskManagement(Integer.parseInt(length),campus);
		}		

	}
	
	@Override
	public  List<Task> taskManagementByRegion(String length,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.taskManagementByRegion(Integer.parseInt(length),campus);	
	}
	
	@Override
	public  List<Switch> getSwitchStatus(String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getSwitchStatus(campus);
	}
	
	@Override
	public int addSwitch(String campus, String status) {
		// TODO Auto-generated method stub
		return quanziDao.addSwitch(campus,status);
	}
	
	@Override
	public  int updateSwitch(String campus, String status) {
		// TODO Auto-generated method stub
		return quanziDao.updateSwitch(campus,status);
	}
	
	@Override
	public  List<QR> getQRList(String region, String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getQRList(region,campus);
	}
	
	
	@Override
	public  List<QR> getQR(String campus) {
		// TODO Auto-generated method stub
		return quanziDao.getQR(campus);
	}
	
	@Override
	public int addQR(String imgPath,String campus) {
		// TODO Auto-generated method stub
		return quanziDao.addQR(imgPath,campus);
	}
	
	@Override
	public int deleteQR(int Id) {
		// TODO Auto-generated method stub
		return quanziDao.deleteQR(Id);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserbyCampusLength(String campus, int length){
		return quanziDao.getVerifyUserbyCampusLength(campus, length);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserbyCampusLengthStatus(String campus, int length, int status){
		return quanziDao.getVerifyUserbyCampusLengthStatus(campus, length, status);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserbyRegionCampusLengthStatus(String region, String campus, int length, int status){
		return quanziDao.getVerifyUserbyRegionCampusLengthStatus(region, campus, length, status);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserbyCampus(String campus){
		return quanziDao.getVerifyUserbyCampus(campus);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserAll(){
		return quanziDao.getVerifyUserAll();
	}
	
	@Override
	public List<VerifyUserIdentity> getVerifyUserByOpenid(String openid){
		return quanziDao.getVerifyUserByOpenid(openid);
	}
	
	@Override
	public List<VerifyUser> getVerifyUserbyEmail(String email){
		return quanziDao.getVerifyUserbyEmail(email);
	}
	
	@Override
	public int udpateUserInfoByOpenid(String openid, String nickname, String avatar) {
		return quanziDao.udpateUserInfoByOpenid(openid, nickname, avatar);
	}
	
	@Override
	public int addUserInfoByOpenid(String openid, String nickname, String avatar, int status) {
		return quanziDao.addUserInfoByOpenid(openid, nickname, avatar, status);
	}
	
	@Override
	public int updateCampusRegionByOpenid(String openid, String campus, String region) {
		return quanziDao.updateCampusRegionByOpenid(openid, campus, region);
	}
	
	@Override
	public int insertCampusRegionByOpenid(String openid, String campus, String region) {
		return quanziDao.insertCampusRegionByOpenid(openid, campus, region);
	}
	
	@Override
	public int udpateUserPhoneByOpenid(String openid, String phone) {
		return quanziDao.udpateUserPhoneByOpenid(openid, phone);
	}
	
	@Override
	public int addUserPhoneByOpenid(String openid, String phone, int status) {
		return quanziDao.addUserPhoneByOpenid(openid, phone, status);
	}
	
	@Override
	public int addVerifyUser(VerifyUser verify_user) {
		return quanziDao.addVerifyUser(verify_user);
	}
	
	@Override
	public int updateVerifyUserVerifyInfo(VerifyUser verify_user) {
		return quanziDao.updateVerifyUserVerifyInfo(verify_user);
	}
	
	@Override
	public int updateVerifyUserStatusById(int id, int status){
		return quanziDao.updateVerifyUserStatusById(id, status);
	}
	
	@Override
	public int deleteVerifyUserById(int id){
		return quanziDao.deleteVerifyUserById(id);
	}
	
	@Override
	public List<PMChat> getChatfromBothOpenid(PMChat chat){
		return quanziDao.getChatfromBothOpenid(chat.getOpenid(), chat.getTargetOpenid(), chat.getRegion(), chat.getCampusGroup());
	}
	
	@Override
	public List<PMChat> getChatfromOneOpenid(String openid, String region, String campusGroup){
		return quanziDao.getChatfromOneOpenid(openid, region, campusGroup);
	}
	
	@Override
	public int addChat(PMChat chat) {
		return quanziDao.addChat(chat.getOpenid(), chat.getTargetOpenid(), chat.getContent(), chat.getRegion(), chat.getCampusGroup());
	}
	
//	@Override
//	public int updateLatestContent(PMChat chat) {
//		return quanziDao.addChat(chat.getOpenid(), chat.getTargetOpenid(), chat.getC_time(), chat.getContent(), chat.getRegion(), chat.getCampusGroup());
//	}
	
	@Override
	public List<PMStatus> getPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup){
		return quanziDao.getPMStatusByBothOpenid(sender, receiver, region, campusGroup);
	}
	
	@Override
	public int addPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup){
		return quanziDao.addPMStatusByBothOpenid(sender, receiver, region, campusGroup);
	}
	
	@Override
	public int udpatePMStatusReceiverByBothOpenid(String sender, String receiver, String region, String campusGroup, int status){
		return quanziDao.udpatePMStatusReceiverByBothOpenid(sender, receiver, region, campusGroup, status);
	}
	
	@Override
	public int deletePMChatDeleteByBothOpenid(String openid1, String openid2, String region, String campusGroup){
		return quanziDao.deletePMChatDeleteByBothOpenid(openid1, openid2, region, campusGroup);
	}
	
	
	@Override
	public int udpatePMStatusByBothOpenid(String openid1, String openid2, String region, String campusGroup, int status){
		return quanziDao.udpatePMStatusByBothOpenid(openid1, openid2, region, campusGroup, status);
	}
	
	@Override
	public List<PMStatus> getVisiblePMUserListByOpenid(String openid, String region, String campusGroup){
		return quanziDao.getVisiblePMUserListByOpenid(openid, region, campusGroup);
	}
	
	@Override
	public List<PMStatus> getLatestUserMsgListByOpenid(String openid, String region, String campusGroup){
		return quanziDao.getLatestUserMsgListByOpenid(openid, region, campusGroup);
	}
	
	@Override
	public List<PMStatus> getPendingPMUserList(String openid, String region, String campusGroup){
		return quanziDao.getPendingPMUserList(openid, region, campusGroup);
	}
	
	@Override
	public int udpatePMStatusTimeById(int id, int status) {
		return quanziDao.udpatePMStatusTimeById(id, status);
	}
	
	@Override
	public List<PMChatDetail> getPMChatByOpenid(String roomId, String region, String campusGroup) {
		return quanziDao.getPMChatByOpenid(roomId, region, campusGroup);
	}
	
	@Override
	public List<PMChatDetail> getPMChatByBothOpenid(String openid1, String openid2, String region, String campusGroup) {
		return quanziDao.getPMChatByBothOpenid(openid1, openid2, region, campusGroup);
	}
	
	@Override
	public List<PMChatDetail> getPMChatStatusByBothOpenid(String openid1, String openid2, String region, String campusGroup) {
		return quanziDao.getPMChatStatusByBothOpenid(openid1, openid2, region, campusGroup);
	}
	
	@Override
	public List<PMFriendList> getPMFriendListByOpenid(String openid, String region, String campusGroup){
		return quanziDao.getPMFriendListByOpenid(openid, region, campusGroup);
	}
	
	
	@Override
	public List<PMFriendMsgList> getPMFriendMsgListByOpenid(String openid, String region, String campusGroup){
		return quanziDao.getPMFriendMsgListByOpenid(openid, region, campusGroup);
	}
	
	@Override
	public int updateLastReadTime(String openid, String targetOpenid, String region, String campus) {
		return quanziDao.updateLastReadTime(openid, targetOpenid, region, campus);
	}
	
	@Override
	public List<Rider> getPaotuiRiderbyRegionLengthStatus(String region, int length, int status){
		return quanziDao.getPaotuiRiderbyRegionLengthStatus(region, length, status);
	}
	
	@Override
	public List<Rider> getPaotuiRiderbyCampusLengthStatus(String campus, int length, int status){
		return quanziDao.getPaotuiRiderbyCampusLengthStatus(campus, length, status);
	}
	
	@Override
	public List<Rider> getPaotuiRiderbyRegionCampusLengthStatus(String region, String campus, int length, int status){
		return quanziDao.getPaotuiRiderbyRegionCampusLengthStatus(region, campus, length, status);
	}
	
	@Override
	public int updatePaotuiUserStatusById(int id, int status){
		return quanziDao.updatePaotuiUserStatusById(id, status);
	}
	
	@Override
	public int deletePaotuiUserById(int id){
		return quanziDao.deletePaotuiUserById(id);
	}
	
	@Override
	public List<WXTemplate> getWXTemplate(String region, String campusGroup, String name){
		return quanziDao.getWXTemplate(region, campusGroup, name);
	}
	
	@Override
	public List<WXTemplate> getWXTemplateByRegion(String region, String name){
		return quanziDao.getWXTemplateByRegion(region, name);
	}
	
	@Override
	public List<Campus> getAllCampusList(){
		return quanziDao.getAllCampusList();
	}
	
	@Override
	public List<RadioGroupCategory> getAllCategoryList(){
		return quanziDao.getAllCategoryList();
	}
	
	@Override
	public int updateUserIdentityByOpenid(String openid, String identity) {
		return quanziDao.updateUserIdentityByOpenid(openid, identity);
	}
	
	@Override
	public List<VerifyUserIdentity> getUserVerifyByOpenidList(List<String> openidList) {
		return quanziDao.getUserVerifyByOpenidList(openidList);
	}
	
}
