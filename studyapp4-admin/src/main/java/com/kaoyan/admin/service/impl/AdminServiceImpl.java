package com.kaoyan.admin.service.impl;

import com.kaoyan.admin.feign.CommunityFeignClient;
import com.kaoyan.admin.feign.ResourceFeignClient;
import com.kaoyan.admin.feign.UserFeignClient;
import com.kaoyan.admin.service.AdminService;
import com.kaoyan.admin.vo.StatisticsVO;
import com.kaoyan.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 后台管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final UserFeignClient userFeignClient;
    private final ResourceFeignClient resourceFeignClient;
    private final CommunityFeignClient communityFeignClient;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String STATS_CACHE_KEY = "admin:statistics";
    
    @Override
    public StatisticsVO getStatistics() {
        // 先从缓存获取
        StatisticsVO cached = (StatisticsVO) redisTemplate.opsForValue().get(STATS_CACHE_KEY);
        if (cached != null) {
            return cached;
        }
        
        // 这里简化实现，实际应通过 Feign 调用各服务获取统计数据
        // 或者直接查询数据库（推荐使用只读副本）
        StatisticsVO stats = new StatisticsVO();
        
        // 模拟数据（实际应从各服务获取）
        stats.setTotalUsers(1000L);
        stats.setTodayNewUsers(50L);
        stats.setTotalResources(500L);
        stats.setTodayNewResources(10L);
        stats.setTotalPosts(2000L);
        stats.setTodayNewPosts(100L);
        stats.setTotalPlans(800L);
        stats.setTodayNewPlans(40L);
        stats.setTotalAnswers(5000L);
        stats.setTodayAnswers(200L);
        
        // 缓存 10 分钟
        redisTemplate.opsForValue().set(STATS_CACHE_KEY, stats, 10, TimeUnit.MINUTES);
        
        return stats;
    }
    
    @Override
    public PageResult<Map<String, Object>> getUserList(Map<String, Object> params) {
        return userFeignClient.getUserList(params);
    }
    
    @Override
    public void updateUserStatus(Long userId, Integer status) {
        userFeignClient.updateUserStatus(userId, status);
        log.info("更新用户 {} 状态为 {}", userId, status);
    }
    
    @Override
    public PageResult<Map<String, Object>> getResourceList(Map<String, Object> params) {
        return resourceFeignClient.getResourceList(params);
    }
    
    @Override
    public void reviewResource(Long resourceId, Integer status) {
        resourceFeignClient.updateResourceStatus(resourceId, status);
        log.info("审核资源 {}，状态：{}", resourceId, status);
        
        // 清除统计缓存
        redisTemplate.delete(STATS_CACHE_KEY);
    }
    
    @Override
    public void deleteResource(Long resourceId) {
        resourceFeignClient.deleteResource(resourceId);
        log.info("删除资源 {}", resourceId);
        
        // 清除统计缓存
        redisTemplate.delete(STATS_CACHE_KEY);
    }
    
    @Override
    public PageResult<Map<String, Object>> getPostList(Map<String, Object> params) {
        return communityFeignClient.getPostList(params);
    }
    
    @Override
    public void reviewPost(Long postId, Integer status) {
        communityFeignClient.updatePostStatus(postId, status);
        log.info("审核帖子 {}，状态：{}", postId, status);
        
        // 清除统计缓存
        redisTemplate.delete(STATS_CACHE_KEY);
    }
    
    @Override
    public void setPostTop(Long postId, Boolean isTop) {
        communityFeignClient.setPostTop(postId, isTop);
        log.info("设置帖子 {} 置顶：{}", postId, isTop);
    }
    
    @Override
    public void setPostEssence(Long postId, Boolean isEssence) {
        communityFeignClient.setPostEssence(postId, isEssence);
        log.info("设置帖子 {} 精华：{}", postId, isEssence);
    }
    
    @Override
    public void deletePost(Long postId) {
        communityFeignClient.deletePost(postId);
        log.info("删除帖子 {}", postId);
        
        // 清除统计缓存
        redisTemplate.delete(STATS_CACHE_KEY);
    }
}
