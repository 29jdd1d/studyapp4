package com.kaoyan.resource.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.kaoyan.resource.config.TencentCosProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * COS 文件上传服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CosUploadService {
    
    private final COSClient cosClient;
    private final TencentCosProperties cosProperties;
    
    /**
     * 上传文件到 COS
     * 
     * @param file 文件
     * @param module 模块名（resource/user/community等）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String module) {
        try {
            // 1. 生成文件路径：module/yyyy/MM/uuid_原文件名
            String filePath = generateFilePath(file.getOriginalFilename(), module);
            
            // 2. 上传到 COS
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putRequest = new PutObjectRequest(
                    cosProperties.getBucketName(), 
                    filePath, 
                    inputStream, 
                    null
            );
            
            PutObjectResult result = cosClient.putObject(putRequest);
            
            // 3. 构建访问 URL
            String fileUrl = cosProperties.getBaseUrl() + "/" + filePath;
            
            log.info("文件上传成功: filePath={}, url={}", filePath, fileUrl);
            
            return fileUrl;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成文件存储路径
     */
    private String generateFilePath(String originalFilename, String module) {
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        
        // 获取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 生成唯一文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        return String.format("%s/%s/%s/%s%s", module, year, month, uuid, extension);
    }
    
    /**
     * 删除 COS 文件
     */
    public void deleteFile(String fileUrl) {
        try {
            // 从完整URL中提取文件路径
            String filePath = fileUrl.replace(cosProperties.getBaseUrl() + "/", "");
            
            cosClient.deleteObject(cosProperties.getBucketName(), filePath);
            
            log.info("文件删除成功: filePath={}", filePath);
            
        } catch (Exception e) {
            log.error("文件删除失败: fileUrl={}", fileUrl, e);
        }
    }
}
