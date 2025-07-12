package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.Secret;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;
import com.example.demo.service.QuanziService;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

import utils.HttpRequest;
import static java.util.Arrays.asList;

@RestController
public class WXController {
	
	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private CaicaiService caicaiService;
	
	@Autowired
	private QuanziService quanziService;
	
	static String charset = "UTF-8";
	static List<String> dirty_word = asList("代写", "daixie", "dai写", "保分", "保pass", "扫码", "群", "二维码", "新币", "支付宝", "人民币", "paynow",
            "换钱", "买手", "代购", "内裤", "内衣", "辅导", "论文", "代考", "贪污", "tanwu", "叛国", "panguo", "腐败", "fubai","江泽民","胡锦涛","温家宝","李克强","邓小平","毛泽东",
            "习近平", "xjp", "xijinping", "平子", "李尚福","fanfu","反腐", "fwb", "陪睡", "女s", "女m", "男s", "男m", "鉴定尺寸");
	
	@RequestMapping(value="/wxLoginQuanzi")
    public  Object wxLoginQuanzi(
    						@RequestParam (value = "code")String code,
    						@RequestParam (value = "appid")String appid){ 
        Map<String,Object>map=new HashMap<>();
        // 小程序唯一标识 (在微信小程序管理后台获取)
 		String wxspAppid = appid;
 		// 小程序的 app secret (在微信小程序管理后台获取)
 		String wxspSecret = quanziService.getSecret(appid).get(0).getSecret();
 		// 授权（必填）
 		String grant_type = "authorization_code";
 		// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
 		// 请求参数
 		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret
 				+ "&js_code=" + code + "&grant_type=" + grant_type;
 		// 发送请求
 		String json = HttpRequest.sendGet(
 				"https://api.weixin.qq.com/sns/jscode2session", params);
 		JSONObject jsonObject =  JSON.parseObject(json);
        map.put("result",jsonObject);       
        return map;
    }
	
	@RequestMapping(value="/wxLoginCaicai")
    public  Object wxLoginCaicai(@RequestParam (value = "code")String code){ 
        Map<String,Object>map=new HashMap<>();
        // 小程序唯一标识 (在微信小程序管理后台获取)
 		String wxspAppid = "[APPID]";
 		// 小程序的 app secret (在微信小程序管理后台获取)
 		String wxspSecret = "[SECRET]";
 		// 授权（必填）
 		String grant_type = "authorization_code";
 		// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
 		// 请求参数
 		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret
 				+ "&js_code=" + code + "&grant_type=" + grant_type;
 		// 发送请求
 		String json = HttpRequest.sendGet(
 				"https://api.weixin.qq.com/sns/jscode2session", params);
 		JSONObject jsonObject =  JSON.parseObject(json);
        map.put("result",jsonObject);       
        return map;
    }
	
	@RequestMapping(value="/wxLogin")
    public  Object wxLogin(@RequestParam (value = "code")String code){ 
        Map<String,Object>map=new HashMap<>();
        // 小程序唯一标识 (在微信小程序管理后台获取)
 		String wxspAppid = "[APPID]";
 		// 小程序的 app secret (在微信小程序管理后台获取)
 		String wxspSecret = "[SECRET]";
 		// 授权（必填）
 		String grant_type = "authorization_code";
 		// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
 		// 请求参数
 		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret
 				+ "&js_code=" + code + "&grant_type=" + grant_type;
 		// 发送请求
 		String json = HttpRequest.sendGet(
 				"https://api.weixin.qq.com/sns/jscode2session", params);
 		JSONObject jsonObject =  JSON.parseObject(json);
        map.put("result",jsonObject);       
        return map;
    }
	
	private String getAccessTokenQuanzi(String appid, String secret, String campus) {
		System.out.println("getAccessTokenQuanzi function刷新token，campus:"+campus);
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String params = "grant_type=client_credential&appid="+appid+"&secret="+secret;
        String json = HttpRequest.sendGet(
        		url, params);
 		JSONObject jsonObject =  JSON.parseObject(json);
 		String accessToken = String.valueOf(jsonObject.get("access_token"));
 		if (accessToken!="null") {
 			quanziService.saveCode(accessToken,System.currentTimeMillis(),campus);
 			
 		}	
		return accessToken;
	}
	
