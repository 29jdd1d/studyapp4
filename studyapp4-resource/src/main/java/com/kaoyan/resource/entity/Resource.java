package com.kaoyan.resource.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源实体
 */
@Data
@TableName("t_resource")
public class Resource implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String type;
    
    private String category;
    
    private String subCategory;
    
    private String fileUrl;
    
    private String coverUrl;
    
    private Integer duration;
    
    private Long fileSize;
    
    private String description;
    
    private String tags;
    
    private Integer difficulty;
    
    private Integer viewCount;
    
    private Integer downloadCount;
    
    private Integer isFree;
    
    private Integer status;
    
    private Long publisherId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
