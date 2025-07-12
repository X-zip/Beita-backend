package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.AccessCode;
import com.example.demo.model.AccessCodePaotui;
import com.example.demo.model.Rider;
import com.example.demo.model.Secret;
import com.example.demo.model.TaskPaotui;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.PaotuiService;
import com.example.demo.service.QuanziService;
import com.example.demo.service.SubscriptionService;

import utils.HttpRequest;

@Controller
public class Subscription {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private PaotuiService paotuiService;
	
	@Autowired
	private CaicaiService caicaiService;
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private QuanziService quanziService;
	
	private final String appid = "[APPID]"; // 公众号
	
	
	private Map<String, String> getAppIdAccessToken(String region, String campusGroup, String name){
		Map<String, String> res = new HashMap<>();
		List<AccessCode> accessCode;
		if (region.equals("9")){
			accessCode = subscriptionService.getCodeCtimeById(System.currentTimeMillis()-5400*1000, region);
      	} else if (region.equals("sg")){
      		accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
      	} else {
      		accessCode = subscriptionService.getCodeCtimeById(System.currentTimeMillis()-5400*1000, "4");
      	} 
		
		String accessToken= "";
		
		// 默认为校不校园,secret id=4
		String appid_mini = "[APPID]";
		String secret_mini = "[SECRET]";
		if (region.equals("9")){
			appid_mini = "[APPID]";
      		secret_mini = "[SECRET]";
      	} else if (region.equals("sg")) {
      		appid_mini = "[APPID]";
      		secret_mini = "[SECRET]";
      	} 
		
		// 获取accesstoken
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println(name + " Subscription刷新token region:"+region+",campusGroup:"+campusGroup);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid="+appid_mini+"&secret="+secret_mini;
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	        System.out.println("json:"+json);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			if (region.equals("9")) {
 	 				subscriptionService.saveCodeById(accessToken,System.currentTimeMillis(), region);
 	 			} else if (region.equals("sg")) {
 	 				caicaiService.saveCode(accessToken, System.currentTimeMillis());
 	 			} else {
 	 				subscriptionService.saveCodeById(accessToken,System.currentTimeMillis(), "4");
 	 			}
 	 		}	
 		}
 		
 		res.put("appid_mini", appid_mini);
 		res.put("secret_mini", secret_mini);
 		res.put("accessToken", accessToken);
 		return res;
 		
	}
	
	@RequestMapping(value="/newTaskSub")
	@ResponseBody
    public  Object newTaskSub(@RequestParam (value = "page")String page,
							  @RequestParam (value = "category")String category,
							  @RequestParam (value = "address_to")String address_to,
							  @RequestParam (value = "order_id")String order_id,
							  @RequestParam (value = "template_id")String template_id,
							  @RequestParam (value = "region", required=false) String region,
					    	  @RequestParam (value = "campusGroup", required=false)String campusGroup){ 

		Map<String, String> appdetail = getAppIdAccessToken(region, campusGroup, "newTaskSub");
		String appid_mini = appdetail.get("appid_mini");
		String accessToken = appdetail.get("accessToken");
		
 		String order_type = category; 		
 		
 		Map<String,Object> paramMap = new HashMap<>();
        
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> value4 = new HashMap<>();
        Map<String,Object> value5 = new HashMap<>();
        Map<String,Object> value6 = new HashMap<>();
        Map<String,Object> value7 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> miniprogram = new HashMap<>();
        miniprogram.put("appid", appid_mini);
        miniprogram.put("pagepath", page);
        value1.put("value","有新用户下单");
        data.put("first",value1);
        value2.put("value",order_type);
        data.put("keyword1",value2);
        value3.put("value","接单查看详情");
        data.put("keyword2",value3);
        value4.put("value","接单查看详情");
        data.put("keyword3",value4);
        value5.put("value","接单查看详情");
        data.put("keyword4",value5);
        value6.put("value",address_to);
        data.put("keyword5",value6);
        value7.put("value","");
        data.put("remark",value7);
        
        System.out.println("模板消息测试");
        System.out.println(data.toString());
        
        mp_template_msg.put("appid", appid);
        mp_template_msg.put("template_id", template_id);
        mp_template_msg.put("url", "x");
        mp_template_msg.put("miniprogram", miniprogram);
        mp_template_msg.put("data", data);
        paramMap.put("mp_template_msg", mp_template_msg);
        JSONObject jsonObject2;
        
 		Map<String,Object>map=new HashMap<>();
 		map.put("details", "");
 		
 		List<Rider> vRiderList = paotuiService.getAllVerifiedRider();
 		System.out.println("骑手数："+vRiderList.size());
 		for (Rider r : vRiderList) {
 			System.out.println("当前推送用户："+r.getOpenid());
 			paramMap.put("touser",r.getOpenid());
 			try {
 				URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token=" + accessToken);
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
 				printWriter.write(JSON.toJSONString(paramMap));
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
 				System.out.println("返回值："+jsonObject2);
 		        
 				map.put("result",jsonObject2);
 		        
 		        String tmp = (String) map.get("details");
 		        tmp = tmp + ";" + r.getOpenid() + ":" + jsonObject2.toString();
 		        map.put("details", tmp);
 		        
 			} catch (MalformedURLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			    map.put("result","error");       
 			} catch (ProtocolException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			    map.put("result","error");       
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			    map.put("result","error");       
 			    
 			}
 			
 		}
 		

		
		return map;

    }
	

	@RequestMapping(value="/riderPickupSub", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
    public  Object riderPickupSub(@RequestParam (value = "page")String page,
					    		  @RequestParam (value = "order_id")String order_id,
					    		  @RequestParam (value = "template_id")String template_id,
								  @RequestParam (value = "region", required=false) String region,
						    	  @RequestParam (value = "campusGroup", required=false)String campusGroup){ 
		Map<String, String> appdetail = getAppIdAccessToken(region, campusGroup, "riderPickupSub");
		String appid_mini = appdetail.get("appid_mini");
		String accessToken = appdetail.get("accessToken");
 		
 		List<TaskPaotui> currentTaskL = paotuiService.gettaskbyOrderId(order_id);
 		TaskPaotui currentTask;
 		Rider rider;
 		if (currentTaskL.size() > 0) {
 			currentTask = currentTaskL.get(0);
 			List<Rider> riderResult = paotuiService.getRiderByOpenid(currentTask.getPickup_openid());
 			if (riderResult.size()>0) {
 				rider = riderResult.get(0);
 			} else {
 				Map<String,Object>map=new HashMap<>();
 			    map.put("result","error");       
 			    return map;
 			}
 		} else {
 			Map<String,Object>map=new HashMap<>();
		    map.put("result","error");       
		    return map;
 		}
 		
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        
        value1.put("value",order_id);
        data.put("character_string1",value1);
        value2.put("value", "CNY"+(Math.round(currentTask.getPrice())/ 100));
        data.put("amount2",value2);
        value3.put("value",rider.getWechat()); // 推送给发单人骑手的微信号
        data.put("thing8",value3);
        
        
        System.out.println("模板消息测试");
        System.out.println(data.toString());
        
        
        mp_template_msg.put("touser", currentTask.getRelease_openid());
        mp_template_msg.put("template_id", template_id);
        mp_template_msg.put("page", page);
        mp_template_msg.put("data", data);
        mp_template_msg.put("miniprogram_state", "formal");
        mp_template_msg.put("lang", "zh_CN");
        
        System.out.println("mp_template_msg:"+mp_template_msg.toString());
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
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
			printWriter.write(JSON.toJSONString(mp_template_msg));
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
			System.out.println("jsonObject2:"+jsonObject2);
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
	
	
	@RequestMapping(value="/riderCompleteSub", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
    public  Object riderCompleteSub(@RequestParam (value = "page")String page,
					    		  	@RequestParam (value = "order_id")String order_id,
					    		  	@RequestParam (value = "template_id")String template_id,
									@RequestParam (value = "region", required=false) String region,
							    	@RequestParam (value = "campusGroup", required=false)String campusGroup){ 
		Map<String, String> appdetail = getAppIdAccessToken(region, campusGroup, "riderCompleteSub");
		String appid_mini = appdetail.get("appid_mini");
		String accessToken = appdetail.get("accessToken");
 		
 		List<TaskPaotui> currentTaskL =paotuiService.gettaskbyOrderId(order_id);
 		TaskPaotui currentTask;
 		if (currentTaskL.size() > 0) {
 			currentTask = currentTaskL.get(0);
 		} else {
 			Map<String,Object>map=new HashMap<>();
		    map.put("result","error");       
		    return map;
 		}
 		
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> value4 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        
        value1.put("value",order_id);
        data.put("character_string1",value1);
        value2.put("value", "CNY"+(Math.round(currentTask.getPrice())/ 100.0));
        data.put("amount2",value2);
        value3.put("value",currentTask.getComplete_datetime());
        data.put("time3",value3);
        value4.put("value","骑手已完成订单，请前往确认。");
        data.put("thing4",value4);
        
        System.out.println("riderCompleteSub模板消息测试");
        System.out.println(data.toString());
        
        mp_template_msg.put("touser", currentTask.getRelease_openid());
        mp_template_msg.put("template_id", template_id);
        mp_template_msg.put("page", page);
        mp_template_msg.put("data", data);
        mp_template_msg.put("miniprogram_state", "formal");
        mp_template_msg.put("lang", "zh_CN");
        
        System.out.println("mp_template_msg:"+mp_template_msg.toString());
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
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
			printWriter.write(JSON.toJSONString(mp_template_msg));
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
			System.out.println("jsonObject2:"+jsonObject2);
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
	
	
	@RequestMapping(value="/orderCompleteSub", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
    public  Object orderCompleteSub(@RequestParam (value = "page")String page,
					    		  @RequestParam (value = "order_id")String order_id,
					    		  @RequestParam (value = "template_id")String template_id,
								  @RequestParam (value = "region", required=false) String region,
						    	  @RequestParam (value = "campusGroup", required=false)String campusGroup){ 
		Map<String, String> appdetail = getAppIdAccessToken(region, campusGroup, "orderCompleteSub");
		String appid_mini = appdetail.get("appid_mini");
		String accessToken = appdetail.get("accessToken");
 		
 		List<TaskPaotui> currentTaskL =paotuiService.gettaskbyOrderId(order_id);
 		TaskPaotui currentTask;
 		if (currentTaskL.size() > 0) {
 			currentTask = currentTaskL.get(0);
 		} else {
 			Map<String,Object>map=new HashMap<>();
		    map.put("result","error");       
		    return map;
 		}
 		
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> value4 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        
        value1.put("value",order_id);
        data.put("character_string1",value1);
        value2.put("value","CNY"+(Math.round(currentTask.getPrice())/ 100));
        data.put("amount2",value2);
        value3.put("value",currentTask.getUpdate_datetime());
        data.put("time3",value3);
        value4.put("value","用户已确认订单完成。");
        data.put("thing4",value4);
        
        System.out.println("orderCompleteSub模板消息测试");
        System.out.println(data.toString());
        
        mp_template_msg.put("touser", currentTask.getPickup_openid());
        mp_template_msg.put("template_id", template_id);
        mp_template_msg.put("page", page);
        mp_template_msg.put("data", data);
        mp_template_msg.put("miniprogram_state", "formal");
        mp_template_msg.put("lang", "zh_CN");
        
        System.out.println("mp_template_msg:"+mp_template_msg.toString());
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
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
			printWriter.write(JSON.toJSONString(mp_template_msg));
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
			System.out.println("jsonObject2:"+jsonObject2);
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
	
	
	@RequestMapping(value="/getDailySummary", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
    public  Object getDailySummary(@RequestParam (value = "region", required=false) String region,
						    	  @RequestParam (value = "campusGroup", required=false)String campusGroup){ 
		Map<String, String> appdetail = getAppIdAccessToken(region, campusGroup, "getDailySummary");
		String appid_mini = appdetail.get("appid_mini");
		String accessToken = appdetail.get("accessToken");
		
		Map<String, Object> para = new HashMap<>();
		
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	    String todayStr = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
	    String ystdStr = df.format(new Date(new Date().getTime()-24*60*60*1000));// new Date()为获取当前系统时间，也可使用当前时间戳
	    para.put("begin_date", ystdStr);
	    para.put("end_date", ystdStr);
//	    System.out.println("para:"+para);
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
//			System.out.println("jsonObject2:"+jsonObject2);
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
	
	private String getAccessTokenXiaoyuan(String appid, String secret, String region, String campus) {
		System.out.println("getAccessTokenXiaoyuan function刷新token，region:"+region+",campus:"+campus);
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String params = "grant_type=client_credential&appid="+appid+"&secret="+secret;
        String json = HttpRequest.sendGet(
        		url, params);
 		JSONObject jsonObject =  JSON.parseObject(json);
 		String accessToken = String.valueOf(jsonObject.get("access_token"));
 		if (accessToken!="null") {
 			if (region.equals("9")) {
	 				subscriptionService.saveCodeById(accessToken,System.currentTimeMillis(), region);
	 			} else if (region.equals("sg")) {
	 				caicaiService.saveCode(accessToken, System.currentTimeMillis());
	 			} else {
	 				subscriptionService.saveCodeById(accessToken,System.currentTimeMillis(), "4");
	 			}
 		}	
		return accessToken;
	}
	
	private JSONObject requestSendComment(String accessToken, Map<String,Object> paramMap) throws Exception {
		URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
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
		printWriter.write(JSON.toJSONString(paramMap));
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
		JSONObject jsonObject2 = JSON.parseObject(response.toString());
		return jsonObject2;
	}
	
	@RequestMapping(value="/sendPMNotifXiaoyuan", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
    public  Object sendPMNotifQuanzi(	@RequestParam (value = "openid")String openid,
					    		  @RequestParam (value = "page")String page,
					    		  @RequestParam (value = "time")String c_time,
					    		  @RequestParam (value = "campus")String campus,
					    		  @RequestParam (value = "region")String region,
					    		  @RequestParam (value = "template_id")String template_id,
    							  @RequestParam (value = "appid")String appid){
		if (appid.equals("[APPID]")) {
			campus = "4";
		} else if (appid.equals("[APPID]")) {
			campus = "9";
		}
		
		Map<String, String> appdetail = getAppIdAccessToken(region, campus, "sendPMNotifXiaoyuan");
		String appid_mini = appdetail.get("appid_mini");
		String secret_mini = appdetail.get("secret_mini");
		String accessToken = appdetail.get("accessToken");
		
//        // 检测
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("touser",openid);
        paramMap.put("template_id",template_id);
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        value1.put("value","私聊");
        data.put("thing1",value1);
        value2.put("value","您有新的私聊消息！");
        data.put("thing2",value2);
        value3.put("value",c_time);
        data.put("date3",value3);
        paramMap.put("data",data);
        paramMap.put("page",page);
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken);
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
			printWriter.write(JSON.toJSONString(paramMap));
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
						
			// check return code
			if (jsonObject2.getString("errcode").equals("40001")){
				accessToken = getAccessTokenXiaoyuan(appid,secret_mini,region,campus);
				System.out.println("sendPMNotifXiaoyuan失败，重新刷新token，campus:"+campus);
				try {
					jsonObject2 = requestSendComment(accessToken,paramMap);
				} catch (Exception e) {
					e.printStackTrace();
					Map<String,Object>map=new HashMap<>();
				    map.put("result","error");       
				    return map;
				}
			}
			
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
	
	
}
