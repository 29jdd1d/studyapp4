package com.kaoyan.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserVO userInfo;
    
    /**
     * 是否新用户
     */
    private Boolean isNewUser;
}
