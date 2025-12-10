package com.kaoyan.resource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.resource.dto.ResourceCreateRequest;
import com.kaoyan.resource.dto.ResourceQueryRequest;
import com.kaoyan.resource.entity.Resource;
import com.kaoyan.resource.vo.CosCredentialVO;

/**
 * 资源服务接口
 */
public interface ResourceService {
    
    /**
     * 获取 COS 上传凭证
     *
     * @param type 资源类型
     * @return 上传凭证
     */
    CosCredentialVO getUploadCredential(String type);
    
    /**
     * 创建资源
     *
     * @param request    创建请求
     * @param publisherId 发布者ID
     * @return 资源ID
     */
    Long createResource(ResourceCreateRequest request, Long publisherId);
    
    /**
     * 分页查询资源
     *
     * @param request 查询请求
     * @return 分页结果
     */
    PageResult<Resource> queryResources(ResourceQueryRequest request);
    
    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    Resource getResourceById(Long id);
    
    /**
     * 增加浏览次数
     *
     * @param id 资源ID
     */
    void incrementViewCount(Long id);
    
    /**
     * 增加下载次数
     *
     * @param id 资源ID
     */
    void incrementDownloadCount(Long id);
    
    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 是否成功
     */
    Boolean deleteResource(Long id);
}
