package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Campus;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentIdentity;
import com.example.demo.model.CommentLevel;
import com.example.demo.model.CommentLevelIdentity;
import com.example.demo.model.GroupBuy;
import com.example.demo.model.Like;
import com.example.demo.model.Meetup;
import com.example.demo.model.Member;
import com.example.demo.model.RadioGroupCategory;
import com.example.demo.model.Secret;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Switch;
import com.example.demo.model.Task;
import com.example.demo.model.TaskIdentity;
import com.example.demo.model.Test;
import com.example.demo.model.VerifyUser;
import com.example.demo.model.VerifyUserIdentity;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.EmailService;
import com.example.demo.service.QuanziService;
import com.example.demo.service.XiaoyuanService;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import utils.AesUtil;
import utils.IpUtil;
import utils.Result;
import utils.ResultGenerator;
import utils.UUIDGenerator;
import utils.BlacklistWord;
import utils.HttpRequest;

@RestController
public class XiaoyuanController {
	
	@Autowired
	private QuanziService quanziService;
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private CaicaiService caicaiService;
	
	@Autowired
	private XiaoyuanService xiaoyuanService;
	
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

	
	
	@RequestMapping(value="/addtaskXiaoyuan",method = {RequestMethod.POST})
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
//		System.out.println("addtask");
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
		if (c_time != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
				date_ori = sdf.parse(c_time);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} //将字符串改为date的格式
		} else {
			date_ori = new Date();
		}
		
        try {
            String password = "[PASSWORD]";
            // encrypted 空值兜底处理
            if (encrypted == null || encrypted.isEmpty()) {
                encrypted = "";
            }
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
        task.setImg(img == null ? "" : img.replace("[","").replace("]","").replace("\"",""));
        task.setRegion(region);
        task.setUserName(userName);
        task.setC_time(c_time_new);
        task.setCover(cover == null ? "" : cover.replace("[","").replace("]","").replace("\"",""));
        task.setIp(ip);
        
        // set blacklist
        if (content_flag==1 || title_flag == 1) {
        	task.setIs_delete(1);
        } else {
        	task.setIs_delete(0);
        }
        
        // if-else on region
        if ("sg".equals(region)) {
            List<BlackList> checkCode =caicaiService.checkBlackList(openid);
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
                int addcode = caicaiService.addTask(task);
            }
        } else if ("beita".equals(region)) {
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
            	task.setRegion("0");
            	int addcode = beitaService.addTask(task);
            }
        } else {
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
        }
        return map;
    }
	
	
	public List<TaskIdentity> convertTaskToTaskIdentity(List<Task> tasks){
		List<TaskIdentity> taskIdentityList = new ArrayList<>();
		List<String> openidList = tasks.stream()
			    .map(Task::getOpenid)
			    .collect(Collectors.toList());
		Map<String, Integer> openidIdentityMap = null;
		
		if (openidList.size()>0) {
			List<VerifyUserIdentity> userList = quanziService.getUserVerifyByOpenidList(openidList);
			openidIdentityMap = userList.stream()
				    .collect(Collectors.toMap(
				    		VerifyUserIdentity::getOpenid,
				    		VerifyUserIdentity::getIdentity,
				    		(existing, replacement) -> existing
				    ));
		}
		
		for (Task task : tasks) {
		    TaskIdentity taskIden = new TaskIdentity();

		    // 复制所有字段
		    taskIden.setId(task.getId());
		    taskIden.setIp(task.getIp());
		    taskIden.setContent(task.getContent());
		    taskIden.setPrice(task.getPrice());
		    taskIden.setTitle(task.getTitle());
		    taskIden.setWechat(task.getWechat());
		    taskIden.setOpenid(task.getOpenid());
		    taskIden.setAvatar(task.getAvatar());
		    taskIden.setCampusGroup(task.getCampusGroup());
		    taskIden.setCommentNum(task.getCommentNum());
		    taskIden.setWatchNum(task.getWatchNum());
		    taskIden.setLikeNum(task.getLikeNum());
		    taskIden.setRadioGroup(task.getRadioGroup());
		    taskIden.setImg(task.getImg());
		    taskIden.setCover(task.getCover());
		    taskIden.setIs_delete(task.getIs_delete());
		    taskIden.setIs_complaint(task.getIs_complaint());
		    taskIden.setRegion(task.getRegion());
		    taskIden.setUserName(task.getUserName());
		    taskIden.setC_time(task.getC_time());
		    taskIden.setComment_time(task.getComment_time());
		    taskIden.setChoose(task.getChoose());
		    taskIden.setHot(task.getHot());

		    // 设置 identity 字段
		    Integer identity = openidIdentityMap.getOrDefault(task.getOpenid(), 1);
		    taskIden.setIdentity(identity);

		    taskIdentityList.add(taskIden);
		}
		
		return taskIdentityList;
	}
	
	@RequestMapping(value="/getallTaskXiaoyuan")
    public  Object getallTaskXiaoyuan(
                           @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
                           @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList = null;
        if (region.equals("sg")) {
        	taskList = caicaiService.getallTask(length);
        } else if (region.equals("beita")) {
        	taskList = beitaService.getallTask(length);
        } else {
        	taskList =quanziService.getallTask(length);
        }
//        map.put("taskList",taskList);  
        map.put("taskList", convertTaskToTaskIdentity(taskList));
        return map;
    }
	
	@RequestMapping(value="/gettaskbyIdXiaoyuan")
    public  Object gettaskbyId(
    					   @RequestParam (value = "pk")int Id,
    					   @RequestParam (value = "region",required = false)String region,
                           @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList = null;
        if (region.equals("sg")) {
        	taskList =caicaiService.gettaskbyId(Id);
            int updateCode =caicaiService.incWatch(Id);
        } else if (region.equals("beita")) {
        	taskList =beitaService.gettaskbyId(Id);
            int updateCode =beitaService.incWatch(Id);
        } else {
        	taskList =quanziService.gettaskbyId(Id);
            int updateCode =quanziService.incWatch(Id);
        }
//        map.put("taskList",taskList);       
        map.put("taskList", convertTaskToTaskIdentity(taskList));
        return map;
    }
	

	
	@RequestMapping(value="/gettaskbyOpenIdXiaoyuan")
    public  Object gettaskbyOpenId(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "region",required = false)String region,
                           @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Task> taskList = null; 
        if (region.equals("sg")) {
        	taskList =caicaiService.gettaskbyOpenId(openid,length);
        } else if (region.equals("beita")) {
        	taskList =beitaService.gettaskbyOpenId(openid,length);
        } else {
        	taskList =quanziService.gettaskbyOpenId(openid,length);
        }
