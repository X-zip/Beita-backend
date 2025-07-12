package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PaotuiDao;
import com.example.demo.model.AccessCode;
import com.example.demo.model.TaskPaotui;
import com.example.demo.service.SubscriptionService;

@Service(value = "SubscriptionService")
public class SubscriptionImpl implements SubscriptionService{
	@Autowired
	PaotuiDao paotuiDao;
	
	@Override
	public  List<AccessCode> getCodeCtime(Long c_time) {
		// TODO Auto-generated method stub
		return paotuiDao.getCodeCtime(c_time);
	}
	
	@Override
	public  List<AccessCode> getCodeCtimeById(Long c_time, String id) {
		// TODO Auto-generated method stub
		return paotuiDao.getCodeCtimeById(c_time, id);
	}
	
	@Override
	public  int saveCode(String code,Long c_time) {
		// TODO Auto-generated method stub
		return paotuiDao.saveCode(code,c_time);
	}
	
	@Override
	public  int saveCodeById(String code,Long c_time, String id) {
		// TODO Auto-generated method stub
		return paotuiDao.saveCodeById(code,c_time,id);
	}
	
}
