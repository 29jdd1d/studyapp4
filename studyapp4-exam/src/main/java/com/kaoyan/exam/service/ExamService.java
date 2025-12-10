package com.kaoyan.exam.service;

import com.kaoyan.common.result.PageResult;
import com.kaoyan.exam.dto.AnswerSubmitRequest;
import com.kaoyan.exam.dto.QuestionQueryRequest;
import com.kaoyan.exam.entity.Question;
import com.kaoyan.exam.entity.WrongQuestion;
import com.kaoyan.exam.vo.AnswerResultVO;

import java.util.List;

/**
 * 题库服务接口
 */
public interface ExamService {
    
    /**
     * 分页查询题目
     *
     * @param request 查询请求
     * @return 分页结果
     */
    PageResult<Question> queryQuestions(QuestionQueryRequest request);
    
    /**
     * 获取题目详情
     *
     * @param id 题目ID
     * @return 题目详情
     */
    Question getQuestionById(Long id);
    
    /**
     * 提交答案
     *
     * @param userId  用户ID
     * @param request 答题请求
     * @return 答题结果
     */
    AnswerResultVO submitAnswer(Long userId, AnswerSubmitRequest request);
    
    /**
     * 获取错题本
     *
     * @param userId 用户ID
     * @return 错题列表
     */
    PageResult<Question> getWrongQuestions(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 标记错题为已掌握
     *
     * @param userId     用户ID
     * @param questionId 题目ID
     * @return 是否成功
     */
    Boolean markAsMastered(Long userId, Long questionId);
    
    /**
     * 智能推荐题目（优先推送错题相关知识点）
     *
     * @param userId   用户ID
     * @param category 科目分类
     * @param count    推荐数量
     * @return 题目列表
     */
    List<Question> recommendQuestions(Long userId, String category, Integer count);
}
