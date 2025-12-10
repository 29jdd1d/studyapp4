package com.kaoyan.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.community.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 帖子 Mapper
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
    /**
     * 增加浏览数
     */
    @Update("UPDATE t_post SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);
    
    /**
     * 增加点赞数
     */
    @Update("UPDATE t_post SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(Long id);
    
    /**
     * 减少点赞数
     */
    @Update("UPDATE t_post SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0")
    void decrementLikeCount(Long id);
    
    /**
     * 增加评论数
     */
    @Update("UPDATE t_post SET comment_count = comment_count + 1 WHERE id = #{id}")
    void incrementCommentCount(Long id);
    
    /**
     * 增加收藏数
     */
    @Update("UPDATE t_post SET collect_count = collect_count + 1 WHERE id = #{id}")
    void incrementCollectCount(Long id);
    
    /**
     * 减少收藏数
     */
    @Update("UPDATE t_post SET collect_count = collect_count - 1 WHERE id = #{id} AND collect_count > 0")
    void decrementCollectCount(Long id);
}
