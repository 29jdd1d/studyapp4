package com.kaoyan.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.resource.entity.Resource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源 Mapper
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
}
