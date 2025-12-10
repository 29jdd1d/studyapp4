package com.kaoyan.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误 4xx
    FAIL(400, "操作失败"),
    UNAUTHORIZED(401, "未授权，请登录"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(405, "参数错误"),
    
    // 服务端错误 5xx
    SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),
    
    // 业务错误 1xxx
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    WX_LOGIN_ERROR(1004, "微信登录失败"),
    TOKEN_EXPIRED(1005, "登录已过期"),
    TOKEN_INVALID(1006, "无效的token"),
    
    RESOURCE_NOT_FOUND(2001, "资源不存在"),
    RESOURCE_UPLOAD_FAIL(2002, "资源上传失败"),
    
    PLAN_GENERATE_FAIL(3001, "学习计划生成失败"),
    
    QUESTION_NOT_FOUND(4001, "题目不存在"),
    ANSWER_SUBMIT_FAIL(4002, "答题提交失败"),
    
    POST_NOT_FOUND(5001, "帖子不存在"),
    COMMENT_FAIL(5002, "评论失败");
    
    private final Integer code;
    private final String message;
}
