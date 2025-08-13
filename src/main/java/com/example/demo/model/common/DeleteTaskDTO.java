package com.example.demo.model.common;

import lombok.Data;

/**
 * @author : zhaoyingxiang
 * 删除任务请求DTO
 */
@Data
public class DeleteTaskDTO {
    /**
     * 用户OpenID
     */
    private String openid;

    /**
     * 关联的任务/帖子ID
     */
    private String pk;
}
