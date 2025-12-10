package com.kaoyan.plan.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 学习计划创建请求DTO
 */
@Data
public class PlanCreateRequest {
    
    @NotBlank(message = "目标院校不能为空")
    private String targetUniversity;
    
    @NotBlank(message = "目标专业不能为空")
    private String targetMajor;
    
    @NotNull(message = "考试日期不能为空")
    private LocalDate examDate;
    
    @NotNull(message = "当前基础不能为空")
    private Integer currentLevel;
}
