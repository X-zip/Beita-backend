package com.example.demo.controller;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentLevel;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.QR;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Switch;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.model.VerifyUser;
import com.example.demo.service.QuanziService;
import com.example.demo.service.impl.EmailUtils;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

import utils.AesUtil;
import utils.IpUtil;
import utils.BlacklistWord;

@RestController
public class QuanziController {
	
	@Autowired
	private QuanziService quanziService;
	static String charset = "UTF-8";
	
	
	public static void log(String postName, String note) {
		System.out.println("====================日志打印====================");
		System.out.println("==================" + postName
				+ "接口==================");
		System.out.println(note);
		System.out.println("===============================================");
	}
	
	public static String getRequestJsonString(HttpServletRequest request,
			String charset) throws IOException {
		String submitMehtod = request.getMethod();
		if (submitMehtod.equals("GET")) {
			return new String(request.getQueryString().getBytes("GBK"), charset)
					.replaceAll("%22", "\"");
		}
		byte[] buffer = getRequestPostBytes(request);
		return new String(buffer, charset);
	}

	public static byte[] getRequestPostBytes(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte[] buffer = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = request.getInputStream().read(buffer, i,
					contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}
	
	
	@RequestMapping(value="/addtaskQuanzi",method = {RequestMethod.POST})
    public  Object addTask(
    						HttpServletRequest request,
                           @RequestParam (value = "c_time",required = false)String c_time,
                           @RequestParam (value = "price",required = false)String price,
                           @RequestParam (value = "wechat",required = false)String wechat,
                           @RequestParam (value = "openid",required = false)String openid,
                           @RequestParam (value = "avatar",required = false)String avatar,
                           @RequestParam (value = "campusGroup",required = false)String campusGroup,
                           @RequestParam (value = "commentNum",required = false)int commentNum,
                           @RequestParam (value = "watchNum",required = false)int watchNum,
                           @RequestParam (value = "likeNum",required = false)int likeNum,
                           @RequestParam (value = "radioGroup",required = false)String radioGroup,
                           @RequestParam (value = "img",required = false)String img,
                           @RequestParam (value = "region",required = false)String region,
                           @RequestParam (value = "userName",required = false)String userName,
                           @RequestParam (value = "cover",required = false)String cover,
                           @RequestParam (value = "encrypted",required = false)String encrypted) throws UnsupportedEncodingException{ 
        Map<String,Object>map=new HashMap<>();
        String ip = IpUtil.getIpAddr(request);
//        System.out.println(region);
        String content = "";
		String title = "";
		String verify = "";
		String c_time_en = "";
		int content_flag = 0;
		int title_flag = 0;
		long time_diff_en = 0;
		// test 时间戳转换
		Date date_ori = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
			date_ori = sdf.parse(c_time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //将字符串改为date的格式
		
		try {
			String password = "[PASSWORD]";
			byte[] decryptFrom = AesUtil.parseHexStr2Byte(encrypted);
			byte[] resultByte = AesUtil.decrypt(decryptFrom,password);
			String result = new String(resultByte,"UTF-8");
//        System.out.println(result);
			JSONObject obj = JSON.parseObject(result);
			content = obj.getString("content");
			title = obj.getString("title");
			verify = obj.getString("verify");
			c_time_en = obj.getString("c_time");
			
			// test 时间戳转换
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
				Date date_en = sdf.parse(c_time_en);
				time_diff_en = date_ori.getTime()-date_en.getTime();
				System.out.println("addtask_encrypt_timestamp_diff:"+(date_ori.getTime()-date_en.getTime()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} //将字符串改为date的格式
			
			// check blacklist - blacklist
			content_flag =  BlacklistWord.check_blacklist(content);
			title_flag =  BlacklistWord.check_blacklist(title);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("code",200);
            map.put("msg","成功");
            return map;
		}
		if (!verify.equals("[VERIFY]")) {
        	map.put("code",200);
            map.put("msg","成功");
            return map;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(); 
//        System.out.println("当前日期字符串：" + format.format(date) + "。");
        String c_time_new = format.format(date);
        Task task=new Task();
        task.setContent(content);
        task.setPrice(price);
        task.setTitle(title);
        task.setWechat(wechat);
        task.setOpenid(openid);
        task.setAvatar(avatar);
        task.setCampusGroup(campusGroup);
        task.setCommentNum(commentNum);
        task.setLikeNum(likeNum);
        task.setWatchNum(watchNum);
        task.setRadioGroup(radioGroup);
        task.setImg(img.replace("[","").replace("]","").replace("\"",""));
        task.setRegion(region);
        task.setUserName(userName);
        task.setC_time(c_time_new);
        task.setCover(cover.replace("[","").replace("]","").replace("\"",""));
        task.setIp(ip);
        
        // set blacklist
        if (content_flag==1 || title_flag == 1) {
        	task.setIs_delete(1);
        } else {
        	task.setIs_delete(0);
        }
        
        List<BlackList> checkCode =quanziService.checkBlackList(openid);
        if(checkCode.size()>0){
        	String period = checkCode.get(0).getPeriod();
        	if (period.equals("1天")) {
        		map.put("code",1);
                map.put("msg","成功");
        	} else if (period.equals("3天")) {
        		map.put("code",3);
                map.put("msg","成功");
        	} else if (period.equals("7天")) {
        		map.put("code",7);
                map.put("msg","成功");
        	} else {
        		map.put("code",200);
                map.put("msg","成功");
        	} 
        }else {
        	List<Switch> switchList = new ArrayList<Switch>();
        	switchList = quanziService.getSwitchStatus(campusGroup);
    		if (switchList.size() <= 0) {
    			int addcode=quanziService.addTask(task,"false");
        	} else {
        		String status=switchList.get(0).getVerify();
        		int addcode=quanziService.addTask(task,status);
        	}
        	
        }
        return map;
    }
	
	@RequestMapping(value="/getallTaskQuanzi")
    public  Object getallTask(
                           @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =quanziService.getallTask(length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyIdQuanzi")
    public  Object gettaskbyId(
    					   @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =quanziService.gettaskbyId(Id);
        int updateCode =quanziService.incWatch(Id);
        map.put("taskList",taskList);       
        return map;
    }
	

	
	@RequestMapping(value="/gettaskbyOpenIdQuanzi")
    public  Object gettaskbyOpenId(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =quanziService.gettaskbyOpenId(openid,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbySearchQuanzi")
    public  Object gettaskbySearch(
    					   @RequestParam (value = "search")String search,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus")String campus){
		Map<String,Object>map=new HashMap<>();
        System.out.println(search);
		if(region == null) {
        	region = "0";
        	List<Task> taskList =quanziService.gettaskbySearch(search,length,campus);
        	map.put("taskList",taskList); 
        } else {
        	System.out.println(region);
        	List<Task> taskList =quanziService.gettaskbySearchRegion(search,length,region);
        	map.put("taskList",taskList); 
        }     
        return map;
    }
	
	@RequestMapping(value="/gettaskbyRadioQuanzi")
    public  Object gettaskbyRadio(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =quanziService.gettaskbyRadio(radioGroup,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyTypeQuanzi")
    public  Object gettaskbyType(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "type")String type,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus")String campus){ 
        Map<String,Object>map=new HashMap<>();
        if(region == null) {
        	region = "0";
        }
        List<String> radioGroupL = Arrays.asList(radioGroup.replace("\"","").replace("[","").replace("]","").split(","));
        List<Task> taskList = new ArrayList<Task>();
        if(!region.equals("0") && campus.equals("0")) {
        	System.out.println("special:"+region);
        	taskList =quanziService.gettaskbyCampus(radioGroupL,type,length,campus,region);
        } else {
        	taskList =quanziService.gettaskbyType(radioGroupL,type,length,campus,region);
        }
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/upDateTaskQuanzi")
    public  Object upDateTask(
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "c_time")String c_time){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =quanziService.upDateTask(c_time,Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incCommentLikeQuanzi")
    public  Object incCommentLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =quanziService.incCommentLike(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/decCommentLikeQuanzi")
    public  Object decCommentLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =quanziService.decCommentLike(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incCommentQuanzi")
    public  Object incComment(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =1;
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	
	@RequestMapping(value="/deleteTaskQuanzi")
    public  Object deleteTask(
    						HttpServletRequest request,
                           @RequestParam (value = "pk")String Id) throws UnsupportedEncodingException{ 
        Map<String,Object>map=new HashMap<>();
        String ip = IpUtil.getIpAddr(request);
        System.out.println(ip);
        int updateCode = 0;
		try {
			String password = "[PASSWORD]";
			byte[] decryptFrom = AesUtil.parseHexStr2Byte(Id);
			byte[] resultByte = AesUtil.decrypt(decryptFrom,password);
			String result = new String(resultByte,"UTF-8");
			JSONObject obj = JSON.parseObject(result);
			String id = obj.getString("id");
//        System.out.println(result);
			updateCode =quanziService.deleteTask(Integer.parseInt(id));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("code",200);
            map.put("msg","成功");
            return map;
		}	
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/addlikeQuanzi")
    public  Object addLike(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        Like like=new Like();
        like.setPk(pk);
        like.setOpenid(openid);
        int addcode=quanziService.addLike(like);
        int updateCode =quanziService.incLike(pk);
        if(addcode==1){
            map.put("code",200);
            map.put("msg","添加数据成功");
        }else {
            map.put("code",100);
            map.put("msg","添加数据失败");
        }
        return map;
    }
	
	@RequestMapping(value="/suggestionQuanzi")
    public  Object suggestion(
                          @RequestParam (value = "id")int id,
                          @RequestParam (value = "openid")String openid,
                          @RequestParam (value = "content")String content){ 
        Map<String,Object>map=new HashMap<>();
        Suggestion suggestion=new Suggestion();
        suggestion.setTask_id(id);
        suggestion.setContent(content);
        suggestion.setOpenid(openid);
        int addcode=quanziService.addSuggestion(suggestion);
        List<Suggestion> suggestionList =quanziService.getSuggestionByPk(id);
        if (suggestionList.size() > 5) {
        	int updateCode =quanziService.hideTask(id);
        }
        if(addcode==1){
            map.put("code",200);
            map.put("msg","添加数据成功");
        }else {
            map.put("code",100);
            map.put("msg","添加数据失败");
        }
        return map;
    }
	
	@RequestMapping(value="/addcommentQuanzi")
    public  Object addComment(
                           @RequestParam (value = "openid",required = false)String openid,
                           @RequestParam (value = "applyTo",required = false)String applyTo,
                           @RequestParam (value = "avatar",required = false)String avatar,
//                           @RequestParam (value = "comment",required = false)String comment,
                           @RequestParam (value = "userName",required = false)String userName,
                           @RequestParam (value = "c_time",required = false)String c_time,
                           @RequestParam (value = "pk",required = false)int pk,
                           @RequestParam (value = "img",required = false)String img,
                           @RequestParam (value = "level",required = false)String level,
                           @RequestParam (value = "pid",required = false)int pid,
                           @RequestParam (value = "encrypted",required = false)String encrypted) throws UnsupportedEncodingException{ 
		Map<String,Object>map=new HashMap<>();
		
        String content = "";
		String title = "";
		String verify = "";
		String c_time_en = "";
		
		String password = "[PASSWORD]";
		byte[] decryptFrom = AesUtil.parseHexStr2Byte(encrypted);
		byte[] resultByte = AesUtil.decrypt(decryptFrom,password);
		String result = new String(resultByte,"UTF-8");
//    System.out.println(result);
		JSONObject obj = JSON.parseObject(result);
		content = obj.getString("content");
		title = obj.getString("title");
		verify = obj.getString("verify");
		c_time_en = obj.getString("c_time");
		
		if (!verify.equals("[VERIFY]")) {
        	map.put("code",200);
            map.put("msg","成功");
            return map;
        }
        
        Comment commenta=new Comment();
        commenta.setApplyTo(applyTo);
        commenta.setAvatar(avatar);
        commenta.setC_time(c_time);
        commenta.setComment(content);
        commenta.setOpenid(openid);
        commenta.setPk(pk);
        commenta.setUserName(userName);
        commenta.setImg(img.replace("[","").replace("]","").replace("\"",""));
        commenta.setLevel(level);
        commenta.setPid(pid);
        List<BlackList> checkCode =quanziService.checkBlackList(openid);
        if(checkCode.size()>0){
        	String period = checkCode.get(0).getPeriod();
        	if (period.equals("1天")) {
        		map.put("code",1);
                map.put("msg","成功");
        	} else if (period.equals("3天")) {
        		map.put("code",3);
                map.put("msg","成功");
        	} else if (period.equals("7天")) {
        		map.put("code",7);
                map.put("msg","成功");
        	} else {
        		map.put("code",200);
                map.put("msg","成功");
        	} 
        }else {
            int addcode=quanziService.addComment(commenta);
            int updateCode =quanziService.incComment(pk);
        }
        return map;
    }
	
	@RequestMapping(value="/deleteLikeQuanzi")
    public  Object deleteLike(
                           @RequestParam (value = "id")int Id,
                           @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =quanziService.deleteLike(Id);
        int updateCode2 =quanziService.decLike(pk);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/getlikeByPkQuanzi")
    public  Object getlikeByPk(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =quanziService.getlikeByPk(openid,pk);
        map.put("likeList",likeList);       
        return map;
    }
	
	@RequestMapping(value="/getlikeByOpenidQuanzi")
    public  Object getlikeByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =quanziService.getlikeByOpenid(openid,length);
        map.put("likeList",likeList);       
        return map;
    }
	
	

	
	@RequestMapping(value="/getSecondLevelQuanzi")
    public  Object getSecondLevel(
    					   @RequestParam (value = "id")int id
    					   ){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentSecondList =quanziService.getCommentByPid(id);
        map.put("commentSecondList",commentSecondList);       
        return map;
    }
	

	
	
	
	@RequestMapping(value="/getCommentByTypeQuanzi")
    public  Object getCommentByType(
    					   @RequestParam (value = "pk")int pk,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "type")String type){ 
        Map<String,Object>map=new HashMap<>();
        long startTime=System.currentTimeMillis();
        List<Comment> commentList =quanziService.getCommentByType(pk,length,type);
        List<String> strL = new ArrayList<>();
        for (Comment com:commentList) {
        	strL.add(String.valueOf(com.getId()));
        }
        List<Comment> commentSecondList = new ArrayList<>();
        if (commentList.size() > 0) {
        	commentSecondList =quanziService.getCommentByIdList(strL);
        }
        List<CommentLevel> commentLevelList = new ArrayList<>();
        for (Comment com:commentList){
        	CommentLevel commentLevel=new CommentLevel();   	
        	commentLevel.setApplyTo(com.getApplyTo());
        	commentLevel.setAvatar(com.getAvatar());
        	commentLevel.setC_time(com.getC_time());
        	commentLevel.setComment(com.getComment());
        	commentLevel.setId(com.getId());
        	commentLevel.setImg(com.getImg());
        	commentLevel.setLevel(com.getLevel());
        	commentLevel.setOpenid(com.getOpenid());
        	commentLevel.setPid(com.getPid());
        	commentLevel.setPk(pk);
        	commentLevel.setLike_num(com.getLike_num());
        	commentLevel.setUserName(com.getUserName());
        	commentLevel.setCommentList(new ArrayList<>());
        	commentLevelList.add(commentLevel);
    	}
        for (Comment com2:commentSecondList) {
        	for (int x = 0; x < commentList.size(); x = x+1){
        		if(com2.getPid() == commentList.get(x).getId()) {
        			List<Comment> commentSecond = commentLevelList.get(x).getCommentList();
        			commentSecond.add(com2);
        			commentLevelList.get(x).setCommentList(commentSecond);
        			break;
        		}
        	} 	
        }

        map.put("commentList",commentLevelList);     
        return map;
    }
	
	
	@RequestMapping(value="/deleteCommentQuanzi")
    public  Object deleteComment(
    						HttpServletRequest request,
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        String ip = IpUtil.getIpAddr(request);
        System.out.println(ip);
        int updateCode =quanziService.deleteComment(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/getCommentByOpenidQuanzi")
    public  Object getCommentByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =quanziService.getCommentByOpenid(openid,length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getAllCommentQuanzi")
    public  Object getAllComment(
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =quanziService.getAllComment(length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getCommentByApplytoQuanzi")
    public  Object getCommentByApplyto(
    					   @RequestParam (value = "applyTo")String applyTo,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =quanziService.getCommentByApplyto(applyTo,length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getMemberQuanzi")
    public  Object getMember(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "campus")String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =quanziService.getMember(openid,campus);
        map.put("memberList",memberList);       
        return map;
    }
	
	@RequestMapping(value="/getAllMemberQuanzi")
    public  Object getAllMember(){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =quanziService.getAllMemberWX();
        map.put("memberList",memberList);       
        return map;
    }
	
	@RequestMapping(value="/upDateWatchQuanzi")
    public  Object upDateWatch(
                           @RequestParam (value = "id")int Id,
                           @RequestParam (value = "watchNum")int watchNum){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =quanziService.upDateWatch(watchNum,Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/imgUploadQuanzi")
	public String imgUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = "";
        request.setCharacterEncoding("utf-8"); //设置编码
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        File dir = new File(realPath);
        //文件目录不存在，就创建一个
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        try {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            //获取formdata的值
            Iterator<String> iterator = req.getFileNames();
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String timedata = request.getParameter("timedata");

            while (iterator.hasNext()) {
                MultipartFile file = req.getFile(iterator.next());
                String fileName = file.getOriginalFilename();
                //真正写到磁盘上
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String kzm = fileName.substring(fileName.lastIndexOf("."));
                String filename = uuid + kzm;
                File file1 = new File(realPath + filename);
                OutputStream out = new FileOutputStream(file1);
                out.write(file.getBytes());
                out.close();
                filePath = request.getScheme() + "://" +
                        request.getServerName() + ":"
                        + request.getServerPort()
                        + "/uploadFile/" + filename;
            }
        } catch (Exception e) {
            
        }
        return filePath;

    }
	
	@RequestMapping(value="/getBannerQuanzi")
    public  Object getBanner(
    						@RequestParam (value = "campus")String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Banner> bannerList =quanziService.getBanner(campus);
        map.put("bannerList",bannerList);       
        return map;
    }
	
	
	
	@RequestMapping(value="/getRankListQuanzi")
    public  Object getRankList(
                           @RequestParam (value = "length")int length){
        Map<String,Object>map=new HashMap<>();
        List<BitRank> rankList =quanziService.getRankList(length);
        map.put("rankList",rankList);    
        return map;
    }
	

	@RequestMapping(value="/addRankQuanzi")
    public  Object addRank(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "avatar")String avatar,
                           @RequestParam (value = "userName")String userName,
                           @RequestParam (value = "score")String score){ 
        Map<String,Object>map=new HashMap<>();
        BitRank bitrank=new BitRank();
        bitrank.setScore(score);
        bitrank.setOpenid(openid);
        bitrank.setAvatar(avatar);
        bitrank.setNickName(userName);
        List<BitRank> rankList =quanziService.getRankListByOpenid(openid);
        int addcode = 0;
        if (rankList.size()>0) {
        	if (Integer.valueOf(rankList.get(0).getScore()) < Integer.valueOf(score)) {
        		addcode=quanziService.updateRank(bitrank);
        	} else {
        		addcode=1;
        	}
            
        } else {
            addcode=quanziService.addRank(bitrank);
        }
        if(addcode==1){
            map.put("code",200);
            map.put("msg","添加数据成功");
        }else {
            map.put("code",100);
            map.put("msg","添加数据失败");
        }
        return map;
    }
	
	@RequestMapping(value="/getQRList")
    public  Object getQRList(
    						@RequestParam (value = "campus")String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<QR> qrList = new ArrayList<QR>();
    	qrList =quanziService.getQR(campus);
        map.put("qrList",qrList);       
        return map;
    }
	
	@RequestMapping(value="/getVerifyUserByOpenid")
	public Object getVerifyUserByOpenid(
		@RequestParam (value = "openid")String openid) {
		 Map<String,Object>map=new HashMap<>();
		 List<VerifyUser> existence = quanziService.getVerifyUserByOpenid(openid);
		 if (existence.size()>0) {
			 map.put("code", "200");
			 map.put("msg", existence.get(0).getStatus());
		 } else {
			 map.put("code", "-1");
			 map.put("msg", "未提交认证信息");
		 }
		 return map;
	}
	
	
	@RequestMapping(value="/addVerifyUserQuanzi")
	public Object addVerifyUserQuanzi(
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "pic")String pic,
			@RequestParam (value = "email")String email,
			@RequestParam (value = "campus", required=false)String campus) {
        Map<String,Object>map=new HashMap<>();
        
        List<VerifyUser> existence = quanziService.getVerifyUserByOpenid(openid);
        if (existence.size()>0) {
        	map.put("code",-1);
            map.put("msg","该微信号已认证");
        	
        } else {
        	VerifyUser verify_user = new VerifyUser();
        	verify_user.setOpenid(openid);
        	verify_user.setPic(pic);
        	if (email.equals("") || email.equals(null)) {
        		verify_user.setStatus(0);
        	} else {
        		verify_user.setStatus(1);
        	}
        	verify_user.setCampus(campus);
        	verify_user.setEmail(email);
        	int res = quanziService.addVerifyUser(verify_user);
        	if (res>0) {
        		map.put("code",200);
                map.put("msg","添加成功！");
        	} else {
        		map.put("code",-1);
                map.put("msg","添加失败，请重试");
                
        	}
        }
        return map;
	}
	
	@RequestMapping(value="/checkVerifyUserQuanzi")
	public Object checkVerifyUserQuanzi(
			@RequestParam (value = "openid")String openid) {
		Map<String,Object>map=new HashMap<>();
		List<VerifyUser> existence = quanziService.getVerifyUserByOpenid(openid);
		if (existence.size()>0) {
			VerifyUser user = existence.get(0);
			if (user.getStatus()==1) {
				map.put("code",200);
	            map.put("msg","该微信号已认证");
			} else {
				map.put("code",0);
	            map.put("msg","请耐心等待审核完成");
			}
		} else {
			map.put("code",-1);
            map.put("msg","该微信号未认证");
		}
		return map;
	}
	
	@RequestMapping(value="/checkEmailQuanzi")
	public Object checkEmailQuanzi(
			@RequestParam (value = "campus")String campus,
			@RequestParam (value = "email")String email) {
		Map<String,Object>map=new HashMap<>();
		List<VerifyUser> emailExistence = quanziService.getVerifyUserbyEmail(email);
		if (emailExistence.size()>0) {
			map.put("code",-1);
            map.put("msg","该邮箱已认证");
		} else {
			map.put("code",200);
            map.put("msg","该邮箱尚未认证");
		}
		return map;
	}
	

	

}
