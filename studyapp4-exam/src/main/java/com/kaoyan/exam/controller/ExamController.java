package com.kaoyan.exam.controller;

import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.Result;
import com.kaoyan.exam.dto.AnswerSubmitRequest;
import com.kaoyan.exam.dto.QuestionQueryRequest;
import com.kaoyan.exam.entity.Question;
import com.kaoyan.exam.service.ExamService;
import com.kaoyan.exam.vo.AnswerResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    
    private final ExamService examService;
    
    /**
     * 分页查询题目
     */
    @GetMapping("/questions")
    public Result<PageResult<Question>> queryQuestions(QuestionQueryRequest request) {
        log.info("查询题目: request={}", request);
        PageResult<Question> result = examService.queryQuestions(request);
        return Result.success(result);
    }
    
    /**
     * 获取题目详情
     */
    @GetMapping("/question/{id}")
    public Result<Question> getQuestionById(@PathVariable Long id) {
        log.info("获取题目详情: id={}", id);
        Question question = examService.getQuestionById(id);
        return Result.success(question);
    }
    
    /**
     * 提交答案
     */
    @PostMapping("/submit")
    public Result<AnswerResultVO> submitAnswer(
            @RequestHeader("X-User-Id") Long userId,
            @Validated @RequestBody AnswerSubmitRequest request) {
        log.info("提交答案: userId={}, request={}", userId, request);
        AnswerResultVO result = examService.submitAnswer(userId, request);
        return Result.success(result);
    }
    
    /**
     * 获取错题本
     */
    @GetMapping("/wrong-questions")
    public Result<PageResult<Question>> getWrongQuestions(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("获取错题本: userId={}", userId);
        PageResult<Question> result = examService.getWrongQuestions(userId, pageNum, pageSize);
        return Result.success(result);
    }
    
    /**
     * 标记错题为已掌握
     */
    @PostMapping("/wrong-questions/{questionId}/mastered")
    public Result<Boolean> markAsMastered(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long questionId) {
        log.info("标记错题为已掌握: userId={}, questionId={}", userId, questionId);
        Boolean result = examService.markAsMastered(userId, questionId);
        return Result.success(result);
    }
    
    /**
     * 智能推荐题目
     */
    @GetMapping("/recommend")
    public Result<List<Question>> recommendQuestions(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam String category,
            @RequestParam(defaultValue = "10") Integer count) {
        log.info("智能推荐题目: userId={}, category={}, count={}", userId, category, count);
        List<Question> questions = examService.recommendQuestions(userId, category, count);
        return Result.success(questions);
    }
}
