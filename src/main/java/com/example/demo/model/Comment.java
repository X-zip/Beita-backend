package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class Comment {
	private int id;
	private String openid;
	private String applyTo;
	private String avatar;
	private String comment;
	private int pk;
	private String userName;
	private String c_time;
	private String img;
	private String level;
	private int pid;
	private int like_num;
}
