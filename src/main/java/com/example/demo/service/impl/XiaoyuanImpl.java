package com.example.demo.service.impl;
import com.example.demo.model.GroupBuy;
import com.example.demo.model.Meetup;
import com.example.demo.dao.XiaoyuanDao;
import com.example.demo.service.XiaoyuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service(value = "xiaoyuanService")
public class XiaoyuanImpl implements XiaoyuanService{
	@Autowired
	XiaoyuanDao xiaoyuanDao;

	@Override
	public List<Meetup> getMeetupByRegionCampus(int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegionCampus(length, region, campus);
	}
	
	@Override
	public List<Meetup> getAllMeetupByRegion(String region) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getAllMeetupByRegion(region);
	}
	
	@Override
	public List<Meetup> getMeetupByRegion(int length, String region) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegion(length, region);
	}
	
	
	@Override
	public List<Meetup> getMeetupByRegionCampusAndAll(int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegionCampusAndAll(length, region, campus);
	}
	
	@Override
	public List<Meetup> getAvailableMeetupByRegionXiaoyuan(int length, String region) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getAvailableMeetupByRegionXiaoyuan(length, region);
	}
	
	@Override
	public List<Meetup> getAvailableMeetupXiaoyuan(int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getAvailableMeetupXiaoyuan(length, region, campus);
	}
	
	@Override
	public List<Meetup> getMeetupByIdXiaoyuan(String group_id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByIdXiaoyuan(group_id);
	}
	
	@Override
	public List<Meetup> getMeetupByCategoryXiaoyuan(String category) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByCategoryXiaoyuan(category);
	}
	
	@Override
	public List<Meetup> getMeetupByRegionCategoryXiaoyuan(String category, int length, String region) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegionCategoryXiaoyuan(category, length, region);
	}
	
	@Override
	public List<Meetup> getMeetupByRegionCampusCategoryXiaoyuan(String category, int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegionCampusCategoryXiaoyuan(category, length, region, campus);
	}
	
	@Override
	public List<Meetup> getMeetupByRegionCampusAllCategoryXiaoyuan(String category, int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByRegionCampusAllCategoryXiaoyuan(category, length, region, campus);
	}

	@Override
	public List<Meetup> getAvailableMeetupByCategoryXiaoyuan(String category) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getAvailableMeetupByCategoryXiaoyuan(category);
	}

	@Override
	public int addMeetup(Meetup meetup) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.addMeetup(meetup);
	}

	@Override
	public int deleteMeetupByGroupId(String group_id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.deleteMeetupByGroupId(group_id);
	}
	
	@Override
	public int restoreMeetupByGroupId(String group_id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.restoreMeetupByGroupId(group_id);
	}

	@Override
	public int deleteMeetupById(int id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.deleteMeetupById(id);
	}
	
	@Override
	public int restoreMeetupById(int id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.restoreMeetupById(id);
	}

	@Override
	public List<GroupBuy> getAllGroupBuyByRegionCampus(String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getAllGroupBuyByRegionCampus(region, campus);
	}
	
	@Override
	public List<GroupBuy> getGroupBuyByRegionCampus(int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getGroupBuyByRegionCampus(length, region, campus);
	}
	
	@Override
	public List<GroupBuy> getGroupBuyByRegionCampusAndAll(int length, String region, String campus) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getGroupBuyByRegionCampusAndAll(length, region, campus);
	}

	@Override
	public List<GroupBuy> getGroupBuyByIdXiaoyuan(String id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getGroupBuyByIdXiaoyuan(id);
	}

	@Override
	public int deleteGroupBuyById(int id) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.deleteGroupBuyById(id);
	}

	@Override
	public List<Meetup> getMeetupByReleaseOpenidXiaoyuan(String openid) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByReleaseOpenidXiaoyuan(openid);
	}

	@Override
	public List<Meetup> getMeetupByJoinOpenidXiaoyuan(String openid) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.getMeetupByJoinOpenidXiaoyuan(openid);
	}

	@Override
	public int updateMeetupJoiner(String group_id, String openid) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.updateMeetupJoiner(group_id, openid);
	}

	@Override
	public int addMeetupJoiner(String group_id, String openid) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.addMeetupJoiner(group_id, openid);
	}

	@Override
	public int addGroupBuy(GroupBuy groupbuy) {
		// TODO Auto-generated method stub
		return xiaoyuanDao.addGroupBuy(groupbuy);
	}

	



	
	
	
}
