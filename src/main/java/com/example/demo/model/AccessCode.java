package com.example.demo.model;

import java.sql.Timestamp;

public class AccessCode {
	private int id;
	private String accessCode;
	private Long c_time;
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

}
