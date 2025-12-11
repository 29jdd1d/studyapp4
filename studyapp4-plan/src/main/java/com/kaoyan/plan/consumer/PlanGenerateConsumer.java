package com.kaoyan.plan.consumer;

import com.kaoyan.plan.dto.PlanGenerateMessage;
import com.kaoyan.plan.entity.StudyPlan;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.mapper.StudyPlanMapper;
import com.kaoyan.plan.mapper.StudyTaskMapper;
import com.kaoyan.plan.service.PlanAlgorithmService;
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
    private final PlanAlgorithmService planAlgorithmService;
    
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
            
            // 2. 使用优化的算法生成计划详细内容
            String content = generatePlanContent(message);
            
            // 3. 使用智能算法生成初始任务列表
            List<StudyTask> tasks = planAlgorithmService.generateSmartTasks(message, 0);
            
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
     * 生成计划详细内容（使用优化的算法）
     */
    private String generatePlanContent(PlanGenerateMessage message) {
        StringBuilder content = new StringBuilder();
        
        content.append("## 考研学习计划\n\n");
        content.append("**目标院校：**").append(message.getTargetSchool()).append("\n");
        content.append("**目标专业：**").append(message.getTargetMajor()).append("\n");
        content.append("**考试日期：**").append(message.getExamDate()).append("\n");
        content.append("**当前基础：**").append(getLevelDescription(message.getCurrentLevel())).append("\n");
        
        // 计算难度系数
        double difficultyFactor = planAlgorithmService.calculateDifficultyFactor(message.getTargetSchool());
        content.append("**院校难度系数：**").append(String.format("%.1f", difficultyFactor)).append("\n\n");
        
        // 计算学习天数
        LocalDate now = LocalDate.now();
        LocalDate examDate = LocalDate.parse(message.getExamDate(), DateTimeFormatter.ISO_DATE);
        long daysToExam = ChronoUnit.DAYS.between(now, examDate);
        
        content.append("### 学习阶段划分\n\n");
        List<String> phases = planAlgorithmService.generateStudyPhases(daysToExam);
        for (int i = 0; i < phases.size(); i++) {
            content.append((i + 1)).append(". ").append(phases.get(i)).append("\n");
        }
        
        content.append("\n### 科目时间分配\n\n");
        Map<String, Integer> subjectWeights = planAlgorithmService.calculateSubjectWeights(
                message.getTargetMajor(), message.getCurrentLevel());
        subjectWeights.forEach((subject, weight) -> 
            content.append("- **").append(subject).append("：** ").append(weight).append("%\n")
        );
        
        content.append("\n### 每日学习建议\n\n");
        content.append("- 早上（8:00-12:00）：数学或专业课（需要深度思考的科目）\n");
        content.append("- 下午（14:00-18:00）：专业课或英语阅读\n");
        content.append("- 晚上（19:00-22:00）：政治、英语单词、错题回顾\n");
        content.append("\n**建议每周休息半天，保持学习节奏，避免过度疲劳！**\n");
        
        return content.toString();
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
}
