package com.kaoyan.community.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户服务 Feign 客户端
 */
@FeignClient(name = "studyapp4-user")
public interface UserFeignClient {
    
    /**
     * 批量获取用户信息
     */
    @GetMapping("/api/v1/user/batch")
    Map<Long, Map<String, String>> batchGetUserInfo(@RequestParam("userIds") String userIds);
}
