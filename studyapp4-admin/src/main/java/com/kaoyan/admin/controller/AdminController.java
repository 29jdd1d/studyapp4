package com.kaoyan.admin.controller;

import com.kaoyan.admin.service.AdminService;
import com.kaoyan.admin.vo.StatisticsVO;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 后台管理控制器
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    
    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics() {
        StatisticsVO stats = adminService.getStatistics();
        return Result.success(stats);
    }
    
    // ==================== 用户管理 ====================
    
    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public Result<PageResult<Map<String, Object>>> getUserList(@RequestParam Map<String, Object> params) {
        PageResult<Map<String, Object>> result = adminService.getUserList(params);
        return Result.success(result);
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/users/{userId}/status")
    public Result<Void> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        adminService.updateUserStatus(userId, status);
        return Result.success();
    }
    
    // ==================== 资源管理 ====================
    
    /**
     * 获取资源列表
     */
    @GetMapping("/resources")
    public Result<PageResult<Map<String, Object>>> getResourceList(@RequestParam Map<String, Object> params) {
        PageResult<Map<String, Object>> result = adminService.getResourceList(params);
        return Result.success(result);
    }
    
    /**
     * 审核资源
     */
    @PutMapping("/resources/{resourceId}/review")
    public Result<Void> reviewResource(@PathVariable Long resourceId, @RequestParam Integer status) {
        adminService.reviewResource(resourceId, status);
        return Result.success();
    }
    
    /**
     * 删除资源
     */
    @DeleteMapping("/resources/{resourceId}")
    public Result<Void> deleteResource(@PathVariable Long resourceId) {
        adminService.deleteResource(resourceId);
        return Result.success();
    }
    
    // ==================== 社区管理 ====================
    
    /**
     * 获取帖子列表
     */
    @GetMapping("/posts")
    public Result<PageResult<Map<String, Object>>> getPostList(@RequestParam Map<String, Object> params) {
        PageResult<Map<String, Object>> result = adminService.getPostList(params);
        return Result.success(result);
    }
    
    /**
     * 审核帖子
     */
    @PutMapping("/posts/{postId}/review")
    public Result<Void> reviewPost(@PathVariable Long postId, @RequestParam Integer status) {
        adminService.reviewPost(postId, status);
        return Result.success();
    }
    
    /**
     * 置顶帖子
     */
    @PutMapping("/posts/{postId}/top")
    public Result<Void> setPostTop(@PathVariable Long postId, @RequestParam Boolean isTop) {
        adminService.setPostTop(postId, isTop);
        return Result.success();
    }
    
    /**
     * 设置精华帖子
     */
    @PutMapping("/posts/{postId}/essence")
    public Result<Void> setPostEssence(@PathVariable Long postId, @RequestParam Boolean isEssence) {
        adminService.setPostEssence(postId, isEssence);
        return Result.success();
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/posts/{postId}")
    public Result<Void> deletePost(@PathVariable Long postId) {
        adminService.deletePost(postId);
        return Result.success();
    }
}
