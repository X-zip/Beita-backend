package com.example.demo.model;

import lombok.Data;

@Data
public class VerifyUser {
	private int id;
	private String openid;
	private String pic;
	private String c_time;
	private int status;
	private String campus;
	private String email;
	private String region;
	private String phone;
}
