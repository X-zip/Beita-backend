package com.example.demo.service.impl;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Avatar;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.NickName;
import com.example.demo.model.Secret;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.dao.QuanziDao;
import com.example.demo.dao.SettingDao;
import com.example.demo.service.QuanziService;
import com.example.demo.service.SettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service(value = "SettingService")
public class SettingImpl implements SettingService{
	@Autowired
	SettingDao settingDao;

	@Override
	public List<NickName> getAllNickName(String campus) {
		// TODO Auto-generated method stub
		return settingDao.getallNickname(campus);
	}
	
	@Override
	public int deleteNickName(String campus, int id) {
		return settingDao.deleteNickName(campus, id);
	}
	
	@Override
	public int addNickName(String campus, String nickname) {
		return settingDao.addNickName(campus, nickname);
	}
	
	@Override
	public List<Avatar> getAllAvatar(String campus) {
		// TODO Auto-generated method stub
		return settingDao.getallAvatar(campus);
	}
	
	@Override
	public int deleteAvatar(String campus, int id) {
		return settingDao.deleteAvatar(campus, id);
	}
	
	@Override
	public int addAvatar(String campus, String nickname) {
		return settingDao.addAvatar(campus, nickname);
	}
	
	
}
