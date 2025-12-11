package com.kaoyan.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.resource.config.TencentCosProperties;
import com.kaoyan.resource.dto.ResourceCreateRequest;
import com.kaoyan.resource.dto.ResourceQueryRequest;
import com.kaoyan.resource.entity.Resource;
import com.kaoyan.resource.mapper.ResourceMapper;
import com.kaoyan.resource.service.ResourceService;
import com.kaoyan.resource.vo.CosCredentialVO;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 资源服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    
    private final ResourceMapper resourceMapper;
    private final TencentCosProperties cosConfig;
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
        credential.setBucket(cosConfig.getBucketName());
        credential.setRegion(cosConfig.getRegion());
        credential.setPrefix(prefix);
        
        // 使用 STS 临时密钥
        if (Boolean.TRUE.equals(cosConfig.getUseSts())) {
            try {
                Response stsResponse = getStsCredential(prefix);
                credential.setTmpSecretId(stsResponse.credentials.tmpSecretId);
                credential.setTmpSecretKey(stsResponse.credentials.tmpSecretKey);
                credential.setSessionToken(stsResponse.credentials.sessionToken);
                // STS 返回的是秒级时间戳，转换为毫秒
                credential.setExpiration(stsResponse.expiredTime * 1000);
                log.info("获取 STS 临时密钥成功, prefix={}, expiredTime={}", prefix, stsResponse.expiredTime);
            } catch (Exception e) {
                log.error("获取 STS 临时密钥失败", e);
                throw new BusinessException(ResultCode.SYSTEM_ERROR, "获取上传凭证失败");
            }
        } else {
            // 不使用 STS，直接返回永久密钥（不推荐用于生产环境）
            log.warn("未启用 STS 临时密钥，使用永久密钥（不推荐）");
            credential.setTmpSecretId(cosConfig.getSecretId());
            credential.setTmpSecretKey(cosConfig.getSecretKey());
            credential.setSessionToken("");
            credential.setExpiration(System.currentTimeMillis() + cosConfig.getDurationSeconds() * 1000);
        }
        
        return credential;
    }
    
    /**
     * 获取 STS 临时凭证
     */
    private Response getStsCredential(String prefix) throws Exception {
        TreeMap<String, Object> config = new TreeMap<>();
        
        // 云 API 密钥 SecretId
        config.put("secretId", cosConfig.getSecretId());
        // 云 API 密钥 SecretKey
        config.put("secretKey", cosConfig.getSecretKey());
        
        // 临时密钥有效时长，单位是秒（默认 30 分钟）
        config.put("durationSeconds", cosConfig.getDurationSeconds());
        
        // 换成你的 bucket
        config.put("bucket", cosConfig.getBucketName());
        // 换成 bucket 所在地区
        config.put("region", cosConfig.getRegion());
        
        // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
        // 例如：resource/video/2025/12/*，表示只允许在该路径下上传
        config.put("allowPrefixes", new String[]{
                prefix + "/*"
        });
        
        // 密钥的权限列表。简单上传、表单上传和分块上传需要以下的权限
        // 其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = cosConfig.getAllowActions().split(",");
        config.put("allowActions", allowActions);
        
        return CosStsClient.getCredential(config);
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
