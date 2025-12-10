package com.kaoyan.plan.consumer;

import com.kaoyan.plan.dto.PlanGenerateMessage;
import com.kaoyan.plan.entity.StudyPlan;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.mapper.StudyPlanMapper;
import com.kaoyan.plan.mapper.StudyTaskMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习计划生成消费者
 * 监听 RabbitMQ 队列，异步处理学习计划的详细内容生成
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlanGenerateConsumer {
    
    private final StudyPlanMapper studyPlanMapper;
    private final StudyTaskMapper studyTaskMapper;
    
    /**
     * 监听学习计划生成队列
     */
    @RabbitListener(queues = "studyapp4.plan.generate")
    public void handlePlanGenerate(
            @Payload PlanGenerateMessage message,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            Channel channel) {
        
        log.info("收到学习计划生成消息: userId={}, planId={}, planName={}", 
                message.getUserId(), message.getPlanId(), message.getPlanName());
        
        try {
            // 1. 查询计划信息
            StudyPlan plan = studyPlanMapper.selectById(message.getPlanId());
            if (plan == null) {
                log.error("计划不存在: planId={}", message.getPlanId());
                channel.basicAck(deliveryTag, false);
                return;
            }
            
            // 2. 生成计划详细内容
            String content = generatePlanContent(message);
            
            // 3. 生成初始任务列表
            List<StudyTask> tasks = generateInitialTasks(message);
            
            // 4. 更新计划内容
            plan.setPlanContent(content);
            plan.setUpdateTime(LocalDateTime.now());
            studyPlanMapper.updateById(plan);
            
            // 5. 批量插入任务
            if (!tasks.isEmpty()) {
                tasks.forEach(studyTaskMapper::insert);
            }
            
            log.info("学习计划生成完成: planId={}, 生成任务数={}", message.getPlanId(), tasks.size());
            
            // 6. 手动确认消息
            channel.basicAck(deliveryTag, false);
            
        } catch (Exception e) {
            log.error("学习计划生成失败: planId={}", message.getPlanId(), e);
            try {
                // 拒绝消息，不重新入队（避免死循环）
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                log.error("消息拒绝失败", ex);
            }
        }
    }
    
    /**
     * 生成计划详细内容（基于目标院校、专业、当前水平）
     */
    private String generatePlanContent(PlanGenerateMessage message) {
        StringBuilder content = new StringBuilder();
        
        content.append("## 考研学习计划\n\n");
        content.append("**目标院校：**").append(message.getTargetSchool()).append("\n");
        content.append("**目标专业：**").append(message.getTargetMajor()).append("\n");
        content.append("**考试日期：**").append(message.getExamDate()).append("\n");
        content.append("**当前基础：**").append(getLevelDescription(message.getCurrentLevel())).append("\n\n");
        
        // 计算学习天数
        LocalDate now = LocalDate.now();
        LocalDate examDate = LocalDate.parse(message.getExamDate(), DateTimeFormatter.ISO_DATE);
        long daysToExam = ChronoUnit.DAYS.between(now, examDate);
        
        content.append("### 学习阶段划分\n\n");
        
        if (daysToExam > 365) {
            // 长期计划（超过1年）
            content.append("**第一阶段（基础巩固）：** 全面复习各科基础知识\n");
            content.append("**第二阶段（强化提升）：** 重点突破难点和薄弱环节\n");
            content.append("**第三阶段（真题冲刺）：** 大量刷题，模拟考试\n");
            content.append("**第四阶段（查漏补缺）：** 回顾错题，调整心态\n");
        } else if (daysToExam > 180) {
            // 中期计划（6个月-1年）
            content.append("**第一阶段（基础强化）：** 快速梳理知识点\n");
            content.append("**第二阶段（真题训练）：** 历年真题练习\n");
            content.append("**第三阶段（冲刺提分）：** 模拟考试，查漏补缺\n");
        } else {
            // 短期冲刺（少于6个月）
            content.append("**第一阶段（核心知识）：** 聚焦高频考点\n");
            content.append("**第二阶段（真题冲刺）：** 近5年真题\n");
        }
        
        content.append("\n### 科目时间分配\n\n");
        Map<String, Integer> subjectWeights = getSubjectWeights(message.getCurrentLevel());
        subjectWeights.forEach((subject, weight) -> 
            content.append("- **").append(subject).append("：** ").append(weight).append("%\n")
        );
        
        content.append("\n### 每日学习建议\n\n");
        content.append("- 早上（8:00-12:00）：数学或专业课（需要思考的科目）\n");
        content.append("- 下午（14:00-18:00）：专业课或英语阅读\n");
        content.append("- 晚上（19:00-22:00）：政治、英语单词、错题回顾\n");
        content.append("\n**建议每周休息半天，保持学习节奏！**\n");
        
        return content.toString();
    }
    
    /**
     * 生成初始任务列表（第一周的任务）
     */
    private List<StudyTask> generateInitialTasks(PlanGenerateMessage message) {
        List<StudyTask> tasks = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        
        // 生成第一周的任务（7天）
        for (int day = 0; day < 7; day++) {
            LocalDate taskDate = startDate.plusDays(day);
            
            // 每天3-4个任务（上午、下午、晚上）
            tasks.add(createTask(message.getUserId(), message.getPlanId(), taskDate, "上午", 
                    day % 2 == 0 ? "数学" : "专业课一", 
                    day % 2 == 0 ? "高等数学第" + (day + 1) + "章" : "专业课核心知识点梳理"));
            
            tasks.add(createTask(message.getUserId(), message.getPlanId(), taskDate, "下午",Id(), taskDate, "下午", 
                    day % 3 == 0 ? "英语" : "专业课二", 
                    day % 3 == 0 ? "英语阅读训练（2篇）" : "专业课习题练习"));
            
            tasks.add(createTask(message.getUserId(), message.getPlanId(), taskDate, "晚上",Id(), taskDate, "晚上", 
                    "政治", "政治选择题练习（50题）"));
            
            // 周日休息或复盘
            if (day == 6) {
                tasks.add(createTask(message.getUserId(), message.getPlanId(), taskDate, "全天", 
                        "复盘", "本周学习总结 + 错题整理"));
            }
        }
        
        return tasks;
    }
    
    /**
     * 创建单个任务
     */
    private StudyTask createTask(Long userId, Long planId, LocalDate taskDate, String period, 
                                 String subject, String content) {
        StudyTask task = new StudyTask();
        task.setUserId(userId);
        task.setPlanId(planId);
        task.setTaskDate(taskDate);
        task.setCategory(subject);
        task.setTaskContent(period + "：" + content);
        task.setStatus(0); // 未完成
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        return task;
    }
    
    /**
     * 获取基础水平描述
     */
    private String getLevelDescription(Integer level) {
        return switch (level) {
            case 1 -> "基础薄弱";
            case 2 -> "基础一般";
            case 3 -> "基础良好";
            case 4 -> "基础优秀";
            case 5 -> "基础扎实";
            default -> "未知";
        };
    }
    
    /**
     * 根据基础水平获取科目时间权重
     */
    private Map<String, Integer> getSubjectWeights(Integer currentLevel) {
        Map<String, Integer> weights = new HashMap<>();
        
        if (currentLevel <= 2) {
            // 基础薄弱：加强数学和专业课
            weights.put("数学", 35);
            weights.put("专业课", 35);
            weights.put("英语", 20);
            weights.put("政治", 10);
        } else if (currentLevel <= 3) {
            // 基础一般：均衡分配
            weights.put("数学", 30);
            weights.put("专业课", 30);
            weights.put("英语", 25);
            weights.put("政治", 15);
        } else {
            // 基础优秀：侧重专业课和拔高
            weights.put("专业课", 40);
            weights.put("数学", 25);
            weights.put("英语", 20);
            weights.put("政治", 15);
        }
        
        return weights;
    }
}
