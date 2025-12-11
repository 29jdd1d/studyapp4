package com.kaoyan.user.controller;

import com.kaoyan.common.result.Result;
import com.kaoyan.user.service.ReportService;
import com.kaoyan.user.vo.StudyReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 学习报告 Controller
 *
 * @author kaoyan
 */
@Tag(name = "学习报告", description = "学习报告相关接口，包括报告生成和导出功能")
@Slf4j
@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportService reportService;
    
    /**
     * 生成学习报告
     */
    @Operation(summary = "生成学习报告", description = "生成指定时间段的学习报告数据")
    @GetMapping("/generate")
    public Result<StudyReportVO> generateReport(
            @Parameter(description = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "开始日期", example = "2025-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2025-01-07") @RequestParam String endDate) {
        log.info("生成学习报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);
        StudyReportVO report = reportService.generateReport(userId, startDate, endDate);
        return Result.success(report);
    }
    
    /**
     * 导出学习报告为PDF
     */
    @Operation(summary = "导出PDF报告", description = "将学习报告导出为PDF文件下载")
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @Parameter(description = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "开始日期", example = "2025-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2025-01-07") @RequestParam String endDate) {
        log.info("导出PDF报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);
        
        byte[] pdfBytes = reportService.exportReportToPdf(userId, startDate, endDate);
        
        String filename = String.format("学习报告_%s_%s.pdf", startDate, endDate);
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", encodedFilename);
        headers.setContentLength(pdfBytes.length);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
