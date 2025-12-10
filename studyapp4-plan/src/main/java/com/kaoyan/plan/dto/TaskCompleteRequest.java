package com.kaoyan.plan.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 任务完成请求DTO
 */
@Data
public class TaskCompleteRequest {
    
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    private Integer studyDuration;
}
