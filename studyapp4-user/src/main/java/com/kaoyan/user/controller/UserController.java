package com.kaoyan.user.controller;

import com.kaoyan.common.result.Result;
import com.kaoyan.user.dto.UserUpdateRequest;
import com.kaoyan.user.dto.WxLoginRequest;
import com.kaoyan.user.service.UserService;
import com.kaoyan.user.vo.LoginVO;
import com.kaoyan.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 Controller
 */
@Tag(name = "用户管理", description = "用户相关接口，包括微信登录、个人信息管理等")
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 微信登录
     */
    @Operation(summary = "微信登录", description = "通过微信小程序授权码进行登录，返回JWT token和用户信息")
    @PostMapping("/wx-login")
    public Result<LoginVO> wxLogin(@Validated @RequestBody WxLoginRequest request) {
        log.info("微信登录请求: code={}", request.getCode());
        LoginVO loginVO = userService.wxLogin(request);
        return Result.success(loginVO);
    }
    
    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取当前登录用户的详细信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(
            @Parameter(description = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        log.info("获取用户信息: userId={}", userId);
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }
    
    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的个人信息")
    @PutMapping("/info")
    public Result<Boolean> updateUserInfo(
            @Parameter(description = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Validated @RequestBody UserUpdateRequest request) {
        log.info("更新用户信息: userId={}, request={}", userId, request);
        Boolean result = userService.updateUserInfo(userId, request);
        return Result.success(result);
    }
    
    /**
     * 增加学习时长
     */
    @PostMapping("/study-time")
    public Result<Boolean> addStudyTime(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam Integer minutes) {
        log.info("增加学习时长: userId={}, minutes={}", userId, minutes);
        Boolean result = userService.addStudyTime(userId, minutes);
        return Result.success(result);
    }
    
    /**
     * 签到打卡
     */
    @PostMapping("/check-in")
    public Result<Integer> checkIn(@RequestHeader("X-User-Id") Long userId) {
        log.info("签到打卡: userId={}", userId);
        Integer continuousDays = userService.checkIn(userId);
        return Result.success("签到成功", continuousDays);
    }
}
