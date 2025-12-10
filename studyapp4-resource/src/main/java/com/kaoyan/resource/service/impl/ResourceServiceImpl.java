package com.kaoyan.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.resource.config.CosConfig;
import com.kaoyan.resource.dto.ResourceCreateRequest;
import com.kaoyan.resource.dto.ResourceQueryRequest;
import com.kaoyan.resource.entity.Resource;
import com.kaoyan.resource.mapper.ResourceMapper;
import com.kaoyan.resource.service.ResourceService;
import com.kaoyan.resource.vo.CosCredentialVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 资源服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    
    private final ResourceMapper resourceMapper;
    private final CosConfig cosConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public CosCredentialVO getUploadCredential(String type) {
        // 生成上传路径：resource/{type}/{year}/{month}/{uuid}
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        String uuid = IdUtil.simpleUUID();
        
        String prefix = String.format("resource/%s/%s/%s/%s", type, year, month, uuid);
        
        CosCredentialVO credential = new CosCredentialVO();
        // 注意：这里简化处理，实际应该使用 STS 临时密钥
        // 参考：https://cloud.tencent.com/document/product/436/14048
        credential.setTmpSecretId(cosConfig.getSecretId());
        credential.setTmpSecretKey(cosConfig.getSecretKey());
        credential.setSessionToken("");
        credential.setExpiration(System.currentTimeMillis() + cosConfig.getDurationSeconds() * 1000);
        credential.setBucket(cosConfig.getBucket());
        credential.setRegion(cosConfig.getRegion());
        credential.setPrefix(prefix);
        
        return credential;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createResource(ResourceCreateRequest request, Long publisherId) {
        Resource resource = BeanUtil.copyProperties(request, Resource.class);
        resource.setPublisherId(publisherId);
        resource.setViewCount(0);
        resource.setDownloadCount(0);
        resource.setStatus(1);
        
        resourceMapper.insert(resource);
        
        log.info("创建资源成功: id={}, title={}", resource.getId(), resource.getTitle());
        return resource.getId();
    }
    
    @Override
    public PageResult<Resource> queryResources(ResourceQueryRequest request) {
        Page<Resource> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(request.getType())) {
            queryWrapper.eq(Resource::getType, request.getType());
        }
        
        if (StringUtils.hasText(request.getCategory())) {
            queryWrapper.eq(Resource::getCategory, request.getCategory());
        }
        
        if (StringUtils.hasText(request.getSubCategory())) {
            queryWrapper.eq(Resource::getSubCategory, request.getSubCategory());
        }
        
        if (request.getDifficulty() != null) {
            queryWrapper.eq(Resource::getDifficulty, request.getDifficulty());
        }
        
        if (request.getIsFree() != null) {
            queryWrapper.eq(Resource::getIsFree, request.getIsFree());
        }
        
        if (request.getStatus() != null) {
            queryWrapper.eq(Resource::getStatus, request.getStatus());
        }
        
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Resource::getTitle, request.getKeyword())
                    .or()
                    .like(Resource::getDescription, request.getKeyword())
            );
        }
        
        queryWrapper.orderByDesc(Resource::getCreateTime);
        
        Page<Resource> result = resourceMapper.selectPage(page, queryWrapper);
        
        return new PageResult<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    public Resource getResourceById(Long id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_FOUND);
        }
        return resource;
    }
    
    @Override
    public void incrementViewCount(Long id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() + 1);
            resourceMapper.updateById(resource);
        }
    }
    
    @Override
    public void incrementDownloadCount(Long id) {
        Resource resource = resourceMapper.selectById(id);
        if (resource != null) {
            resource.setDownloadCount(resource.getDownloadCount() + 1);
            resourceMapper.updateById(resource);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResource(Long id) {
        int rows = resourceMapper.deleteById(id);
        return rows > 0;
    }
}
