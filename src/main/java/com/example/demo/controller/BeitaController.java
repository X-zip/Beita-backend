package com.example.demo.controller;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentLevel;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.model.VerifyUser;
import com.example.demo.service.BeitaService;
import com.example.demo.service.EmailService;
import com.example.demo.service.impl.EmailUtils;

import utils.AesUtil;
import utils.IpUtil;
import utils.BlacklistWord;


@RestController
public class BeitaController {
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private EmailService emailService;
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
	
	
	@RequestMapping(value="/addtask")
    public  Object addTask(
    						HttpServletRequest request,
                           @RequestParam (value = "content")String content,
                           @RequestParam (value = "c_time")String c_time,
                           @RequestParam (value = "price")String price,
                           @RequestParam (value = "title")String title,
                           @RequestParam (value = "wechat")String wechat,
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "avatar")String avatar,
                           @RequestParam (value = "campusGroup")String campusGroup,
                           @RequestParam (value = "commentNum")int commentNum,
                           @RequestParam (value = "watchNum")int watchNum,
                           @RequestParam (value = "likeNum")int likeNum,
                           @RequestParam (value = "radioGroup")String radioGroup,
                           @RequestParam (value = "img")String img,
                           @RequestParam (value = "region")String region,
                           @RequestParam (value = "userName")String userName,
                           @RequestParam (value = "cover")String cover){ 
        Map<String,Object>map=new HashMap<>();
        int content_flag = 0;
		int title_flag = 0;
        String ip = IpUtil.getIpAddr(request);
//        System.out.println(ip);
        Task task=new Task();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(); 
        String c_time_new = format.format(date);
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
        
     // check blacklist - blacklist
		content_flag = BlacklistWord.check_blacklist(content);
		title_flag = BlacklistWord.check_blacklist(title);
        // set blacklist
        if (content_flag==1 || title_flag == 1) {
        	task.setIs_delete(1);
        } else {
        	task.setIs_delete(0);
        }
        
        List<BlackList> checkCode =beitaService.checkBlackList(openid);
        if(checkCode.size()>0){
        	String period = checkCode.get(0).getPeriod();
        	int id = checkCode.get(0).getId();
        	if (period.equals("1天")) {
        		map.put("code",1);
        		map.put("id",id);
                map.put("msg","成功");
        	} else if (period.equals("3天")) {
        		map.put("code",3);
        		map.put("id",id);
                map.put("msg","成功");
        	} else if (period.equals("7天")) {
        		map.put("code",7);
        		map.put("id",id);
                map.put("msg","成功");
        	} else {
        		map.put("code",200);
        		map.put("id",id);
                map.put("msg","成功");
        	} 
        }else {
        	int addcode=beitaService.addTask(task);
        }
//        int addcode=beitaService.addTask(task);
        return map;
    }
	
	@RequestMapping(value="/getallTask")
    public  Object getallTask(
                           @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =beitaService.getallTask(length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyId")
    public  Object gettaskbyId(
    					   @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =beitaService.gettaskbyId(Id);
        int updateCode =beitaService.incWatch(Id);
        map.put("taskList",taskList);       
        return map;
    }
	

	
	@RequestMapping(value="/gettaskbyOpenId")
    public  Object gettaskbyOpenId(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =beitaService.gettaskbyOpenId(openid,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbySearch")
    public  Object gettaskbySearch(
    					   @RequestParam (value = "search")String search,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =beitaService.gettaskbySearch(search,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyRadio")
    public  Object gettaskbyRadio(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList =beitaService.gettaskbyRadio(radioGroup,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyRadioSecond")
    public  Object gettaskbyRadioSecond(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<String> radioGroupL = Arrays.asList(radioGroup.replace("\"","").replace("[","").replace("]","").split(","));
        List<Task> taskList =beitaService.gettaskbyRadioSecond(radioGroupL,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/gettaskbyType")
    public  Object gettaskbyType(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "type")String type,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<String> radioGroupL = Arrays.asList(radioGroup.replace("\"","").replace("[","").replace("]","").split(","));
        List<Task> taskList =beitaService.gettaskbyType(radioGroupL,type,length);
        map.put("taskList",taskList);       
        return map;
    }
	
	@RequestMapping(value="/upDateTask")
    public  Object upDateTask(
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "c_time")String c_time){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =beitaService.upDateTask(c_time,Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incWatch")
    public  Object incWatch(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
//        int updateCode =beitaService.incWatch(Id);
        int updateCode = 1;
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incLike")
    public  Object incLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
//        int updateCode =beitaService.incLike(Id);
        int updateCode = 1;
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/decLike")
    public  Object decLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
//        int updateCode =beitaService.decLike(Id);
        int updateCode = 1;
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incCommentLike")
    public  Object incCommentLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =beitaService.incCommentLike(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/decCommentLike")
    public  Object decCommentLike(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =beitaService.decCommentLike(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/incComment")
    public  Object incComment(
                           @RequestParam (value = "pk")int Id){ 
        Map<String,Object>map=new HashMap<>();
//        int updateCode =beitaService.incComment(Id);
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
	
	
	@RequestMapping(value="/deleteTask")
    public  Object deleteTask(
    						HttpServletRequest request,
    						@RequestParam (value = "pk")String Id){ 
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
			updateCode =beitaService.deleteTask(Integer.parseInt(id));
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
	
	@RequestMapping(value="/addlike")
    public  Object addLike(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        Like like=new Like();
        like.setPk(pk);
        like.setOpenid(openid);
        int addcode=beitaService.addLike(like);
        int updateCode =beitaService.incLike(pk);
        if(addcode==1){
            map.put("code",200);
            map.put("msg","添加数据成功");
        }else {
            map.put("code",100);
            map.put("msg","添加数据失败");
        }
        return map;
    }
	
	@RequestMapping(value="/suggestion")
    public  Object suggestion(
                          @RequestParam (value = "id")int id,
                          @RequestParam (value = "openid")String openid,
                          @RequestParam (value = "content")String content){ 
        Map<String,Object>map=new HashMap<>();
        Suggestion suggestion=new Suggestion();
        suggestion.setTask_id(id);
        suggestion.setContent(content);
        suggestion.setOpenid(openid);
        int addcode=beitaService.addSuggestion(suggestion);
        List<Suggestion> suggestionList =beitaService.getSuggestionByPk(id);
        if (suggestionList.size() > 5) {
        	int updateCode =beitaService.hideTask(id);
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
	
	@RequestMapping(value="/addcomment")
    public  Object addComment(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "applyTo")String applyTo,
                           @RequestParam (value = "avatar")String avatar,
                           @RequestParam (value = "comment")String comment,
                           @RequestParam (value = "userName")String userName,
                           @RequestParam (value = "c_time")String c_time,
                           @RequestParam (value = "pk")int pk,
                           @RequestParam (value = "img")String img,
                           @RequestParam (value = "level")String level,
                           @RequestParam (value = "pid")int pid){ 
        Map<String,Object>map=new HashMap<>();
        Comment commenta=new Comment();
        commenta.setApplyTo(applyTo);
        commenta.setAvatar(avatar);
        commenta.setC_time(c_time);
        commenta.setComment(comment);
        commenta.setOpenid(openid);
        commenta.setPk(pk);
        commenta.setUserName(userName);
        commenta.setImg(img.replace("[","").replace("]","").replace("\"",""));
        commenta.setLevel(level);
        commenta.setPid(pid);
        List<BlackList> checkCode =beitaService.checkBlackList(openid);
        if(checkCode.size()>0){
        	String period = checkCode.get(0).getPeriod();
        	int id = checkCode.get(0).getId();
        	if (period.equals("1天")) {
        		map.put("code",1);
        		map.put("id",id);
                map.put("msg","成功");
        	} else if (period.equals("3天")) {
        		map.put("code",3);
        		map.put("id",id);
                map.put("msg","成功");
        	} else if (period.equals("7天")) {
        		map.put("code",7);
        		map.put("id",id);
                map.put("msg","成功");
        	} else {
        		map.put("code",200);
        		map.put("id",id);
                map.put("msg","成功");
        	} 
        }else {
            int addcode=beitaService.addComment(commenta);
            int updateCode =beitaService.incComment(pk);
        }
//        if(addcode==1){
//            map.put("code",200);
//            map.put("msg","添加数据成功");
//        }else {
//            map.put("code",100);
//            map.put("msg","添加数据失败");
//        }
        return map;
    }
	
	@RequestMapping(value="/deleteLike")
    public  Object deleteLike(
				    		@RequestParam (value = "id")int Id,
				            @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =beitaService.deleteLike(Id);
        int updateCode2 =beitaService.decLike(pk);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/getlikeByPk")
    public  Object getlikeByPk(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "pk")int pk){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =beitaService.getlikeByPk(openid,pk);
        map.put("likeList",likeList);       
        return map;
    }
	
	@RequestMapping(value="/getlikeByOpenid")
    public  Object getlikeByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =beitaService.getlikeByOpenid(openid,length);
        map.put("likeList",likeList);       
        return map;
    }
	
	

	
	@RequestMapping(value="/getSecondLevel")
    public  Object getSecondLevel(
    					   @RequestParam (value = "id")int id
    					   ){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentSecondList =beitaService.getCommentByPid(id);
        map.put("commentSecondList",commentSecondList);       
        return map;
    }
	

	
	
	
	@RequestMapping(value="/getCommentByType")
    public  Object getCommentByType(
    					   @RequestParam (value = "pk")int pk,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "type")String type){ 
        Map<String,Object>map=new HashMap<>();
        long startTime=System.currentTimeMillis();
        List<Comment> commentList =beitaService.getCommentByType(pk,length,type);
//        System.out.println("现程序运行时间： "+(System.currentTimeMillis()-startTime)+"ms");
        List<String> strL = new ArrayList<>();
        for (Comment com:commentList) {
        	strL.add(String.valueOf(com.getId()));
        }
        List<Comment> commentSecondList = new ArrayList<>();
        if (commentList.size() > 0) {
        	commentSecondList =beitaService.getCommentByIdList(strL);
        }
//        System.out.println("现程序运行时间： "+(System.currentTimeMillis()-startTime)+"ms");
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
//        System.out.println("现程序运行时间： "+(System.currentTimeMillis()-startTime)+"ms");
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
//        System.out.println("现程序运行时间： "+(System.currentTimeMillis()-startTime)+"ms");

        map.put("commentList",commentLevelList);     
//        long endTime=System.currentTimeMillis();
//        System.out.println("现程序运行时间： "+(System.currentTimeMillis()-startTime)+"ms");
        return map;
    }
	
	
	@RequestMapping(value="/deleteComment")
    public  Object deleteComment(
    						HttpServletRequest request,
                           @RequestParam (value = "pk")int Id){ 
		Map<String,Object>map=new HashMap<>();
        String ip = IpUtil.getIpAddr(request);
        System.out.println(ip);
        int updateCode =beitaService.deleteComment(Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/getCommentByOpenid")
    public  Object getCommentByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =beitaService.getCommentByOpenid(openid,length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getAllComment")
    public  Object getAllComment(
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =beitaService.getAllComment(length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getCommentByApplyto")
    public  Object getCommentByApplyto(
    					   @RequestParam (value = "applyTo")String applyTo,
    					   @RequestParam (value = "length")int length){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList =beitaService.getCommentByApplyto(applyTo,length);
        map.put("commentList",commentList);       
        return map;
    }
	
	@RequestMapping(value="/getMember")
    public  Object getMember(
    					   @RequestParam (value = "openid")String openid){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =beitaService.getMember(openid);
        map.put("memberList",memberList);       
        return map;
    }
	
	@RequestMapping(value="/getAllMember")
    public  Object getAllMember(){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =beitaService.getAllMember();
        map.put("memberList",memberList);       
        return map;
    }
	

	
	@RequestMapping(value="/addMember")
    public  Object addMember(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "au1")int au1,
                           @RequestParam (value = "au2")int au2,
                           @RequestParam (value = "au3")int au3,
                           @RequestParam (value = "au4")int au4,
                           @RequestParam (value = "au5")int au5,
                           @RequestParam (value = "avatar")String avatar,
                           @RequestParam (value = "name")String name){ 
        Map<String,Object>map=new HashMap<>();
        Member member=new Member();
        member.setAu1(au1);
        member.setAu2(au2);
        member.setAu3(au3);
        member.setAu4(au4);
        member.setAu5(au5);
        member.setAvatar(avatar);
        member.setName(name);
        member.setOpenid(openid);
        int addcode=beitaService.addMember(member);
        if(addcode==1){
            map.put("code",200);
            map.put("msg","添加数据成功");
        }else {
            map.put("code",100);
            map.put("msg","添加数据失败");
        }
        return map;
    }
	
	@RequestMapping(value="/upDateWatch")
    public  Object upDateWatch(
                           @RequestParam (value = "id")int Id,
                           @RequestParam (value = "watchNum")int watchNum){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =beitaService.upDateWatch(watchNum,Id);
        if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",100);
            map.put("msg","失败");
        }     
        return map;
    }
	
	@RequestMapping(value="/imgUpload")
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
	
	@RequestMapping(value="/getBanner")
    public  Object getBanner(){ 
        Map<String,Object>map=new HashMap<>();
        List<Banner> bannerList =beitaService.getBanner();
        map.put("bannerList",bannerList);       
        return map;
    }
	
	@RequestMapping(value="/getBanner2")
    public  Object getBanner2(){ 
        Map<String,Object>map=new HashMap<>();
//        List<Banner> bannerList =beitaService.getBanner2();
        List<Banner> bannerList =beitaService.getBanner();
        map.put("bannerList",bannerList);       
        return map;
    }
	
	@RequestMapping(value="/checkBlackList")
    public  Object checkBlackList(
                           @RequestParam (value = "openid")String openid){
        Map<String,Object>map=new HashMap<>();
        map.put("code",100);
        return map;
    }
	
	@RequestMapping(value="/getRankList")
    public  Object getRankList(
                           @RequestParam (value = "length")int length){
        Map<String,Object>map=new HashMap<>();
        List<BitRank> rankList =beitaService.getRankList(length);
        map.put("rankList",rankList);    
        return map;
    }
	

	@RequestMapping(value="/addRank")
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
        List<BitRank> rankList =beitaService.getRankListByOpenid(openid);
        int addcode = 0;
        if (rankList.size()>0) {
        	if (Integer.valueOf(rankList.get(0).getScore()) < Integer.valueOf(score)) {
        		addcode=beitaService.updateRank(bitrank);
        	} else {
        		addcode=1;
        	}
            
        } else {
            addcode=beitaService.addRank(bitrank);
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
	
	
	@RequestMapping(value="/sendEmailBeita")
	public Object sendEmailBeita(
			@RequestParam (value = "email")String email,
			@RequestParam (value = "code")String code) {
		String subject = "贝塔驿站校园认证";
		String content = "您的验证码为:"+code;
		int emailSendCode = emailService.sendHtmlMail(email, subject, content);
		Map<String,Object>map=new HashMap<>();
		if (emailSendCode == 1) {
			map.put("code", 200);
			map.put("code", "发送成功");
		} else if (emailSendCode == -1) {
			map.put("code", 404);
			map.put("code", "请检查邮箱地址");
		} else {
			map.put("code", 500);
			map.put("code", "请稍后重试");
		}
		return map;
	}
	
	

}
