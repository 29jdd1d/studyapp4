-- ================================================
-- 考研学习小程序 - 测试数据脚本（修正版）
-- 匹配 init.sql 中的表结构
-- ================================================

-- ================================================
-- 1. 用户测试数据
-- ================================================
USE studyapp4_user;

-- 插入测试用户（模拟微信登录后的用户）
INSERT INTO `t_user` (`openid`, `unionid`, `nickname`, `avatar`, `phone`, `target_university`, `target_major`, `exam_year`, `current_level`, `total_study_time`, `continuous_days`, `create_time`) VALUES
('wx_openid_001', 'wx_unionid_001', '考研小王', 'https://example.com/avatar/user1.jpg', '13800138001', '清华大学', '计算机科学与技术', 2025, 3, 10800, 60, DATE_SUB(NOW(), INTERVAL 60 DAY)),
('wx_openid_002', 'wx_unionid_002', '奋斗的小李', 'https://example.com/avatar/user2.jpg', '13800138002', '北京大学', '金融学', 2025, 4, 13500, 75, DATE_SUB(NOW(), INTERVAL 75 DAY)),
('wx_openid_003', 'wx_unionid_003', '上岸必胜', 'https://example.com/avatar/user3.jpg', '13800138003', '复旦大学', '软件工程', 2026, 2, 7200, 40, DATE_SUB(NOW(), INTERVAL 40 DAY)),
('wx_openid_004', 'wx_unionid_004', '研途顺利', 'https://example.com/avatar/user4.jpg', '13800138004', '浙江大学', '法律硕士', 2025, 3, 16200, 90, DATE_SUB(NOW(), INTERVAL 90 DAY)),
('wx_openid_005', 'wx_unionid_005', '加油鸭', 'https://example.com/avatar/user5.jpg', '13800138005', '上海交通大学', '机械工程', 2025, 3, 9000, 50, DATE_SUB(NOW(), INTERVAL 50 DAY));

-- ================================================
-- 2. 资源测试数据
-- ================================================
USE studyapp4_resource;

