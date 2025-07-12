package com.example.demo.service;

public interface EmailService {


	int sendSimpleMail(String to, String subject, String content);

	public int sendHtmlMail(String to, String subject, String content);

	public void sendAttachmentsMail(String to, String subject, String content, String filePath);

}
