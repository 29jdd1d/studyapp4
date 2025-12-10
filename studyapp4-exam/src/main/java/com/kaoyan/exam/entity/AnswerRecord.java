package com.kaoyan.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 答题记录实体
 */
@Data
@TableName("t_answer_record")
public class AnswerRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long questionId;
    
    private String userAnswer;
    
    private Integer isCorrect;
    
    private Integer timeSpent;
    
    private LocalDateTime submitTime;
    
    private LocalDateTime createTime;
}
