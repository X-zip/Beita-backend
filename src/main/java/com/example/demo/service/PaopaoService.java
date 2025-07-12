package com.example.demo.service;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;

public interface PaopaoService {
	
	int authRegister(String username,String password);
}
