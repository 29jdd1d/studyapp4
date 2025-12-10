package com.kaoyan.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 评论 Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 增加点赞数
     */
    @Update("UPDATE t_comment SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(Long id);
    
    /**
     * 减少点赞数
     */
    @Update("UPDATE t_comment SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0")
    void decrementLikeCount(Long id);
}
