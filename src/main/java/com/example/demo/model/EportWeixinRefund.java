package com.example.demo.model;

public class EportWeixinRefund {
//	private String prepay_id;
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String notify_url;
	private String out_trade_no;
	private String out_refund_no;
	// 订单总金额，单位为分，详见支付金额
//	private String spbill_create_ip;
	private int total_fee;
	private int refund_fee;
	private String refund_desc;
	private String sign;
//	private String timeStamp;
	
//	public String getPrepay_id() {
//		return prepay_id;
//	}
//
//	public void setPrepay_id(String prepay_id) {
//		this.prepay_id = prepay_id;
//	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public int getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_desc() {
		return refund_desc;
	}

	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}
	
	

}
