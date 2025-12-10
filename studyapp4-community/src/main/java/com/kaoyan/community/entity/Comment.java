package com.kaoyan.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类（支持二级评论）
 */
@Data
@TableName("t_comment")
public class Comment {
    
    @TableId(type = IdType.AUTO)
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
     * 父评论 ID（一级评论为 0）
     */
    private Long parentId;
    
    /**
     * 回复的用户 ID（二级评论）
     */
    private Long replyUserId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
