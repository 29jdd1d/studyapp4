package com.kaoyan.plan.controller;

import com.kaoyan.common.result.Result;
import com.kaoyan.plan.dto.PlanCreateRequest;
import com.kaoyan.plan.dto.TaskCompleteRequest;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.service.PlanService;
import com.kaoyan.plan.vo.StudyPlanVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习计划 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {
    
    private final PlanService planService;
    
    /**
     * 创建学习计划
     */
    @PostMapping
    public Result<Long> createPlan(
            @RequestHeader("X-User-Id") Long userId,
            @Validated @RequestBody PlanCreateRequest request) {
        log.info("创建学习计划: userId={}, request={}", userId, request);
        Long planId = planService.createPlan(userId, request);
        return Result.success(planId);
    }
    
    /**
     * 获取用户的学习计划
     */
    @GetMapping
    public Result<StudyPlanVO> getUserPlan(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取学习计划: userId={}", userId);
        StudyPlanVO plan = planService.getUserPlan(userId);
        return Result.success(plan);
    }
    
    /**
     * 获取今日任务
     */
    @GetMapping("/tasks/today")
    public Result<List<StudyTask>> getTodayTasks(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取今日任务: userId={}", userId);
        List<StudyTask> tasks = planService.getTodayTasks(userId);
        return Result.success(tasks);
    }
    
    /**
     * 获取指定日期的任务
     */
    @GetMapping("/tasks")
    public Result<List<StudyTask>> getTasksByDate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("获取任务: userId={}, date={}", userId, date);
        List<StudyTask> tasks = planService.getTasksByDate(userId, date);
        return Result.success(tasks);
    }
    
    /**
     * 完成任务
     */
    @PostMapping("/tasks/complete")
    public Result<Boolean> completeTask(
            @RequestHeader("X-User-Id") Long userId,
            @Validated @RequestBody TaskCompleteRequest request) {
        log.info("完成任务: userId={}, request={}", userId, request);
        Boolean result = planService.completeTask(userId, request);
        return Result.success(result);
    }
}
