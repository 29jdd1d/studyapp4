package com.kaoyan.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 学习报告 VO
 *
 * @author kaoyan
 */
@Data
@Schema(description = "学习报告")
public class StudyReportVO {
    
    @Schema(description = "用户昵称")
    private String nickname;
    
    @Schema(description = "报告周期")
    private String period;
    
    @Schema(description = "学习总时长（分钟）")
    private Integer totalStudyTime;
    
    @Schema(description = "完成任务数")
    private Integer completedTasks;
    
    @Schema(description = "总任务数")
    private Integer totalTasks;
    
    @Schema(description = "任务完成率")
    private Double taskCompletionRate;
    
    @Schema(description = "答题总数")
    private Integer totalQuestions;
    
    @Schema(description = "答题正确数")
    private Integer correctQuestions;
    
    @Schema(description = "答题正确率")
    private Double questionAccuracy;
    
    @Schema(description = "错题数")
    private Integer wrongQuestions;
    
    @Schema(description = "连续学习天数")
    private Integer continuousStudyDays;
    
    @Schema(description = "每日学习时长统计")
    private List<DailyStudyVO> dailyStudyList;
    
    @Schema(description = "科目学习时长占比")
    private List<SubjectStudyVO> subjectStudyList;
    
    @Schema(description = "薄弱知识点")
    private List<WeakPointVO> weakPoints;
    
    /**
     * 每日学习统计
     */
    @Data
    @Schema(description = "每日学习统计")
    public static class DailyStudyVO {
        @Schema(description = "日期")
        private String date;
        
        @Schema(description = "学习时长（分钟）")
        private Integer studyTime;
        
        @Schema(description = "完成任务数")
        private Integer completedTasks;
    }
    
    /**
     * 科目学习统计
     */
    @Data
    @Schema(description = "科目学习统计")
    public static class SubjectStudyVO {
        @Schema(description = "科目名称")
        private String subjectName;
        
        @Schema(description = "学习时长（分钟）")
        private Integer studyTime;
        
        @Schema(description = "占比")
        private Double percentage;
    }
    
    /**
     * 薄弱知识点
     */
    @Data
    @Schema(description = "薄弱知识点")
    public static class WeakPointVO {
        @Schema(description = "知识点名称")
        private String pointName;
        
        @Schema(description = "正确率")
        private Double accuracy;
        
        @Schema(description = "练习次数")
        private Integer practiceCount;
    }
}
