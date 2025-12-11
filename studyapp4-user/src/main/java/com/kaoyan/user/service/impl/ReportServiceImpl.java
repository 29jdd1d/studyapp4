package com.kaoyan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.user.entity.User;
import com.kaoyan.user.mapper.UserMapper;
import com.kaoyan.user.service.ReportService;
import com.kaoyan.user.vo.StudyReportVO;
import com.kaoyan.user.vo.StudyReportVO.DailyStudyVO;
import com.kaoyan.user.vo.StudyReportVO.SubjectStudyVO;
import com.kaoyan.user.vo.StudyReportVO.WeakPointVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 学习报告服务实现
 *
 * @author kaoyan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    
    private final UserMapper userMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public StudyReportVO generateReport(Long userId, String startDate, String endDate) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        
        // 构建报告
        StudyReportVO report = new StudyReportVO();
        report.setNickname(user.getNickname());
        report.setPeriod(startDate + " 至 " + endDate);
        
        // TODO: 这里应该从实际的学习记录表中统计数据
        // 当前使用模拟数据作为示例
        
        // 学习时长统计
        Random random = new Random();
        int totalMinutes = random.nextInt(300) + 200; // 200-500分钟
        report.setTotalStudyTime(totalMinutes);
        
        // 任务完成情况
        int totalTasks = (int) days * 5; // 每天5个任务
        int completedTasks = random.nextInt(totalTasks / 2) + totalTasks / 2;
        report.setTotalTasks(totalTasks);
        report.setCompletedTasks(completedTasks);
        report.setTaskCompletionRate((double) completedTasks / totalTasks * 100);
        
        // 答题情况
        int totalQuestions = random.nextInt(100) + 50;
        int correctQuestions = random.nextInt(totalQuestions / 2) + totalQuestions / 2;
        report.setTotalQuestions(totalQuestions);
        report.setCorrectQuestions(correctQuestions);
        report.setQuestionAccuracy((double) correctQuestions / totalQuestions * 100);
        report.setWrongQuestions(totalQuestions - correctQuestions);
        
        // 连续学习天数
        report.setContinuousStudyDays(random.nextInt((int) days) + 1);
        
        // 每日学习时长
        List<DailyStudyVO> dailyList = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            DailyStudyVO daily = new DailyStudyVO();
            daily.setDate(current.format(DATE_FORMATTER));
            daily.setStudyTime(random.nextInt(60) + 30);
            daily.setCompletedTasks(random.nextInt(6) + 2);
            dailyList.add(daily);
            current = current.plusDays(1);
        }
        report.setDailyStudyList(dailyList);
        
        // 科目学习占比
        List<SubjectStudyVO> subjectList = new ArrayList<>();
        String[] subjects = {"政治", "英语", "数学", "专业课"};
        double[] percentages = {20.0, 25.0, 30.0, 25.0};
        for (int i = 0; i < subjects.length; i++) {
            SubjectStudyVO subject = new SubjectStudyVO();
            subject.setSubjectName(subjects[i]);
            subject.setStudyTime((int) (totalMinutes * percentages[i] / 100));
            subject.setPercentage(percentages[i]);
            subjectList.add(subject);
        }
        report.setSubjectStudyList(subjectList);
        
        // 薄弱知识点
        List<WeakPointVO> weakPoints = new ArrayList<>();
        String[] points = {"马克思主义基本原理", "英语阅读理解", "高等数学-极限", "数据结构-树"};
        for (String point : points) {
            WeakPointVO weak = new WeakPointVO();
            weak.setPointName(point);
            weak.setAccuracy(random.nextDouble() * 30 + 40); // 40-70%
            weak.setPracticeCount(random.nextInt(20) + 5);
            weakPoints.add(weak);
        }
        report.setWeakPoints(weakPoints);
        
        return report;
    }
    
    @Override
    public byte[] exportReportToPdf(Long userId, String startDate, String endDate) {
        StudyReportVO report = generateReport(userId, startDate, endDate);
        
        // TODO: 使用 iText 或 Apache PDFBox 生成PDF
        // 当前返回模拟数据
        
        String pdfContent = String.format(
                "学习报告\n\n" +
                "学员：%s\n" +
                "报告周期：%s\n\n" +
                "学习概况：\n" +
                "- 学习总时长：%d 分钟\n" +
                "- 完成任务：%d/%d (%.1f%%)\n" +
                "- 答题情况：%d/%d (%.1f%%)\n" +
                "- 连续学习：%d 天\n\n" +
                "科目学习占比：\n",
                report.getNickname(),
                report.getPeriod(),
                report.getTotalStudyTime(),
                report.getCompletedTasks(), report.getTotalTasks(), report.getTaskCompletionRate(),
                report.getCorrectQuestions(), report.getTotalQuestions(), report.getQuestionAccuracy(),
                report.getContinuousStudyDays()
        );
        
        for (SubjectStudyVO subject : report.getSubjectStudyList()) {
            pdfContent += String.format("- %s: %d 分钟 (%.1f%%)\n", 
                    subject.getSubjectName(), subject.getStudyTime(), subject.getPercentage());
        }
        
        pdfContent += "\n薄弱知识点：\n";
        for (WeakPointVO weak : report.getWeakPoints()) {
            pdfContent += String.format("- %s: 正确率 %.1f%% (%d 次练习)\n", 
                    weak.getPointName(), weak.getAccuracy(), weak.getPracticeCount());
        }
        
        return pdfContent.getBytes();
    }
}
