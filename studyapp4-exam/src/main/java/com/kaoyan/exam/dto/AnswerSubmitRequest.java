package com.kaoyan.exam.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 答题请求DTO
 */
@Data
public class AnswerSubmitRequest {
    
    @NotNull(message = "题目ID不能为空")
    private Long questionId;
    
    private String userAnswer;
    
    private Integer timeSpent;
}
