package com.kaoyan.user.service;

import com.kaoyan.user.entity.User;
import com.kaoyan.user.mapper.UserMapper;
import com.kaoyan.user.service.impl.ReportServiceImpl;
import com.kaoyan.user.vo.StudyReportVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * 学习报告服务测试
 *
 * @author kaoyan
 */
class ReportServiceTest {
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private ReportServiceImpl reportService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGenerateReport() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setNickname("测试用户");
        
        when(userMapper.selectById(anyLong())).thenReturn(user);
        
        // 执行测试
        StudyReportVO report = reportService.generateReport(1L, "2025-01-01", "2025-01-07");
        
        // 验证结果
        assertNotNull(report);
        assertEquals("测试用户", report.getNickname());
        assertNotNull(report.getPeriod());
        assertTrue(report.getTotalStudyTime() > 0);
        assertTrue(report.getTaskCompletionRate() >= 0 && report.getTaskCompletionRate() <= 100);
        assertTrue(report.getQuestionAccuracy() >= 0 && report.getQuestionAccuracy() <= 100);
        assertNotNull(report.getDailyStudyList());
        assertFalse(report.getDailyStudyList().isEmpty());
        assertEquals(7, report.getDailyStudyList().size());
        assertNotNull(report.getSubjectStudyList());
        assertEquals(4, report.getSubjectStudyList().size());
    }
    
    @Test
    void testExportReportToPdf() {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setNickname("测试用户");
        
        when(userMapper.selectById(anyLong())).thenReturn(user);
        
        // 执行测试
        byte[] pdfBytes = reportService.exportReportToPdf(1L, "2025-01-01", "2025-01-07");
        
        // 验证结果
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // 验证PDF内容包含关键信息
        String content = new String(pdfBytes);
        assertTrue(content.contains("学习报告"));
        assertTrue(content.contains("测试用户"));
        assertTrue(content.contains("学习概况"));
    }
}
