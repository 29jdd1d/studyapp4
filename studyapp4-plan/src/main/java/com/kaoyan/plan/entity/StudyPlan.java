package com.kaoyan.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习计划实体
 */
@Data
@TableName("t_study_plan")
public class StudyPlan implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String targetUniversity;
    
    private String targetMajor;
    
    private LocalDate examDate;
    
    private Integer currentLevel;
    
    private Integer totalDays;
    
    private String planContent;
    
    private Integer status;
    
    private BigDecimal completionRate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
