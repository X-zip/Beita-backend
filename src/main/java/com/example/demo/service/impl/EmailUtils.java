package com.example.demo.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.service.EmailService;

@Service(value = "EmailService")
public class EmailUtils implements EmailService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
    @Autowired
    private JavaMailSender mailSender;
    
 // 配置文件中我的qq邮箱
    @Value("${spring.mail.from}")
    private String from;
    /**
     * 简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public int sendSimpleMail(String to, String subject, String content) {
    	try {
    		logger.info("from:",from);
            //创建SimpleMailMessage对象
            SimpleMailMessage message = new SimpleMailMessage();
            //邮件发送人
            message.setFrom(from);
            //邮件接收人
            message.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容
            message.setText(content);
            //发送邮件
            mailSender.send(message);
            return 1;
    	}catch (Throwable e) {
    		System.out.println(e);
            String[] invalid = getInvalidAddresses(e);
            if (invalid != null) {
                return -1;
            } else {
            	return -2;
            }
        }


    }
 
    /**
     * html邮件
     * @param to 收件人,多个时参数形式 ："xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public int sendHtmlMail(String to, String subject, String content) {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人,设置多个收件人地址
            InternetAddress[] internetAddressTo = InternetAddress.parse(to);
            messageHelper.setTo(internetAddressTo);
            //messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            //日志信息
            System.out.println("邮件已经发送给收件人："+to);
            return 1;
        } catch (Exception e) {
        	System.out.println("发送邮件时发生异常！"+e);
            String[] invalid = getInvalidAddresses(e);
            if (invalid != null) {
                return -1;
            } else {
            	return -2;
            }
        }
    }
 
    /**
     * 带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
 
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            //日志信息
            System.out.println("邮件已经发送。");
        } catch (Exception e) {
        	System.out.println("发送邮件时发生异常！"+e);
        }

    }
    

    /**
     * 从异常获取无效地址
     * @param e
     * @return
     */
    private static String[] getInvalidAddresses(Throwable e) {
        if (e == null) {
            return null;
        }
        if (e instanceof MailSendException) {
            System.out.println("e instanceof SendFailedException");
            Exception[] exceptions = ((MailSendException) e).getMessageExceptions();
            for (Exception exception : exceptions) {
                if (exception instanceof SendFailedException) {
                    return getStringAddress(((SendFailedException) exception).getInvalidAddresses());
                }
            }
        }
        if (e instanceof SendFailedException) {
            return getStringAddress(((SendFailedException) e).getInvalidAddresses());
        }
        return null;
    }
 
    /**
     * 将Address[]转成String[]
     * @param address
     * @return
     */
    private static String[] getStringAddress(Address[] address) {
        List<String> invalid = new ArrayList<>();
        for (Address a : address) {
            String aa = ((InternetAddress) a).getAddress();
            if (!StringUtils.isEmpty(aa)) {
                invalid.add(aa);
            }
        }
        return invalid.stream().distinct().toArray(String[]::new);
    }
}
