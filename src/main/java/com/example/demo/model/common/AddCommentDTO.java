package com.example.demo.model.common;

import lombok.Data;

/**
 * @author : zhaoyingxiang
 * 添加评论请求DTO
 */
@Data
public class AddCommentDTO {
    /**
     * 用户OpenID
     */
    private String openid;
    
    /**
     * 回复目标
     */
    private String applyTo;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 评论内容
     */
    private String comment;
    
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 创建时间
     */
    private String c_time;
    
    /**
     * 关联的任务/帖子ID
     */
    private int pk;
    
    /**
     * 图片
     */
    private String img;
    
    /**
     * 评论层级
     */
    private String level;
    
    /**
     * 二级评论的parent id
     */
    private int pid;
}