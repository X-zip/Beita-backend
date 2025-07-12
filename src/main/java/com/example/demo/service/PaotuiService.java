package com.example.demo.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.model.BlackList;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.Profile;
import com.example.demo.model.TaskPaotui;
import com.example.demo.model.UserPaotui;
import com.example.demo.model.WXTemplate;
import com.example.demo.model.Rider;
import com.example.demo.model.Secret;

public interface PaotuiService {
	
//	public List<User> checkBlackList(String openid);

	public int addTask(TaskPaotui task);
	
	public List<TaskPaotui> getallTask(int length, String region, String campusGroup);
	public List<TaskPaotui> getallTaskByRegion(int length, String region);
	
	public List<TaskPaotui> gettaskbyOrderId(String order_id);
	
	public List<TaskPaotui> gettaskbyReleaseOpenId(String openid, int length, String region, String campusGroup);
	public List<TaskPaotui> gettaskbyReleaseOpenIdRegion(String openid, int length, String region);

	public List<TaskPaotui> gettaskbyPickupOpenId(String openid, int length, String region, String campusGroup);
	
	public List<TaskPaotui> getCurrentTaskbyPickupOpenId(String openid, String region, String campusGroup);
	public List<TaskPaotui> getCurrentTaskbyPickupOpenIdRegion(String openid, String region);

	List<TaskPaotui> gettaskbySearch(String search, int length, String region, String campusGroup);
	List<TaskPaotui> gettaskbySearchRegion(String search, int length, String region);

    List<TaskPaotui> gettaskbyCategory(String category, int length, String region, String campusGroup);
    List<TaskPaotui> gettaskbyCategoryRegion(String category, int length, String region);
    
    List<TaskPaotui> gettaskbyStatus(String status, int length, String region, String campusGroup);
    List<TaskPaotui> gettaskbyStatusRegion(String status, int length, String region);

    List<TaskPaotui> gettaskbyCategoryStatus(String category,String status, int length, String region, String campusGroup);
    List<TaskPaotui> gettaskbyCategoryStatusRegion(String category,String status, int length, String region);
    
    List<TaskPaotui> gettaskbyReleaseOpenIdCategory(String category, String openid, int length, String region, String campusGroup);
    List<TaskPaotui> gettaskbyReleaseOpenIdCategoryRegion(String category, String openid, int length, String region);
    
    List<TaskPaotui> gettaskbyPickupOpenIdCategory(String category, String openid, int length, String region, String campusGroup);
    List<TaskPaotui> gettaskbyPickupOpenIdCategoryRegion(String category, String openid, int length, String region);
    
    int updateTradeNoByOrderId(String order_id, String trade_number);
    
    int modifyTask(@Param("order_id")String order_id, @Param("address_from")String address_from,@Param("address_to")String address_to, 
    		@Param("due_date")String due_date,@Param("due_time")String due_time,@Param("description")String description,
    		@Param("content")String content,@Param("img_url")String img_url);
    
    int deleteTask(@Param("order_id")String order_id);
    
    int expireTask(@Param("order_id")String order_id);
    
    List<Rider> checkRider(Rider rider);
    
    int addRider(Rider rider);
    
//    List<User> getUserByOpenid(String openid);
    
    List<Rider> getRiderByOpenid(String openid);
    
    int updateOrderPickup(@Param("openid")String openid, @Param("order_id")String order_id);
    
    int orderComplete(@Param("order_id")String order_id);
    
    int orderCompleteByRider(@Param("order_id")String order_id);
    
    List<Rider> getAllRider(int length);
    
//    List<User> getAllUser(int length);
    
    int verifyRider(@Param("openid")String openid);
    
    List<AccessCode> getCodeCtime(Long c_time);
    
    int saveCode(String code,Long c_time);
    
    List<Banner> getBanner();
        
    int insertUserOpenidPhone(String openid, String phone);
    
    int updateTaskPendingPayStatus(String order_id);
    
    int updateTaskPayStatus(String openid, String order_id);
    
    int riderAddIncome(String openid, double price);
    
    int updateUserOrderNum(String openid);
    List<Profile> getProfileByOpenid(String openid);
    
    int updateRiderBusyStatus(String openid, Integer status);
    
    List<Rider> getAllVerifiedRider();
    
    List<WXTemplate> getWXTemplate(String region, String campusGroup, String name);
    List<WXTemplate> getWXTemplateByRegion(String region, String name);
    
    List<BlackList> checkBlackList(String openid);
    List<BlackList> getBlackList(int length, String campus);
    
    List<Secret> getSecretById(String id);
}
