package com.kaoyan.plan.service;

import com.kaoyan.plan.dto.PlanGenerateMessage;
import com.kaoyan.plan.entity.StudyTask;

import java.util.List;
import java.util.Map;

/**
 * 学习计划算法服务
 *
 * @author kaoyan
 */
public interface PlanAlgorithmService {
    
    /**
     * 计算难度系数（基于目标院校）
     *
     * @param targetSchool 目标院校
     * @return 难度系数（1.0-2.0）
     */
    double calculateDifficultyFactor(String targetSchool);
    
    /**
     * 计算科目时间权重（基于专业和当前水平）
     *
     * @param major 专业
     * @param currentLevel 当前水平（1-5）
     * @return 科目权重分配（Map<科目名, 权重百分比>）
     */
    Map<String, Integer> calculateSubjectWeights(String major, Integer currentLevel);
    
    /**
     * 生成学习阶段划分（基于剩余天数）
     *
     * @param daysToExam 距离考试天数
     * @return 阶段划分描述列表
     */
    List<String> generateStudyPhases(long daysToExam);
    
    /**
     * 生成智能任务列表（基于用户历史学习数据）
     *
     * @param message 计划生成消息
     * @param weekOffset 周偏移（0表示第一周）
     * @return 任务列表
     */
    List<StudyTask> generateSmartTasks(PlanGenerateMessage message, int weekOffset);
    
    /**
     * 动态调整计划（基于完成度）
     *
     * @param planId 计划ID
     * @param completionRate 完成率
     * @return 调整建议
     */
    String adjustPlan(Long planId, double completionRate);
}
