package com.kaoyan.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.exam.entity.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目 Mapper
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
