package com.kaoyan.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("t_user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 微信 openid
     */
    private String openid;
    
    /**
     * 微信 unionid
     */
    private String unionid;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 性别（0-未知 1-男 2-女）
     */
    private Integer gender;
    
    /**
     * 目标院校
     */
    private String targetUniversity;
    
    /**
     * 目标专业
     */
    private String targetMajor;
    
    /**
     * 考试年份
     */
    private Integer examYear;
    
    /**
     * 当前基础（1-零基础 2-基础薄弱 3-基础一般 4-基础较好 5-基础优秀）
     */
    private Integer currentLevel;
    
    /**
     * 会员状态（0-普通用户 1-VIP）
     */
    private Integer vipStatus;
    
    /**
     * 会员过期时间
     */
    private LocalDateTime vipExpireTime;
    
    /**
     * 累计学习时长（分钟）
     */
    private Integer totalStudyTime;
    
    /**
     * 连续打卡天数
     */
    private Integer continuousDays;
    
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
    
    /**
     * 逻辑删除（0-未删除 1-已删除）
     */
    @TableLogic
    private Integer deleted;
}
