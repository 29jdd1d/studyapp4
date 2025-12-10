package com.kaoyan.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.exam.entity.AnswerRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 答题记录 Mapper
 */
@Mapper
public interface AnswerRecordMapper extends BaseMapper<AnswerRecord> {
}
