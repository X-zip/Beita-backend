package com.example.demo.model;

import lombok.Data;
import com.example.demo.model.common.TaskXiaoyuan;

@Data
public class Task {
	private int id;
	private String ip;
	private String content;
	private String price;
	private String title;
	private String wechat;
	private String openid;
	private String avatar;
	private String campusGroup;
	private int commentNum;
	private int watchNum;
	private int likeNum;
	private String radioGroup;
	private String img;
	private String cover;
	private int is_delete;
	private int is_complaint;

	private String region;
	private String userName;
	private String c_time;
	private String comment_time;
	private int choose;
	private int hot;

    /**
     * 从TaskXiaoyuan DTO创建Task对象的便捷构造器
     */
    public Task(TaskXiaoyuan taskXiaoyuan, String content, String title, String c_time, String ip) {
        this.content = content;
        this.title = title;
        this.c_time = c_time;
        this.ip = ip;
        
        // 从DTO拷贝属性
        this.price = taskXiaoyuan.getPrice();
        this.wechat = taskXiaoyuan.getWechat();
        this.openid = taskXiaoyuan.getOpenid();
        this.avatar = taskXiaoyuan.getAvatar();
        this.campusGroup = taskXiaoyuan.getCampusGroup();
        this.commentNum = taskXiaoyuan.getCommentNum();
        this.likeNum = taskXiaoyuan.getLikeNum();
        this.watchNum = taskXiaoyuan.getWatchNum();
        this.radioGroup = taskXiaoyuan.getRadioGroup();
        this.region = taskXiaoyuan.getRegion();
        this.userName = taskXiaoyuan.getUserName();
        
        // 特殊处理的字段
        this.img = taskXiaoyuan.getImg() == null ? "" : taskXiaoyuan.getImg().replace("[","").replace("]","").replace("\"","");
        this.cover = taskXiaoyuan.getCover() == null ? "" : taskXiaoyuan.getCover().replace("[","").replace("]","").replace("\"","");
    }
}
