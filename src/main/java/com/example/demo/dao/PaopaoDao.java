package com.example.demo.dao;
import com.example.demo.model.TaskPaotui;
//import com.example.demo.model.User;
import com.example.demo.model.WXTemplate;
import com.example.demo.model.Rider;
import com.example.demo.model.BlackList;
import com.example.demo.model.Secret;
import com.example.demo.model.AccessCode;
import com.example.demo.model.Banner;
import com.example.demo.model.Profile;

import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface PaopaoDao {
	@Update("insert web_user_verify(username, password, c_time) values (#{username}, #{password}, NOW()) ")
	int authRegister(String username,String password);
}
