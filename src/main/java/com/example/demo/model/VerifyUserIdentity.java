package com.example.demo.model;

import lombok.Data;

@Data
public class VerifyUserIdentity {
	private int id;
	private String openid;
	private String pic;
	private String c_time;
	private int status;
	private String campus;
	private String email;
	private String region;
	private String phone;

	/**
	 * 校园认证状态
	 * 0.默认 3.认证
	 */
	private int identity;
}
