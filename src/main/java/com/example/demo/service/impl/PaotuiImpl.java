package com.example.demo.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.demo.model.AccessCode;
import com.example.demo.model.AccessCodePaotui;
import com.example.demo.model.Banner;
import com.example.demo.model.Profile;
import com.example.demo.model.TaskPaotui;
import com.example.demo.model.UserPaotui;
import com.example.demo.model.WXTemplate;
import com.example.demo.model.Rider;
import com.example.demo.model.Secret;
import com.example.demo.model.BlackList;
import com.example.demo.dao.PaotuiDao;
import com.example.demo.service.PaotuiService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "paotuiService")
public class PaotuiImpl  implements PaotuiService{
	@Autowired
	PaotuiDao paotuiDao;
//	@Override
//	public  List<User> checkBlackList(String openid) {
//		return paotuiDao.checkBlackList(openid);
//	}

	@Override
	public int addTask(TaskPaotui task) {
		return paotuiDao.addTask(
				task.getOrder_id(),
				task.getCategory(),
				task.getAddress_from(),
				task.getAddress_to(),
				task.getDue_date(),
				task.getDue_time(),
				task.getDescription(),
				task.getContent(),
				task.getPrice(),
				task.getRelease_openid(),
				task.getImg_url(),
				task.getRelease_phone(),
				task.getRelease_wechat(),
				task.getRelease_name(),
				task.getAmount(),
				task.getWeight(),
				task.getStatus(),
				task.getRegion(),
				task.getCampusGroup(),
				task.getTrade_number());
	}
	
	@Override
	public List<TaskPaotui> getallTask(int length, String region, String campusGroup){
		return paotuiDao.getallTask(length, region, campusGroup);
	}
	
