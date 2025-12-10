package com.kaoyan.admin.feign;

import com.kaoyan.common.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 资源服务 Feign 客户端
 */
@FeignClient(name = "studyapp4-resource")
public interface ResourceFeignClient {
    
    @GetMapping("/api/v1/resource/list")
    PageResult<Map<String, Object>> getResourceList(@RequestParam Map<String, Object> params);
    
    @PutMapping("/api/v1/resource/{resourceId}/status")
    void updateResourceStatus(@PathVariable("resourceId") Long resourceId, @RequestParam("status") Integer status);
    
    @DeleteMapping("/api/v1/resource/{resourceId}")
    void deleteResource(@PathVariable("resourceId") Long resourceId);
}
