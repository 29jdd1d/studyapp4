package com.kaoyan.user.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信登录请求DTO
 */
@Data
public class WxLoginRequest {
    
    /**
     * 微信登录凭证 code
     */
    @NotBlank(message = "登录凭证不能为空")
    private String code;
    
    /**
     * 用户昵称（可选）
     */
    private String nickname;
    
    /**
     * 头像URL（可选）
     */
    private String avatar;
}
