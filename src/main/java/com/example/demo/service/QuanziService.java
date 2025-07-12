package com.example.demo.service;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
import com.example.demo.model.WXTemplate;

public interface QuanziService {
	List<Task> getallTask(int length);
	List<Task> getHotTaskByRegion(String region, int length);
	List<Task> getHotTaskByRegionCampus(String region, String campus, int length);
	List<Task> gettaskbyOpenId(String openid,int length);
	List<Task> gettaskbyId(int Id);
	List<Task> gettaskbySearch(String search,int length,String campus);
	List<Task> gettaskbySearchRegion(String search,int length,String region);
    int addTask(Task task,String status);
    List<Task> gettaskbyRadio(String radioGroup,int length);
  
    List<Task> gettaskbyType(List<String> radioGroup,String type,int length,String campus,String region);
    List<Task> gettaskbyCampus(List<String> radioGroup,String type,int length,String campus,String region);
    
    List<Task> getAllTaskForWX(String length,String campus,String start,String end,List<String> radioGroup,String order,String keyword);
    List<Task> getAllTaskForWXByRegion(String length,String campus,String start,String end,List<String> radioGroup,String order,String keyword);
    int upDateTask(String c_time,int Id);
    int incWatch(int Id);
    int incLike(int Id);
    int decLike(int Id);
    int incComment(int Id);
    int deleteTask(int Id);
    int recoverTask(int Id);
    int recoverTaskHide(int Id);
    int hideTask(int Id);
    int incCommentLike(int Id);
    int decCommentLike(int Id);
    
    int topTask(int Id);
    int downTask(int Id);
    
    int addLike(Like like);
    public List<Like>getlikeByPk(String openid,int pk);
    int  deleteLike(@Param("Id")int Id);
    int addComment(Comment comment);
    public List<Comment>getCommentByPk(int pk);
    public List<Comment>getCommentByLength(int pk,int length);
    public List<Comment>getCommentByType(int pk,int length,String type);
    public List<Comment>getCommentByPid(int pid);
    public List<Comment>getFirstCommentByPid(int pid);
    
    public List<Comment>getCommentByIdList(List<String> str);
    public List<Like>getlikeByOpenid(String openid,int length);
    int  deleteComment(@Param("Id")int Id);
    public List<Comment>getCommentByOpenid(String openid,int length);
    public List<Comment>getCommentByApplyto(String applyTo,int length);
	
    public List<Member>getMember(String openid,String campus);
    public List<Member>getAllMember(String campus);
    public List<Member>getAllMemberWX();
    int addMember(Member member,String campus);
    int  deleteMember(@Param("Id")int Id);
    
    int upDateWatch(int watchNum,int Id);
    int updateChoose(int Id,int choose, String campus);
    public  List<BlackList> checkBlackList(String openid);
    public  List<BlackList> getBlackList(int length, String campus);
    int addBlacklistXiaoyuan(String openid, String period, String campus);
    
	List<Task> getOpenidbySearch(String search, String campus);
	List<Task> getOpenidbySearchByRegion(String search, String campus);
	List<Task> getAllOpenid(String search, String campus);
	List<Comment> getOpenidBySearchComment(String search, String campus);
	List<Comment> getOpenidBySearchCommentByRegion(String search, String campus);
	List<Comment> getOpenidBySearchAllComment(String search, String campus);
	int addBlacklist(BlackList blacklist, String campus);
	int  deleteBlacklist(@Param("Id")int Id);
    
    public List<Comment>getAllComment(int length);
    
    List<Banner> getBanner(String campus);
    List<Banner> getBannerXY(String region, String campus);
    int addBanner(String page,String imgPath,String url,String campus,String weight);
    int  deleteBanner(@Param("Id")int Id);
    
    int addSuggestion(Suggestion suggestion);
    public List<Suggestion>getSuggestionByPk(int id);
    public List<Suggestion>getSuggestion();
    
    List<BitRank> getRankList(int length);
    List<BitRank> getRankListByOpenid(String openid);
    int addRank(BitRank bitrank);
    int updateRank(BitRank bitrank);
    
    List<Task> getChooseBySearch(String search,String campus);
    List<Task> getChooseBySearchByRegion(String search,String campus);
    List<AccessCode> getCodeCtime(Long c_time,String campus);
    int saveCode(String code,Long c_time,String campus);
    List<Secret> getSecret(String appid);
    List<Secret> getSecretByCampus(String campus);
    List<User> getUser(String account,String password);
    List<Task> taskManagement(String length,String campus);
    List<Task> taskManagementByRegion(String length,String campus);
    
