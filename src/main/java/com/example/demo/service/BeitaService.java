package com.example.demo.service;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;

public interface BeitaService {
    List<Task> getallTaskbyBatch(int start, int limit);
    List<Task> getallTask(int length);
    int getTaskCount();
    void initMeilisearch();
	List<Task> getHotTask(int length);
	List<Task> gettaskbyOpenId(String openid,int length);
	List<Task> gettaskbyId(int Id);
	List<Task> gettaskbySearch(String search,int length);
    int addTask(Task task);
    List<Task> gettaskbyRadio(String radioGroup,int length);
    List<Task> gettaskbyRadioSecond(List<String> radioGroup,int length);
    
    List<Task> gettaskbyType(List<String> radioGroup,String type,int length);
    List<Task> gettaskbyTypeCampus(List<String> radioGroup,String type,int length, String campus);
    
    List<Task> gettaskbyRadioSecondForWX(String radioGroup,int length);
    int upDateTask(String c_time,int Id);
    int incWatch(int Id);
    int incLike(int Id);
    int decLike(int Id);
    int incComment(int Id);
    int deleteTask(int Id);
    int recoverTask(int Id);
    int hideTask(int Id);
    int recoverTaskHide(int Id);
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
	
    public List<Member>getMember(String openid);
    public List<Member>getAllMember();
    int addMember(Member member);
    int upDateWatch(int watchNum,int Id);
    int updateChoose(int Id,int choose);
    public  List<BlackList> checkBlackList(String openid);
    public  List<BlackList> getBlackList(int length);
	List<Task> getOpenidbySearch(String search);
	List<Task> getAllOpenid(String search);
	List<Comment> getOpenidBySearchComment(String search);
	List<Comment> getOpenidBySearchAllComment(String search);
	int addBlacklist(BlackList blacklist);
	int  deleteBlacklist(@Param("Id")int Id);
    
    public List<Comment>getAllComment(int length);
    
    List<Banner> getBanner();
    List<Banner> getBanner2();
    int addSuggestion(Suggestion suggestion);
    public List<Suggestion>getSuggestionByPk(int id);
    public List<Suggestion>getSuggestion();
    int addBanner(String imgPath,String url,String weight);
    int  deleteBanner(@Param("Id")int Id);
    
    List<BitRank> getRankList(int length);
    List<BitRank> getRankListByOpenid(String openid);
    int addRank(BitRank bitrank);
    int updateRank(BitRank bitrank);
    
    List<Task> getChooseBySearch(String search);
    List<AccessCode> getCodeCtime(Long c_time);
    int saveCode(String code,Long c_time);
    
//    int addbannerTask(int id);
//    
//    int downBannerTask(int id);

}
