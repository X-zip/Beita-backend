package com.example.demo.model;

import java.sql.Timestamp;

public class AccessCodePaotui {
	private int id;
	private String accessCode;
	private Long c_time;
	private String region;
	private String campusGroup;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public Long getC_time() {
		return c_time;
	}
	public void setC_time(Long c_time) {
		this.c_time = c_time;
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
	
	

}
