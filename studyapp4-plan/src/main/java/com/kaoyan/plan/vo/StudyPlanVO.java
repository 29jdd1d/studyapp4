package com.kaoyan.plan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 学习计划VO
 */
@Data
public class StudyPlanVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String targetUniversity;
    
    private String targetMajor;
    
    private LocalDate examDate;
    
    private Integer totalDays;
    
    private Integer remainingDays;
    
    private BigDecimal completionRate;
    
    private Integer status;
    
    private String planContent;
}
