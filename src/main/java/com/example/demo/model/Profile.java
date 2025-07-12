package com.example.demo.model;

public class Profile {
	private int id;
	private String openid;
	private int release_order_num;	
	private double income;
	private int pickup_order_num;
	private String region;
	private String campusGroup;
	
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
	public int getRelease_order_num() {
		return release_order_num;
	}
	public void setRelease_order_num(int release_order_num) {
		this.release_order_num = release_order_num;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public int getPickup_order_num() {
		return pickup_order_num;
	}
	public void setPickup_order_num(int pickup_order_num) {
		this.pickup_order_num = pickup_order_num;
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
