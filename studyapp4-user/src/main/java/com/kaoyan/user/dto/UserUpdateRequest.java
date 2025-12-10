package com.kaoyan.user.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户信息更新请求DTO
 */
@Data
public class UserUpdateRequest {
    
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
}
