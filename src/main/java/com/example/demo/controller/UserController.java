package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.AccessCode;
import com.example.demo.model.Secret;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.QuanziService;

import utils.HttpRequest;
import utils.SessionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

@Controller
public class UserController {
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private CaicaiService caicaiService;
	
	@Autowired
	private QuanziService quanziService;
	
    @GetMapping({"/login"})
    public  Object login(HttpServletRequest request,@RequestParam Map<String, Object> params) throws ParseException{
    	Map<String, Object> rtnMap = new HashMap<String, Object>();
    	String account = String.valueOf(params.get("account"));
    	String password = String.valueOf(params.get("password"));
    	List<User> userList = quanziService.getUser(account,password);
    	if (account.equals("")) {
    		request.setAttribute("code", 500);
    	} else if (userList.size()>0) {
    		Date date=new Date();//此时date为当前的时间
    		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    		System.out.println(dateFormat.format(date));
    		Date expire_date = dateFormat.parse(userList.get(0).getExpired());
    		int days = (int) ((expire_date.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
    		request.setAttribute("days", days);
    		if(days <= 0 ) {
    			request.setAttribute("code", 300);
    		} else {
    			request.setAttribute("code", 200);
    			SessionUtils.setUser(request,userList.get(0));
    			
    			String campus = userList.get(0).getCampus();
    			String page_visit_pv = "";
        		String page_visit_uv = "";
        		Map<String, Object> summary = getDailySummary(campus);
        		JSONArray page_list = JSON.parseObject(summary.get("result").toString()).getJSONArray("list");
        		for (int i = 0; i < page_list.size(); i++) {
        			JSONObject page = JSON.parseObject(page_list.get(i).toString());
        			if (page.getString("page_path").equals("pages/index/index")) {
        				page_visit_pv = String.valueOf(page.get("page_visit_pv"));
        				page_visit_uv = String.valueOf(page.get("page_visit_uv"));
        			}
        		}
        		request.setAttribute("page_visit_pv", page_visit_pv);
        		request.setAttribute("page_visit_uv", page_visit_uv);
    			
    		}
    	} else {
    		request.setAttribute("code", 400);
        }
    	return "welcome";
    }
    
    private String getAccessToken(String campus, String name){
    	Map<String, String> res = new HashMap<>();
    	List<AccessCode> accessCode = new ArrayList<>();
    	if (campus.equals("beita")) {
    		accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    	} else if (campus.equals("sg")) {
    		accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)) {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,"4");
    	} else {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,campus);
    	}
    	
    	String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println(name+" 刷新token，campus:"+campus);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String pa = ""; 
 	        if (campus.equals("beita")) {
 	        	pa = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
        	} else if (campus.equals("sg")) {
        		pa = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
        	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)){
        		Secret secretList = quanziService.getSecretByCampus("4").get(0);
        		pa = "grant_type=client_credential&appid="+secretList.getAppid()+"&secret="+secretList.getSecret();
        	} else {
        		Secret secretList = quanziService.getSecretByCampus(campus).get(0);
        		pa = "grant_type=client_credential&appid="+secretList.getAppid()+"&secret="+secretList.getSecret();
        	}
 	        String json = HttpRequest.sendGet(url, pa);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			if (campus.equals("beita")) {
 	 				beitaService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else if (campus.equals("sg")) {
 	        		caicaiService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)){
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),"4");
 	        	} else {
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),campus);
 	        	}
 	 			
 	 		}	
 		}
 		return accessToken;
    }
      
    private  Map<String, Object> getDailySummary(String campus){ 
    	String accessToken = getAccessToken(campus, "getDailySummary");
		
		Map<String, Object> para = new HashMap<>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String todayStr = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		String ystdStr = df.format(new Date(new Date().getTime()-24*60*60*1000));// new Date()为获取当前系统时间，也可使用当前时间戳
		para.put("begin_date", ystdStr);
		para.put("end_date", ystdStr);
//		System.out.println("para:"+para);
		JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/datacube/getweanalysisappidvisitpage?access_token=" + accessToken);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
			httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8"); 
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			printWriter.write(JSON.toJSONString(para));
			// flush输出流的缓冲
			printWriter.flush();
			//开始获取数据
			BufferedReader in = new BufferedReader(
			new InputStreamReader(httpURLConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				jsonObject2 = JSON.parseObject(response.toString());
//				System.out.println("jsonObject2:"+jsonObject2);
				Map<String,Object>map=new HashMap<>();
				map.put("result",jsonObject2);       
				return map;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map<String,Object>map=new HashMap<>();
			map.put("result","error");       
			return map;
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map<String,Object>map=new HashMap<>();
			map.put("result","error");       
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map<String,Object>map=new HashMap<>();
			map.put("result","error");       
			return map;
		}	
	
	}
    
    @GetMapping({"/logout"})
    public  Object logout(HttpServletRequest request,@RequestParam Map<String, Object> params) throws ParseException{
    	Map<String, Object> rtnMap = new HashMap<String, Object>();
    	SessionUtils.removeUser(request);
    	return "welcome";
    }

}
