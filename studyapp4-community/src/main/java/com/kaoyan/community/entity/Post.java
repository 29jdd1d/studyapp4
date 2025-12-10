package com.kaoyan.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 帖子实体类（资讯、经验分享、打卡）
 */
@Data
@TableName("t_post")
public class Post {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * 类型：news-资讯，experience-经验分享，checkin-打卡
     */
    private String type;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 内容（支持富文本）
     */
    private String content;
    
    /**
     * 图片 URL（多个用逗号分隔）
     */
    private String images;
    
    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;
    
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
     * 状态：0-待审核，1-已发布，2-已拒绝
     */
    private Integer status;
    
    /**
     * 是否置顶
     */
    private Boolean isTop;
    
    /**
     * 是否精华
     */
    private Boolean isEssence;
    
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
