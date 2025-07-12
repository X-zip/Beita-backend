package com.example.demo.model;

public class PMChatDetail {
	private int id;
    private String openid;
    private String nickname;
    private String avatar;
    private String targetOpenid;
    private String targetNickname;
    private String targetAvatar;
    private String c_time;
    private String content;
    private String region;
    private String campusGroup;
    private int is_delete;
    
	public int getId() {
		return id;
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
	public String getTargetOpenid() {
		return targetOpenid;
	}
	public void setTargetOpenid(String targetOpenid) {
		this.targetOpenid = targetOpenid;
	}
	public String getC_time() {
		return c_time;
	}
	public void setC_time(String c_time) {
		this.c_time = c_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCampusGroup() {
		return campusGroup;
	}
	public void setCampusGroup(String campusGroup) {
		this.campusGroup = campusGroup;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTargetNickname() {
		return targetNickname;
	}
	public void setTargetNickname(String targetNickname) {
		this.targetNickname = targetNickname;
	}
	public String getTargetAvatar() {
		return targetAvatar;
	}
	public void setTargetAvatar(String targetAvatar) {
		this.targetAvatar = targetAvatar;
	}
		
    
}
