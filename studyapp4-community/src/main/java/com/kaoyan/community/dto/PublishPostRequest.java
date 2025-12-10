package com.kaoyan.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发布帖子请求
 */
@Data
public class PublishPostRequest {
    
    /**
     * 类型：news-资讯，experience-经验分享，checkin-打卡
     */
    @NotBlank(message = "类型不能为空")
    private String type;
    
    /**
     * 标题（打卡可为空）
     */
    private String title;
    
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;
    
    /**
     * 图片 URL（多个用逗号分隔）
     */
    private String images;
    
    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;
}
