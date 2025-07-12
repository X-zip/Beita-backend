package com.example.demo.model;

public class PMStatus {
	private int id;
    private String sender;
    private String receiver;
    private String c_time;
    private int status;
    private String region;
    private String campusGroup;
    private String update_time;
    private String sender_last_read_time;
    private String receiver_last_read_time;
 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getC_time() {
		return c_time;
	}
	public void setC_time(String c_time) {
		this.c_time = c_time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getSender_last_read_time() {
		return sender_last_read_time;
	}
	public void setSender_last_read_time(String sender_last_read_time) {
		this.sender_last_read_time = sender_last_read_time;
	}
	public String getReceiver_last_read_time() {
		return receiver_last_read_time;
	}
	public void setReceiver_last_read_time(String receiver_last_read_time) {
		this.receiver_last_read_time = receiver_last_read_time;
	}

	
    
}
