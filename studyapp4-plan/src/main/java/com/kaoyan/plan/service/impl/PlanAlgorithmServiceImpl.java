package com.kaoyan.plan.service.impl;

import com.kaoyan.plan.dto.PlanGenerateMessage;
import com.kaoyan.plan.entity.StudyTask;
import com.kaoyan.plan.service.PlanAlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 学习计划算法服务实现
 *
 * @author kaoyan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanAlgorithmServiceImpl implements PlanAlgorithmService {
    
    // 985高校列表（部分）
    private static final Set<String> TOP_TIER_SCHOOLS = Set.of(
            "清华大学", "北京大学", "复旦大学", "上海交通大学", "浙江大学",
            "中国科学技术大学", "南京大学", "西安交通大学", "哈尔滨工业大学"
    );
    
    // 211高校列表（部分）
    private static final Set<String> SECOND_TIER_SCHOOLS = Set.of(
            "北京理工大学", "北京航空航天大学", "同济大学", "华东师范大学",
            "武汉大学", "华中科技大学", "中山大学", "四川大学"
    );
    
    // 需要数学的专业
    private static final Set<String> MATH_REQUIRED_MAJORS = Set.of(
            "计算机科学与技术", "软件工程", "数学", "物理学", "统计学",
            "金融学", "经济学", "电子信息工程", "自动化", "机械工程"
    );
    
    @Override
    public double calculateDifficultyFactor(String targetSchool) {
        if (TOP_TIER_SCHOOLS.contains(targetSchool)) {
            return 2.0; // 顶尖院校难度最高
        } else if (SECOND_TIER_SCHOOLS.contains(targetSchool)) {
            return 1.5; // 重点院校难度较高
        } else {
            return 1.0; // 普通院校标准难度
        }
    }
    
    @Override
    public Map<String, Integer> calculateSubjectWeights(String major, Integer currentLevel) {
        Map<String, Integer> weights = new HashMap<>();
        
        boolean needsMath = MATH_REQUIRED_MAJORS.stream()
                .anyMatch(m -> major.contains(m));
        
        if (needsMath) {
            // 需要数学的专业
            if (currentLevel <= 2) {
                // 基础薄弱：加强数学基础
                weights.put("数学", 40);
                weights.put("专业课", 30);
                weights.put("英语", 20);
                weights.put("政治", 10);
            } else if (currentLevel <= 3) {
                // 基础一般：均衡发展
                weights.put("数学", 35);
                weights.put("专业课", 30);
                weights.put("英语", 20);
                weights.put("政治", 15);
            } else {
                // 基础优秀：侧重专业课和拔高
                weights.put("专业课", 35);
                weights.put("数学", 30);
                weights.put("英语", 20);
                weights.put("政治", 15);
            }
        } else {
            // 不需要数学的专业（如文科类）
            if (currentLevel <= 2) {
                weights.put("专业课", 45);
                weights.put("英语", 30);
                weights.put("政治", 25);
            } else if (currentLevel <= 3) {
                weights.put("专业课", 40);
                weights.put("英语", 35);
                weights.put("政治", 25);
            } else {
                weights.put("专业课", 50);
                weights.put("英语", 30);
                weights.put("政治", 20);
            }
        }
        
        return weights;
    }
    
    @Override
    public List<String> generateStudyPhases(long daysToExam) {
        List<String> phases = new ArrayList<>();
        
        if (daysToExam > 365) {
            // 超过1年：4个阶段
            phases.add("第一阶段（基础巩固期）：全面系统复习各科基础知识，建立完整知识体系");
            phases.add("第二阶段（强化提升期）：重点突破难点和薄弱环节，深化理解");
            phases.add("第三阶段（真题冲刺期）：大量刷题，掌握考试规律和答题技巧");
            phases.add("第四阶段（查漏补缺期）：回顾错题，调整心态，模拟实战");
        } else if (daysToExam > 180) {
            // 6个月-1年：3个阶段
            phases.add("第一阶段（基础强化期）：快速梳理知识点，补齐薄弱环节");
            phases.add("第二阶段（真题训练期）：历年真题练习，掌握考点分布");
            phases.add("第三阶段（冲刺提分期）：模拟考试，查漏补缺，冲刺提分");
        } else if (daysToExam > 90) {
            // 3-6个月：2个阶段
            phases.add("第一阶段（核心知识期）：聚焦高频考点和核心知识");
            phases.add("第二阶段（真题冲刺期）：近5年真题反复练习，掌握答题节奏");
        } else {
            // 少于3个月：1个阶段冲刺
            phases.add("冲刺阶段：聚焦必考知识点，真题模拟，高效提分");
        }
        
        return phases;
    }
    
    @Override
    public List<StudyTask> generateSmartTasks(PlanGenerateMessage message, int weekOffset) {
        List<StudyTask> tasks = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusWeeks(weekOffset);
        
        // 获取科目权重
        Map<String, Integer> weights = calculateSubjectWeights(
                message.getTargetMajor(), message.getCurrentLevel());
        
        // 获取难度系数
        double difficultyFactor = calculateDifficultyFactor(message.getTargetSchool());
        
        // 生成一周任务（周一到周六学习，周日复盘）
        for (int day = 0; day < 7; day++) {
            LocalDate taskDate = startDate.plusDays(day);
            
            if (day == 6) {
                // 周日：复盘和休息
                tasks.add(createTask(message.getUserId(), message.getPlanId(), taskDate,
                        "复盘", "本周学习总结 + 错题整理 + 下周计划"));
            } else {
                // 周一到周六：根据权重分配任务
                List<String> dailySubjects = allocateDailySubjects(weights, day);
                
                for (int i = 0; i < dailySubjects.size(); i++) {
                    String subject = dailySubjects.get(i);
                    String period = i == 0 ? "上午" : (i == 1 ? "下午" : "晚上");
                    String content = generateTaskContent(subject, weekOffset, day, difficultyFactor);
                    
                    tasks.add(createTask(message.getUserId(), message.getPlanId(), 
                            taskDate, subject, period + "：" + content));
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * 分配每日学习科目
     */
    private List<String> allocateDailySubjects(Map<String, Integer> weights, int dayOfWeek) {
        List<String> subjects = new ArrayList<>();
        
        // 上午：数学或专业课（需要深度思考的科目）
        if (weights.containsKey("数学") && (dayOfWeek % 2 == 0)) {
            subjects.add("数学");
        } else {
            subjects.add("专业课");
        }
        
        // 下午：专业课或英语
        if (dayOfWeek % 3 == 0) {
            subjects.add("英语");
        } else {
            subjects.add("专业课");
        }
        
        // 晚上：政治、英语单词或错题回顾
        if (dayOfWeek % 2 == 0) {
            subjects.add("政治");
        } else {
            subjects.add("英语");
        }
        
        return subjects;
    }
    
    /**
     * 生成任务内容
     */
    private String generateTaskContent(String subject, int weekOffset, 
                                      int dayOfWeek, double difficultyFactor) {
        int chapterBase = weekOffset * 7 + dayOfWeek + 1;
        
        return switch (subject) {
            case "数学" -> {
                if (difficultyFactor >= 2.0) {
                    yield String.format("高等数学第%d章深度学习 + 20道拔高题", chapterBase);
                } else {
                    yield String.format("高等数学第%d章学习 + 基础习题练习", chapterBase);
                }
            }
            case "专业课" -> String.format("专业课核心知识点梳理（第%d部分） + 课后习题", chapterBase);
            case "英语" -> {
                if (dayOfWeek % 3 == 0) {
                    yield "英语阅读训练（2篇） + 长难句分析";
                } else {
                    yield "英语单词记忆（100个） + 作文素材积累";
                }
            }
            case "政治" -> String.format("政治选择题练习（50题） + 知识点背诵（第%d部分）", chapterBase);
            case "复盘" -> "本周学习总结 + 错题整理 + 薄弱环节分析";
            default -> "学习任务";
        };
    }
    
    /**
     * 创建任务实体
     */
    private StudyTask createTask(Long userId, Long planId, LocalDate taskDate,
                                 String subject, String content) {
        StudyTask task = new StudyTask();
        task.setUserId(userId);
        task.setPlanId(planId);
        task.setTaskDate(taskDate);
        task.setCategory(subject);
        task.setTaskContent(content);
        task.setStatus(0); // 未完成
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        return task;
    }
    
    @Override
    public String adjustPlan(Long planId, double completionRate) {
        if (completionRate >= 0.9) {
            return "完成度优秀！保持当前学习节奏，可适当增加难度提升自己。";
        } else if (completionRate >= 0.7) {
            return "完成度良好！继续保持，注意巩固薄弱环节。";
        } else if (completionRate >= 0.5) {
            return "完成度一般。建议分析未完成原因，适当调整学习计划的强度和节奏。";
        } else {
            return "完成度偏低。建议降低学习强度，聚焦核心知识点，避免过度疲劳。可咨询老师或学长获取帮助。";
        }
    }
}
