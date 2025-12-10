package com.kaoyan.plan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaoyan.common.constant.RabbitMQConstants;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.plan.dto.PlanCreateRequest;
import com.kaoyan.plan.dto.PlanGenerateMessage;
import com.kaoyan.plan.dto.TaskCompleteRequest;
import com.kaoyan.plan.entity.StudyPlan;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.mapper.StudyPlanMapper;
import com.kaoyan.plan.mapper.StudyTaskMapper;
import com.kaoyan.plan.service.PlanService;
import com.kaoyan.plan.vo.StudyPlanVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 学习计划服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    
    private final StudyPlanMapper studyPlanMapper;
    private final StudyTaskMapper studyTaskMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlan(Long userId, PlanCreateRequest request) {
        // 检查是否已有进行中的计划
        LambdaQueryWrapper<StudyPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyPlan::getUserId, userId);
        wrapper.eq(StudyPlan::getStatus, 1);
        
        StudyPlan existingPlan = studyPlanMapper.selectOne(wrapper);
        if (existingPlan != null) {
            throw new BusinessException("您已有进行中的学习计划，请先完成或放弃当前计划");
        }
        
        // 计算总天数
        long totalDays = ChronoUnit.DAYS.between(LocalDate.now(), request.getExamDate());
        if (totalDays <= 0) {
            throw new BusinessException("考试日期必须在今天之后");
        }
        
        // 创建学习计划
        StudyPlan plan = new StudyPlan();
        plan.setUserId(userId);
        plan.setTargetUniversity(request.getTargetUniversity());
        plan.setTargetMajor(request.getTargetMajor());
        plan.setExamDate(request.getExamDate());
        plan.setCurrentLevel(request.getCurrentLevel());
        plan.setTotalDays((int) totalDays);
        plan.setStatus(1);
        plan.setCompletionRate(BigDecimal.ZERO);
        
        // 生成计划内容（简化版，实际应该调用更复杂的算法）
        Map<String, Object> planContent = generatePlanContent(request, (int) totalDays);
        plan.setPlanContent(JSON.toJSONString(planContent));
        
        studyPlanMapper.insert(plan);
        
        // 发送消息到 RabbitMQ，异步生成详细任务
        PlanGenerateMessage message = new PlanGenerateMessage(
                userId,
                plan.getId(),
                "考研学习计划",
                request.getTargetUniversity(),
                request.getTargetMajor(),
                request.getExamDate().toString(),
                request.getCurrentLevel()
        );
        
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.PLAN_GENERATE_EXCHANGE,
                RabbitMQConstants.PLAN_GENERATE_ROUTING_KEY,
                message
        );
        
        log.info("学习计划创建成功: planId={}, userId={}", plan.getId(), userId);
        
        // 生成初始任务
        generateInitialTasks(plan.getId(), userId, (int) totalDays);
        
        return plan.getId();
    }
    
    @Override
    public StudyPlanVO getUserPlan(Long userId) {
        LambdaQueryWrapper<StudyPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyPlan::getUserId, userId);
        wrapper.eq(StudyPlan::getStatus, 1);
        wrapper.orderByDesc(StudyPlan::getCreateTime);
        wrapper.last("LIMIT 1");
        
        StudyPlan plan = studyPlanMapper.selectOne(wrapper);
        if (plan == null) {
            return null;
        }
        
        StudyPlanVO vo = BeanUtil.copyProperties(plan, StudyPlanVO.class);
        
        // 计算剩余天数
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), plan.getExamDate());
        vo.setRemainingDays((int) Math.max(0, remainingDays));
        
        return vo;
    }
    
    @Override
    public List<StudyTask> getTasksByDate(Long userId, LocalDate date) {
        LambdaQueryWrapper<StudyTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyTask::getUserId, userId);
        wrapper.eq(StudyTask::getTaskDate, date);
        wrapper.orderByAsc(StudyTask::getCategory);
        
        return studyTaskMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeTask(Long userId, TaskCompleteRequest request) {
        StudyTask task = studyTaskMapper.selectById(request.getTaskId());
        
        if (task == null || !task.getUserId().equals(userId)) {
            throw new BusinessException("任务不存在");
        }
        
        if (task.getStatus() == 2) {
            throw new BusinessException("任务已完成");
        }
        
        // 更新任务状态
        task.setStatus(2);
        task.setCompletionTime(LocalDateTime.now());
        task.setStudyDuration(request.getStudyDuration());
        studyTaskMapper.updateById(task);
        
        // 更新计划完成率
        updatePlanCompletionRate(task.getPlanId());
        
        return true;
    }
    
    @Override
    public List<StudyTask> getTodayTasks(Long userId) {
        return getTasksByDate(userId, LocalDate.now());
    }
    
    /**
     * 生成计划内容
     */
    private Map<String, Object> generatePlanContent(PlanCreateRequest request, int totalDays) {
        Map<String, Object> content = new HashMap<>();
        content.put("targetUniversity", request.getTargetUniversity());
        content.put("targetMajor", request.getTargetMajor());
        content.put("totalDays", totalDays);
        
        // 根据当前基础调整学习强度
        Map<String, Integer> subjectWeights = new HashMap<>();
        switch (request.getCurrentLevel()) {
            case 1, 2 -> { // 零基础、基础薄弱
                subjectWeights.put("politics", 20);
                subjectWeights.put("english", 30);
                subjectWeights.put("math", 30);
                subjectWeights.put("professional", 20);
            }
            case 3 -> { // 基础一般
                subjectWeights.put("politics", 20);
                subjectWeights.put("english", 25);
                subjectWeights.put("math", 25);
                subjectWeights.put("professional", 30);
            }
            case 4, 5 -> { // 基础较好、优秀
                subjectWeights.put("politics", 15);
                subjectWeights.put("english", 20);
                subjectWeights.put("math", 25);
                subjectWeights.put("professional", 40);
            }
            default -> {
                subjectWeights.put("politics", 20);
                subjectWeights.put("english", 25);
                subjectWeights.put("math", 25);
                subjectWeights.put("professional", 30);
            }
        }
        
        content.put("subjectWeights", subjectWeights);
        content.put("dailyStudyHours", totalDays > 180 ? 6 : 8);
        
        return content;
    }
    
    /**
     * 生成初始任务
     */
    private void generateInitialTasks(Long planId, Long userId, int totalDays) {
        LocalDate startDate = LocalDate.now();
        
        // 生成前 7 天的任务
        for (int i = 0; i < Math.min(7, totalDays); i++) {
            LocalDate taskDate = startDate.plusDays(i);
            
            // 政治任务
            createTask(planId, userId, taskDate, "politics", "政治基础复习");
            
            // 英语任务
            createTask(planId, userId, taskDate, "english", "英语单词背诵 + 阅读理解");
            
            // 数学任务
            createTask(planId, userId, taskDate, "math", "数学基础知识学习");
            
            // 专业课任务
            createTask(planId, userId, taskDate, "professional", "专业课教材阅读");
        }
    }
    
    /**
     * 创建任务
     */
    private void createTask(Long planId, Long userId, LocalDate taskDate, String category, String content) {
        StudyTask task = new StudyTask();
        task.setPlanId(planId);
        task.setUserId(userId);
        task.setTaskDate(taskDate);
        task.setCategory(category);
        
        Map<String, Object> taskContent = new HashMap<>();
        taskContent.put("title", content);
        taskContent.put("description", "完成" + content);
        task.setTaskContent(JSON.toJSONString(taskContent));
        
        task.setStatus(0);
        task.setStudyDuration(0);
        
        studyTaskMapper.insert(task);
    }
    
    /**
     * 更新计划完成率
     */
    private void updatePlanCompletionRate(Long planId) {
        // 查询该计划的所有任务
        LambdaQueryWrapper<StudyTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyTask::getPlanId, planId);
        
        List<StudyTask> allTasks = studyTaskMapper.selectList(wrapper);
        
        if (allTasks.isEmpty()) {
            return;
        }
        
        // 计算完成率
        long completedCount = allTasks.stream().filter(t -> t.getStatus() == 2).count();
        BigDecimal completionRate = BigDecimal.valueOf(completedCount)
                .divide(BigDecimal.valueOf(allTasks.size()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        
        // 更新计划
        StudyPlan plan = studyPlanMapper.selectById(planId);
        if (plan != null) {
            plan.setCompletionRate(completionRate);
            studyPlanMapper.updateById(plan);
        }
    }
}
