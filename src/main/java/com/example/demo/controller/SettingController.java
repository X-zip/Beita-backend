package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.NickName;
import com.example.demo.model.Avatar;
import com.example.demo.model.Banner;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Member;
import com.example.demo.model.Secret;
import com.example.demo.model.Switch;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.service.SettingService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.QuanziService;

import utils.HttpRequest;
import utils.Result;
import utils.ResultGenerator;
import utils.SessionUtils;

import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import com.google.gson.Gson;

@Controller
public class SettingController {
	
	@Autowired
	private QuanziService quanziService;
	
	@Autowired
	private SettingService settingService;
	
	
	public HttpServletRequest getAllDefault(HttpServletRequest request, String campus){
		List<NickName> nickNameList = settingService.getAllNickName(campus);
		request.setAttribute("nickNameList", nickNameList);
		
		List<Avatar> avatarList = settingService.getAllAvatar(campus);
		request.setAttribute("avatarList", avatarList);
		return request;
	}
	
	
	@GetMapping({"deleteNickname"})
	public Object deleteNickname(HttpServletRequest request,@RequestParam Map<String, Object> params){
		String campus = SessionUtils.getUser(request).getCampus();
		String id = String.valueOf(params.get("id"));
		settingService.deleteNickName(campus, Integer.parseInt(id));
		
		request = getAllDefault(request, campus);
        return "system";
		
	}
	
	@GetMapping({"addNickname"})
	public Object addNickname(HttpServletRequest request,@RequestParam Map<String, Object> params){
		String campus = SessionUtils.getUser(request).getCampus();
		String nickname = String.valueOf(params.get("nickname"));
		settingService.addNickName(campus, nickname);
		
		request = getAllDefault(request, campus);
		return "system";
	}
	
	
	@GetMapping({"deleteAvatar"})
	public Object deleteAvatar(HttpServletRequest request,@RequestParam Map<String, Object> params){
		String campus = SessionUtils.getUser(request).getCampus();
		String id = String.valueOf(params.get("id"));
		settingService.deleteAvatar(campus, Integer.parseInt(id));
		
		request = getAllDefault(request, campus);
		return "system";
	}
	
	@GetMapping({"addAvatar"})
	public Object addAvatar(HttpServletRequest request,@RequestParam Map<String, Object> params){
		String campus = SessionUtils.getUser(request).getCampus();
		String avatar = String.valueOf(params.get("avatar"));
		settingService.addAvatar(campus, avatar);
		
		request = getAllDefault(request, campus);
		return "system";
	} 
  
    
	@GetMapping({"/addSwitch"})
	public Object addSwitch(HttpServletRequest request,@RequestParam Map<String, Object> params){
		String campus = SessionUtils.getUser(request).getCampus();
		String status = String.valueOf(params.get("status"));
		List<Switch> switchList = new ArrayList<Switch>();
    	switchList = quanziService.getSwitchStatus(campus);
		System.out.println(status);
		if (switchList.size() <= 0) {
			int addcode=quanziService.addSwitch(campus,status);
    	} else {
    		int addcode=quanziService.updateSwitch(campus,status);
    	}
		request.setAttribute("switchStatus", status);
		return "system";
	}
	
    @GetMapping({"/systemSetting"})
    public  Object systemSetting(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Switch> switchList = new ArrayList<Switch>();
    	switchList = quanziService.getSwitchStatus(campus);
    	if (switchList.size() <= 0) {
    		request.setAttribute("switchStatus", "false");
    	} else {
    		request.setAttribute("switchStatus", switchList.get(0).getVerify());
    	}
    	List<NickName> nickNameList = settingService.getAllNickName(campus);
		request.setAttribute("nickNameList", nickNameList);
		
		List<Avatar> avatarList = settingService.getAllAvatar(campus);
		request.setAttribute("avatarList", avatarList);
        return "system";
    }

}
