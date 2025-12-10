package com.kaoyan.exam.dto;

import lombok.Data;

/**
 * 题目查询请求DTO
 */
@Data
public class QuestionQueryRequest {
    
    private String type;
    
    private String category;
    
    private String chapter;
    
    private String knowledgePoint;
    
    private Integer year;
    
    private Integer difficulty;
    
    private String keyword;
    
    private Integer pageNum = 1;
    
    private Integer pageSize = 10;
}
