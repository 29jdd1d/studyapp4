package com.kaoyan.plan.service;

import com.kaoyan.plan.dto.PlanCreateRequest;
import com.kaoyan.plan.dto.TaskCompleteRequest;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.vo.StudyPlanVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习计划服务接口
 */
public interface PlanService {
    
    /**
     * 创建学习计划
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return 计划ID
     */
    Long createPlan(Long userId, PlanCreateRequest request);
    
    /**
     * 获取用户的学习计划
     *
     * @param userId 用户ID
     * @return 学习计划
     */
    StudyPlanVO getUserPlan(Long userId);
    
    /**
     * 获取指定日期的任务列表
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 任务列表
     */
    List<StudyTask> getTasksByDate(Long userId, LocalDate date);
    
    /**
     * 完成任务
     *
     * @param userId  用户ID
     * @param request 完成请求
     * @return 是否成功
     */
    Boolean completeTask(Long userId, TaskCompleteRequest request);
    
    /**
     * 获取今日任务
     *
     * @param userId 用户ID
     * @return 今日任务列表
     */
    List<StudyTask> getTodayTasks(Long userId);
}