-- 插入资源（匹配表结构：type, category, sub_category, publisher_id, is_free）
INSERT INTO `t_resource` (`title`, `description`, `type`, `category`, `sub_category`, `file_url`, `cover_url`, `file_size`, `duration`, `view_count`, `download_count`, `is_free`, `status`, `publisher_id`, `create_time`) VALUES
-- 政治资源
('马克思主义基本原理概论', '马原核心知识点精讲视频', 'video', 'politics', '马克思主义基本原理', 'https://cos.example.com/video/politics_marxism.mp4', 'https://cos.example.com/cover/politics1.jpg', 524288000, 3600, 1500, 200, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
('毛泽东思想概论', '毛概核心知识点梳理', 'video', 'politics', '毛泽东思想和中国特色社会主义理论', 'https://cos.example.com/video/politics_mao.mp4', 'https://cos.example.com/cover/politics2.jpg', 486539264, 2700, 1200, 150, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
('政治选择题技巧手册', '选择题答题技巧PDF文档', 'document', 'politics', '答题技巧', 'https://cos.example.com/doc/politics_tips.pdf', 'https://cos.example.com/cover/doc1.jpg', 5242880, NULL, 800, 500, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 25 DAY)),

-- 英语资源
('考研英语词汇5500精讲', '核心词汇系统讲解', 'video', 'english', '核心词汇', 'https://cos.example.com/video/english_vocab.mp4', 'https://cos.example.com/cover/english1.jpg', 419430400, 2400, 3000, 800, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),
('英语阅读理解技巧', '阅读理解解题方法论', 'video', 'english', '阅读理解', 'https://cos.example.com/video/english_reading.mp4', 'https://cos.example.com/cover/english2.jpg', 367001600, 1800, 2500, 600, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 18 DAY)),
('英语作文模板汇总', '高分作文模板大全', 'document', 'english', '写作模板', 'https://cos.example.com/doc/english_writing.pdf', 'https://cos.example.com/cover/doc2.jpg', 3145728, NULL, 1500, 1000, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- 数学资源
('高等数学-极限与连续', '高数第一章系统精讲', 'video', 'math', '高等数学', 'https://cos.example.com/video/math_calculus.mp4', 'https://cos.example.com/cover/math1.jpg', 524288000, 4200, 2800, 500, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 35 DAY)),
('线性代数-行列式', '线代第一章详细讲解', 'video', 'math', '线性代数', 'https://cos.example.com/video/math_algebra.mp4', 'https://cos.example.com/cover/math2.jpg', 471859200, 3000, 2200, 450, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 32 DAY)),
('概率论与数理统计基础', '概率论入门课程', 'video', 'math', '概率论与数理统计', 'https://cos.example.com/video/math_probability.mp4', 'https://cos.example.com/cover/math3.jpg', 445644800, 2700, 1800, 400, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
('数学公式速查手册', '常用公式汇总PDF', 'document', 'math', '公式汇总', 'https://cos.example.com/doc/math_formulas.pdf', 'https://cos.example.com/cover/doc3.jpg', 8388608, NULL, 3500, 2000, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- 专业课资源（计算机类）
('数据结构-线性表', '数据结构基础章节', 'video', 'professional', '数据结构', 'https://cos.example.com/video/cs_datastructure.mp4', 'https://cos.example.com/cover/cs1.jpg', 524288000, 3600, 2000, 400, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 40 DAY)),
('计算机组成原理', '组成原理系统课程', 'video', 'professional', '计算机组成原理', 'https://cos.example.com/video/cs_organization.mp4', 'https://cos.example.com/cover/cs2.jpg', 471859200, 3000, 1500, 300, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 38 DAY)),
('操作系统-进程管理', '操作系统核心概念', 'video', 'professional', '操作系统', 'https://cos.example.com/video/cs_os.mp4', 'https://cos.example.com/cover/cs3.jpg', 498073600, 3300, 1800, 350, 0, 1, 1, DATE_SUB(NOW(), INTERVAL 35 DAY)),
('计算机网络协议详解', '网络协议系统讲解', 'document', 'professional', '计算机网络', 'https://cos.example.com/doc/cs_network.pdf', 'https://cos.example.com/cover/doc4.jpg', 10485760, NULL, 1200, 600, 0, 1, 1, DATE_SUB(NOW(), INTERVAL 12 DAY));

-- ================================================
-- 3. 学习计划测试数据
-- ================================================
USE studyapp4_plan;

-- 插入学习计划（匹配表结构：target_university, current_level, total_days, plan_content, completion_rate）
INSERT INTO `t_study_plan` (`user_id`, `target_university`, `target_major`, `exam_date`, `current_level`, `total_days`, `plan_content`, `completion_rate`, `status`, `create_time`) VALUES
(1, '清华大学', '计算机科学与技术', '2025-12-26', 3, 365, '{"stage":"基础阶段","subjects":["数学","专业课","英语","政治"]}', 16.44, 1, DATE_SUB(NOW(), INTERVAL 60 DAY)),
(2, '北京大学', '金融学', '2025-12-26', 4, 300, '{"stage":"强化阶段","subjects":["数学","专业课","英语","政治"]}', 25.00, 1, DATE_SUB(NOW(), INTERVAL 75 DAY)),
(3, '复旦大学', '软件工程', '2026-12-26', 2, 500, '{"stage":"基础阶段","subjects":["数学","专业课","英语","政治"]}', 8.00, 1, DATE_SUB(NOW(), INTERVAL 40 DAY));

-- 插入学习任务（为第一个用户创建任务）
INSERT INTO `t_study_task` (`user_id`, `plan_id`, `task_date`, `category`, `task_content`, `status`, `create_time`) VALUES
-- 今天的任务
(1, 1, CURDATE(), 'math', '上午：高等数学第1章复习 + 练习题20道', 0, NOW()),
(1, 1, CURDATE(), 'professional', '下午：数据结构线性表章节学习 + 代码实现', 0, NOW()),
(1, 1, CURDATE(), 'english', '晚上：英语单词背诵100个 + 阅读理解1篇', 0, NOW()),

