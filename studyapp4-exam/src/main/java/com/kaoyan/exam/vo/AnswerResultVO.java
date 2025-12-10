package com.kaoyan.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 答题结果VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResultVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 是否正确
     */
    private Boolean isCorrect;
    
    /**
     * 正确答案
     */
    private String correctAnswer;
    
    /**
     * 解析
     */
    private String analysis;
    
    /**
     * 是否加入错题本
     */
    private Boolean addedToWrongBook;
}
