package com.kaoyan.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加评论请求
 */
@Data
public class AddCommentRequest {
    
    /**
     * 帖子 ID
     */
    @NotNull(message = "帖子ID不能为空")
    private Long postId;
    
    /**
     * 父评论 ID（一级评论传 0）
     */
    @NotNull(message = "父评论ID不能为空")
    private Long parentId;
    
    /**
     * 回复的用户 ID（二级评论必填）
     */
    private Long replyUserId;
    
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
