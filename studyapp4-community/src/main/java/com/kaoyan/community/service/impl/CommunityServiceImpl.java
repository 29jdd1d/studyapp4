package com.kaoyan.community.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.community.dto.AddCommentRequest;
import com.kaoyan.community.dto.PostQueryRequest;
import com.kaoyan.community.dto.PublishPostRequest;
import com.kaoyan.community.entity.Collect;
import com.kaoyan.community.entity.Comment;
import com.kaoyan.community.entity.Like;
import com.kaoyan.community.entity.Post;
import com.kaoyan.community.feign.UserFeignClient;
import com.kaoyan.community.mapper.CollectMapper;
import com.kaoyan.community.mapper.CommentMapper;
import com.kaoyan.community.mapper.LikeMapper;
import com.kaoyan.community.mapper.PostMapper;
import com.kaoyan.community.service.CommunityService;
import com.kaoyan.community.vo.CommentVO;
import com.kaoyan.community.vo.PostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 社区服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final CollectMapper collectMapper;
    private final UserFeignClient userFeignClient;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String POST_VIEW_KEY = "community:post:view:";
    private static final String POST_CACHE_KEY = "community:post:";
    
    @Override
    @Transactional
    public Long publishPost(Long userId, PublishPostRequest request) {
        Post post = new Post();
        post.setUserId(userId);
        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImages(request.getImages());
        post.setTags(request.getTags());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        
        // 资讯需要审核，其他类型直接发布
        if ("news".equals(request.getType())) {
            post.setStatus(0); // 待审核
        } else {
            post.setStatus(1); // 已发布
        }
        
        post.setIsTop(false);
        post.setIsEssence(false);
        
        postMapper.insert(post);
        log.info("用户 {} 发布帖子，类型：{}, ID：{}", userId, request.getType(), post.getId());
        
        return post.getId();
    }
    
    @Override
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "帖子不存在");
        }
        
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权删除此帖子");
        }
        
        postMapper.deleteById(postId);
        
        // 删除缓存
        redisTemplate.delete(POST_CACHE_KEY + postId);
        
        log.info("用户 {} 删除帖子 {}", userId, postId);
    }
    
    @Override
    public Page<PostVO> getPostList(Long userId, PostQueryRequest request) {
        Page<Post> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, 1); // 只显示已发布
        
        if (StrUtil.isNotBlank(request.getType())) {
            wrapper.eq(Post::getType, request.getType());
        }
        
        if (StrUtil.isNotBlank(request.getTag())) {
            wrapper.like(Post::getTags, request.getTag());
        }
        
        if (StrUtil.isNotBlank(request.getKeyword())) {
            wrapper.and(w -> w.like(Post::getTitle, request.getKeyword())
                    .or().like(Post::getContent, request.getKeyword()));
        }
        
        if (request.getOnlyEssence() != null && request.getOnlyEssence()) {
            wrapper.eq(Post::getIsEssence, true);
        }
        
        // 置顶优先，然后按创建时间倒序
        wrapper.orderByDesc(Post::getIsTop, Post::getCreateTime);
        
        Page<Post> postPage = postMapper.selectPage(page, wrapper);
        
        // 转换为 VO
        Page<PostVO> voPage = new Page<>();
        BeanUtils.copyProperties(postPage, voPage, "records");
        
        List<PostVO> voList = convertToVOList(postPage.getRecords(), userId);
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public PostVO getPostDetail(Long userId, Long postId) {
        // 先从缓存获取
        String cacheKey = POST_CACHE_KEY + postId;
        PostVO cached = (PostVO) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            // 增加浏览数
            incrementViewCount(userId, postId);
            // 填充用户相关信息
            fillUserRelatedInfo(cached, userId);
            return cached;
        }
        
        Post post = postMapper.selectById(postId);
        if (post == null || post.getStatus() != 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "帖子不存在");
        }
        
        PostVO vo = convertToVO(post, userId);
        
        // 缓存 30 分钟
        redisTemplate.opsForValue().set(cacheKey, vo, 30, TimeUnit.MINUTES);
        
        // 增加浏览数
        incrementViewCount(userId, postId);
        
        return vo;
    }
    
    /**
     * 增加浏览数（每个用户每天只计数一次）
     */
    private void incrementViewCount(Long userId, Long postId) {
        String viewKey = POST_VIEW_KEY + postId + ":" + userId;
        Boolean hasViewed = redisTemplate.hasKey(viewKey);
        
        if (Boolean.FALSE.equals(hasViewed)) {
            postMapper.incrementViewCount(postId);
            // 记录已浏览，24 小时过期
            redisTemplate.opsForValue().set(viewKey, "1", 24, TimeUnit.HOURS);
        }
    }
    
    @Override
    @Transactional
    public void likePost(Long userId, Long postId) {
        // 检查是否已点赞
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetId, postId)
                .eq(Like::getType, "post");
        
        if (likeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "已经点赞过了");
        }
        
        // 添加点赞记录
        Like like = new Like();
        like.setUserId(userId);
        like.setTargetId(postId);
        like.setType("post");
        likeMapper.insert(like);
        
        // 增加点赞数
        postMapper.incrementLikeCount(postId);
        
        // 清除缓存
        redisTemplate.delete(POST_CACHE_KEY + postId);
    }
    
    @Override
    @Transactional
    public void unlikePost(Long userId, Long postId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetId, postId)
                .eq(Like::getType, "post");
        
        likeMapper.delete(wrapper);
        
        // 减少点赞数
        postMapper.decrementLikeCount(postId);
        
        // 清除缓存
        redisTemplate.delete(POST_CACHE_KEY + postId);
    }
    
    @Override
    @Transactional
    public void collectPost(Long userId, Long postId) {
        // 检查是否已收藏
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
                .eq(Collect::getPostId, postId);
        
        if (collectMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "已经收藏过了");
        }
        
        // 添加收藏记录
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setPostId(postId);
        collectMapper.insert(collect);
        
        // 增加收藏数
        postMapper.incrementCollectCount(postId);
        
        // 清除缓存
        redisTemplate.delete(POST_CACHE_KEY + postId);
    }
    
    @Override
    @Transactional
    public void uncollectPost(Long userId, Long postId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
                .eq(Collect::getPostId, postId);
        
        collectMapper.delete(wrapper);
        
        // 减少收藏数
        postMapper.decrementCollectCount(postId);
        
        // 清除缓存
        redisTemplate.delete(POST_CACHE_KEY + postId);
    }
    
    @Override
    @Transactional
    public Long addComment(Long userId, AddCommentRequest request) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(request.getPostId());
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "帖子不存在");
        }
        
        Comment comment = new Comment();
        comment.setPostId(request.getPostId());
        comment.setUserId(userId);
        comment.setParentId(request.getParentId());
        comment.setReplyUserId(request.getReplyUserId());
        comment.setContent(request.getContent());
        comment.setLikeCount(0);
        
        commentMapper.insert(comment);
        
        // 增加帖子评论数
        postMapper.incrementCommentCount(request.getPostId());
        
        log.info("用户 {} 评论帖子 {}", userId, request.getPostId());
        
        return comment.getId();
    }
    
    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "评论不存在");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权删除此评论");
        }
        
        commentMapper.deleteById(commentId);
        
        log.info("用户 {} 删除评论 {}", userId, commentId);
    }
    
    @Override
    public List<CommentVO> getCommentList(Long userId, Long postId) {
        // 查询所有评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId)
                .orderByAsc(Comment::getCreateTime);
        
        List<Comment> comments = commentMapper.selectList(wrapper);
        
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 转换为 VO 并构建树形结构
        return buildCommentTree(comments, userId);
    }
    
    /**
     * 构建评论树形结构
     */
    private List<CommentVO> buildCommentTree(List<Comment> comments, Long userId) {
        // 获取所有用户信息
        Set<Long> userIds = comments.stream()
                .flatMap(c -> {
                    List<Long> ids = new ArrayList<>();
                    ids.add(c.getUserId());
                    if (c.getReplyUserId() != null) {
                        ids.add(c.getReplyUserId());
                    }
                    return ids.stream();
                })
                .collect(Collectors.toSet());
        
        Map<Long, Map<String, String>> userInfoMap = getUserInfoMap(userIds);
        
        // 获取点赞状态
        Set<Long> likedCommentIds = getLikedCommentIds(userId, 
                comments.stream().map(Comment::getId).collect(Collectors.toSet()));
        
        // 转换为 VO
        Map<Long, CommentVO> voMap = new HashMap<>();
        for (Comment comment : comments) {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);
            
            Map<String, String> userInfo = userInfoMap.get(comment.getUserId());
            if (userInfo != null) {
                vo.setUserName(userInfo.get("nickname"));
                vo.setUserAvatar(userInfo.get("avatar"));
            }
            
            if (comment.getReplyUserId() != null) {
                Map<String, String> replyUserInfo = userInfoMap.get(comment.getReplyUserId());
                if (replyUserInfo != null) {
                    vo.setReplyUserName(replyUserInfo.get("nickname"));
                }
            }
            
            vo.setIsLiked(likedCommentIds.contains(comment.getId()));
            vo.setReplies(new ArrayList<>());
            
            voMap.put(comment.getId(), vo);
        }
        
        // 构建树形结构
        List<CommentVO> result = new ArrayList<>();
        for (CommentVO vo : voMap.values()) {
            if (vo.getParentId() == 0) {
                result.add(vo);
            } else {
                CommentVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getReplies().add(vo);
                }
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public void likeComment(Long userId, Long commentId) {
        // 检查是否已点赞
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetId, commentId)
                .eq(Like::getType, "comment");
        
        if (likeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "已经点赞过了");
        }
        
        // 添加点赞记录
        Like like = new Like();
        like.setUserId(userId);
        like.setTargetId(commentId);
        like.setType("comment");
        likeMapper.insert(like);
        
        // 增加点赞数
        commentMapper.incrementLikeCount(commentId);
    }
    
    @Override
    @Transactional
    public void unlikeComment(Long userId, Long commentId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetId, commentId)
                .eq(Like::getType, "comment");
        
        likeMapper.delete(wrapper);
        
        // 减少点赞数
        commentMapper.decrementLikeCount(commentId);
    }
    
    @Override
    public Page<PostVO> getMyPosts(Long userId, Integer pageNum, Integer pageSize) {
        Page<Post> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId)
                .orderByDesc(Post::getCreateTime);
        
        Page<Post> postPage = postMapper.selectPage(page, wrapper);
        
        Page<PostVO> voPage = new Page<>();
        BeanUtils.copyProperties(postPage, voPage, "records");
        
        List<PostVO> voList = convertToVOList(postPage.getRecords(), userId);
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public Page<PostVO> getMyCollects(Long userId, Integer pageNum, Integer pageSize) {
        Page<Collect> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
                .orderByDesc(Collect::getCreateTime);
        
        Page<Collect> collectPage = collectMapper.selectPage(page, wrapper);
        
        if (collectPage.getRecords().isEmpty()) {
            Page<PostVO> voPage = new Page<>();
            BeanUtils.copyProperties(collectPage, voPage, "records");
            voPage.setRecords(Collections.emptyList());
            return voPage;
        }
        
        // 查询帖子信息
        List<Long> postIds = collectPage.getRecords().stream()
                .map(Collect::getPostId)
                .collect(Collectors.toList());
        
        List<Post> posts = postMapper.selectBatchIds(postIds);
        
        Page<PostVO> voPage = new Page<>();
        BeanUtils.copyProperties(collectPage, voPage, "records");
        
        List<PostVO> voList = convertToVOList(posts, userId);
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    /**
     * 批量转换为 VO
     */
    private List<PostVO> convertToVOList(List<Post> posts, Long userId) {
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }
        
        Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());
        Map<Long, Map<String, String>> userInfoMap = getUserInfoMap(userIds);
        
        Set<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        Set<Long> likedPostIds = getLikedPostIds(userId, postIds);
        Set<Long> collectedPostIds = getCollectedPostIds(userId, postIds);
        
        return posts.stream().map(post -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(post, vo);
            
            Map<String, String> userInfo = userInfoMap.get(post.getUserId());
            if (userInfo != null) {
                vo.setUserName(userInfo.get("nickname"));
                vo.setUserAvatar(userInfo.get("avatar"));
            }
            
            if (StrUtil.isNotBlank(post.getImages())) {
                vo.setImageList(post.getImages().split(","));
            }
            
            if (StrUtil.isNotBlank(post.getTags())) {
                vo.setTagList(post.getTags().split(","));
            }
            
            vo.setIsLiked(likedPostIds.contains(post.getId()));
            vo.setIsCollected(collectedPostIds.contains(post.getId()));
            
            return vo;
        }).collect(Collectors.toList());
    }
    
    /**
     * 转换为 VO
     */
    private PostVO convertToVO(Post post, Long userId) {
        return convertToVOList(Collections.singletonList(post), userId).get(0);
    }
    
    /**
     * 填充用户相关信息
     */
    private void fillUserRelatedInfo(PostVO vo, Long userId) {
        Set<Long> likedPostIds = getLikedPostIds(userId, Collections.singleton(vo.getId()));
        Set<Long> collectedPostIds = getCollectedPostIds(userId, Collections.singleton(vo.getId()));
        
        vo.setIsLiked(likedPostIds.contains(vo.getId()));
        vo.setIsCollected(collectedPostIds.contains(vo.getId()));
    }
    
    /**
     * 获取用户信息映射
     */
    private Map<Long, Map<String, String>> getUserInfoMap(Set<Long> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        
        String userIdStr = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        
        try {
            return userFeignClient.batchGetUserInfo(userIdStr);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Collections.emptyMap();
        }
    }
    
    /**
     * 获取已点赞的帖子 ID
     */
    private Set<Long> getLikedPostIds(Long userId, Set<Long> postIds) {
        if (userId == null || postIds.isEmpty()) {
            return Collections.emptySet();
        }
        
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getType, "post")
                .in(Like::getTargetId, postIds);
        
        return likeMapper.selectList(wrapper).stream()
                .map(Like::getTargetId)
                .collect(Collectors.toSet());
    }
    
    /**
     * 获取已收藏的帖子 ID
     */
    private Set<Long> getCollectedPostIds(Long userId, Set<Long> postIds) {
        if (userId == null || postIds.isEmpty()) {
            return Collections.emptySet();
        }
        
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
                .in(Collect::getPostId, postIds);
        
        return collectMapper.selectList(wrapper).stream()
                .map(Collect::getPostId)
                .collect(Collectors.toSet());
    }
    
    /**
     * 获取已点赞的评论 ID
     */
    private Set<Long> getLikedCommentIds(Long userId, Set<Long> commentIds) {
        if (userId == null || commentIds.isEmpty()) {
            return Collections.emptySet();
        }
        
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getType, "comment")
                .in(Like::getTargetId, commentIds);
        
        return likeMapper.selectList(wrapper).stream()
                .map(Like::getTargetId)
                .collect(Collectors.toSet());
    }
}
