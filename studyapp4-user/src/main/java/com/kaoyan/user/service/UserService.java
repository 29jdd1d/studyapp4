package com.kaoyan.user.service;

import com.kaoyan.user.dto.UserUpdateRequest;
import com.kaoyan.user.dto.WxLoginRequest;
import com.kaoyan.user.entity.User;
import com.kaoyan.user.vo.LoginVO;
import com.kaoyan.user.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 微信登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginVO wxLogin(WxLoginRequest request);
    
    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     *
     * @param userId  用户ID
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean updateUserInfo(Long userId, UserUpdateRequest request);
    
    /**
     * 增加学习时长
     *
     * @param userId  用户ID
     * @param minutes 学习时长（分钟）
     * @return 是否成功
     */
    Boolean addStudyTime(Long userId, Integer minutes);
    
    /**
     * 签到打卡
     *
     * @param userId 用户ID
     * @return 连续打卡天数
     */
    Integer checkIn(Long userId);
}
