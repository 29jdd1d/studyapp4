package com.kaoyan.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.exception.BusinessException;
import com.kaoyan.common.result.PageResult;
import com.kaoyan.common.result.ResultCode;
import com.kaoyan.exam.dto.AnswerSubmitRequest;
import com.kaoyan.exam.dto.QuestionQueryRequest;
import com.kaoyan.exam.entity.AnswerRecord;
import com.kaoyan.exam.entity.Question;
import com.kaoyan.exam.entity.WrongQuestion;
import com.kaoyan.exam.mapper.AnswerRecordMapper;
import com.kaoyan.exam.mapper.QuestionMapper;
import com.kaoyan.exam.mapper.WrongQuestionMapper;
import com.kaoyan.exam.service.ExamService;
import com.kaoyan.exam.vo.AnswerResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    
    private final QuestionMapper questionMapper;
    private final AnswerRecordMapper answerRecordMapper;
    private final WrongQuestionMapper wrongQuestionMapper;
    
    @Override
    public PageResult<Question> queryQuestions(QuestionQueryRequest request) {
        Page<Question> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(request.getType())) {
            queryWrapper.eq(Question::getType, request.getType());
        }
        
        if (StringUtils.hasText(request.getCategory())) {
            queryWrapper.eq(Question::getCategory, request.getCategory());
        }
        
        if (StringUtils.hasText(request.getChapter())) {
            queryWrapper.eq(Question::getChapter, request.getChapter());
        }
        
        if (StringUtils.hasText(request.getKnowledgePoint())) {
            queryWrapper.like(Question::getKnowledgePoint, request.getKnowledgePoint());
        }
        
        if (request.getYear() != null) {
            queryWrapper.eq(Question::getYear, request.getYear());
        }
        
        if (request.getDifficulty() != null) {
            queryWrapper.eq(Question::getDifficulty, request.getDifficulty());
        }
        
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like(Question::getContent, request.getKeyword());
        }
        
        queryWrapper.eq(Question::getStatus, 1);
        queryWrapper.orderByDesc(Question::getCreateTime);
        
        Page<Question> result = questionMapper.selectPage(page, queryWrapper);
        
        return new PageResult<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    public Question getQuestionById(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null || question.getStatus() == 0) {
            throw new BusinessException(ResultCode.QUESTION_NOT_FOUND);
        }
        
        // 增加浏览次数
        question.setViewCount(question.getViewCount() + 1);
        questionMapper.updateById(question);
        
        return question;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnswerResultVO submitAnswer(Long userId, AnswerSubmitRequest request) {
        // 获取题目
        Question question = questionMapper.selectById(request.getQuestionId());
        if (question == null) {
            throw new BusinessException(ResultCode.QUESTION_NOT_FOUND);
        }
        
        // 判断答案是否正确
        boolean isCorrect = checkAnswer(question.getAnswer(), request.getUserAnswer());
        
        // 保存答题记录
        AnswerRecord record = new AnswerRecord();
        record.setUserId(userId);
        record.setQuestionId(request.getQuestionId());
        record.setUserAnswer(request.getUserAnswer());
        record.setIsCorrect(isCorrect ? 1 : 0);
        record.setTimeSpent(request.getTimeSpent());
        record.setSubmitTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        answerRecordMapper.insert(record);
        
        boolean addedToWrongBook = false;
        
        // 如果答错，加入或更新错题本
        if (!isCorrect) {
            addedToWrongBook = addToWrongBook(userId, request.getQuestionId());
        } else {
            // 答对了，检查是否在错题本中，如果在则标记为已掌握
            LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WrongQuestion::getUserId, userId);
            wrapper.eq(WrongQuestion::getQuestionId, request.getQuestionId());
            wrapper.eq(WrongQuestion::getIsMastered, 0);
            
            WrongQuestion wrongQuestion = wrongQuestionMapper.selectOne(wrapper);
            if (wrongQuestion != null) {
                wrongQuestion.setIsMastered(1);
                wrongQuestionMapper.updateById(wrongQuestion);
            }
        }
        
        return new AnswerResultVO(isCorrect, question.getAnswer(), question.getAnalysis(), addedToWrongBook);
    }
    
    @Override
    public PageResult<Question> getWrongQuestions(Long userId, Integer pageNum, Integer pageSize) {
        Page<WrongQuestion> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getUserId, userId);
        wrapper.eq(WrongQuestion::getIsMastered, 0);
        wrapper.orderByDesc(WrongQuestion::getLastWrongTime);
        
        Page<WrongQuestion> wrongPage = wrongQuestionMapper.selectPage(page, wrapper);
        
        // 获取题目ID列表
        List<Long> questionIds = wrongPage.getRecords().stream()
                .map(WrongQuestion::getQuestionId)
                .collect(Collectors.toList());
        
        List<Question> questions = questionIds.isEmpty() ? List.of() : questionMapper.selectBatchIds(questionIds);
        
        return new PageResult<>(
                wrongPage.getTotal(),
                wrongPage.getPages(),
                wrongPage.getCurrent(),
                wrongPage.getSize(),
                questions
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean markAsMastered(Long userId, Long questionId) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getUserId, userId);
        wrapper.eq(WrongQuestion::getQuestionId, questionId);
        
        WrongQuestion wrongQuestion = wrongQuestionMapper.selectOne(wrapper);
        if (wrongQuestion == null) {
            return false;
        }
        
        wrongQuestion.setIsMastered(1);
        wrongQuestionMapper.updateById(wrongQuestion);
        
        return true;
    }
    
    @Override
    public List<Question> recommendQuestions(Long userId, String category, Integer count) {
        // 1. 先获取用户的错题知识点
        LambdaQueryWrapper<WrongQuestion> wrongWrapper = new LambdaQueryWrapper<>();
        wrongWrapper.eq(WrongQuestion::getUserId, userId);
        wrongWrapper.eq(WrongQuestion::getIsMastered, 0);
        wrongWrapper.last("LIMIT 10");
        
        List<WrongQuestion> wrongQuestions = wrongQuestionMapper.selectList(wrongWrapper);
        
        if (wrongQuestions.isEmpty()) {
            // 没有错题，随机推荐
            LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Question::getCategory, category);
            wrapper.eq(Question::getStatus, 1);
            wrapper.last("ORDER BY RAND() LIMIT " + count);
            return questionMapper.selectList(wrapper);
        }
        
        // 2. 获取错题相关知识点的题目
        List<Long> wrongQuestionIds = wrongQuestions.stream()
                .map(WrongQuestion::getQuestionId)
                .collect(Collectors.toList());
        
        List<Question> wrongQuestionList = questionMapper.selectBatchIds(wrongQuestionIds);
        
        // 提取知识点
        List<String> knowledgePoints = wrongQuestionList.stream()
                .map(Question::getKnowledgePoint)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
        
        // 3. 推荐相关知识点的题目
        if (!knowledgePoints.isEmpty()) {
            LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Question::getCategory, category);
            wrapper.in(Question::getKnowledgePoint, knowledgePoints);
            wrapper.eq(Question::getStatus, 1);
            wrapper.notIn(!wrongQuestionIds.isEmpty(), Question::getId, wrongQuestionIds);
            wrapper.last("ORDER BY RAND() LIMIT " + count);
            return questionMapper.selectList(wrapper);
        }
        
        // 兜底：随机推荐
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getCategory, category);
        wrapper.eq(Question::getStatus, 1);
        wrapper.last("ORDER BY RAND() LIMIT " + count);
        return questionMapper.selectList(wrapper);
    }
    
    /**
     * 检查答案是否正确
     */
    private boolean checkAnswer(String correctAnswer, String userAnswer) {
        if (correctAnswer == null || userAnswer == null) {
            return false;
        }
        // 去除空格，转大写进行比较
        return correctAnswer.replaceAll("\\s+", "").equalsIgnoreCase(
                userAnswer.replaceAll("\\s+", "")
        );
    }
    
    /**
     * 添加到错题本
     */
    private boolean addToWrongBook(Long userId, Long questionId) {
        LambdaQueryWrapper<WrongQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WrongQuestion::getUserId, userId);
        wrapper.eq(WrongQuestion::getQuestionId, questionId);
        
        WrongQuestion wrongQuestion = wrongQuestionMapper.selectOne(wrapper);
        
        if (wrongQuestion == null) {
            // 新增错题
            wrongQuestion = new WrongQuestion();
            wrongQuestion.setUserId(userId);
            wrongQuestion.setQuestionId(questionId);
            wrongQuestion.setWrongCount(1);
            wrongQuestion.setLastWrongTime(LocalDateTime.now());
            wrongQuestion.setIsMastered(0);
            wrongQuestionMapper.insert(wrongQuestion);
            return true;
        } else {
            // 更新错题次数
            wrongQuestion.setWrongCount(wrongQuestion.getWrongCount() + 1);
            wrongQuestion.setLastWrongTime(LocalDateTime.now());
            wrongQuestion.setIsMastered(0);  // 重置掌握状态
            wrongQuestionMapper.updateById(wrongQuestion);
            return false;
        }
    }
}
