package com.kaoyan.community.dto;

import lombok.Data;

/**
 * 帖子查询请求
 */
@Data
public class PostQueryRequest {
    
    /**
     * 类型：news-资讯，experience-经验分享，checkin-打卡
     */
    private String type;
    
    /**
     * 标签
     */
    private String tag;
    
    /**
     * 关键词
     */
    private String keyword;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 是否只看精华
     */
    private Boolean onlyEssence;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
