package com.kaoyan.admin.feign;

import com.kaoyan.common.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户服务 Feign 客户端
 */
@FeignClient(name = "studyapp4-user")
public interface UserFeignClient {
    
    @GetMapping("/api/v1/user/list")
    PageResult<Map<String, Object>> getUserList(@RequestParam Map<String, Object> params);
    
    @PutMapping("/api/v1/user/{userId}/status")
    void updateUserStatus(@PathVariable("userId") Long userId, @RequestParam("status") Integer status);
}