-- 昨天的任务（已完成）
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'math', '上午：线性代数第1章学习', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'english', '下午：英语阅读理解训练', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'politics', '晚上：政治选择题练习50道', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 前天的任务（部分完成）
(1, 1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'math', '上午：概率论基础知识学习', 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'professional', '下午：计算机组成原理学习', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 用户2的任务
(2, 2, CURDATE(), 'math', '上午：数学三高数部分复习', 0, NOW()),
(2, 2, CURDATE(), 'professional', '下午：金融学专业课学习', 0, NOW());

-- ================================================
-- 4. 考试题库测试数据
-- ================================================
USE studyapp4_exam;

-- 插入题目（匹配表结构：type, category, content, options JSON格式）
INSERT INTO `t_question` (`type`, `category`, `content`, `options`, `answer`, `analysis`, `difficulty`, `year`, `view_count`, `correct_rate`, `status`, `create_time`) VALUES
-- 政治选择题
('single', 'politics', '马克思主义哲学的直接理论来源是（）', JSON_ARRAY('古希腊朴素唯物主义哲学', '17世纪英国唯物主义哲学', '18世纪法国唯物主义哲学', '19世纪德国古典哲学'), 'D', '马克思主义哲学的直接理论来源是德国古典哲学，主要是黑格尔的辩证法和费尔巴哈的唯物主义。', 2, 2024, 580, 75.5, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
('single', 'politics', '实践的最基本的形式是（）', JSON_ARRAY('处理社会关系的实践', '科学实验', '生产实践', '艺术创作'), 'C', '生产实践是人类最基本的实践活动，是其他一切实践活动的基础和前提。', 1, 2023, 620, 82.3, 1, DATE_SUB(NOW(), INTERVAL 12 DAY)),
('multiple', 'politics', '社会主义核心价值观在国家层面的价值要求是（）', JSON_ARRAY('富强', '民主', '文明', '和谐'), 'ABCD', '社会主义核心价值观在国家层面的价值要求包括：富强、民主、文明、和谐。', 1, 2024, 450, 68.9, 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- 英语选择题
('single', 'english', 'The company has _______ a new policy regarding remote work.', JSON_ARRAY('adapted', 'adopted', 'adjusted', 'admitted'), 'B', 'adopt表示"采用、采纳"，符合句意"公司采纳了关于远程工作的新政策"。adapt表示"适应"，adjust表示"调整"，admit表示"承认"。', 2, 2024, 890, 71.2, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('single', 'english', 'Despite the heavy rain, they decided to _______ with their original plan.', JSON_ARRAY('go ahead', 'go on', 'go over', 'go through'), 'A', 'go ahead with表示"继续进行、推进"，符合句意。go on继续，go over检查，go through经历。', 2, 2023, 720, 65.8, 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 数学选择题
('single', 'math', '设函数f(x)在x=0处连续,且lim(x→0)[f(x)/x]=1,则f(0)等于（）', JSON_ARRAY('1', '0', '-1', '不存在'), 'B', '因为f(x)在x=0处连续，所以lim(x→0)f(x)=f(0)。又因为lim(x→0)[f(x)/x]=1，所以lim(x→0)f(x)=lim(x→0)x=0，因此f(0)=0。', 3, 2024, 1200, 58.5, 1, DATE_SUB(NOW(), INTERVAL 15 DAY)),
('single', 'math', '设矩阵A为3阶方阵，|A|=2，则|2A|等于（）', JSON_ARRAY('4', '8', '16', '6'), 'C', '对于n阶方阵A，|kA|=k^n|A|。这里n=3，k=2，所以|2A|=2^3×2=16。', 2, 2023, 980, 62.3, 1, DATE_SUB(NOW(), INTERVAL 18 DAY)),
('single', 'math', '设随机变量X~N(2,4)，则P(X<0)等于（）', JSON_ARRAY('Φ(-1)', 'Φ(1)', '1-Φ(1)', 'Φ(-0.5)'), 'A', 'X~N(2,4)表示均值为2，方差为4（标准差为2）。标准化：(X-2)/2~N(0,1)，所以P(X<0)=P((X-2)/2<-1)=Φ(-1)。', 3, 2024, 850, 55.7, 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),

-- 专业课选择题（计算机）
('single', 'professional', '在顺序表中插入或删除一个元素，需要平均移动（）个元素', JSON_ARRAY('n/4', 'n/2', 'n', '(n-1)/2'), 'B', '在顺序表中插入或删除元素时，平均情况下需要移动表中一半的元素，即n/2个元素。', 2, 2024, 650, 70.5, 1, DATE_SUB(NOW(), INTERVAL 25 DAY)),
('single', 'professional', 'CPU中用来保存当前正在执行的指令的部件是（）', JSON_ARRAY('程序计数器PC', '指令寄存器IR', '累加器ACC', '地址寄存器AR'), 'B', '指令寄存器(IR)用来保存当前正在执行的指令。程序计数器(PC)保存下一条指令的地址。', 2, 2023, 580, 68.2, 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
('multiple', 'professional', '进程的状态包括（）', JSON_ARRAY('运行态', '就绪态', '阻塞态', '挂起态'), 'ABC', '进程的三种基本状态是：运行态、就绪态、阻塞态。挂起态是扩展状态，不属于基本状态。', 2, 2024, 720, 64.8, 1, DATE_SUB(NOW(), INTERVAL 22 DAY));

-- 插入答题记录
INSERT INTO `t_answer_record` (`user_id`, `question_id`, `user_answer`, `is_correct`, `time_spent`, `submit_time`) VALUES
(1, 1, 'D', 1, 45, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 2, 'C', 1, 38, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 3, 'ABC', 0, 62, DATE_SUB(NOW(), INTERVAL 2 DAY)),  -- 答错了
(1, 4, 'B', 1, 55, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 5, 'A', 1, 48, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 6, 'C', 0, 120, DATE_SUB(NOW(), INTERVAL 1 DAY)), -- 答错了
(1, 7, 'C', 0, 95, NOW()),  -- 答错了
(2, 1, 'D', 1, 50, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 4, 'B', 1, 60, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 6, 'B', 1, 115, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 插入错题本（自动收录答错的题目）
INSERT INTO `t_wrong_question` (`user_id`, `question_id`, `wrong_count`, `last_wrong_time`, `is_mastered`) VALUES
(1, 3, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), 0),  -- 政治多选题答错
(1, 6, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), 0),  -- 数学题答错
(1, 7, 1, NOW(), 0);  -- 数学题答错

-- ================================================
-- 5. 社区测试数据
-- ================================================
USE studyapp4_community;

-- 插入帖子（匹配表结构：author_id, is_official）
INSERT INTO `t_post` (`title`, `content`, `type`, `author_id`, `is_official`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`) VALUES
-- 官方资讯（is_official=1）
('2025年考研初试时间确定！', '## 2025年全国硕士研究生招生考试公告\n\n教育部发布通知，2025年全国硕士研究生招生考试初试时间为：**2025年12月26日至12月27日**（每天上午8:30-11:30，下午14:00-17:00）。\n\n### 考试科目安排\n- 12月26日上午：思想政治理论、管理类综合能力\n- 12月26日下午：外国语\n- 12月27日上午：业务课一\n- 12月27日下午：业务课二\n\n请各位考生提前做好准备！', 'news', 1, 1, 3500, 850, 128, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
('考研报名注意事项汇总', '## 2025年考研报名重要提醒\n\n### 网上报名时间\n- 预报名：2024年9月24日-27日\n- 正式报名：2024年10月5日-25日\n\n### 注意事项\n1. 必须在规定时间内完成报名\n2. 仔细核对个人信息\n3. 准确填写报考单位和专业\n4. 及时完成网上缴费', 'news', 1, 1, 2800, 620, 95, 1, DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- 用户经验分享（is_official=0）
('三个月从零基础到130+，我的数学逆袭之路', '## 背景\n我是一名跨考生，大学期间数学基础很差，高数只考了60分飘过。决定考研后，给自己定的目标是数学130+。\n\n## 复习方法\n### 第一阶段：打基础（2个月）\n- 每天看视频课3小时\n- 课后立即做配套习题\n- 整理错题本\n\n### 第二阶段：刷题强化（1个月）\n- 每天真题模拟2套\n- 总结题型和解题技巧\n- 重点攻克薄弱知识点\n\n## 最终成绩\n初试数学**135分**！证明方法对了，短期逆袭完全可行！', 'experience', 2, 0, 4200, 1580, 236, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('清华大学计算机考研经验分享', '## 个人情况\n本科双非，初试总分398分，成功上岸清华计算机。\n\n## 各科备考经验\n### 数学（140分）\n- 基础阶段：教材+基础班视频\n- 强化阶段：1000题+强化班\n- 冲刺阶段：真题+模拟题\n\n### 专业课（130分）\n- 数据结构：王道书+配套视频\n- 计算机组成：唐朔飞教材\n- 操作系统：王道书\n- 计算机网络：谢希仁教材\n\n### 英语（75分）\n- 单词：恋恋有词\n- 阅读：张剑黄皮书\n- 作文：王江涛作文书\n\n### 政治（53分）\n- 选择题：肖秀荣1000题\n- 大题：肖四肖八背诵', 'experience', 3, 0, 5800, 2350, 418, 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- 学习打卡
('Day 60 - 今日学习打卡', '✅ **今日完成：**\n- 上午：高等数学第3章复习\n- 下午：数据结构线性表代码实现\n- 晚上：英语单词100个 + 阅读1篇\n\n📊 **学习时长：** 9小时\n💪 **心得：** 今天状态不错，知识点掌握得比较扎实。继续加油！\n\n#考研加油 #每日打卡', 'check_in', 1, 0, 180, 45, 8, 1, NOW()),
('Day 75 - 坚持就是胜利', '✅ **今日完成：**\n- 数学三高数部分复习\n- 金融学专业课笔记整理\n- 英语作文模板背诵\n\n📊 **学习时长：** 8小时\n💪 **感悟：** 越来越接近考试，压力越来越大，但不能放弃！\n\n#考研倒计时 #冲刺阶段', 'check_in', 2, 0, 120, 32, 5, 1, NOW());

-- 插入评论
INSERT INTO `t_comment` (`post_id`, `user_id`, `parent_id`, `content`, `like_count`, `create_time`) VALUES
-- 对第1个帖子的评论
(1, 2, NULL, '终于等到官方通知了！开始最后冲刺！', 28, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(1, 3, NULL, '时间过得好快，还有不到一年了，加油！', 15, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(1, 4, 1, '一起加油！我们一定可以的！', 8, DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- 对第3个帖子的评论
(3, 1, NULL, '太励志了！我也是数学基础很差，给了我信心！', 45, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 4, NULL, '请问视频课看的是哪个老师的？', 22, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 2, 5, '我看的是汤家凤老师的，讲得很细致', 18, DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 对第4个帖子的评论
(4, 1, NULL, '学长太强了！双非逆袭清华，膜拜！', 68, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(4, 2, NULL, '专业课130怎么做到的？求详细经验！', 35, DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 插入点赞记录（使用通用表 t_like，target_type 区分类型）
INSERT INTO `t_like` (`user_id`, `target_type`, `target_id`, `create_time`) VALUES
-- 对帖子的点赞
(1, 'post', 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(1, 'post', 3, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 'post', 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(2, 'post', 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(2, 'post', 3, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 'post', 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(3, 'post', 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 对评论的点赞
(2, 'comment', 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(3, 'comment', 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(4, 'comment', 4, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 插入收藏记录（使用通用表 t_collect，target_type 区分类型）
INSERT INTO `t_collect` (`user_id`, `target_type`, `target_id`, `create_time`) VALUES
(1, 'post', 3, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 'post', 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(1, 'resource', 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(1, 'resource', 4, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(1, 'resource', 7, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(2, 'post', 3, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2, 'resource', 4, DATE_SUB(NOW(), INTERVAL 18 DAY)),
(3, 'post', 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(3, 'resource', 11, DATE_SUB(NOW(), INTERVAL 25 DAY));

-- ================================================
-- 测试数据导入完成
-- ================================================
-- 数据统计：
-- - 用户：5人
-- - 资源：14个（视频10个，文档4个）
-- - 学习计划：3个
-- - 学习任务：11个
-- - 题目：11道（单选8道，多选3道）
-- - 答题记录：10条
-- - 错题：3道
-- - 帖子：6个（官方2个，经验2个，打卡2个）
-- - 评论：8条
-- - 点赞：10个
-- - 收藏：9个
-- ================================================
