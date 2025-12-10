package com.kaoyan.resource.dto;

import lombok.Data;

/**
 * 资源查询请求DTO
 */
@Data
public class ResourceQueryRequest {
    
    private String type;
    
    private String category;
    
    private String subCategory;
    
    private Integer difficulty;
    
    private Integer isFree;
    
    private Integer status;
    
    private String keyword;
    
    private Integer pageNum = 1;
    
    private Integer pageSize = 10;
}
