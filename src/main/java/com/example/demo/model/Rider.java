package com.example.demo.model;

public class Rider {
	
	private int id;
	private String openid;
	private String realname;
	private String phone;
	private String wechat;
	private String school;
	private String campus;
	private String ic_number;
	private String student_card_pic;
	private String ic_pic_front;
	private String ic_pic_back;
	private int is_verified;
	private int is_blacklist;
	private String remark;
	private double income;
	private int pickup_order_num;
	private int busy;
	private String c_time;
	
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
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getIc_number() {
		return ic_number;
	}
	public void setIc_number(String ic_number) {
		this.ic_number = ic_number;
	}
	public String getStudent_card_pic() {
		return student_card_pic;
	}
	public void setStudent_card_pic(String student_card_pic) {
		this.student_card_pic = student_card_pic;
	}
	public String getIc_pic_front() {
		return ic_pic_front;
	}
	public void setIc_pic_front(String ic_pic_front) {
		this.ic_pic_front = ic_pic_front;
	}
	public String getIc_pic_back() {
		return ic_pic_back;
	}
	public void setIc_pic_back(String ic_pic_back) {
		this.ic_pic_back = ic_pic_back;
	}
	public int getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(int is_verified) {
		this.is_verified = is_verified;
	}
	public int getIs_blacklist() {
		return is_blacklist;
	}
	public void setIs_blacklist(int is_blacklist) {
		this.is_blacklist = is_blacklist;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public int getBusy() {
		return busy;
	}
	public void setBusy(int busy) {
		this.busy = busy;
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
	public String getC_time() {
		return c_time;
	}
	public void setC_time(String c_time) {
		this.c_time = c_time;
	}


}
