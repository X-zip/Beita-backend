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
import com.example.demo.dao.PaopaoDao;
import com.example.demo.dao.PaotuiDao;
import com.example.demo.service.PaopaoService;
import com.example.demo.service.PaotuiService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(value = "paopaoService")
public class PaopaoImpl  implements PaopaoService{
	@Autowired
	PaopaoDao paopaoDao;
	
	
	@Override
	public int authRegister(String username,String password) {
		// TODO Auto-generated method stub
		return paopaoDao.authRegister(username, password);
	}
	
}

