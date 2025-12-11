package com.kaoyan.user.service;

import com.kaoyan.user.vo.StudyReportVO;

/**
 * 学习报告服务
 *
 * @author kaoyan
 */
public interface ReportService {
    
    /**
     * 生成学习报告
     *
     * @param userId 用户ID
     * @param startDate 开始日期（yyyy-MM-dd）
     * @param endDate 结束日期（yyyy-MM-dd）
     * @return 学习报告数据
     */
    StudyReportVO generateReport(Long userId, String startDate, String endDate);
    
    /**
     * 导出学习报告为PDF
     *
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return PDF文件字节数组
     */
    byte[] exportReportToPdf(Long userId, String startDate, String endDate);
}
