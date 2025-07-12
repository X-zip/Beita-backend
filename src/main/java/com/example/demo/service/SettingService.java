package com.example.demo.service;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.model.AccessCode;
import com.example.demo.model.Avatar;
import com.example.demo.model.Banner;
import com.example.demo.model.BitRank;
import com.example.demo.model.BlackList;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.model.Member;
import com.example.demo.model.NickName;
import com.example.demo.model.Secret;
import com.example.demo.model.Suggestion;
import com.example.demo.model.Task;
import com.example.demo.model.User;

public interface SettingService {
	
	List<NickName> getAllNickName(String campus);
	
	int deleteNickName(String campus, int id);

	int addNickName(String campus, String nickname);
	
	List<Avatar> getAllAvatar(String campus);
	
	int deleteAvatar(String campus, int id);

	int addAvatar(String campus, String avatar);
}