	private JSONObject requestMSGCheck(String accessToken, Map<String,Object> paramMap) throws Exception {
		URL url2 = new URL("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
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
	
	
	
	@RequestMapping(value="/msgCheckQuanzi")
    public  Object msgCheckQuanzi(@RequestParam (value = "content")String content,
    							  @RequestParam (value = "openid")String openid,
    							  @RequestParam (value = "campus")String campus,
    							  @RequestParam (value = "appid")String appid){
		// 校不校园的campus传入时即为4
		System.out.println("campus:"+campus);
		List<AccessCode> accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,campus);
		String accessToken= "";
		String secret = quanziService.getSecret(appid).get(0).getSecret();
		System.out.println("secret:"+secret);
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("msgCheckQuanzi刷新token，campus:"+campus);
 		// 获取access token
 			String url = "https://api.weixin.qq.com/cgi-bin/token";
 			String params = "grant_type=client_credential&appid="+appid+"&secret="+secret;
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			quanziService.saveCode(accessToken,System.currentTimeMillis(),campus);
 	 		}	
 		}		
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("content",content);
        paramMap.put("openid",openid);
        paramMap.put("version",2);
        paramMap.put("scene",3);
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
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
				accessToken = getAccessTokenQuanzi(appid,secret,campus);
				System.out.println("msgCheckQuanzi失败，重新刷新token，campus:"+campus);
				try {
					jsonObject2 = requestMSGCheck(accessToken,paramMap);
				} catch (Exception e) {
					e.printStackTrace();
					Map<String,Object>map=new HashMap<>();
				    map.put("result","error");       
				    return map;
				}
			}
			
			Map<String,Object>map=new HashMap<>();
	        map.put("result",jsonObject2);       
	        System.out.println("map:"+map);
	        

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
	
	private String getAccessTokenCaicai(String appid, String secret) {
		System.out.println("getAccessTokenCaicai function刷新token");
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String params = "grant_type=client_credential&appid="+appid+"&secret="+secret;
        String json = HttpRequest.sendGet(
        		url, params);
 		JSONObject jsonObject =  JSON.parseObject(json);
 		String accessToken = String.valueOf(jsonObject.get("access_token"));
 		if (accessToken!="null") {
 			caicaiService.saveCode(accessToken,System.currentTimeMillis());
 			
 		}	
		return accessToken;
	}
	
	@RequestMapping(value="/msgCheckCaicai")
    public  Object msgCheckCaicai(@RequestParam (value = "content")String content,
    							  @RequestParam (value = "openid")String openid){ 
		List<AccessCode> accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("msgCheckCaicai刷新token");
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	       String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			caicaiService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}		
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("content",content);
        paramMap.put("openid",openid);
        paramMap.put("version",2);
        paramMap.put("scene",3);
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
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
				accessToken = getAccessTokenCaicai("[APPID]", "[SECRET]");
				System.out.println("msgCheckCaicai失败，重新刷新token");
				try {
					jsonObject2 = requestMSGCheck(accessToken,paramMap);
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
	
	@RequestMapping(value="/msgCheck")
    public  Object msgCheck(@RequestParam (value = "content")String content,
    							  @RequestParam (value = "openid")String openid){ 
		List<AccessCode> accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("msgCheck刷新token");
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			beitaService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}
//        // 检测
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("content",content);
        paramMap.put("openid",openid);
        paramMap.put("version",2);
        paramMap.put("scene",3);
        JSONObject jsonObject2;
		try {
			URL url2 = new URL("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken);
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
	
	

	@RequestMapping(value="/imgCheckCaicai")
    public  Object imgCheckCaicai(MultipartFile file){ 
		List<AccessCode> accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("imgCheckCaicai刷新token");
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			caicaiService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}
//        // 检测
 		String line = null;//接口返回的结果
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"image\";filename=\""
                    + "https://api.weixin.qq.com" + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
 
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
 
            // 读取文件数据
            out.write(file.getBytes());
            // 最后添加换行
            out.write(newLine.getBytes());
 
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY
                    + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
        }
        return line;

    }
	
	@RequestMapping(value="/imgCheck")
    public  Object imgCheck(MultipartFile file){ 
		List<AccessCode> accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			beitaService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}
//        // 检测
 		String line = null;//接口返回的结果
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"image\";filename=\""
                    + "https://api.weixin.qq.com" + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 读取文件数据
            out.write(file.getBytes());
            // 最后添加换行
            out.write(newLine.getBytes());
 
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY
                    + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
        }
        return line;

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
	
	@RequestMapping(value="/sendCommentQuanzi")
    public  Object sendCommentQuanzi(	@RequestParam (value = "openid")String openid,
					    		  @RequestParam (value = "page")String page,
					    		  @RequestParam (value = "title")String title,
					    		  @RequestParam (value = "comment")String comment,
					    		  @RequestParam (value = "time")String c_time,
					    		  @RequestParam (value = "campus")String campus,
					    		  @RequestParam (value = "template_id")String template_id,
    							  @RequestParam (value = "appid")String appid){
		System.out.println(openid);
		System.out.println(page);
		System.out.println(title);
		System.out.println(comment);
		System.out.println(c_time);
		System.out.println(campus);
		System.out.println(template_id);
		System.out.println(appid);
		if (appid.equals("[APPID]")) {
			campus = "4";
		} else if (appid.equals("[APPID]")) {
			campus = "9";
		}
		List<AccessCode> accessCode = quanziService.getCodeCtime(System.currentTimeMillis()-5400*1000,campus);
		String accessToken= "";
		String secret = quanziService.getSecret(appid).get(0).getSecret();
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("sendCommentQuanzi刷新token，campus:"+campus);
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	       String params = "grant_type=client_credential&appid="+appid+"&secret="+secret;
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			quanziService.saveCode(accessToken,System.currentTimeMillis(),campus);
 	 		}	
 		}
