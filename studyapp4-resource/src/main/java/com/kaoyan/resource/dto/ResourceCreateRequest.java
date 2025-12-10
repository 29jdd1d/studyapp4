package com.kaoyan.resource.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 资源创建请求DTO
 */
@Data
public class ResourceCreateRequest {
    
    @NotBlank(message = "资源标题不能为空")
    private String title;
    
    @NotBlank(message = "资源类型不能为空")
    private String type;
    
    @NotBlank(message = "分类不能为空")
    private String category;
    
    private String subCategory;
    
    private String fileUrl;
    
    private String coverUrl;
    
    private Integer duration;
    
    private Long fileSize;
    
    private String description;
    
    private String tags;
    
    private Integer difficulty;
    
    private Integer isFree;
}
