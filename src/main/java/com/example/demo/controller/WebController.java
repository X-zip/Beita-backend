package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.GroupBuy;
import com.example.demo.model.Meetup;
import com.example.demo.model.Member;
import com.example.demo.model.QR;
import com.example.demo.model.Secret;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.model.TestAppid;
import com.example.demo.model.VerifyUser;
import com.example.demo.model.Rider;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.QuanziService;
import com.example.demo.service.XiaoyuanService;
import com.example.demo.model.WXTemplate;

import utils.HttpRequest;
import utils.Result;
import utils.ResultGenerator;
import utils.SessionUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.google.gson.Gson;

@Controller
public class WebController {
	
	@Autowired
	private QuanziService quanziService;
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private CaicaiService caicaiService;
	
	@Autowired
	private XiaoyuanService xiaoyuanService;
	
    @GetMapping({"/welcome"})
    public  Object welcome(HttpServletRequest request) throws ParseException{
    	if(null != SessionUtils.getUser(request)){
    		request.setAttribute("code", 200);
    		Date date=new Date();//此时date为当前的时间
    		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    		Date expire_date = dateFormat.parse(SessionUtils.getUser(request).getExpired());
    		int days = (int) ((expire_date.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
    		request.setAttribute("code", 200);
    		request.setAttribute("days", days);
    		
    		String campus = SessionUtils.getUser(request).getCampus();
    		Map<String, Object> summary = getDailySummary(campus);
//    		System.out.println("summary:"+summary);
    		
    	} else {
    		request.setAttribute("code", 500);
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
        	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)) {
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
    
    private Map<String, String> getAccessTokenAppid(String campus, String name){
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
    	
    	Map<String, String> allreturnvalue = new HashMap<>();
    	
    	// get appid
    	String appid = "";
        if (campus.equals("beita")) {
        	appid = "[APPID]";
    	} else if (campus.equals("sg")) {
    		appid = "[APPID]";
    	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)) {
    		Secret secretList = quanziService.getSecretByCampus("4").get(0);
    		appid = secretList.getAppid();
    	} else {
    		Secret secretList = quanziService.getSecretByCampus(campus).get(0);
    		appid = secretList.getAppid();
    	}
        
        // get accesstoken
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
        	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)) {
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
 		allreturnvalue.put("accessToken", accessToken);
 		allreturnvalue.put("appid", appid);
 		return allreturnvalue;
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
    
    @GetMapping({"/getTaskForWX"})
    public  Object getTaskForWX(HttpServletRequest request,@RequestParam Map<String, Object> params){
    	String keyword = String.valueOf(params.get("keyword"));
    	String start = String.valueOf(params.get("start"));
    	String end = String.valueOf(params.get("end"));
    	String type1 = String.valueOf(params.get("type1"));
    	String type2 = String.valueOf(params.get("type2"));
    	String order = String.valueOf(params.get("order"));
    	String length = String.valueOf(params.get("length"));
    	List<String> radioGroup = new ArrayList<>();
    	if (length.equals("null")) {
    		length = "0";
    	}
    	if (start.equals("null") || start.equals("")) {
    		start = "2022/01/01 ";
    	} else {
    		start = start.replaceAll("-", "/").replaceAll("T", " ");
    	}
    	if (end.equals("null") || end.equals("")) {
    		end = "3099/01/01";
    	} else {
    		end = end.replaceAll("-", "/").replaceAll("T", " ");
    	}
    	if (order.equals("null")) {
    		order = "time";
    	}
    	if (type1.equals("1") && type2.equals("null")) {
    		String[] strs = {"rent","owner","tenent","radio1","radio2","radio3","radio5","radio6","radio7","radio10", "radio11", "radio12", "radio13", "radio14", 
    				"radio15","radio16", "radio17", "radio18","radio20", "radio19","radio21","career","job","refer","study","class","question","tutor","roommate",
    				"social","eat","play","help","pinche","qiwen","weilai","campusStory","socialStory","idolStory","求助","求职","闲置","租房"};
    		radioGroup= Arrays.asList(strs);
    	} else if(type1.equals("null") && type2.equals("2")) {
    		String[] strs = {"radio40","radio41", "radio42","radio43","树洞"};
    		radioGroup= Arrays.asList(strs);
    	} else {
    		String[] strs = {"rent","owner","tenent","radio1","radio2","radio3","radio5","radio6","radio7","radio10", "radio11", "radio12", "radio13", "radio14", 
    				"radio15","radio16", "radio17", "radio18","radio20", "radio19","radio21","radio40","radio41", "radio42","radio43","career","job","refer",
    				"study","class","question","tutor","roommate","social","eat","play","help","pinche","qiwen","weilai","campusStory","socialStory","idolStory",
    				"树洞","求助","求职","闲置","租房"};
    		radioGroup= Arrays.asList(strs);
    	}
    	String campus = SessionUtils.getUser(request).getCampus();
    	if (keyword.equals("null") || keyword.equals("")) {
    		keyword="";
    	}
    	List<Task> taskList = new ArrayList<Task>();
    	if (campus.equals("9") || campus.equals("10")) {
    		taskList =quanziService.getAllTaskForWXByRegion(length,campus,start,end,radioGroup,order,keyword);
    	} else {
    		taskList =quanziService.getAllTaskForWX(length,campus,start,end,radioGroup,order,keyword);
    	}
        request.setAttribute("taskList", taskList);
        request.setAttribute("curPage", Integer.parseInt(length)/50);
        request.setAttribute("number", taskList.size());
        request.setAttribute("start", start);
        request.setAttribute("end", end);
        request.setAttribute("type1", type1);
        request.setAttribute("type2", type2);
        request.setAttribute("order", order);
        return "allTask";
    }
    
    @GetMapping({"/getSelected"})
    public  Object getSelected(HttpServletRequest request,@RequestParam Map<String, Object> params) throws MalformedURLException, ProtocolException{ 
        String result = String.valueOf(params.get("id"));
        List<String> idList = new ArrayList<String>();
        Collections.addAll(idList,result.split(","));
        List<TestAppid> selectList = new ArrayList<>();
        List<AccessCode> accessCode = new ArrayList<>();
        String campus = SessionUtils.getUser(request).getCampus();
        
        Map<String, String> accessTokenAppid = getAccessTokenAppid(campus, "getSelected");
    	String accessToken = accessTokenAppid.get("accessToken").toString();
    	String appid = accessTokenAppid.get("appid").toString();
        System.out.println("appid:"+appid);
    	
        if (idList.size()>0) {
        	for(String id : idList ) {
        		List<Task> taskList = new ArrayList<>();
        		if (campus.equals("beita")) {
        			taskList =beitaService.gettaskbyId(Integer.parseInt(id));
 	        	} else if (campus.equals("sg")) {
 	        		taskList =caicaiService.gettaskbyId(Integer.parseInt(id));
 	        	} else {
 	        		taskList =quanziService.gettaskbyId(Integer.parseInt(id));
 	        	}
        		String page = "pages/detail/detail?id=";
        	
                TestAppid test=new TestAppid();
	            test.setC_time(taskList.get(0).getC_time());
	            test.setCommentNum(taskList.get(0).getCommentNum());
	            test.setContent(taskList.get(0).getContent());
	            test.setRadioGroup(taskList.get(0).getRadioGroup());
//    		            test.setImg(PIC_URL + newFileName);
	            test.setImg(page+id);
	            test.setAppid(appid);
	            if (!taskList.get(0).getImg().equals("")) {
	            	test.setCover(taskList.get(0).getImg().split(",")[0]);
	            } else {
	            	test.setCover("");
	            }	            
	            test.setPrice(taskList.get(0).getPrice());
	            selectList.add(test);

		        }
	        }      
        request.setAttribute("selectList", selectList);
        return "copy";
    }
    
    @GetMapping({"/addChoose"})
    public  Object addChoose(HttpServletRequest request,@RequestParam Map<String, Object> params){
    	String length = String.valueOf(params.get("length"));
    	if (length.equals("null")) {
    		length = "0";
    	}
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Task> taskList = new ArrayList<Task>();
    	if (campus.equals("9") || campus.equals("10")) {
    		taskList =quanziService.taskManagementByRegion(length,campus);
    	} else {
    		taskList =quanziService.taskManagement(length,campus);
    	}
        request.setAttribute("taskList", taskList);
        request.setAttribute("curPage",0 );
        request.setAttribute("number", taskList.size());
        return "choose";
    }
    
    
    @GetMapping({"/getChooseBySearch"})
    public  Object getChooseBySearch(HttpServletRequest request,@RequestParam (value = "search")String search){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Task> taskList = new ArrayList<Task>();
    	if(campus.equals("beita")) {
    		taskList = beitaService.getChooseBySearch(search);
    	} else if (campus.equals("sg")){
    		taskList = caicaiService.getChooseBySearch(search);
    	} else {
    		if (campus.equals("9") || campus.equals("10")) {
    			taskList = quanziService.getChooseBySearchByRegion(search,campus);
        	} else {
        		taskList = quanziService.getChooseBySearch(search,campus);
        	}
    		
    	}      
    	request.setAttribute("taskList", taskList);
        request.setAttribute("curPage",0 );
        return "choose";
    }
    
    @GetMapping({"/addChooseList"})
    public  Object addChooseList(HttpServletRequest request,@RequestParam Map<String, Object> params){ 
        String result = String.valueOf(params.get("id"));
        String choose = String.valueOf(params.get("choose"));
        List<String> idList = new ArrayList<String>();
        Collections.addAll(idList,result.split(","));
        String campus = SessionUtils.getUser(request).getCampus();
        if (campus.equals("beita")) {
            if (idList.size()>0) {
            	for(String id : idList ) {
            		beitaService.updateChoose(Integer.parseInt(id),Integer.parseInt(choose));
            	}
            }
     	} else if (campus.equals("sg")) {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		caicaiService.updateChoose(Integer.parseInt(id),Integer.parseInt(choose));
            	}
            }
     	} else {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		quanziService.updateChoose(Integer.parseInt(id),Integer.parseInt(choose),campus);
            	}
            }
     	}
        return "choose";
    }
    
    @GetMapping({"/addDeleteList"})
    public  Object addDeleteList(HttpServletRequest request,@RequestParam Map<String, Object> params){ 
        String result = String.valueOf(params.get("id"));
        String is_delete = String.valueOf(params.get("is_delete"));
        List<String> idList = new ArrayList<String>();
        Collections.addAll(idList,result.split(","));
        String campus = SessionUtils.getUser(request).getCampus();
        if (campus.equals("beita")) {
            if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
            			beitaService.deleteTask(Integer.parseInt(id));
            		} else {
            			beitaService.recoverTask(Integer.parseInt(id));
            		}
            	}
            }
     	} else if (campus.equals("sg")) {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
                		caicaiService.deleteTask(Integer.parseInt(id));
            		} else {
            			caicaiService.recoverTask(Integer.parseInt(id));
            		}
            	}
            }
     	} else {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
            			quanziService.deleteTask(Integer.parseInt(id));
            		} else {
            			quanziService.recoverTask(Integer.parseInt(id));
            		}
            	}
            }
     	}
        return "choose";
    }
    
    @GetMapping({"/addHideList"})
    public  Object addHideList(HttpServletRequest request,@RequestParam Map<String, Object> params){ 
        String result = String.valueOf(params.get("id"));
        String is_complaint = String.valueOf(params.get("is_complaint"));
        List<String> idList = new ArrayList<String>();
        Collections.addAll(idList,result.split(","));
        String campus = SessionUtils.getUser(request).getCampus();
        if (campus.equals("beita")) {
            if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_complaint.equals("1")) {
            			beitaService.hideTask(Integer.parseInt(id));
            		} else {
            			beitaService.recoverTaskHide(Integer.parseInt(id));
            		}
            	}
            }
     	} else if (campus.equals("sg")) {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_complaint.equals("1")) {
                		caicaiService.hideTask(Integer.parseInt(id));
            		} else {
            			caicaiService.recoverTaskHide(Integer.parseInt(id));
            		}
            	}
            }
     	} else {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_complaint.equals("1")) {
            			quanziService.hideTask(Integer.parseInt(id));
            		} else {
            			quanziService.recoverTaskHide(Integer.parseInt(id));
            		}
            	}
            }
     	}
        return "choose";
    }
    
    @GetMapping({"/addTopList"})
    public  Object addTopList(HttpServletRequest request,@RequestParam Map<String, Object> params){ 
        String result = String.valueOf(params.get("id"));
        String is_delete = String.valueOf(params.get("is_top"));
        List<String> idList = new ArrayList<String>();
        Collections.addAll(idList,result.split(","));
        String campus = SessionUtils.getUser(request).getCampus();
        if (campus.equals("beita")) {
            if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
            			beitaService.topTask(Integer.parseInt(id));
            		} else {
            			beitaService.downTask(Integer.parseInt(id));
            		}
            	}
            }
     	} else if (campus.equals("sg")) {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
                		caicaiService.topTask(Integer.parseInt(id));
            		} else {
            			caicaiService.downTask(Integer.parseInt(id));
            		}
            	}
            }
     	} else {
     		if (idList.size()>0) {
            	for(String id : idList ) {
            		if (is_delete.equals("1")) {
            			quanziService.topTask(Integer.parseInt(id));
            		} else {
            			quanziService.downTask(Integer.parseInt(id));
            		}
            	}
            }
     	}
        return "choose";
    }
    
    
    @GetMapping({"/blacklist"})
    public  Object getBlacklist(HttpServletRequest request,@RequestParam Map<String, Object> params){
    	String campus = SessionUtils.getUser(request).getCampus();
    	String length = String.valueOf(params.get("length"));
    	if (length.equals("null")) {
    		length = "0";
    	}
    	List<BlackList> blackList = new ArrayList<BlackList>();
    	blackList =quanziService.getBlackList(Integer.parseInt(length),campus);  
        request.setAttribute("blackList", blackList);
        request.setAttribute("curPage",Integer.parseInt(length)/10);
        request.setAttribute("number", blackList.size());
        return "blacklist"; 
    }
    
    @GetMapping({"/getOpenidBySearch"})
    public  Object getOpenidBySearch(HttpServletRequest request,@RequestParam (value = "search")String search){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Task> taskList = new ArrayList<Task>();
    	if(campus.equals("beita")) {
    		taskList =beitaService.getOpenidbySearch(search);
    	} else if (campus.equals("sg")){
    		taskList =caicaiService.getOpenidbySearch(search);
    	} else {
    		if (campus.equals("9") || campus.equals("10")) {
    			taskList =quanziService.getOpenidbySearchByRegion(search,campus); 
        	} else {
        		taskList =quanziService.getOpenidbySearch(search,campus);
        	} 
    	}
        request.setAttribute("taskList", taskList);
        List<BlackList> blackList = new ArrayList<BlackList>();
        blackList =quanziService.getBlackList(0,campus);  
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    
    @GetMapping({"/getOpenidBySearchComment"})
    public  Object getOpenidBySearchComment(HttpServletRequest request,@RequestParam (value = "search")String search){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Comment> commentList = new ArrayList<Comment>();
    	if(campus.equals("beita")) {
    		commentList =beitaService.getOpenidBySearchComment(search);
    	} else if (campus.equals("sg")){
    		commentList =caicaiService.getOpenidBySearchComment(search);
    	} else {
    		if (campus.equals("9") || campus.equals("10")) {
    			commentList =quanziService.getOpenidBySearchCommentByRegion(search,campus); 
        	} else {
        		commentList =quanziService.getOpenidBySearchComment(search,campus);
        	} 	
    	}
    	List<Task> taskList = new ArrayList<>();
    	for (int i = 0;i<commentList.size();i++) {
    		Task task=new Task();
    		int id = commentList.get(i).getId();
    		String comment = commentList.get(i).getComment();
    		String openid = commentList.get(i).getOpenid();
    		task.setId(id);
    		task.setTitle(comment);
    		task.setOpenid(openid);
    		taskList.add(task);
    	}
        request.setAttribute("taskList", taskList);
        List<BlackList> blackList = new ArrayList<BlackList>();
        blackList =quanziService.getBlackList(0,campus);  
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    @GetMapping({"/getAllByOpenid"})
    public  Object getAllByOpenid(HttpServletRequest request,@RequestParam (value = "search")String search){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Task> taskList = new ArrayList<Task>();
    	if(campus.equals("beita")) {
    		taskList =beitaService.getAllOpenid(search);
    	} else if (campus.equals("sg")){
    		taskList =caicaiService.getAllOpenid(search);
    	} else {
    		taskList =quanziService.getAllOpenid(search,campus);
    	}
    	System.out.println(campus);
    	System.out.println(search);
        request.setAttribute("taskList", taskList);
        System.out.println(campus);
        List<BlackList> blackList =quanziService.getBlackList(0,campus);  
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    
    @GetMapping({"/getAllCommentByOpenid"})
    public  Object getAllCommentByOpenid(HttpServletRequest request,@RequestParam (value = "search")String search){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Comment> commentList = new ArrayList<Comment>();
    	if(campus.equals("beita")) {
    		commentList =beitaService.getOpenidBySearchAllComment(search);
    	} else if (campus.equals("sg")){
    		commentList =caicaiService.getOpenidBySearchAllComment(search);
    	} else {
    		commentList =quanziService.getOpenidBySearchAllComment(search,campus);
    	}
    	List<Task> taskList = new ArrayList<>();
    	for (int i = 0;i<commentList.size();i++) {
    		Task task=new Task();
    		int id = commentList.get(i).getId();
    		String comment = commentList.get(i).getComment();
    		String openid = commentList.get(i).getOpenid();
    		task.setId(id);
    		task.setTitle(comment);
    		task.setOpenid(openid);
    		taskList.add(task);
    	}
        request.setAttribute("taskList", taskList);
        List<BlackList> blackList =quanziService.getBlackList(0,campus);
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    
    
    @GetMapping({"/addBlacklist"})
    public  Object addBlacklist(HttpServletRequest request,@RequestParam (value = "period")String period,@RequestParam (value = "description")String description,@RequestParam (value = "openid")String openid){
    	System.out.println(openid);
    	BlackList blacklist=new BlackList();
    	if (period.equals("0")) {
    		blacklist.setPeriod("1天");
    	} else if (period.equals("1")) {
    		blacklist.setPeriod("3天");
    	} else if (period.equals("2")) {
    		blacklist.setPeriod("7天");
    	} else {
    		blacklist.setPeriod("永久");
    	} 	
    	blacklist.setDescription(description);
    	blacklist.setOpenid(openid);
    	String campus = SessionUtils.getUser(request).getCampus();
    	int addcode=quanziService.addBlacklist(blacklist,campus);   
        List<BlackList> blackList =quanziService.getBlackList(0,campus);
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    @GetMapping({"/deleteBlacklist"})
    public  Object deleteBlacklist(HttpServletRequest request,@RequestParam (value = "id")int id){
        int addcode=quanziService.deleteBlacklist(id);
        String campus = SessionUtils.getUser(request).getCampus();
        List<BlackList> blackList =quanziService.getBlackList(0,campus);
        request.setAttribute("blackList", blackList);
        return "blacklist";
    }
    
    @GetMapping({"/bannerSetting"})
    public  Object getBannerlist(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Banner> bannerList = new ArrayList<Banner>();
    	if (campus.equals("beita")) {
    		bannerList = beitaService.getBanner();
    	} else if (campus.equals("sg")) {
    		bannerList = caicaiService.getBanner();
    	} else {
    		bannerList =quanziService.getBanner(campus);
    	}
        request.setAttribute("bannerList", bannerList);
        return "bannerSetting";
    }
    
    @GetMapping({"/addBanner"})
    public  Object addBanner(HttpServletRequest request,@RequestParam (value = "page")String page,@RequestParam (value = "imgPath")String imgPath,@RequestParam (value = "url")String url,@RequestParam (value = "weight")String weight){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Banner> bannerList = new ArrayList<Banner>();
    	System.out.println(url);
    	Banner ban = new Banner();
    	if (campus.equals("beita")) {
    		int addcode=beitaService.addBanner(imgPath,url.replace("转换","&"),weight);
    	} else if (campus.equals("sg")) {
    		int addcode=caicaiService.addBanner(imgPath,url.replace("转换","&"),weight);
    	} else {
    		int addcode=quanziService.addBanner(page,imgPath,url.replace("转换","&"),campus,weight);
    	}	
    	if (campus.equals("beita")) {
    		bannerList = beitaService.getBanner();
    	} else if (campus.equals("sg")) {
    		bannerList = beitaService.getBanner();
    	} else {
    		bannerList =quanziService.getBanner(campus);
    	}
        request.setAttribute("bannerList", bannerList);
        return "bannerSetting";
    }
    
    @GetMapping({"/deleteBanner"})
    public  Object deleteBanner(HttpServletRequest request,@RequestParam (value = "id")int id){
        
        String campus = SessionUtils.getUser(request).getCampus();
        System.out.println(campus);
        if (campus.equals("beita")) {
        	int addcode=beitaService.deleteBanner(id);
    	} else if (campus.equals("sg")) {
    		int addcode=caicaiService.deleteBanner(id);
    	} else {
    		int addcode=quanziService.deleteBanner(id);
    	}	
        List<Banner> bannerList = new ArrayList<Banner>();
        if (campus.equals("beita")) {
    		bannerList = beitaService.getBanner();
    	} else if (campus.equals("sg")) {
    		bannerList = caicaiService.getBanner();
    	} else {
    		bannerList =quanziService.getBanner(campus);
    	}
        request.setAttribute("bannerList", bannerList);
        return "bannerSetting";
    }
    
    
    @PostMapping({"/uploadPicture"})
    @ResponseBody
    public Result uploadViaQiniu(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        String ACCESS_KEY = "[ACCESS_KEY]";
        String SECRET_KEY = "[SECRET_KEY]";
        String BUCKET = "[BUCKET]";
        String PIC_URL = "[PIC_URL]/img/";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        
        try {
         byte[] uploadBytes = file.getBytes();
            try {
                Response response = uploadManager.put(uploadBytes, "quanzi/img/"+newFileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                Result resultSuccess = ResultGenerator.genSuccessResult();
                resultSuccess.setData(PIC_URL + newFileName);
                return resultSuccess;
            } catch (QiniuException ex) {
                Response r2 = ex.response;
                System.err.println(r2.toString());
                try {
                    System.err.println(r2.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                 return ResultGenerator.genFailResult("文件上传失败");
                }
                return ResultGenerator.genFailResult("文件上传失败");
            }
        } catch (Exception e) {
         //ignore
         return ResultGenerator.genFailResult("文件上传失败");
        }
    }
    
    @GetMapping({"/managementSetting"})
    public  Object managementSetting(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Member> memberList = new ArrayList<Member>();
    	if (campus.equals("beita")) {
    		memberList =beitaService.getAllMember();
    	} else if (campus.equals("sg")) {
    		memberList =caicaiService.getAllMember();
    	} else {
    		memberList =quanziService.getAllMember(campus);
    	}
    	System.out.println(memberList);
        request.setAttribute("memberList", memberList);
        return "managementSetting";
    }
    
    @GetMapping({"/addMember"})
    public  Object addMember(HttpServletRequest request,@RequestParam (value = "name")String name,@RequestParam (value = "openid")String openid){
    	String campus = SessionUtils.getUser(request).getCampus();
    	Member member = new Member();
    	member.setName(name);
    	member.setOpenid(openid);
    	if (campus.equals("beita")) {
    		int addcode=beitaService.addMember(member);
    	} else if (campus.equals("sg")) {
    		int addcode =caicaiService.addMember(member);
    	} else {
    		int addcode =quanziService.addMember(member,campus);
    	}
    	List<Member> memberList = new ArrayList<Member>();
    	if (campus.equals("beita")) {
    		memberList =beitaService.getAllMember();
    	} else if (campus.equals("sg")) {
    		memberList =caicaiService.getAllMember();
    	} else {
    		memberList =quanziService.getAllMember(campus);
    	}
    	System.out.println(memberList);
        request.setAttribute("memberList", memberList);
        return "managementSetting";
    }
    
    @GetMapping({"/deleteMember"})
    public  Object deleteMember(HttpServletRequest request,@RequestParam (value = "id")int id){
        int addcode=quanziService.deleteMember(id);
        String campus = SessionUtils.getUser(request).getCampus();
        List<Member> memberList = new ArrayList<Member>();
    	if (campus.equals("beita")) {
    		memberList =beitaService.getAllMember();
    	} else if (campus.equals("sg")) {
    		memberList =caicaiService.getAllMember();
    	} else {
    		memberList =quanziService.getAllMember(campus);
    	}
    	System.out.println(memberList);
        request.setAttribute("memberList", memberList);
        return "managementSetting";
    }
    
    @GetMapping({"/qrSetting"})
    public  Object getQrlist(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<QR> qrList = new ArrayList<QR>();
    	qrList =quanziService.getQR(campus);
        request.setAttribute("qrList", qrList);
        return "qrSetting";
    }
    
    @GetMapping({"/addQR"})
    public  Object addQR(HttpServletRequest request,@RequestParam (value = "imgPath")String imgPath){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<QR> qrList = new ArrayList<QR>();
    	QR qr = new QR();
    	int addcode=quanziService.addQR(imgPath,campus);
    	qrList =quanziService.getQR(campus);
    	request.setAttribute("qrList", qrList);
    	return "qrSetting";
    }
    
    @GetMapping({"/deleteQR"})
    public  Object deleteQR(HttpServletRequest request,@RequestParam (value = "id")int id){
        int addcode=quanziService.deleteQR(id);
        String campus = SessionUtils.getUser(request).getCampus();
        List<QR> qrList = new ArrayList<QR>();
        qrList =quanziService.getQR(campus);
    	request.setAttribute("qrList", qrList);
    	return "qrSetting";
    }
    
   
    @GetMapping({"/verifyUserSetting"})
    public Object verifyUserSetting(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	String region = SessionUtils.getUser(request).getRegion();
    	List<VerifyUser> verifyUserList;
    	if (region==null) {
    		verifyUserList = quanziService.getVerifyUserbyCampusLengthStatus(campus, 0, 0);
    	} else {
    		verifyUserList = quanziService.getVerifyUserbyRegionCampusLengthStatus(region, campus, 0, 0);
    	}
    	
    	request.setAttribute("verifyUserList", verifyUserList);
    	request.setAttribute("number", verifyUserList.size());
    	request.setAttribute("curPage", 0);
    	return "verifyUserSetting";
    }
    
    @GetMapping({"getVerifyUserbyLength"})
    public Object getVerifyUserbyLength(HttpServletRequest request,
    		@RequestParam (value = "length")int length){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<VerifyUser> verifyUserList = quanziService.getVerifyUserbyCampusLengthStatus(campus, length, 0);
    	request.setAttribute("verifyUserList", verifyUserList);
    	request.setAttribute("number", verifyUserList.size());
    	request.setAttribute("curPage",length/50);
    	return "verifyUserSetting";
    }
    
    
    @GetMapping({"/updateVerifyUserStatus"})
    public Object updateVerifyUserStatus(HttpServletRequest request, 
    		@RequestParam (value = "id")int id,
    		@RequestParam (value = "openid") String openid,
    		@RequestParam (value = "status")int status){
    	String campus = SessionUtils.getUser(request).getCampus();
    	System.out.println("updateVerifyUserStatus campus:"+campus);
    	int updateCode = quanziService.updateVerifyUserStatusById(id, status);
    	
    	// push notification
    	try {
    		if (status == 1) {
    			userVerifyeSub(openid, "认证成功", campus);
    		}     		
    	} catch (Exception e){
    		System.out.println("消息推送失败:"+ e);
    	}
    	
    	
    	List<VerifyUser> verifyUserList = quanziService.getVerifyUserbyCampusLengthStatus(campus, 0, 0);
    	request.setAttribute("verifyUserList", verifyUserList);
    	return "verifyUserSetting";
    }
    
    @GetMapping({"/deleteVerifyUser"})
    public Object deleteVerifyUser(HttpServletRequest request, 
    		@RequestParam (value = "id")int id,
    		@RequestParam (value = "openid") String openid){
    	String campus = SessionUtils.getUser(request).getCampus();
    	int updateCode = quanziService.deleteVerifyUserById(id);
    	
    	// push notification
    	if (updateCode==1) {
        	try {
        		userVerifyeSub(openid, "请重新提交", campus);
        	} catch (Exception e){
        		System.out.println("消息推送失败:"+ e);
        	}
    	}
    	
    	List<VerifyUser> verifyUserList = quanziService.getVerifyUserbyCampusLengthStatus(campus, 0, 0);
    	request.setAttribute("verifyUserList", verifyUserList);
    	return "verifyUserSetting";
    }
    
    
    /*
	 * 将审核结果推送给用户
	 * 审核时间{{time2.DATA}}
		审核结果{{thing3.DATA}}
		审核意见{{thing4.DATA}}
	 */
    public  Map<String,Object> userVerifyeSub(String openid, String res, String campus){ 
    	
    	String page = "/pages/index/index";
    	String template_id = "[TEMPLATE_ID]";
    	
    	List<AccessCode> accessCode = new ArrayList<>();
    	String appid_mini;
    	String secret_mini;
    	if (campus.equals("beita")) {
    		accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    		appid_mini="[APPID]";
    		secret_mini = "[SECRET]";
    	} else {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,"4");
    		appid_mini="[APPID]";
    		secret_mini="[SECRET]";
    	}
		String accessToken= "";
		
		// 获取accesstoken
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("userVerifyeSub刷新token，campus:"+campus);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid="+appid_mini+"&secret="+secret_mini;
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	        System.out.println("json:"+json);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		System.out.println("accessToken:"+accessToken);
 	 		if (accessToken!="null") {
 	 			if (campus.equals("beita")) {
 	 				beitaService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else {
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),"4");
 	        	}
 	 		}	
 		}
 		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	    String dateTimeStr = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
 		
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        
        value1.put("value",dateTimeStr);
        data.put("time2",value1);
        value2.put("value",res);
        data.put("phrase1",value2);

        System.out.println("模板消息测试");
        System.out.println(data.toString());
        
        mp_template_msg.put("touser", openid);
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
    
    
    @GetMapping({"/verifyRiderSetting"})
    public Object verifyRiderSetting(HttpServletRequest request){
    	String campus = SessionUtils.getUser(request).getCampus();
    	String region = SessionUtils.getUser(request).getRegion();
    	System.out.println("campus:"+campus);
    	System.out.println("region:"+region);
    	List<Rider> paotuiRiderList;
    	if (campus.equals("9")) {
    		paotuiRiderList = quanziService.getPaotuiRiderbyRegionLengthStatus(campus, 0, 0);
    	} else if (campus.equals("sg")){
    		paotuiRiderList = quanziService.getPaotuiRiderbyRegionLengthStatus(campus, 0, 0);
    	} else if ((region==null)||(campus.equals("13"))||(campus.equals("14"))) {
    		paotuiRiderList = quanziService.getPaotuiRiderbyCampusLengthStatus(campus, 0, 0);
    	} else {
    		paotuiRiderList = quanziService.getPaotuiRiderbyRegionCampusLengthStatus(region, campus, 0, 0);
    	}
    	
    	request.setAttribute("paotuiRiderList", paotuiRiderList);
    	request.setAttribute("number", paotuiRiderList.size());
    	request.setAttribute("curPage", 0);
    	return "verifyRiderSetting";
    }
    
    @GetMapping({"getPaotuiRiderbyLength"})
    public Object getPaotuiRiderbyLength(HttpServletRequest request,
    		@RequestParam (value = "length")int length){
    	String campus = SessionUtils.getUser(request).getCampus();
    	List<Rider> paotuiRiderList = quanziService.getPaotuiRiderbyCampusLengthStatus(campus, length, 0);
    	request.setAttribute("paotuiRiderList", paotuiRiderList);
    	request.setAttribute("number", paotuiRiderList.size());
    	request.setAttribute("curPage",length/50);
    	return "verifyRiderSetting";
    }
    
    
    @GetMapping({"/updatePaotuiRiderStatus"})
    public Object updatePaotuiRiderStatus(HttpServletRequest request, 
    		@RequestParam (value = "id")int id,
    		@RequestParam (value = "openid") String openid,
    		@RequestParam (value = "status")int status){
    	String campus = SessionUtils.getUser(request).getCampus();
    	int updateCode = quanziService.updatePaotuiUserStatusById(id, status);
    	String name = "verify_rider";
    	// push notification
		if (status == 1) {
			
			List<WXTemplate> templateList;
    		if (campus.equals("9")) {
    			templateList =quanziService.getWXTemplateByRegion(campus, name);
    		} else if (campus.equals("sg")) {
    			templateList =quanziService.getWXTemplateByRegion(campus, name);
    		} else if ((campus.equals("13"))||(campus.equals("14"))){
    			templateList =quanziService.getWXTemplateByRegion("4", name);
    		} else {
    			templateList =quanziService.getWXTemplate("", campus, name);
    		}
    		String templateId = "";
        	if (templateList.size()>0) {
        		templateId = templateList.get(0).getTemplate_id();
        	}
    		String page = "/pages/group/group";
        	try {
        		riderVerifyeSub("", campus, page, templateId, openid, "认证成功", "认证成功");
        	} catch (Exception e){
        		System.out.println("消息推送失败:"+ e);
        	}
        }
			
    	List<Rider> paotuiRiderList = quanziService.getPaotuiRiderbyRegionLengthStatus(campus, 0, 0);
    	request.setAttribute("paotuiRiderList", paotuiRiderList);
    	return "verifyRiderSetting";
    }
    
    @GetMapping({"/deletePaotuiRider"})
    public Object deletePaotuiRider(HttpServletRequest request, 
    		@RequestParam (value = "id")int id,
    		@RequestParam (value = "openid") String openid){
    	String campus = SessionUtils.getUser(request).getCampus();
    	int updateCode = quanziService.deletePaotuiUserById(id);
    	String name = "verify_rider";
    	// push notification
    	if (updateCode==1) {
    		List<WXTemplate> templateList;
    		if (campus.equals("9")) {
    			templateList =quanziService.getWXTemplateByRegion(campus, name);
    		} else if (campus.equals("sg")) {
    			templateList =quanziService.getWXTemplateByRegion(campus, name);
    		} else if ((campus.equals("13"))||(campus.equals("14"))){
    			templateList =quanziService.getWXTemplateByRegion("4", name);
    		} else {
    			templateList =quanziService.getWXTemplate("", campus, name);
    		}
    		String templateId = "";
        	if (templateList.size()>0) {
        		templateId = templateList.get(0).getTemplate_id();
        	}
    		String page = "/pages/group/group";
        	try {
        		riderVerifyeSub("", campus, page, templateId, openid, "请重新提交", "请重新提交");
        	} catch (Exception e){
        		System.out.println("消息推送失败:"+ e);
        	}
    	}
    	
    	List<Rider> paotuiRiderList = quanziService.getPaotuiRiderbyRegionLengthStatus(campus, 0, 0);
    	request.setAttribute("paotuiRiderList", paotuiRiderList);
    	return "verifyRiderSetting";
    }
    
    /*
	 * 将审核结果推送给骑手
	 * 审核时间{{time2.DATA}}
		审核结果{{thing3.DATA}}
		审核意见{{thing4.DATA}}
	 */
    public  Map<String,Object> riderVerifyeSub(
    		String region, String campus, String page, String template_id,
    		String openid, String res, String msg
    	){ 
    	System.out.println("rider verify region:"+region);
    	System.out.println("rider verify campus:"+campus);
    	List<AccessCode> accessCode = new ArrayList<>();
    	String appid_mini = "";
    	String secret_mini = "";
    	if (campus.equals("beita")) {
    		accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    		appid_mini="[APPID]";
    		secret_mini = "[SECRET]";
    	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)){
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,"4");
    		appid_mini="[APPID]";
    		secret_mini="[SECRET]";
    	} else if (campus.equals("sg")){
    		accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    		appid_mini="[APPID]";
    		secret_mini="[SECRET]";
    	} else {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,campus);
    		List<Secret> secretList = quanziService.getSecretByCampus(campus);
    		if (secretList.size()>0) {
    			appid_mini = secretList.get(0).getAppid();
        		secret_mini = secretList.get(0).getSecret();
    		}
    	}
		String accessToken= "";
		
		// 获取accesstoken
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("riderVerifyeSub刷新token，campus:"+campus);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid="+appid_mini+"&secret="+secret_mini;
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	        System.out.println("json:"+json);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		System.out.println("accessToken:"+accessToken);
 	 		if (accessToken!="null") {
 	 			if (campus.equals("beita")) {
 	 				beitaService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else if ((campus!=null)&&(campus!="")&&(Integer.parseInt(campus)>=13)){
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),"4");
 	        	} else if (campus.equals("sg")){
 	        		caicaiService.saveCode(accessToken, System.currentTimeMillis());
 	        	} else {
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),campus);
 	        	}
 	 		}	
 		}
 		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	    String dateTimeStr = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
 		
        Map<String, Object> mp_template_msg = new HashMap<>();
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        
        value1.put("value",dateTimeStr);
        data.put("time2",value1);
        value2.put("value",res);
        data.put("thing3",value2);
        value3.put("value",msg);
        data.put("thing4",value3);

        
        System.out.println("riderVerifyeSub模板消息测试");
        System.out.println(data.toString());
        
        mp_template_msg.put("touser", openid);
        mp_template_msg.put("template_id", template_id);
        mp_template_msg.put("page", page);
        mp_template_msg.put("data", data);
        mp_template_msg.put("miniprogram_state", "formal");
        mp_template_msg.put("lang", "zh_CN");
        
        System.out.println("mp_template_msg:"+mp_template_msg.toString());
        JSONObject jsonObject2;
        System.out.println("accessToken:"+accessToken);
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
    
    private String campusConversion(String campusGroup) {
    	String region = "";
    	String campus = "";
    	if (campusGroup.equals("sg")) {
    		region = "sg";
    		campus = "0";
    	} else if (campusGroup.equals("beita")) {
    		region = "beita";
    		campus = "0";
    	} else if (campusGroup.equals("9")) {
    		region = "9";
    		campus = "0";
    	} else if (Integer.parseInt(campusGroup)>=13) {
    		region = "0";
    		campus = campusGroup;
    	}
    	return region+","+campus;
    }
    
    @GetMapping({"/groupBuySetting"})
    public  Object groupBuySetting(HttpServletRequest request){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	List<GroupBuy> groupbuyList = xiaoyuanService.getAllGroupBuyByRegionCampus(region, campus);
        request.setAttribute("groupbuyList", groupbuyList);
        return "groupBuySetting";
    }
    
    @GetMapping({"/deleteGroupbuy"})
    public Object deleteGroupbuy(HttpServletRequest request, 
    		@RequestParam (value = "id")int id){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	int deletecode = xiaoyuanService.deleteGroupBuyById(id);
    	List<GroupBuy> groupbuyList = xiaoyuanService.getAllGroupBuyByRegionCampus(region, campus);
        request.setAttribute("groupbuyList", groupbuyList);
        return "groupBuySetting";
    	
    }
    
    @GetMapping({"/addGroupbuy"})
    public  Object addGroupbuy(HttpServletRequest request,
    		@RequestParam (value = "name")String name,
    		@RequestParam (value = "qr_pic")String qr_pic,
    		@RequestParam (value = "poster_pic")String poster_pic,
    		@RequestParam (value = "ori_price")String ori_price,
    		@RequestParam (value = "cur_price")String cur_price,
    		@RequestParam (value = "discount")String discount){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	GroupBuy groupbuy = new GroupBuy();
    	groupbuy.setName(name);
    	groupbuy.setQr_pic(qr_pic);
    	groupbuy.setPoster_pic(poster_pic);
    	groupbuy.setOri_price(ori_price);
    	groupbuy.setCurrent_price(cur_price);
    	groupbuy.setDiscount(discount);
    	groupbuy.setRegion(region);
    	groupbuy.setCampus(campus);
    	int addcode = xiaoyuanService.addGroupBuy(groupbuy);
    	List<GroupBuy> groupbuyList = xiaoyuanService.getAllGroupBuyByRegionCampus(region, campus);
        request.setAttribute("groupbuyList", groupbuyList);
        return "groupBuySetting";
    }
    
    @GetMapping({"/meetUpSetting"})
    public  Object meetUpSetting(HttpServletRequest request){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	System.out.println(campusGroup+" "+region+" "+campus);
    	List<Meetup> meetupList = xiaoyuanService.getAllMeetupByRegion(region);
        request.setAttribute("meetupList", meetupList);
        return "meetUpSetting";
    }
    
    @GetMapping({"/deleteMeetup"})
    public Object deleteMeetup(HttpServletRequest request, 
    		@RequestParam (value = "id")int id){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	int deletecode = xiaoyuanService.deleteMeetupById(id);
    	List<Meetup> meetupList = xiaoyuanService.getAllMeetupByRegion(region);
        request.setAttribute("meetupList", meetupList);
        return "meetUpSetting";
    	
    }
    
    @GetMapping({"/restoreMeetup"})
    public Object restoreMeetup(HttpServletRequest request, 
    		@RequestParam (value = "id")int id){
    	String campusGroup = SessionUtils.getUser(request).getCampus();
    	String region = campusConversion(campusGroup).split(",")[0];
    	String campus = campusConversion(campusGroup).split(",")[1];
    	int code = xiaoyuanService.restoreMeetupById(id);
    	List<Meetup> meetupList = xiaoyuanService.getAllMeetupByRegion(region);
        request.setAttribute("meetupList", meetupList);
        return "meetUpSetting";
    	
    }
    


}