    List<Switch> getSwitchStatus(String campus);
    int addSwitch(String campus, String status);
    int updateSwitch(String campus, String status);
    
    List<QR> getQR(String campus);
    int addQR(String imgPath,String campus);
    int  deleteQR(@Param("Id")int Id);
    List<QR> getQRList(String region,String campus);
    
    List<VerifyUser> getVerifyUserAll();
    List<VerifyUser> getVerifyUserbyCampusLength(String campus, int length);
    List<VerifyUser> getVerifyUserbyCampusLengthStatus(String campus, int length, int status);
    List<VerifyUser> getVerifyUserbyRegionCampusLengthStatus(String region, String campus, int length, int status);
    List<VerifyUser> getVerifyUserbyCampus(String campus);
    List<VerifyUser> getVerifyUserByOpenid(String openid);
    int udpateUserInfoByOpenid(String openid, String nickname, String avatar);
    int addUserInfoByOpenid(String openid, String nickname, String avatar, int status);
    int addVerifyUser(VerifyUser verify_user);
    int updateVerifyUserVerifyInfo(VerifyUser verify_user);
    int updateVerifyUserStatusById(int id, int status);
    int deleteVerifyUserById(int id);
    List<VerifyUser> getVerifyUserbyEmail(String email);
    int udpateUserPhoneByOpenid(String openid, String phone);
    int addUserPhoneByOpenid(String openid, String phone, int status);
//    int addbannerTask(int id);
//    int downBannerTask(int id);
    int updateCampusRegionByOpenid(String openid, String campus, String region);
    int insertCampusRegionByOpenid(String openid, String campus, String region);
    
    List<PMChat> getChatfromBothOpenid(PMChat chat);
    List<PMChat> getChatfromOneOpenid(String openid, String region, String campusGroup);
    int addChat(PMChat chat);
//    int updateLatestContent(PMChat chat);
    
    List<PMStatus> getPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup);
    int udpatePMStatusReceiverByBothOpenid(String sender, String receiver, String region, String campusGroup, int status);
    int udpatePMStatusByBothOpenid(String openid1, String openid2, String region, String campusGroup, int status);
    int deletePMChatDeleteByBothOpenid(String openid1, String openid2, String region, String campusGroup);

    int addPMStatusByBothOpenid(String sender, String receiver, String region, String campusGroup);
    int udpatePMStatusTimeById(int id, int status);
    //    List<PMStatus> getPMUserListByOpenidStatus(String openid, int status);
    List<PMStatus> getLatestUserMsgListByOpenid(String openid, String region, String campusGroup);

    List<PMStatus> getVisiblePMUserListByOpenid(String openid, String region, String campusGroup);
    List<PMStatus> getPendingPMUserList(String openid, String region, String campusGroup);
    
    List<PMChatDetail> getPMChatByOpenid(String roomId, String region, String campusGroup);
    List<PMChatDetail> getPMChatByBothOpenid(String openid1, String openid2, String region, String campusGroup);
    List<PMChatDetail> getPMChatStatusByBothOpenid(String openid1, String openid2, String region, String campusGroup);
    List<PMFriendList> getPMFriendListByOpenid(String openid, String region, String campusGroup);
    List<PMFriendMsgList> getPMFriendMsgListByOpenid(String openid, String region, String campusGroup);
    int updateLastReadTime(String openid, String targetOpenid, String region, String campus);
    
    List<Rider> getPaotuiRiderbyRegionLengthStatus(String region, int length, int status);
    List<Rider> getPaotuiRiderbyCampusLengthStatus(String campus, int length, int status);
    List<Rider> getPaotuiRiderbyRegionCampusLengthStatus(String region, String campus, int length, int status);
    int updatePaotuiUserStatusById(int id, int status);
    int deletePaotuiUserById(int id);
    
    List<WXTemplate> getWXTemplate(String region, String campusGroup, String name);
    List<WXTemplate> getWXTemplateByRegion(String region, String name);
    
    List<Campus> getAllCampusList();
    List<RadioGroupCategory> getAllCategoryList();
    
    int updateUserIdentityByOpenid(String openid, String identity);
    List<VerifyUserIdentity> getUserVerifyByOpenidList(List<String> openidList);
}
