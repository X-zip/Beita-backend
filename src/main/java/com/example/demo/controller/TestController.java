package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.model.Test;
import com.example.demo.service.BeitaService;
import com.example.demo.service.CaicaiService;

import utils.HttpRequest;
import utils.SessionUtils;

@Controller
public class TestController {

	@Autowired
	private BeitaService beitaService;
	
	@Autowired
	private CaicaiService caicaiService;
	
    

    
    public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
    


    
    @GetMapping({"/getSuggestion"})
    public  Object getSuggestion(HttpServletRequest request){
    	List<Suggestion> suggestionList =beitaService.getSuggestion();
        request.setAttribute("suggestionList", suggestionList);
        System.out.println("suggestionList::::"+suggestionList);
        return "suggestion";
    }
    

    
}
