package com.kaoyan.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 题目实体
 */
@Data
@TableName("t_question")
public class Question implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String type;
    
    private String category;
    
    private String chapter;
    
    private String knowledgePoint;
    
    private Integer year;
    
    private String content;
    
    private String options;
    
    private String answer;
    
    private String analysis;
    
    private Integer difficulty;
    
    private Integer score;
    
    private String tags;
    
    private Integer viewCount;
    
    private BigDecimal correctRate;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
