package com.kaoyan.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习任务实体
 */
@Data
@TableName("t_study_task")
public class StudyTask implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long planId;
    
    private Long userId;
    
    private LocalDate taskDate;
    
    private String category;
    
    private String taskContent;
    
    private String resourceIds;
    
    private Integer status;
    
    private LocalDateTime completionTime;
    
    private Integer studyDuration;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
