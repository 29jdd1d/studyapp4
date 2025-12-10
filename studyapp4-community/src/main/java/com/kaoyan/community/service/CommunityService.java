package com.kaoyan.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.community.dto.AddCommentRequest;
import com.kaoyan.community.dto.PostQueryRequest;
import com.kaoyan.community.dto.PublishPostRequest;
import com.kaoyan.community.vo.CommentVO;
import com.kaoyan.community.vo.PostVO;

import java.util.List;

/**
 * 社区服务接口
 */
public interface CommunityService {
    
    /**
     * 发布帖子
     */
    Long publishPost(Long userId, PublishPostRequest request);
    
    /**
     * 删除帖子
     */
    void deletePost(Long userId, Long postId);
    
    /**
     * 获取帖子列表
     */
    Page<PostVO> getPostList(Long userId, PostQueryRequest request);
    
    /**
     * 获取帖子详情
     */
    PostVO getPostDetail(Long userId, Long postId);
    
    /**
     * 点赞帖子
     */
    void likePost(Long userId, Long postId);
    
    /**
     * 取消点赞帖子
     */
    void unlikePost(Long userId, Long postId);
    
    /**
     * 收藏帖子
     */
    void collectPost(Long userId, Long postId);
    
    /**
     * 取消收藏帖子
     */
    void uncollectPost(Long userId, Long postId);
    
    /**
     * 添加评论
     */
    Long addComment(Long userId, AddCommentRequest request);
    
    /**
     * 删除评论
     */
    void deleteComment(Long userId, Long commentId);
    
    /**
     * 获取评论列表
     */
    List<CommentVO> getCommentList(Long userId, Long postId);
    
    /**
     * 点赞评论
     */
    void likeComment(Long userId, Long commentId);
    
    /**
     * 取消点赞评论
     */
    void unlikeComment(Long userId, Long commentId);
    
    /**
     * 获取我的帖子
     */
    Page<PostVO> getMyPosts(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取我的收藏
     */
    Page<PostVO> getMyCollects(Long userId, Integer pageNum, Integer pageSize);
}