	@Override
	public List<TaskPaotui> getallTaskByRegion(int length, String region){
		return paotuiDao.getallTaskByRegion(length, region);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyOrderId(String order_id){
		return paotuiDao.gettaskbyOrderId(order_id);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyReleaseOpenId(String openid, int length, String region, String campusGroup){
		return paotuiDao.gettaskbyReleaseOpenId(openid,length, region, campusGroup);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyReleaseOpenIdRegion(String openid, int length, String region){
		return paotuiDao.gettaskbyReleaseOpenIdRegion(openid,length, region);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyPickupOpenId(String openid, int length, String region, String campusGroup){
		return paotuiDao.gettaskbyPickupOpenId(openid,length,region, campusGroup);
	}
	
	@Override
	public List<TaskPaotui> getCurrentTaskbyPickupOpenId(String openid, String region, String campusGroup){
		return paotuiDao.getCurrentTaskbyPickupOpenId(openid, region, campusGroup);
	}
	
	@Override
	public List<TaskPaotui> getCurrentTaskbyPickupOpenIdRegion(String openid, String region){
		return paotuiDao.getCurrentTaskbyPickupOpenIdRegion(openid, region);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbySearch(String search, int length, String region, String campusGroup) {
		return paotuiDao.gettaskbySearch(search,length, region, campusGroup);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbySearchRegion(String search, int length, String region) {
		return paotuiDao.gettaskbySearchRegion(search,length, region);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyCategory(String category, int length, String region, String campusGroup) {
		return paotuiDao.gettaskbyCategory(category,length, region, campusGroup);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyCategoryRegion(String category, int length, String region) {
		return paotuiDao.gettaskbyCategoryRegion(category,length, region);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyStatus(String status, int length, String region, String campusGroup){
		return paotuiDao.gettaskbyStatus(status,length, region, campusGroup);
	}
	
	@Override
	public List<TaskPaotui> gettaskbyStatusRegion(String status, int length, String region){
		return paotuiDao.gettaskbyStatusRegion(status,length, region);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyCategoryStatus(String category,String status, int length, String region, String campusGroup) {
		return paotuiDao.gettaskbyCategoryStatus(category,status,length, region, campusGroup);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyCategoryStatusRegion(String category,String status, int length, String region) {
		return paotuiDao.gettaskbyCategoryStatusRegion(category,status,length, region);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyReleaseOpenIdCategory(String category,String openid, int length, String region, String campusGroup) {
		return paotuiDao.gettaskbyReleaseOpenIdCategory(category,openid,length, region, campusGroup);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyReleaseOpenIdCategoryRegion(String category,String openid, int length, String region) {
		return paotuiDao.gettaskbyReleaseOpenIdCategoryRegion(category,openid,length, region);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyPickupOpenIdCategory(String category,String openid, int length, String region, String campusGroup) {
		return paotuiDao.gettaskbyPickupOpenIdCategory(category,openid,length, region, campusGroup);
	}
	
	@Override
	public  List<TaskPaotui> gettaskbyPickupOpenIdCategoryRegion(String category,String openid, int length, String region) {
		return paotuiDao.gettaskbyPickupOpenIdCategoryRegion(category,openid,length, region);
	}
	
	@Override
	public  int modifyTask(String order_id, String address_from,String address_to, 
			String due_date,String due_time,
			String description,String content,String img_url) {
		// TODO Auto-generated method stub
		return paotuiDao.modifyTask(order_id,address_from,address_to,
				due_date,due_time,description,content,img_url);
	}
	
	@Override
	public int deleteTask(String order_id) {
		return paotuiDao.deleteTask(order_id);
	}
	
	@Override
	public int expireTask(String order_id) {
		return paotuiDao.expireTask(order_id);
	}
	
	@Override
	public List<Rider> checkRider(Rider rider) {
		return paotuiDao.checkRider(rider.getOpenid(), rider.getPhone(), rider.getWechat(), rider.getIc_number());
	}
	
	@Override
	public int addRider(Rider rider) {
		return paotuiDao.addRider(rider.getOpenid(), rider.getRealname(), rider.getPhone(), rider.getWechat(),
				rider.getSchool(), rider.getCampus(),rider.getIc_number(), 
				rider.getStudent_card_pic(), rider.getIc_pic_front(), rider.getIc_pic_back(),
				rider.getRegion(), rider.getCampusGroup());
	}
	
//	@Override
//	public List<User> getUserByOpenid(String openid){
//		return paotuiDao.getUserByOpenid(openid);
//	}
	
	@Override
	public List<Rider> getRiderByOpenid(String openid){
		return paotuiDao.getRiderByOpenid(openid);
	}
	
	@Override
	public int updateOrderPickup(String openid, String order_id) {
		return paotuiDao.updateOrderPickup(openid, order_id);
	}
	
	@Override
	public int orderComplete(String order_id) {
		return paotuiDao.orderComplete(order_id);
	}
	
	@Override
	public int orderCompleteByRider(String order_id) {
		return paotuiDao.orderCompleteByRider(order_id);
	}
	
//	@Override
//	public List<User> getAllUser(int length){
//		return paotuiDao.getAllUser(length);
//	}
	
	@Override
	public List<Rider> getAllRider(int length){
		return paotuiDao.getAllRider(length);
	}
	
	@Override
	public int verifyRider(String openid) {
		return paotuiDao.verifyRider(openid);
	}
	
	@Override
	public  List<AccessCode> getCodeCtime(Long c_time) {
		// TODO Auto-generated method stub
		return paotuiDao.getCodeCtime(c_time);
	}
	
	@Override
	public  int saveCode(String code,Long c_time) {
		// TODO Auto-generated method stub
		return paotuiDao.saveCode(code,c_time);
	}
	
	@Override
	public  List<Banner> getBanner() {
		// TODO Auto-generated method stub
		return paotuiDao.getBanner();
	}
	
	@Override
	public int insertUserOpenidPhone(String openid, String phone){
		return paotuiDao.insertUserOpenidPhone(openid, phone);
	}
	
	@Override
	public int updateTaskPendingPayStatus(String order_id) {
		return paotuiDao.updateTaskPendingPayStatus(order_id);
	}
	
	@Override
	public int updateTaskPayStatus(String openid, String order_id) {
		return paotuiDao.updateTaskPayStatus(openid, order_id);
	}
	
	@Override
	public int riderAddIncome(String openid, double price) {
		return paotuiDao.riderAddIncome(openid, price);
	}
	
	@Override
	public int updateUserOrderNum(String openid) {
		return paotuiDao.updateUserOrderNum(openid);
	}
	
	@Override
	public List<Profile> getProfileByOpenid(String openid) {
		return paotuiDao.getProfileByOpenid(openid);
	}
	
	@Override
	public int updateRiderBusyStatus(String openid, Integer status) {
		return paotuiDao.updateRiderBusyStatus(openid, status);
	}
	
	@Override
	public List<Rider> getAllVerifiedRider() {
		return paotuiDao.getAllVerifiedRider();
	}
	
	@Override
	public List<WXTemplate> getWXTemplate(String region, String campusGroup, String name){
		return paotuiDao.getWXTemplate(region, campusGroup, name);
	}
	
	@Override
	public List<WXTemplate> getWXTemplateByRegion(String region, String name){
		return paotuiDao.getWXTemplateByRegion(region, name);
	}
	
	@Override
	public  List<BlackList> checkBlackList(String openid) {
		// TODO Auto-generated method stub
		return paotuiDao.checkBlackList(openid);
	}
	
	@Override
	public  List<BlackList> getBlackList(int length, String campus) {
		// TODO Auto-generated method stub
		return paotuiDao.getBlackList(length,campus);
	}
	
	@Override
	public  List<Secret> getSecretById(String id) {
		// TODO Auto-generated method stub
		return paotuiDao.getSecretById(id);
	}
	
	@Override
	public int updateTradeNoByOrderId(String order_id, String trade_number) {
		return paotuiDao.updateTradeNoByOrderId(order_id, trade_number);
	}
}

