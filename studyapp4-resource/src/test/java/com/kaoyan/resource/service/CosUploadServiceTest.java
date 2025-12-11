package com.kaoyan.resource.service;

import com.kaoyan.resource.config.TencentCosProperties;
import com.kaoyan.resource.service.impl.CosUploadServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * COS上传服务测试
 *
 * @author kaoyan
 */
class CosUploadServiceTest {
    
    @Mock
    private COSClient cosClient;
    
    @Mock
    private TencentCosProperties cosProperties;
    
    @Mock
    private MultipartFile file;
    
    @InjectMocks
    private CosUploadServiceImpl cosUploadService;
    
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // 模拟配置
        when(cosProperties.getBucketName()).thenReturn("test-bucket");
        when(cosProperties.getBaseUrl()).thenReturn("https://test-bucket.cos.ap-guangzhou.myqcloud.com");
        
        // 模拟文件
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[1024]));
        
        // 模拟COS上传
        PutObjectResult putResult = new PutObjectResult();
        when(cosClient.putObject(any())).thenReturn(putResult);
    }
    
    @Test
    void testUploadFile() {
        // 执行上传
        String fileUrl = cosUploadService.uploadFile(file, "resource");
        
        // 验证结果
        assertNotNull(fileUrl);
        assertTrue(fileUrl.startsWith("https://"));
        assertTrue(fileUrl.contains("resource/"));
        assertTrue(fileUrl.endsWith("test.jpg"));
        
        // 验证调用
        verify(cosClient, times(1)).putObject(any());
    }
    
    @Test
    void testDeleteFile() {
        // 执行删除
        String fileKey = "resource/2025/12/test.jpg";
        cosUploadService.deleteFile(fileKey);
        
        // 验证调用
        verify(cosClient, times(1)).deleteObject(anyString(), eq(fileKey));
    }
}
