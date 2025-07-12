package com.example.demo.model;

import java.util.List;

public class CommentIdentity {
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
	private int identity_openid;
	private int identity_applyto;
	
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public int getId() {
		return id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getApplyTo() {
		return applyTo;
	}
	public void setApplyTo(String applyTo) {
		this.applyTo = applyTo;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getC_time() {
		return c_time;
	}
	public void setC_time(String c_time) {
		this.c_time = c_time;
	}
	public int getIdentity_openid() {
		return identity_openid;
	}
	public void setIdentity_openid(int identity_openid) {
		this.identity_openid = identity_openid;
	}
	public int getIdentity_applyto() {
		return identity_applyto;
	}
	public void setIdentity_applyto(int identity_applyto) {
		this.identity_applyto = identity_applyto;
	}
	
	
	
}
