package com.kaoyan.admin.feign;

import com.kaoyan.common.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 社区服务 Feign 客户端
 */
@FeignClient(name = "studyapp4-community")
public interface CommunityFeignClient {
    
    @GetMapping("/api/v1/community/post/list")
    PageResult<Map<String, Object>> getPostList(@RequestParam Map<String, Object> params);
    
    @PutMapping("/api/v1/community/post/{postId}/status")
    void updatePostStatus(@PathVariable("postId") Long postId, @RequestParam("status") Integer status);
    
    @PutMapping("/api/v1/community/post/{postId}/top")
    void setPostTop(@PathVariable("postId") Long postId, @RequestParam("isTop") Boolean isTop);
    
    @PutMapping("/api/v1/community/post/{postId}/essence")
    void setPostEssence(@PathVariable("postId") Long postId, @RequestParam("isEssence") Boolean isEssence);
    
    @DeleteMapping("/api/v1/community/post/{postId}")
    void deletePost(@PathVariable("postId") Long postId);
}
