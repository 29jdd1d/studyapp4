package com.kaoyan.community.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 帖子详情响应
 */
@Data
public class PostVO {
    
    private Long id;
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String userName;
    
    /**
     * 用户头像
     */
    private String userAvatar;
    
    /**
     * 类型
     */
    private String type;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 图片列表
     */
    private String[] imageList;
    
    /**
     * 标签列表
     */
    private String[] tagList;
    
    /**
     * 浏览数
     */
    private Integer viewCount;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 收藏数
     */
    private Integer collectCount;
    
    /**
     * 是否置顶
     */
    private Boolean isTop;
    
    /**
     * 是否精华
     */
    private Boolean isEssence;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked;
    
    /**
     * 当前用户是否收藏
     */
    private Boolean isCollected;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
