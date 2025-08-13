package com.example.demo.model;

import lombok.Data;

@Data
public class Task {
	private int id;
	private String ip;
	private String content;
	private String price;
	private String title;
	private String wechat;
	private String openid;
	private String avatar;
	private String campusGroup;
	private int commentNum;
	private int watchNum;
	private int likeNum;
	private String radioGroup;
	private String img;
	private String cover;
	private int is_delete;
	private int is_complaint;

	private String region;
	private String userName;
	private String c_time;
	private String comment_time;
	private int choose;
	private int hot;

}
