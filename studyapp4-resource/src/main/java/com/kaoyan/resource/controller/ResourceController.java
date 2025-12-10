package com.kaoyan.resource.controller;

import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.Result;
import com.kaoyan.resource.dto.ResourceCreateRequest;
import com.kaoyan.resource.dto.ResourceQueryRequest;
import com.kaoyan.resource.entity.Resource;
import com.kaoyan.resource.service.ResourceService;
import com.kaoyan.resource.vo.CosCredentialVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 资源 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
public class ResourceController {
    
    private final ResourceService resourceService;
    
    /**
     * 获取 COS 上传凭证
     */
    @GetMapping("/upload-credential")
    public Result<CosCredentialVO> getUploadCredential(@RequestParam String type) {
        log.info("获取上传凭证: type={}", type);
        CosCredentialVO credential = resourceService.getUploadCredential(type);
        return Result.success(credential);
    }
    
    /**
     * 创建资源
     */
    @PostMapping
    public Result<Long> createResource(
            @RequestHeader("X-User-Id") Long userId,
            @Validated @RequestBody ResourceCreateRequest request) {
        log.info("创建资源: userId={}, request={}", userId, request);
        Long resourceId = resourceService.createResource(request, userId);
        return Result.success(resourceId);
    }
    
    /**
     * 分页查询资源
     */
    @GetMapping("/list")
    public Result<PageResult<Resource>> queryResources(ResourceQueryRequest request) {
        log.info("查询资源: request={}", request);
        PageResult<Resource> result = resourceService.queryResources(request);
        return Result.success(result);
    }
    
    /**
     * 获取资源详情
     */
    @GetMapping("/{id}")
    public Result<Resource> getResourceById(@PathVariable Long id) {
        log.info("获取资源详情: id={}", id);
        Resource resource = resourceService.getResourceById(id);
        // 增加浏览次数
        resourceService.incrementViewCount(id);
        return Result.success(resource);
    }
    
    /**
     * 下载资源
     */
    @PostMapping("/{id}/download")
    public Result<String> downloadResource(@PathVariable Long id) {
        log.info("下载资源: id={}", id);
        Resource resource = resourceService.getResourceById(id);
        // 增加下载次数
        resourceService.incrementDownloadCount(id);
        return Result.success(resource.getFileUrl());
    }
    
    /**
     * 删除资源
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteResource(@PathVariable Long id) {
        log.info("删除资源: id={}", id);
        Boolean result = resourceService.deleteResource(id);
        return Result.success(result);
    }
}
