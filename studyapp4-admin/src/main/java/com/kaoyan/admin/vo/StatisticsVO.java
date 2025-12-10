package com.kaoyan.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计数据响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO {
    
    /**
     * 总用户数
     */
    private Long totalUsers;
    
    /**
     * 今日新增用户
     */
    private Long todayNewUsers;
    
    /**
     * 总资源数
     */
    private Long totalResources;
    
    /**
     * 今日新增资源
     */
    private Long todayNewResources;
    
    /**
     * 总帖子数
     */
    private Long totalPosts;
    
    /**
     * 今日新增帖子
     */
    private Long todayNewPosts;
    
    /**
     * 总学习计划数
     */
    private Long totalPlans;
    
    /**
     * 今日新增计划
     */
    private Long todayNewPlans;
    
    /**
     * 总答题记录数
     */
    private Long totalAnswers;
    
    /**
     * 今日答题次数
     */
    private Long todayAnswers;
}