//        // 检测
 		//System.out.println(title);
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("touser",openid);
        paramMap.put("template_id",template_id);
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        value1.put("value",title);
        data.put("thing1",value1);
        value2.put("value",comment);
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
				accessToken = getAccessTokenQuanzi(appid,secret,campus);
				System.out.println("sendCommentQuanzi失败，重新刷新token，campus:"+campus);
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
	

	
	@RequestMapping(value="/sendCommentCaicai")
    public  Object sendCommentCaicai(	@RequestParam (value = "openid")String openid,
					    		  @RequestParam (value = "page")String page,
					    		  @RequestParam (value = "title")String title,
					    		  @RequestParam (value = "comment")String comment,
					    		  @RequestParam (value = "time")String c_time){ 
		List<AccessCode> accessCode = caicaiService.getCodeCtime(System.currentTimeMillis()-5400*1000);
		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("sendCommentCaicai刷新token");
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	       String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			caicaiService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}
//        // 检测
 		//System.out.println(title);
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("touser",openid);
        paramMap.put("template_id","[TEMPLATE_ID]");
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        value1.put("value",title);
        data.put("thing1",value1);
        value2.put("value",comment);
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
				accessToken = getAccessTokenCaicai("[APPID]", "[SECRET]");
				System.out.println("sendCommentCaicai失败，重新刷新token");
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
	
	@RequestMapping(value="/sendComment")
    public  Object sendComment(	@RequestParam (value = "openid")String openid,
					    		  @RequestParam (value = "page")String page,
					    		  @RequestParam (value = "title")String title,
					    		  @RequestParam (value = "comment")String comment,
					    		  @RequestParam (value = "time")String c_time){
 		List<AccessCode> accessCode = beitaService.getCodeCtime(System.currentTimeMillis()-5400*1000);
 		String accessToken= "";
 		if (accessCode.size() > 0) {
 			accessToken = accessCode.get(0).getAccessCode();
 		} else {
 			System.out.println("sendComment刷新token");
 		// 获取access token
 	        String url = "https://api.weixin.qq.com/cgi-bin/token";
 	        String params = "grant_type=client_credential&appid=[APPID]&secret=[SECRET]";
 	        String json = HttpRequest.sendGet(
 	        		url, params);
 	 		JSONObject jsonObject =  JSON.parseObject(json);
 	 		accessToken = String.valueOf(jsonObject.get("access_token"));
 	 		if (accessToken!="null") {
 	 			beitaService.saveCode(accessToken,System.currentTimeMillis());
 	 		}	
 		}
//        // 检测
 		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("touser",openid);
        paramMap.put("template_id","[TEMPLATE_ID]");
        Map<String,Object> value1 = new HashMap<>();
        Map<String,Object> value2 = new HashMap<>();
        Map<String,Object> value3 = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        value1.put("value",title);
        data.put("thing1",value1);
        value2.put("value",comment);
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
