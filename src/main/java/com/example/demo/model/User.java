package com.example.demo.model;

import lombok.Data;

@Data
public class User {
	private int id;
	private String account;
	private String password;
	private String campus;
	private String region;
	private String expired;
}
