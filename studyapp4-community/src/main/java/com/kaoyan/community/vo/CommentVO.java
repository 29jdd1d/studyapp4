package com.kaoyan.community.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论响应
 */
@Data
public class CommentVO {
    
    private Long id;
    
    /**
     * 帖子 ID
     */
    private Long postId;
    
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
     * 父评论 ID
     */
    private Long parentId;
    
    /**
     * 回复的用户 ID
     */
    private Long replyUserId;
    
    /**
     * 回复的用户昵称
     */
    private String replyUserName;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked;
    
    /**
     * 二级评论列表
     */
    private List<CommentVO> replies;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
