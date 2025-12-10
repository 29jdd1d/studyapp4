package com.kaoyan.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.Result;
import com.kaoyan.community.dto.AddCommentRequest;
import com.kaoyan.community.dto.PostQueryRequest;
import com.kaoyan.community.dto.PublishPostRequest;
import com.kaoyan.community.service.CommunityService;
import com.kaoyan.community.vo.CommentVO;
import com.kaoyan.community.vo.PostVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社区控制器
 */
@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    
    private final CommunityService communityService;
    
    /**
     * 发布帖子
     */
    @PostMapping("/post")
    public Result<Long> publishPost(@RequestHeader("X-User-Id") Long userId,
                                    @Valid @RequestBody PublishPostRequest request) {
        Long postId = communityService.publishPost(userId, request);
        return Result.success(postId);
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/post/{postId}")
    public Result<Void> deletePost(@RequestHeader("X-User-Id") Long userId,
                                   @PathVariable Long postId) {
        communityService.deletePost(userId, postId);
        return Result.success();
    }
    
    /**
     * 获取帖子列表
     */
    @GetMapping("/post/list")
    public Result<PageResult<PostVO>> getPostList(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                                                   PostQueryRequest request) {
        Page<PostVO> page = communityService.getPostList(userId, request);
        return Result.success(PageResult.of(page));
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/post/{postId}")
    public Result<PostVO> getPostDetail(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                                        @PathVariable Long postId) {
        PostVO vo = communityService.getPostDetail(userId, postId);
        return Result.success(vo);
    }
    
    /**
     * 点赞帖子
     */
    @PostMapping("/post/{postId}/like")
    public Result<Void> likePost(@RequestHeader("X-User-Id") Long userId,
                                 @PathVariable Long postId) {
        communityService.likePost(userId, postId);
        return Result.success();
    }
    
    /**
     * 取消点赞帖子
     */
    @DeleteMapping("/post/{postId}/like")
    public Result<Void> unlikePost(@RequestHeader("X-User-Id") Long userId,
                                   @PathVariable Long postId) {
        communityService.unlikePost(userId, postId);
        return Result.success();
    }
    
    /**
     * 收藏帖子
     */
    @PostMapping("/post/{postId}/collect")
    public Result<Void> collectPost(@RequestHeader("X-User-Id") Long userId,
                                    @PathVariable Long postId) {
        communityService.collectPost(userId, postId);
        return Result.success();
    }
    
    /**
     * 取消收藏帖子
     */
    @DeleteMapping("/post/{postId}/collect")
    public Result<Void> uncollectPost(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long postId) {
        communityService.uncollectPost(userId, postId);
        return Result.success();
    }
    
    /**
     * 添加评论
     */
    @PostMapping("/comment")
    public Result<Long> addComment(@RequestHeader("X-User-Id") Long userId,
                                   @Valid @RequestBody AddCommentRequest request) {
        Long commentId = communityService.addComment(userId, request);
        return Result.success(commentId);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/comment/{commentId}")
    public Result<Void> deleteComment(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long commentId) {
        communityService.deleteComment(userId, commentId);
        return Result.success();
    }
    
    /**
     * 获取评论列表
     */
    @GetMapping("/comment/list")
    public Result<List<CommentVO>> getCommentList(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                                                   @RequestParam Long postId) {
        List<CommentVO> list = communityService.getCommentList(userId, postId);
        return Result.success(list);
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/comment/{commentId}/like")
    public Result<Void> likeComment(@RequestHeader("X-User-Id") Long userId,
                                    @PathVariable Long commentId) {
        communityService.likeComment(userId, commentId);
        return Result.success();
    }
    
    /**
     * 取消点赞评论
     */
    @DeleteMapping("/comment/{commentId}/like")
    public Result<Void> unlikeComment(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long commentId) {
        communityService.unlikeComment(userId, commentId);
        return Result.success();
    }
    
    /**
     * 获取我的帖子
     */
    @GetMapping("/my/posts")
    public Result<PageResult<PostVO>> getMyPosts(@RequestHeader("X-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PostVO> page = communityService.getMyPosts(userId, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }
    
    /**
     * 获取我的收藏
     */
    @GetMapping("/my/collects")
    public Result<PageResult<PostVO>> getMyCollects(@RequestHeader("X-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PostVO> page = communityService.getMyCollects(userId, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }
}
