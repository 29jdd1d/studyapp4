package com.kaoyan.admin.service;

import com.kaoyan.admin.vo.StatisticsVO;
import com.kaoyan.common.result.PageResult;

import java.util.Map;

/**
 * 后台管理服务接口
 */
public interface AdminService {
    
    /**
     * 获取统计数据
     */
    StatisticsVO getStatistics();
    
    /**
     * 获取用户列表
     */
    PageResult<Map<String, Object>> getUserList(Map<String, Object> params);
    
    /**
     * 更新用户状态
     */
    void updateUserStatus(Long userId, Integer status);
    
    /**
     * 获取资源列表
     */
    PageResult<Map<String, Object>> getResourceList(Map<String, Object> params);
    
    /**
     * 审核资源
     */
    void reviewResource(Long resourceId, Integer status);
    
    /**
     * 删除资源
     */
    void deleteResource(Long resourceId);
    
    /**
     * 获取帖子列表
     */
    PageResult<Map<String, Object>> getPostList(Map<String, Object> params);
    
    /**
     * 审核帖子
     */
    void reviewPost(Long postId, Integer status);
    
    /**
     * 置顶帖子
     */
    void setPostTop(Long postId, Boolean isTop);
    
    /**
     * 设置精华帖子
     */
    void setPostEssence(Long postId, Boolean isEssence);
    
    /**
     * 删除帖子
     */
    void deletePost(Long postId);
}