//        map.put("taskList",taskList); 
        map.put("taskList", convertTaskToTaskIdentity(taskList));
        return map;
    }
	
	@RequestMapping(value="/gettaskbySearchXiaoyuan")
    public  Object gettaskbySearch(
    					   @RequestParam (value = "search")String search,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "region",required = false)String region,
                           @RequestParam (value = "campus",required = false)String campus){
		Map<String,Object>map=new HashMap<>();
		List<Task> taskList = null;
		
		if (region.equals("sg")) {
			taskList =caicaiService.gettaskbySearch(search,length);
		} else if (region.equals("beita")) {
			taskList =beitaService.gettaskbySearch(search,length);
		} else {
			if((region.equals(""))||(region.equals(null))) {
	        	region = "0";
	        	taskList =quanziService.gettaskbySearch(search,length,campus);
	        } else {
	        	taskList =quanziService.gettaskbySearchRegion(search,length,region);
	        }  
		}
//		map.put("taskList",taskList); 
		map.put("taskList", convertTaskToTaskIdentity(taskList));
        return map;
    }
	
	
	@RequestMapping(value="/gettaskbyTypeXiaoyuan")
    public  Object gettaskbyType(
    					   @RequestParam (value = "radioGroup")String radioGroup,
    					   @RequestParam (value = "type")String type,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<String> radioGroupL = Arrays.asList(radioGroup.replace("\"","").replace("[","").replace("]","").split(","));
        List<Task> taskList = new ArrayList<Task>();
        if (region.equals("sg")) {
        	if (campus.equals("0")) {
        		taskList =caicaiService.gettaskbyType(radioGroupL,type,length);
        	} else {
        		taskList =caicaiService.gettaskbyTypeCampus(radioGroupL,type,length, campus);
        	}
        	
		} else if (region.equals("beita")) {
			if (campus.equals("0")) {
				taskList =beitaService.gettaskbyType(radioGroupL,type,length);
			} else {
				String campusNew = String.valueOf(2-Integer.parseInt(campus));
				taskList =beitaService.gettaskbyTypeCampus(radioGroupL,type,length, campusNew);
			}
		} else {
			if (region.equals(null)) {
				region = "0";
			}
			if(!region.equals("0") && campus.equals("0")) {
	        	taskList =quanziService.gettaskbyCampus(radioGroupL,type,length,campus,region);
	        } else {
	        	taskList =quanziService.gettaskbyType(radioGroupL,type,length,campus,region);
	        }
		}
//        map.put("taskList",taskList);     
        map.put("taskList",  convertTaskToTaskIdentity(taskList));
        return map;
    }
	
	@RequestMapping(value="/upDateTaskXiaoyuan")
    public  Object upDateTask(
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "c_time")String c_time,
                           @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode = 0;
        if (region.equals("sg")) {
        	updateCode =caicaiService.upDateTask(c_time,Id);
		} else if (region.equals("beita")) {
			updateCode =beitaService.upDateTask(c_time,Id);
		} else {
			updateCode =quanziService.upDateTask(c_time,Id);
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

	
	@RequestMapping(value="/incCommentLikeXiaoyuan")
    public  Object incCommentLike(
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode = 0;
        if (region.equals("sg")) {
        	updateCode =caicaiService.incCommentLike(Id);
		} else if (region.equals("beita")) {
			updateCode =beitaService.incCommentLike(Id);
		} else {
			updateCode =quanziService.incCommentLike(Id);
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
	
	@RequestMapping(value="/decCommentLikeXiaoyuan")
    public  Object decCommentLike(
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode = 0;
        if (region.equals("sg")) {
        	updateCode =caicaiService.decCommentLike(Id);
		} else if (region.equals("beita")) {
			updateCode =beitaService.decCommentLike(Id);
		} else {
			updateCode =quanziService.decCommentLike(Id);
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
	
	
	@RequestMapping(value="/deleteTaskXiaoyuan")
    public  Object deleteTask(
    						HttpServletRequest request,
                           @RequestParam (value = "pk")String Id,
                           @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus) throws UnsupportedEncodingException{ 
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
	        if (region.equals("sg")) {
	        	updateCode =caicaiService.deleteTask(Integer.parseInt(id));
			} else if (region.equals("beita")) {
				updateCode =beitaService.deleteTask(Integer.parseInt(id));
			} else {
				updateCode =quanziService.deleteTask(Integer.parseInt(id));
			}
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
	
	@RequestMapping(value="/addlikeXiaoyuan")
    public  Object addLike(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "pk")int pk,
                           @RequestParam (value = "region",required = false)String region,
    					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        Like like=new Like();
        like.setPk(pk);
        like.setOpenid(openid);
        int addcode = 0;
        int updateCode = 0;
        if (region.equals("sg")) {
        	addcode=caicaiService.addLike(like);
            updateCode =caicaiService.incLike(pk);
        } else if (region.equals("beita")) {
        	addcode=beitaService.addLike(like);
        	updateCode =beitaService.incLike(pk);
        } else {
        	addcode=quanziService.addLike(like);
            updateCode =quanziService.incLike(pk);
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
	
	@RequestMapping(value="/suggestionXiaoyuan")
    public  Object suggestion(
                          @RequestParam (value = "id")int id,
                          @RequestParam (value = "openid")String openid,
                          @RequestParam (value = "content")String content,
                          @RequestParam (value = "region",required = false)String region,
   					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        Suggestion suggestion=new Suggestion();
        suggestion.setTask_id(id);
        suggestion.setContent(content);
        suggestion.setOpenid(openid);
        int addcode = 0;
        List<Suggestion> suggestionList = null;
        if (region.equals("sg")) {
        	addcode=caicaiService.addSuggestion(suggestion);
        	suggestionList =caicaiService.getSuggestionByPk(id);
        } else if (region.equals("beita")) {
        	addcode=beitaService.addSuggestion(suggestion);
        	suggestionList =beitaService.getSuggestionByPk(id);
        } else {
        	addcode=quanziService.addSuggestion(suggestion);
        	suggestionList =quanziService.getSuggestionByPk(id);
        }
        
        if (suggestionList.size() > 5) {
        	int updateCode = 0;
        	if (region.equals("sg")) {
        		updateCode=caicaiService.hideTask(id);
            } else if (region.equals("beita")) {
            	updateCode=beitaService.hideTask(id);
            }
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
	
	@RequestMapping(value="/addcommentXiaoyuan")
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
                           @RequestParam (value = "pid")int pid,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
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
        
        List<BlackList> checkCode = null;
    	if (region.equals("sg")) {
    		checkCode =caicaiService.checkBlackList(openid);
        } else if (region.equals("beita")) {
        	checkCode =beitaService.checkBlackList(openid);
        } else {
        	checkCode =quanziService.checkBlackList(openid);
        }
    	
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
        	int addcode = 0;
        	int updateCode = 0;
        	
        	if (region.equals("sg")) {
        		addcode=caicaiService.addComment(commenta);
        		updateCode =caicaiService.incComment(pk);
        	} else if (region.equals("beita")) {
            	addcode=beitaService.addComment(commenta);
            	updateCode =beitaService.incComment(pk);
            } else {
            	addcode=quanziService.addComment(commenta);
            	updateCode =quanziService.incComment(pk);
            }
        }
        return map;
    }
	
	@RequestMapping(value="/deleteLikeXiaoyuan")
    public  Object deleteLike(
                           @RequestParam (value = "id")int Id,
                           @RequestParam (value = "pk")int pk,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =0;
        int updateCode2 =0;
        
        if (region.equals("sg")) {
        	updateCode =caicaiService.deleteLike(Id);
    		updateCode2 =caicaiService.decLike(pk);
    	} else if (region.equals("beita")) {
    		updateCode =beitaService.deleteLike(Id);
    		updateCode2 =beitaService.decLike(pk);
        } else {
        	updateCode =quanziService.deleteLike(Id);
        	updateCode2 =quanziService.decLike(pk);
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
	
	@RequestMapping(value="/getlikeByPkXiaoyuan")
    public  Object getlikeByPk(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "pk")int pk,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =null;
        if (region.equals("sg")) {
        	likeList =caicaiService.getlikeByPk(openid,pk);
    	} else if (region.equals("beita")) {
    		likeList =beitaService.getlikeByPk(openid,pk);
        } else {
        	likeList =quanziService.getlikeByPk(openid,pk);
        }
        
        map.put("likeList",likeList);       
        return map;
    }
	
	@RequestMapping(value="/getlikeByOpenidXiaoyuan")
    public  Object getlikeByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Like> likeList =null;
        if (region.equals("sg")) {
        	likeList =caicaiService.getlikeByOpenid(openid,length);
    	} else if (region.equals("beita")) {
    		likeList =beitaService.getlikeByOpenid(openid,length);
        } else {
        	likeList =quanziService.getlikeByOpenid(openid,length);
        }
        map.put("likeList",likeList);       
        return map;
    }
	
	
	public List<CommentIdentity> convertCommentToCommentIdentity(List<Comment> comments) {
	    List<CommentIdentity> commentIdentityList = new ArrayList<>();
	    // 提取所有 openid 和 applyTo
	    Set<String> openidSet = new HashSet<>();
	    for (Comment comment : comments) {
	        if (comment.getOpenid() != null) {
	            openidSet.add(comment.getOpenid());
	        }
	        if (comment.getApplyTo() != null) {
	            openidSet.add(comment.getApplyTo());
	        }
	    }
	    // 查询身份映射
	    Map<String, Integer> openidIdentityMap = null;
	    if (openidSet.size()>0) {
		    // 查询身份信息
		    List<VerifyUserIdentity> userList = quanziService.getUserVerifyByOpenidList(new ArrayList<>(openidSet));
		    // 构建 openid -> identity 映射
		    openidIdentityMap = userList.stream()
		        .collect(Collectors.toMap(
		            VerifyUserIdentity::getOpenid,
		            VerifyUserIdentity::getIdentity,
		            (existing, replacement) -> existing
		        ));
	    }

	    // 构造带双重身份字段的 CommentIdentity 列表
	    for (Comment comment : comments) {
	        CommentIdentity commentIden = new CommentIdentity();
	        commentIden.setId(comment.getId());
	        commentIden.setOpenid(comment.getOpenid());
	        commentIden.setApplyTo(comment.getApplyTo());
	        commentIden.setAvatar(comment.getAvatar());
	        commentIden.setComment(comment.getComment());
	        commentIden.setPk(comment.getPk());
	        commentIden.setUserName(comment.getUserName());
	        commentIden.setC_time(comment.getC_time());
	        commentIden.setImg(comment.getImg());
	        commentIden.setLevel(comment.getLevel());
	        commentIden.setPid(comment.getPid());
	        commentIden.setLike_num(comment.getLike_num());
	        // 分别设置 identity 字段
	        int identityOpenid = openidIdentityMap.getOrDefault(comment.getOpenid(), 1);
	        int identityApplyTo = openidIdentityMap.getOrDefault(comment.getApplyTo(), 1);
	        commentIden.setIdentity_openid(identityOpenid);
	        commentIden.setIdentity_applyto(identityApplyTo);
	        commentIdentityList.add(commentIden);
	    }
	    return commentIdentityList;
	}


	
	@RequestMapping(value="/getSecondLevelXiaoyuan")
    public  Object getSecondLevel(
    					   @RequestParam (value = "id")int id,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentSecondList = null;
        if (region.equals("sg")) {
        	commentSecondList =caicaiService.getCommentByPid(id);
    	} else if (region.equals("beita")) {
    		commentSecondList =beitaService.getCommentByPid(id);
        } else {
        	commentSecondList =quanziService.getCommentByPid(id);
        }
//        map.put("commentSecondList",commentSecondList);     
        map.put("commentSecondList", convertCommentToCommentIdentity(commentSecondList));
        return map;
    }
	
	public List<CommentLevelIdentity> convertCommentLevelToCommentLevelIdentity(List<CommentLevel> commentLevels) {
	    List<CommentLevelIdentity> result = new ArrayList<>();

	    // 收集所有openid和applyTo，用于批量查询identity
	    Set<String> openidSet = new HashSet<>();
	    for (CommentLevel cl : commentLevels) {
	        if (cl.getOpenid() != null) openidSet.add(cl.getOpenid());
	        if (cl.getApplyTo() != null) openidSet.add(cl.getApplyTo());

	        // 递归收集内嵌评论的openid和applyTo
	        if (cl.getCommentList() != null) {
	            for (Comment c : cl.getCommentList()) {
	                if (c.getOpenid() != null) openidSet.add(c.getOpenid());
	                if (c.getApplyTo() != null) openidSet.add(c.getApplyTo());
	            }
	        }
	    }
	    
	    // 查询身份映射
	    Map<String, Integer> openidIdentityMap = null;
	    
	    if (openidSet.size()>0) {
	    	List<VerifyUserIdentity> userList = quanziService.getUserVerifyByOpenidList(new ArrayList<>(openidSet));
		    openidIdentityMap = userList.stream()
		        .collect(Collectors.toMap(
		            VerifyUserIdentity::getOpenid,
		            VerifyUserIdentity::getIdentity,
		            (existing, replacement) -> existing
		        ));
	    }
	    

	    // 转换 CommentLevel -> CommentLevelIdentity
	    for (CommentLevel cl : commentLevels) {
	        CommentLevelIdentity cli = new CommentLevelIdentity();

	        cli.setId(cl.getId());
	        cli.setOpenid(cl.getOpenid());
	        cli.setApplyTo(cl.getApplyTo());
	        cli.setAvatar(cl.getAvatar());
	        cli.setComment(cl.getComment());
	        cli.setPk(cl.getPk());
	        cli.setUserName(cl.getUserName());
	        cli.setC_time(cl.getC_time());
	        cli.setImg(cl.getImg());
	        cli.setLevel(cl.getLevel());
	        cli.setPid(cl.getPid());
	        cli.setLike_num(cl.getLike_num());

	        // 设置身份字段，假设 CommentLevelIdentity 有这两个字段
	        cli.setIdentity_openid(openidIdentityMap.getOrDefault(cl.getOpenid(), 1));
	        cli.setIdentity_applyto(openidIdentityMap.getOrDefault(cl.getApplyTo(), 1));

	        // 转换内部 commentList
	        if (cl.getCommentList() != null) {
	            List<CommentIdentity> commentIdentities = convertCommentToCommentIdentity(cl.getCommentList());
	            cli.setCommentList(commentIdentities);
	        } else {
	            cli.setCommentList(Collections.emptyList());
	        }

	        result.add(cli);
	    }

	    return result;
	}

	
	
	
	@RequestMapping(value="/getCommentByTypeXiaoyuan")
    public  Object getCommentByType(
    					   @RequestParam (value = "pk")int pk,
    					   @RequestParam (value = "length")int length,
    					   @RequestParam (value = "type")String type,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        long startTime=System.currentTimeMillis();
        List<Comment> commentList = null;
        if (region.equals("sg")) {
        	commentList =caicaiService.getCommentByType(pk,length,type);
    	} else if (region.equals("beita")) {
    		commentList =beitaService.getCommentByType(pk,length,type);
        } else {
        	commentList =quanziService.getCommentByType(pk,length,type);
        }
        
        List<String> strL = new ArrayList<>();
        for (Comment com:commentList) {
        	strL.add(String.valueOf(com.getId()));
        }
        List<Comment> commentSecondList = new ArrayList<>();
        if (commentList.size() > 0) {
        	
            if (region.equals("sg")) {
            	commentSecondList =caicaiService.getCommentByIdList(strL);
        	} else if (region.equals("beita")) {
        		commentSecondList =beitaService.getCommentByIdList(strL);
            } else {
            	commentSecondList =quanziService.getCommentByIdList(strL);
            }
        	
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

//        map.put("commentList",commentLevelList);     
        map.put("commentList",convertCommentLevelToCommentLevelIdentity(commentLevelList));
        return map;
    }
	
	
	@RequestMapping(value="/deleteCommentXiaoyuan")
    public  Object deleteComment(
    						HttpServletRequest request,
                           @RequestParam (value = "pk")int Id,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        String ip = IpUtil.getIpAddr(request);
        System.out.println(ip);
        int updateCode = 0;
        if (region.equals("sg")) {
        	updateCode =caicaiService.deleteComment(Id);
    	} else if (region.equals("beita")) {
    		updateCode =beitaService.deleteComment(Id);
        } else {
        	updateCode =quanziService.deleteComment(Id);
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
	
	@RequestMapping(value="/getCommentByOpenidXiaoyuan")
    public  Object getCommentByOpenid(
    					   @RequestParam (value = "openid")String openid,
    					   @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList = null;
        
        if (region.equals("sg")) {
        	commentList =caicaiService.getCommentByOpenid(openid,length);
    	} else if (region.equals("beita")) {
    		commentList =beitaService.getCommentByOpenid(openid,length);
        } else {
        	commentList =quanziService.getCommentByOpenid(openid,length);
        }
        
//        map.put("commentList",commentList);       
        map.put("commentList", convertCommentToCommentIdentity(commentList));
        return map;
    }
	

	@RequestMapping(value="/getAllCommentXiaoyuan")
    public  Object getAllComment(
    					   @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList = null;
        
        if (region.equals("sg")) {
        	commentList =caicaiService.getAllComment(length);
    	} else if (region.equals("beita")) {
    		commentList =beitaService.getAllComment(length);
        } else {
        	commentList =quanziService.getAllComment(length);
        }
        
//        map.put("commentList",commentList);       
        map.put("commentList", convertCommentToCommentIdentity(commentList));
        return map;
    }
	
	@RequestMapping(value="/getCommentByApplytoXiaoyuan")
    public  Object getCommentByApplyto(
    					   @RequestParam (value = "applyTo")String applyTo,
    					   @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Comment> commentList = null;
        
        if (region.equals("sg")) {
        	commentList =caicaiService.getCommentByApplyto(applyTo,length);
    	} else if (region.equals("beita")) {
    		commentList =beitaService.getCommentByApplyto(applyTo,length);
        } else {
        	commentList =quanziService.getCommentByApplyto(applyTo,length);
        }
        
//        map.put("commentList",commentList);    
        map.put("commentList", convertCommentToCommentIdentity(commentList));
        return map;
    }
	
	@RequestMapping(value="/getMemberXiaoyuan")
    public  Object getMember(
    					   @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =null;
        
        if (region.equals("sg")) {
        	memberList =caicaiService.getMember(openid);
    	} else if (region.equals("beita")) {
    		memberList =beitaService.getMember(openid);
        } else {
        	memberList =quanziService.getMember(openid,campus);
        }
        map.put("memberList",memberList);       
        return map;
    }
	
	@RequestMapping(value="/getAllMemberXiaoyuan")
    public  Object getAllMember(
            @RequestParam (value = "region",required = false)String region,
			   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Member> memberList =null;
        if (region.equals("sg")) {
        	memberList =caicaiService.getAllMember();
    	} else if (region.equals("beita")) {
    		memberList =beitaService.getAllMember();
        } else {
        	 memberList =quanziService.getAllMemberWX();
        }
        map.put("memberList",memberList);       
        return map;
    }
	
	@RequestMapping(value="/upDateWatchXiaoyuan")
    public  Object upDateWatch(
                           @RequestParam (value = "id")int Id,
                           @RequestParam (value = "watchNum")int watchNum,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        int updateCode =0;
        if (region.equals("sg")) {
        	updateCode =caicaiService.upDateWatch(watchNum,Id);
    	} else if (region.equals("beita")) {
    		updateCode =beitaService.upDateWatch(watchNum,Id);
        } else {
        	updateCode =quanziService.upDateWatch(watchNum,Id);
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
	
	@RequestMapping(value="/imgUploadXiaoyuan")
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
	
	@RequestMapping(value="/getBannerXiaoyuan")
    public  Object getBanner(
                            @RequestParam (value = "region",required = false)String region,
        					@RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        List<Banner> bannerList =null;
        if (region.equals("sg")) {
        	bannerList =caicaiService.getBanner();
    	} else if (region.equals("beita")) {
    		bannerList =beitaService.getBanner();
        } else {
        	bannerList =quanziService.getBannerXY(region, campus);
        }
        map.put("bannerList",bannerList);       
        return map;
    }
	
	
	
	@RequestMapping(value="/getRankListXiaoyuan")
    public  Object getRankList(
                           @RequestParam (value = "length")int length,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){
        Map<String,Object>map=new HashMap<>();
        List<BitRank> rankList =null;
        if (region.equals("sg")) {
        	rankList =caicaiService.getRankList(length);
    	} else if (region.equals("beita")) {
    		rankList =beitaService.getRankList(length);
        } else {
        	rankList =quanziService.getRankList(length);
        }
        map.put("rankList",rankList);    
        return map;
    }
	

	@RequestMapping(value="/addRankXiaoyuan")
    public  Object addRank(
                           @RequestParam (value = "openid")String openid,
                           @RequestParam (value = "avatar")String avatar,
                           @RequestParam (value = "userName")String userName,
                           @RequestParam (value = "score")String score,
                           @RequestParam (value = "region",required = false)String region,
       					   @RequestParam (value = "campus",required = false)String campus){ 
        Map<String,Object>map=new HashMap<>();
        BitRank bitrank=new BitRank();
        bitrank.setScore(score);
        bitrank.setOpenid(openid);
        bitrank.setAvatar(avatar);
        bitrank.setNickName(userName);
        List<BitRank> rankList =null;
        if (region.equals("sg")) {
        	rankList =caicaiService.getRankListByOpenid(openid);
    	} else if (region.equals("beita")) {
    		rankList =beitaService.getRankListByOpenid(openid);
        } else {
        	rankList =quanziService.getRankListByOpenid(openid);
        }
        int addcode = 0;
        if (rankList.size()>0) {
        	if (Integer.valueOf(rankList.get(0).getScore()) < Integer.valueOf(score)) {
        		if (region.equals("sg")) {
        			addcode=caicaiService.updateRank(bitrank);
            	} else if (region.equals("beita")) {
            		addcode =beitaService.updateRank(bitrank);
                } else {
                	addcode=quanziService.updateRank(bitrank);
                }
        	} else {
        		addcode=1;
        	}
            
        } else {
        	if (region.equals("sg")) {
    			addcode=caicaiService.addRank(bitrank);
        	} else if (region.equals("beita")) {
        		addcode =beitaService.addRank(bitrank);
            } else {
            	addcode=quanziService.addRank(bitrank);
            }
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
	

	@RequestMapping(value="/udpateUserInfoXiaoyuan")
	public Object addUserInfoXiaoyuan(
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "avatar")String avatar,
			@RequestParam (value = "nickname")String nickname
			) {
		Map<String,Object>map=new HashMap<>();
        // check existence
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
		if (existence.size()>0) {
			int updateCode = quanziService.udpateUserInfoByOpenid(openid,nickname,avatar);
			map.put("code", updateCode);
		 } else {
			 int status = -1;
			 int addCode = quanziService.addUserInfoByOpenid(openid,nickname,avatar, status);
			 map.put("code", addCode);
		 }
		return map;
	}
	
	@RequestMapping(value="/getVerifyUserByOpenidXiaoyuan")
	public Object getVerifyUserByOpenidXiaoyuan(
		@RequestParam (value = "openid")String openid) {
     Map<String,Object>map=new HashMap<>();
     List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
    if ((existence.size()>0)) {
        map.put("code", "200");
        map.put("msg", existence.get(0).getStatus());
		 } else {
			 map.put("code", "-1");
			 map.put("msg", "未提交认证信息");
		 }
		 return map;
	}
	
	
	@RequestMapping(value="/addVerifyUserXiaoyuan")
	public Object addVerifyUserQuanzi(
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "pic")String pic,
			@RequestParam (value = "email")String email,
			@RequestParam (value = "campus", required=false)String campus,
            @RequestParam (value = "region",required = false)String region) {
        Map<String,Object>map=new HashMap<>();
        
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
        if (existence.size()>0) {
            // 已存在：若为软删除/未注册态（status==-1）允许更新，否则视为已认证
            if (existence.get(0).getStatus()==-1) {
                int res = -1;
                VerifyUser verify_user = new VerifyUser();
                verify_user.setOpenid(openid);
                verify_user.setPic(pic);
                if (email.equals("") || email.equals(null)) {
                    verify_user.setStatus(0);
                } else {
                    verify_user.setStatus(1);
                }
                verify_user.setRegion(region);
                verify_user.setCampus(campus);
                verify_user.setEmail(email);
                res = quanziService.updateVerifyUserVerifyInfo(verify_user);
                if (res>0) {
                    map.put("code",200);
                    map.put("msg","更新成功！");
                } else {
                    map.put("code",-1);
                    map.put("msg","更新失败，请重试");
                }
            } else {
            	map.put("code",-1);
                map.put("msg","该微信号已认证");
            }
        } else {
        	int res = -1;
        	VerifyUser verify_user = new VerifyUser();
        	verify_user.setOpenid(openid);
        	verify_user.setPic(pic);
        	if (email.equals("") || email.equals(null)) {
        		verify_user.setStatus(0);
        	} else {
        		verify_user.setStatus(1);
        	}
        	verify_user.setRegion(region);
        	verify_user.setCampus(campus);
        	verify_user.setEmail(email);
            if (existence.size()==0) {
                res = quanziService.addVerifyUser(verify_user);
            }
        	
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
	
	@RequestMapping(value="/checkVerifyUserXiaoyuan")
	public Object checkVerifyUserQuanzi(
			@RequestParam (value = "openid")String openid) {
		Map<String,Object>map=new HashMap<>();
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
        if (existence.size()>0) {
            VerifyUserIdentity user = existence.get(0);
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
	
	@RequestMapping(value="/checkEmailXiaoyuan")
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
	
	@RequestMapping(value="/sendEmailXiaoyuan")
	public Object sendEmailXiaoyuan(
			@RequestParam (value = "email")String email,
			@RequestParam (value = "code")String code) {
		String subject = "校园认证";
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
	
	@RequestMapping(value="/setIdentityXiaoyuan")
	public Object setIdentityXiaoyuan(
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "identity")String identity) {
        Map<String,Object>map=new HashMap<>();
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
		if (existence.size()>0) {
        	int updateCode = quanziService.updateUserIdentityByOpenid(openid,identity);
        	map.put("code", 200);
			map.put("res", "更新成功");
        } else {
        	
        	map.put("code", 404);
        	map.put("res", "该用户未注册");
        }
		return map;
	}
	
	@RequestMapping(value="/getHotTaskXiaoyuan")
	public  Object getHotTaskXiaoyuan(
			@RequestParam (value = "length")int length,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus){ 
		Map<String,Object>map=new HashMap<>();
		List<Task> taskList = null;
		if (region.equals("sg")) {
			taskList = caicaiService.getHotTask(length);
		} else if (region.equals("beita")) {
			taskList = beitaService.getHotTask(length);
		} else if (region.equals("9")) {
			taskList = quanziService.getHotTaskByRegion(region, length);
		} else {
			taskList = quanziService.getHotTaskByRegionCampus(region, campus, length);
		}
		map.put("taskList",taskList);       
		return map;
	}
	
	@RequestMapping(value="/getCampusListXiaoyuan")
	public  Object getCampusListXiaoyuan(){ 
		Map<String,Object>map=new HashMap<>();
		List<Campus> campusList = quanziService.getAllCampusList();
		map.put("data",campusList);       
		return map;
	}
	
	@RequestMapping(value="/getCategoryListXiaoyuan")
	public  Object getCategoryListXiaoyuan(){ 
		Map<String,Object>map=new HashMap<>();
		List<RadioGroupCategory> categoryList = quanziService.getAllCategoryList();
		map.put("data",categoryList);       
		return map;
	}
	
	
	@RequestMapping(value="/addBlacklistXiaoyuan")
	public  Object addBlacklistXiaoyuan(
            @RequestParam (value = "openid",required = false)String openid,
            @RequestParam (value = "campus",required = false)String campus){ 
		Map<String,Object>map=new HashMap<>();
		String period = "永久";
		int resCode = quanziService.addBlacklistXiaoyuan(openid, period, campus);
		map.put("code",resCode);       
		return map;
	}
	
	@RequestMapping(value="/topTaskXiaoyuan")
	public  Object topTaskXiaoyuan(
            @RequestParam (value = "taskid")String taskid,
            @RequestParam (value = "region")String region){ 
		Map<String,Object>map=new HashMap<>();
		int updateCode = 0;
		int Id = Integer.parseInt(taskid);
		if (region.equals("sg")) {
        	updateCode =caicaiService.topTask(Id);
		} else if (region.equals("beita")) {
			updateCode = beitaService.topTask(Id);
		} else {
			updateCode = quanziService.topTask(Id);
		}
		if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",-1);
            map.put("msg","失败");
        }   
		return map;
	}
	
	@RequestMapping(value="/downTaskXiaoyuan")
	public  Object downTaskXiaoyuan(
            @RequestParam (value = "taskid")String taskid,
            @RequestParam (value = "region",required = false)String region,
			@RequestParam (value = "campus",required = false)String campus){ 
		Map<String,Object>map=new HashMap<>();
		int updateCode = 0;
		int Id = Integer.parseInt(taskid);
		if (region.equals("sg")) {
        	updateCode =caicaiService.downTask(Id);
		} else if (region.equals("beita")) {
			updateCode = beitaService.downTask(Id);
		} else {
			updateCode = quanziService.downTask(Id);
		}
		if(updateCode==1){
            map.put("code",200);
            map.put("msg","成功");
        }else {
            map.put("code",-1);
            map.put("msg","失败");
        }   
		return map;
	}
	
    private String getAccessToken(String region, String name){
    	Map<String, String> res = new HashMap<>();
    	List<AccessCode> accessCode = new ArrayList<>();
    	if (region.equals("beita")) {
    		accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    	} else if (region.equals("sg")) {
    		accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
    	} else if (region.equals("9")) {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000, "9");
    	} else {
    		accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000, "4");
    	}
    	
    	String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println(name+" 刷新token，campus:"+region);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String pa = ""; 
 	        if (region.equals("beita")) {
 	        	pa = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
        	} else if (region.equals("sg")) {
        		pa = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
        	} else if (region.equals("9")) {
        		Secret secretList = quanziService.getSecretByCampus("9").get(0);
        		pa = "grant_type=client_credential&appid="+secretList.getAppid()+"&secret="+secretList.getSecret();
        	} else {
        		Secret secretList = quanziService.getSecretByCampus("4").get(0);
        		pa = "grant_type=client_credential&appid="+secretList.getAppid()+"&secret="+secretList.getSecret();
        	}
 	        String json = HttpRequest.sendGet(url, pa);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			if (region.equals("beita")) {
 	 				beitaService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else if (region.equals("sg")) {
 	        		caicaiService.saveCode(accessToken,System.currentTimeMillis());
 	        	} else if (region.equals("9")){
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),"9");
 	        	} else {
 	        		quanziService.saveCode(accessToken,System.currentTimeMillis(),"4");
 	        	}
 	 			
 	 		}	
 		}
 		return accessToken;
    }
    
	@RequestMapping(value="/generateSelectedXiaoyuan")
	public  Object generateSelectedXiaoyuan(
			@RequestParam (value = "taskid")String taskid,
			@RequestParam (value = "region")String region) {
		Map<String,Object>map=new HashMap<>();
		List<AccessCode> accessCode = new ArrayList<>();
		List<Test> selectList = new ArrayList<>();
		try {
			String accessToken = getAccessToken(region, "getSelected");
			List<Task> taskList = new ArrayList<>();
    		if (region.equals("beita")) {
    			taskList =beitaService.gettaskbyId(Integer.parseInt(taskid));
	        	} else if (region.equals("sg")) {
	        		taskList =caicaiService.gettaskbyId(Integer.parseInt(taskid));
	        	} else {
	        		taskList =quanziService.gettaskbyId(Integer.parseInt(taskid));
	        	}
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("page","pages/detail/detail");
            paramMap.put("scene",String.valueOf("id="+taskid));
            paramMap.put("width",280);
            String timeString = String.valueOf(System.currentTimeMillis());
            String imgName = taskid+timeString+".png";
            String jar_parent = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent().replace("file:","");
            try
            {
                URL url2 = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                httpURLConnection.setRequestMethod("POST");// 提交模式
                httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
                httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
                // 发送POST请求必须设置如下两行
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                printWriter.write(JSON.toJSONString(paramMap));
                // flush输出流的缓冲
                printWriter.flush();
                //开始获取数据
                BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int n = 0;
                while (-1 != (n = bis.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                String newFileName = imgName.toString();
                String ACCESS_KEY = "[ACCESS_KEY]";
                String SECRET_KEY = "[SECRET_KEY]";
                String BUCKET = "[BUCKET]";
                Date date=new Date();//此时date为当前的时间
                System.out.println(date);
                SimpleDateFormat dateFormat=new SimpleDateFormat("YYYYMMdd");
                String PIC_URL = "[PIC_URL]/qr/"+dateFormat.format(date)+"/"+region+"/";
                Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
                String upToken = auth.uploadToken(BUCKET);
                Configuration cfg = new Configuration(Region.huanan());
                UploadManager uploadManager = new UploadManager(cfg);
                byte[] uploadBytes = output.toByteArray();
                Response response = uploadManager.put(uploadBytes, "quanzi/qr/"+dateFormat.format(date)+"/"+region+"/"+newFileName, upToken);
                System.out.println(PIC_URL + newFileName);
                //解析上传成功的结果
                //DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                Result resultSuccess = ResultGenerator.genSuccessResult();
                resultSuccess.setData(PIC_URL + newFileName);
                Test test=new Test();
	            test.setC_time(taskList.get(0).getC_time());
	            test.setCommentNum(taskList.get(0).getCommentNum());
	            test.setContent(taskList.get(0).getContent());
	            test.setRadioGroup(taskList.get(0).getRadioGroup());
	            test.setImg(PIC_URL + newFileName);
	            if (!taskList.get(0).getImg().equals("")) {
	            	test.setCover(taskList.get(0).getImg().split(",")[0]);
	            } else {
	            	test.setCover("");
	            }	            
	            test.setPrice(taskList.get(0).getPrice());
	            selectList.add(test);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (selectList.size()>0) {
			map.put("code",200);
			map.put("res", selectList.get(0).getImg());
		} else {
			map.put("code", -1);
			map.put("res", "fail");
		}
		
		return map;
	}
	
	
	private static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type","application/json;charset=UTF-8");
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
	
	
	// 获取登录手机号
	@RequestMapping(value="/getPhoneXiaoyuan")
	public  Object getPhoneXiaoyuan(
			@RequestParam (value = "code")String code,
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "region")String region,
			@RequestParam (value = "campus", required=false)String campus) {
		Map<String,Object> map = new HashMap<>();
		
		if (openid.equals("undefined") || openid.equals("") || openid.length()<20) {
			map.put("code", -1);
			map.put("res", -1);
		} else {
			
			String accessToken = getAccessToken(region, "getPhoneXiaoyuan");
			String url2 = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token="+accessToken;
	        
	        JSONObject obj = new JSONObject();
	        obj.put("code", code);
	        JSONObject testJ = JSONObject.parseObject(doPostJson(url2, obj.toString()));
	        System.out.println("testJ:"+testJ);
	        
	        if (testJ.getString("errcode").equals("0") || testJ.getInteger("errcode")==0) {
	            String phone = testJ.getJSONObject("phone_info").getString("phoneNumber");
	            // insert into db
	            List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
	            if (existence.size()>0) {
	            	int updateCode = quanziService.udpateUserPhoneByOpenid(openid,phone);
	            	map.put("code", updateCode);
	    			map.put("res", phone);
	            } else {
	            	int status = -1;
	            	int addCode = quanziService.addUserPhoneByOpenid(openid,phone, status);
	            	map.put("code", addCode);
	            	map.put("res", phone);
	            }

	        } else {
	        	map.put("code", -1);
	        	map.put("res",-1);
	        }
	        
		}
		
		return map;
	}
	
	@RequestMapping(value="/checkUserPhoneXiaoyuan")
	public Object checkUserPhoneXiaoyuan(
			@RequestParam (value = "openid")String openid
			) {
		Map<String,Object>map=new HashMap<>();
		// check existence
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
		if (existence.size()>0) {
            if (existence.get(0).getPhone()!=null) {
                String phone = existence.get(0).getPhone();
				if (phone.length()>0) {
					map.put("res", phone);
				} else {
					map.put("res", -1);
				}
			} else {
				map.put("res", -1);
			}
		} else {
			map.put("res",-1);
		 }
		return map;
	}
	
	@RequestMapping(value="/setCampusRegion")
	public Object setCampusRegion(
			@RequestParam (value = "openid")String openid,
			@RequestParam (value = "campus")String campus,
			@RequestParam (value = "region")String region
			) {
		Map<String,Object>map=new HashMap<>();
		// check existence
        List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
		int res = -1;
		if (existence.size()>0) {
			// 用户存在时更新
			res = quanziService.updateCampusRegionByOpenid(openid, campus, region);
		} else {
			// 用户不存在时新增
			res = quanziService.insertCampusRegionByOpenid(openid, campus, region);
		}
		map.put("res", res);
		return map;
	}
	
	@RequestMapping(value="/getUserInfo")
	public Object getUserInfo(
		@RequestParam (value = "openid")String openid) {
		System.out.println("openid for getuserinfo:"+openid);
		 Map<String,Object>map=new HashMap<>();
     List<VerifyUserIdentity> existence = quanziService.getVerifyUserByOpenid(openid);
		
		 if (existence.size()>0) {
			 map.put("res", existence.get(0));
		 } else {
			 map.put("res", null);
		 }
		 return map;
	}
	
	@RequestMapping(value="/getAllMeetupXiaoyuan")
	public Object getAllMeetupXiaoyuan(
			@RequestParam (value = "length")int length,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups;
		if (campus.equals("0")) {
//			meetups = xiaoyuanService.getMeetupByRegionCampus(length, region, campus);
			meetups = xiaoyuanService.getMeetupByRegion(length, region);
		} else {
			meetups = xiaoyuanService.getMeetupByRegionCampusAndAll(length, region, campus);
		}
		
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/getAvailableMeetupXiaoyuan")
	public Object getAvailableMeetupXiaoyuan(
			@RequestParam (value = "length")int length,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups;
		if (campus.equals("0")) {
			meetups = xiaoyuanService.getAvailableMeetupByRegionXiaoyuan(length, region);
		} else {
			meetups = xiaoyuanService.getAvailableMeetupXiaoyuan(length, region, campus);
		}
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/getMeetupByIdXiaoyuan")
	public Object getMeetupByIdXiaoyuan(
			@RequestParam (value = "group_id")String group_id,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups = xiaoyuanService.getMeetupByIdXiaoyuan(group_id);
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/getAllMeetupByCategoryXiaoyuan")
	public Object getAllMeetupByCategoryXiaoyuan(
			@RequestParam (value = "category")String category,
			@RequestParam (value = "length")int length,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups;
		if (campus.equals("0")) {
			meetups = xiaoyuanService.getMeetupByRegionCategoryXiaoyuan(category,length,region);
		} else {
			meetups = xiaoyuanService.getMeetupByRegionCampusAllCategoryXiaoyuan(category,length,region, campus);
		}
		
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/getAvailableMeetupByCategoryXiaoyuan")
	public Object getAvailableMeetupByCategoryXiaoyuan(
			@RequestParam (value = "category")String category,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups = xiaoyuanService.getAvailableMeetupByCategoryXiaoyuan(category);
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/addMeetupXiaoyuan")
	public Object addMeetupXiaoyuan(
			@RequestParam (value = "category") String category,
			@RequestParam (value = "description") String description,
			@RequestParam (value = "limitation") int limitation,
			@RequestParam (value = "release_openid") String release_openid,
			@RequestParam (value = "due_datetime") String due_datetime,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		Meetup meetup = new Meetup();
		meetup.setCategory(category);
		meetup.setDescription(description);
		meetup.setLimitation(limitation);
		meetup.setRelease_openid(release_openid);
		meetup.setDue_datetime(due_datetime);
		meetup.setRegion(region);
		meetup.setCampus(campus);
		meetup.setIs_delete(0);
		meetup.setPax(1);
		
		String group_id = UUIDGenerator.random12UUID();
		meetup.setGroup_id(group_id);
		int addcode = xiaoyuanService.addMeetup(meetup);
		map.put("res", addcode);
		return map;
	}
	
	@RequestMapping(value="/deleteMeetupXiaoyuan")
	public Object deleteMeetupXiaoyuan(
			@RequestParam (value = "group_id")String group_id
			) {
		Map<String,Object>map=new HashMap<>();
		int deletecode = xiaoyuanService.deleteMeetupByGroupId(group_id);
		map.put("res", deletecode);
		return map;
	}
	
	@RequestMapping(value="/getMeetupByReleaseOpenidXiaoyuan")
	public Object getMeetupByReleaseOpenidXiaoyuan(
			@RequestParam (value = "openid")String openid,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups = xiaoyuanService.getMeetupByReleaseOpenidXiaoyuan(openid);
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/getMeetupByJoinOpenidXiaoyuan")
	public Object getMeetupByJoinOpenidXiaoyuan(
			@RequestParam (value = "openid")String openid,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups = xiaoyuanService.getMeetupByJoinOpenidXiaoyuan(openid);
		map.put("res", meetups);
		return map;
	}
	
	@RequestMapping(value="/joinMeetupXiaoyuan")
	public Object joinMeetupXiaoyuan(
			@RequestParam (value = "group_id")String group_id,
			@RequestParam (value = "openid") String openid
			) {
		Map<String,Object>map=new HashMap<>();
		int addcode = xiaoyuanService.addMeetupJoiner(group_id, openid);
		map.put("res", addcode);
		return map;
	}
	
	@RequestMapping(value="/leaveMeetupXiaoyuan")
	public Object leaveMeetupXiaoyuan(
			@RequestParam (value = "group_id")String group_id,
			@RequestParam (value = "openid") String openid
			) {
		Map<String,Object>map=new HashMap<>();
		List<Meetup> meetups = xiaoyuanService.getMeetupByIdXiaoyuan(group_id);
		if (meetups.size()>0) {
			Meetup meetup = meetups.get(0);
			if (meetup.getIs_delete()==0) {
				String joiner = meetup.getJoin_openid();
				if ((joiner==null) || !(joiner.contains(openid))) {
					map.put("res", "-1");
					map.put("data", "您未参与该组局");
				} else {
					String[] joiner_list = joiner.split(",");
					List<String> new_list = new ArrayList<String>(Arrays.asList(joiner_list));
					new_list.remove(openid);
					new_list.remove("");
					new_list.remove(null);
					String joiner_string = String.join(",", new_list);
					int updateCode = xiaoyuanService.updateMeetupJoiner(group_id, joiner_string);
					map.put("res", updateCode);
					map.put("data", "退出成功");
				}
			} else {
				map.put("res", "-1");
				map.put("data", "该组局已被解散");
			}
			
		} else {
			map.put("res", "-1");
			map.put("data", "该组局不存在");
		}
		
		
		return map;
	}
	
	
	@RequestMapping(value="/getAllGroupBuyXiaoyuan")
	public Object getAllGroupBuyXiaoyuan(
			@RequestParam (value = "length")int length,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<GroupBuy> groupbuys;
		if (campus.equals("0")) {
			groupbuys = xiaoyuanService.getGroupBuyByRegionCampus(length, region, campus);
		} else {
			groupbuys = xiaoyuanService.getGroupBuyByRegionCampusAndAll(length, region, campus);
		}
		
		map.put("res", groupbuys);
		return map;
	}
	
	@RequestMapping(value="/getGroupBuyByIdXiaoyuan")
	public Object getGroupBuyByIdXiaoyuan(
			@RequestParam (value = "id")String id,
            @RequestParam (value = "region",required = false)String region,
            @RequestParam (value = "campus",required = false)String campus
			) {
		Map<String,Object>map=new HashMap<>();
		List<GroupBuy> groupbuys = xiaoyuanService.getGroupBuyByIdXiaoyuan(id);
		map.put("res", groupbuys);
		return map;
	}
	

	
	
	
	
}
