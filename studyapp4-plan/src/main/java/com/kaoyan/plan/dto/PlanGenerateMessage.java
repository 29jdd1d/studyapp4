package com.kaoyan.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学习计划生成消息（用于 RabbitMQ 队列传递）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanGenerateMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * 计划 ID
     */
    private Long planId;
    
    /**
     * 计划名称
     */
    private String planName;
    
    /**
     * 目标院校
     */
    private String targetSchool;
    
    /**
     * 目标专业
     */
    private String targetMajor;
    
    /**
     * 考试日期（格式：yyyy-MM-dd）
     */
    private String examDate;
    
    /**
     * 当前基础水平（1-5，1最弱，5最强）
     */
    private Integer currentLevel;
}
