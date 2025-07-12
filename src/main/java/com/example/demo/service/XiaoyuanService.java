package com.example.demo.service;

import java.util.List;

import com.example.demo.model.GroupBuy;
import com.example.demo.model.Meetup;

public interface XiaoyuanService {
	List<Meetup> getMeetupByRegionCampus(int length, String region, String campus);
	List<Meetup> getMeetupByRegionCampusAndAll(int length, String region, String campus);
	List<Meetup> getAvailableMeetupByRegionXiaoyuan(int length, String region);
	List<Meetup> getAvailableMeetupXiaoyuan(int length, String region, String campus);
	List<Meetup> getMeetupByIdXiaoyuan(String group_id);
	List<Meetup> getMeetupByCategoryXiaoyuan(String category);
	
	List<Meetup> getAllMeetupByRegion(String region);
	List<Meetup> getMeetupByRegion(int length, String region);
	
	List<Meetup> getMeetupByRegionCategoryXiaoyuan(String category, int length, String region);
	List<Meetup> getMeetupByRegionCampusCategoryXiaoyuan(String category, int length, String region, String campus);
	List<Meetup> getMeetupByRegionCampusAllCategoryXiaoyuan(String category, int length, String region, String campus);
	
	List<Meetup> getAvailableMeetupByCategoryXiaoyuan(String category);
	List<Meetup> getMeetupByReleaseOpenidXiaoyuan(String openid);
	List<Meetup> getMeetupByJoinOpenidXiaoyuan(String openid);
	int addMeetup(Meetup meetup);
	int deleteMeetupByGroupId(String group_id);
	int restoreMeetupByGroupId(String group_id);
	
	int deleteMeetupById(int id);
	int restoreMeetupById(int id);
	
	int addMeetupJoiner(String group_id, String openid);
	int updateMeetupJoiner(String group_id, String openid);
	
	List<GroupBuy> getAllGroupBuyByRegionCampus(String region, String campus);
	List<GroupBuy> getGroupBuyByRegionCampus(int length, String region, String campus);
	List<GroupBuy> getGroupBuyByRegionCampusAndAll(int length, String region, String campus);
	List<GroupBuy> getGroupBuyByIdXiaoyuan(String id);
	int deleteGroupBuyById(int id);
	int addGroupBuy(GroupBuy groupbuy);
	
}
