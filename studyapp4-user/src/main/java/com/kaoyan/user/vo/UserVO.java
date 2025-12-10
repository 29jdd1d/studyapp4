package com.kaoyan.user.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息响应VO
 */
@Data
public class UserVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long id;
    
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
     * 当前基础
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
    private LocalDateTime createTime;
}
