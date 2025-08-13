package com.example.demo.model.common;

import lombok.Data;

/**
 * @author : zhaoyingxiang
 * 校园板块新增task的入参DTO
 */
@Data
public class AddtaskXiaoyuanDTO {
    /**
     * 创建时间
     */
    private String c_time;
    /**
     * 价格
     */
    private String price;
    /**
     * 微信号
     */
    private String wechat;
    /**
     * 用户OpenID
     */
    private String openid;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 校园分组
     */
    private String campusGroup;
    /**
     * 评论数量
     */
    private int commentNum;
    /**
     * 浏览数量
     */
    private int watchNum;
    /**
     * 点赞数量
     */
    private int likeNum;
    /**
     * radio分组
     */
    private String radioGroup;
    /**
     * 图片
     */
    private String img;
    /**
     * 地区
     */
    private String region;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 封面图片
     */
    private String cover;
    /**
     * 加密字段
     */
    private String encrypted;
}
