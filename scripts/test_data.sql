-- =============================================
-- 考研学习小程序 测试数据
-- 根据 init.sql 表结构生成
-- =============================================

-- =============================================
-- 1. 用户服务数据 (studyapp4_user)
-- =============================================
USE studyapp4_user;

-- 用户表 t_user
-- current_level: 1-零基础 2-基础薄弱 3-基础一般 4-基础较好 5-基础优秀
INSERT INTO t_user (id, openid, unionid, nickname, avatar, phone, gender, target_university, target_major, exam_year, current_level, vip_status, vip_expire_time, total_study_time, continuous_days, create_time, update_time, deleted) VALUES
(1, 'wx_openid_001', 'wx_unionid_001', '考研小王', 'https://example.com/avatar/1.jpg', '13800138001', 1, '北京大学', '计算机科学与技术', 2025, 3, 1, '2025-12-31 23:59:59', 12500, 45, NOW(), NOW(), 0),
(2, 'wx_openid_002', 'wx_unionid_002', '学霸小李', 'https://example.com/avatar/2.jpg', '13800138002', 0, '清华大学', '软件工程', 2025, 5, 1, '2025-06-30 23:59:59', 28000, 120, NOW(), NOW(), 0),
(3, 'wx_openid_003', 'wx_unionid_003', '努力的小张', 'https://example.com/avatar/3.jpg', '13800138003', 1, '浙江大学', '人工智能', 2025, 2, 0, NULL, 5600, 15, NOW(), NOW(), 0),
(4, 'wx_openid_004', 'wx_unionid_004', '奋斗中的小陈', 'https://example.com/avatar/4.jpg', '13800138004', 0, '复旦大学', '金融学', 2026, 3, 0, NULL, 8900, 30, NOW(), NOW(), 0),
(5, 'wx_openid_005', 'wx_unionid_005', '冲刺985的小刘', 'https://example.com/avatar/5.jpg', '13800138005', 1, '上海交通大学', '电子信息工程', 2025, 4, 1, '2025-03-01 23:59:59', 15800, 60, NOW(), NOW(), 0);

-- =============================================
-- 2. 资源服务数据 (studyapp4_resource)
-- =============================================
USE studyapp4_resource;

-- 资源表 t_resource
INSERT INTO t_resource (id, title, type, category, sub_category, file_url, cover_url, duration, file_size, description, tags, difficulty, view_count, download_count, is_free, status, publisher_id, create_time, update_time, deleted) VALUES
-- 视频资源
(1, '考研政治马原核心考点精讲', 'video', 'politics', '马克思主义基本原理', 'https://cos.example.com/video/politics/mayuan_core.mp4', 'https://cos.example.com/cover/politics_1.jpg', 7200, 524288000, '全面解析马原核心知识点，重点突破唯物论、辩证法', '["马原","政治","核心考点"]', 3, 15680, 0, 1, 1, 1, NOW(), NOW(), 0),
(2, '考研政治毛中特重点串讲', 'video', 'politics', '毛泽东思想和中国特色社会主义理论', 'https://cos.example.com/video/politics/mzt_key.mp4', 'https://cos.example.com/cover/politics_2.jpg', 5400, 412000000, '毛中特核心要点总结，考前必看', '["毛中特","政治","冲刺"]', 3, 12350, 0, 0, 1, 1, NOW(), NOW(), 0),
(3, '考研英语一阅读理解技巧', 'video', 'english', '阅读理解', 'https://cos.example.com/video/english/reading_skills.mp4', 'https://cos.example.com/cover/english_1.jpg', 6000, 456000000, '阅读理解解题技巧全攻略，提分必备', '["英语一","阅读","技巧"]', 4, 23500, 0, 0, 1, 1, NOW(), NOW(), 0),
(4, '考研英语写作高分模板', 'video', 'english', '写作', 'https://cos.example.com/video/english/writing_template.mp4', 'https://cos.example.com/cover/english_2.jpg', 4800, 380000000, '写作高分模板及万能句型', '["英语","写作","模板"]', 2, 18900, 0, 1, 1, 1, NOW(), NOW(), 0),
(5, '考研数学一高等数学基础', 'video', 'math', '高等数学', 'https://cos.example.com/video/math/calculus_basic.mp4', 'https://cos.example.com/cover/math_1.jpg', 9000, 680000000, '高等数学零基础入门到精通', '["数学一","高数","基础"]', 2, 32100, 0, 1, 1, 1, NOW(), NOW(), 0),
(6, '考研数学线性代数强化', 'video', 'math', '线性代数', 'https://cos.example.com/video/math/linear_algebra.mp4', 'https://cos.example.com/cover/math_2.jpg', 7800, 590000000, '线性代数核心知识点强化训练', '["数学","线代","强化"]', 4, 19800, 0, 0, 1, 1, NOW(), NOW(), 0),

-- 文档资源
(7, '考研政治知识点思维导图', 'document', 'politics', '综合', 'https://cos.example.com/doc/politics/mindmap.pdf', 'https://cos.example.com/cover/doc_1.jpg', 0, 15000000, '政治四门课程思维导图合集', '["政治","思维导图","总结"]', 2, 28600, 15800, 1, 1, 1, NOW(), NOW(), 0),
(8, '考研英语真题词汇手册', 'document', 'english', '词汇', 'https://cos.example.com/doc/english/vocabulary.pdf', 'https://cos.example.com/cover/doc_2.jpg', 0, 8500000, '历年真题高频词汇整理', '["英语","词汇","真题"]', 3, 35200, 22100, 0, 1, 1, NOW(), NOW(), 0),
(9, '考研数学公式大全', 'document', 'math', '综合', 'https://cos.example.com/doc/math/formulas.pdf', 'https://cos.example.com/cover/doc_3.jpg', 0, 12000000, '数学一/二/三公式速查手册', '["数学","公式","速查"]', 2, 42000, 31500, 1, 1, 1, NOW(), NOW(), 0),
(10, '计算机408核心知识点', 'document', 'professional', '计算机', 'https://cos.example.com/doc/cs/408_core.pdf', 'https://cos.example.com/cover/doc_4.jpg', 0, 25000000, '408四门课程核心考点汇总', '["408","计算机","专业课"]', 4, 18500, 12300, 0, 1, 1, NOW(), NOW(), 0),

-- 题库资源
(11, '政治选择题1000道', 'question', 'politics', '选择题', 'https://cos.example.com/question/politics_1000.json', 'https://cos.example.com/cover/q_1.jpg', 0, 2500000, '政治客观题精选练习', '["政治","选择题","练习"]', 3, 25600, 18900, 0, 1, 1, NOW(), NOW(), 0),
(12, '英语阅读真题100篇', 'question', 'english', '阅读理解', 'https://cos.example.com/question/english_reading.json', 'https://cos.example.com/cover/q_2.jpg', 0, 5800000, '历年英语阅读真题精编', '["英语","阅读","真题"]', 4, 31200, 24500, 1, 1, 1, NOW(), NOW(), 0),
(13, '数学历年真题详解', 'question', 'math', '真题', 'https://cos.example.com/question/math_real.json', 'https://cos.example.com/cover/q_3.jpg', 0, 18000000, '2010-2024数学真题全解析', '["数学","真题","解析"]', 5, 28900, 21300, 0, 1, 1, NOW(), NOW(), 0),
(14, '408数据结构习题集', 'question', 'professional', '数据结构', 'https://cos.example.com/question/ds_practice.json', 'https://cos.example.com/cover/q_4.jpg', 0, 8000000, '数据结构经典习题500道', '["408","数据结构","习题"]', 4, 15600, 11200, 0, 1, 1, NOW(), NOW(), 0);

-- =============================================
-- 3. 学习计划服务数据 (studyapp4_plan)
-- =============================================
USE studyapp4_plan;

-- 学习计划表 t_study_plan
-- current_level: 1-5
INSERT INTO t_study_plan (id, user_id, target_university, target_major, exam_date, current_level, total_days, plan_content, status, completion_rate, create_time, update_time, deleted) VALUES
(1, 1, '北京大学', '计算机科学与技术', '2025-12-20', 3, 365, '{"daily_hours": 8, "subjects": [{"name": "政治", "weight": 0.15}, {"name": "英语", "weight": 0.25}, {"name": "数学", "weight": 0.30}, {"name": "专业课", "weight": 0.30}], "phases": [{"name": "基础阶段", "months": 4}, {"name": "强化阶段", "months": 4}, {"name": "冲刺阶段", "months": 3}]}', 1, 35.50, NOW(), NOW(), 0),
(2, 2, '清华大学', '软件工程', '2025-12-20', 5, 365, '{"daily_hours": 10, "subjects": [{"name": "政治", "weight": 0.15}, {"name": "英语", "weight": 0.20}, {"name": "数学", "weight": 0.35}, {"name": "专业课", "weight": 0.30}], "phases": [{"name": "基础阶段", "months": 3}, {"name": "强化阶段", "months": 5}, {"name": "冲刺阶段", "months": 3}]}', 1, 68.20, NOW(), NOW(), 0),
(3, 3, '浙江大学', '人工智能', '2025-12-20', 2, 365, '{"daily_hours": 6, "subjects": [{"name": "政治", "weight": 0.20}, {"name": "英语", "weight": 0.25}, {"name": "数学", "weight": 0.30}, {"name": "专业课", "weight": 0.25}], "phases": [{"name": "基础阶段", "months": 5}, {"name": "强化阶段", "months": 4}, {"name": "冲刺阶段", "months": 2}]}', 1, 12.80, NOW(), NOW(), 0);

-- 学习任务表 t_study_task
INSERT INTO t_study_task (id, plan_id, user_id, task_date, category, task_content, resource_ids, status, completion_time, study_duration, create_time, update_time, deleted) VALUES
-- 用户1的任务
(1, 1, 1, CURDATE(), 'math', '{"title": "高等数学-极限与连续", "description": "学习极限的定义、性质及计算方法", "estimated_hours": 3}', '5', 2, NOW(), 180, NOW(), NOW(), 0),
(2, 1, 1, CURDATE(), 'english', '{"title": "英语阅读Day1", "description": "完成2篇阅读理解真题", "estimated_hours": 2}', '3,12', 2, NOW(), 120, NOW(), NOW(), 0),
(3, 1, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'politics', '{"title": "马原-唯物论", "description": "学习物质与意识的关系", "estimated_hours": 2}', '1,7', 0, NULL, 0, NOW(), NOW(), 0),
(4, 1, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'professional', '{"title": "数据结构-线性表", "description": "复习顺序表和链表的基本操作", "estimated_hours": 3}', '14', 0, NULL, 0, NOW(), NOW(), 0),

-- 用户2的任务
(5, 2, 2, CURDATE(), 'math', '{"title": "线性代数-矩阵运算", "description": "矩阵的乘法、求逆、行列式计算", "estimated_hours": 4}', '6', 2, NOW(), 240, NOW(), NOW(), 0),
(6, 2, 2, CURDATE(), 'english', '{"title": "写作练习", "description": "完成一篇大作文", "estimated_hours": 2}', '4', 1, NULL, 60, NOW(), NOW(), 0),
(7, 2, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'politics', '{"title": "毛中特专题", "description": "新时代中国特色社会主义思想", "estimated_hours": 2}', '2', 0, NULL, 0, NOW(), NOW(), 0),

-- 用户3的任务
(8, 3, 3, CURDATE(), 'math', '{"title": "高数入门", "description": "函数与极限基础概念", "estimated_hours": 2}', '5', 1, NULL, 45, NOW(), NOW(), 0),
(9, 3, 3, CURDATE(), 'english', '{"title": "词汇背诵", "description": "背诵考研核心词汇50个", "estimated_hours": 1}', '8', 0, NULL, 0, NOW(), NOW(), 0),
(10, 3, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'politics', '{"title": "政治基础", "description": "了解考研政治考试大纲", "estimated_hours": 1}', '7', 0, NULL, 0, NOW(), NOW(), 0),
(11, 3, 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'professional', '{"title": "408概述", "description": "了解408四门课程框架", "estimated_hours": 2}', '10', 0, NULL, 0, NOW(), NOW(), 0);

-- =============================================
-- 4. 考试服务数据 (studyapp4_exam)
-- =============================================
USE studyapp4_exam;

-- 题目表 t_question
-- type: choice-选择题 judge-判断题 blank-填空题 essay-简答题
INSERT INTO t_question (id, type, category, chapter, knowledge_point, year, content, options, answer, analysis, difficulty, score, tags, view_count, correct_rate, status, create_time, update_time, deleted) VALUES
-- 政治选择题
(1, 'choice', 'politics', '马克思主义基本原理', '唯物论', 2024, '物质的唯一特性是（）', '["A. 运动性", "B. 客观实在性", "C. 可知性", "D. 永恒性"]', 'B', '物质的唯一特性是客观实在性，这是物质区别于意识的根本标志。运动性是物质的根本属性，不是唯一特性。', 2, 1, '["马原","唯物论","基础"]', 5680, 78.50, 1, NOW(), NOW(), 0),
(2, 'choice', 'politics', '马克思主义基本原理', '辩证法', 2024, '对立统一规律是唯物辩证法的实质和核心，这是因为（）', '["A. 它揭示了事物发展的方向和道路", "B. 它揭示了事物发展的源泉和动力", "C. 它揭示了事物发展的状态和过程", "D. 它揭示了事物发展的趋势和特征"]', 'B', '对立统一规律（矛盾规律）揭示了事物发展的源泉和动力，是唯物辩证法的实质和核心。A项是否定之否定规律的内容，C项是质量互变规律的内容。', 3, 1, '["马原","辩证法","核心"]', 4520, 65.30, 1, NOW(), NOW(), 0),
(3, 'blank', 'politics', '毛泽东思想和中国特色社会主义理论', '新时代思想', 2023, '新时代我国社会主要矛盾已经转化为人民日益增长的美好生活需要和______之间的矛盾。', '[]', '不平衡不充分的发展', '十九大报告指出，中国特色社会主义进入新时代，我国社会主要矛盾已经转化为人民日益增长的美好生活需要和不平衡不充分的发展之间的矛盾。', 2, 2, '["毛中特","新时代","主要矛盾"]', 6200, 82.40, 1, NOW(), NOW(), 0),

-- 英语题
(4, 'choice', 'english', '阅读理解', '主旨大意', 2024, 'What is the main idea of the passage?', '["A. The impact of technology on education", "B. The history of online learning", "C. The future of traditional classrooms", "D. The benefits of face-to-face teaching"]', 'A', '根据文章首段和尾段的主题句，全文主要讨论的是技术对教育的影响，包括正面和负面的方面。', 3, 2, '["英语一","阅读","主旨题"]', 8900, 56.80, 1, NOW(), NOW(), 0),
(5, 'choice', 'english', '阅读理解', '细节理解', 2024, 'According to the passage, which of the following is TRUE?', '["A. Online education will replace traditional education", "B. Technology has both advantages and disadvantages in education", "C. Students prefer online learning to classroom learning", "D. Teachers are against using technology in classrooms"]', 'B', '文中明确提到了技术在教育中既有优势（如便捷性、资源丰富）也有劣势（如缺乏互动、自律要求高），因此B选项正确。', 3, 2, '["英语一","阅读","细节题"]', 7650, 62.30, 1, NOW(), NOW(), 0),

-- 数学题
(6, 'choice', 'math', '高等数学', '极限', 2024, '求极限 lim(x→0) (sin x)/x = ?', '["A. 0", "B. 1", "C. ∞", "D. 不存在"]', 'B', '这是一个重要极限公式。可以用洛必达法则或泰勒展开证明：当x→0时，sin x ≈ x，因此极限为1。', 2, 4, '["高数","极限","重要极限"]', 12500, 85.60, 1, NOW(), NOW(), 0),
(7, 'essay', 'math', '高等数学', '导数', 2024, '求函数 f(x) = x³ + 2x² - 5x + 1 的导数。', '[]', "f'(x) = 3x² + 4x - 5", '根据导数的基本公式：(xⁿ)′ = nxⁿ⁻¹，对每一项分别求导：(x³)′ = 3x²，(2x²)′ = 4x，(-5x)′ = -5，(1)′ = 0。', 2, 6, '["高数","导数","基础"]', 9800, 78.20, 1, NOW(), NOW(), 0),
(8, 'choice', 'math', '线性代数', '矩阵', 2023, '设A为n阶方阵，则下列说法正确的是（）', '["A. |A|=0时，A无逆矩阵", "B. |A|≠0时，A无逆矩阵", "C. A的逆矩阵一定存在", "D. A的逆矩阵一定不存在"]', 'A', '方阵A可逆的充要条件是|A|≠0。当|A|=0时，A是奇异矩阵，不存在逆矩阵。', 3, 4, '["线代","矩阵","逆矩阵"]', 8600, 71.50, 1, NOW(), NOW(), 0),

-- 专业课题（408）
(9, 'choice', 'professional', '数据结构', '线性表', 2024, '在单链表中，增加头结点的目的是（）', '["A. 使单链表至少有一个结点", "B. 标识表结点中首结点的位置", "C. 方便运算的实现", "D. 说明单链表是线性表的链式存储"]', 'C', '增加头结点后，对链表第一个结点的操作与其他结点相同，不需要特殊处理，从而简化了算法实现。', 2, 2, '["408","数据结构","链表"]', 6500, 73.80, 1, NOW(), NOW(), 0),
(10, 'choice', 'professional', '数据结构', '排序', 2024, '在下列排序算法中，时间复杂度与初始数据无关的是（）', '["A. 冒泡排序", "B. 快速排序", "C. 归并排序", "D. 插入排序"]', 'C', '归并排序的时间复杂度始终为O(nlogn)，与初始数据状态无关。冒泡、插入排序最好情况O(n)，快速排序最坏情况O(n²)。', 3, 2, '["408","数据结构","排序"]', 7200, 58.60, 1, NOW(), NOW(), 0),
(11, 'choice', 'professional', '操作系统', '进程管理', 2023, '下列关于进程和线程的说法，正确的是（）', '["A. 进程是资源分配的基本单位，线程是调度的基本单位", "B. 线程是资源分配的基本单位，进程是调度的基本单位", "C. 进程和线程都是资源分配的基本单位", "D. 进程和线程都是调度的基本单位"]', 'A', '在现代操作系统中，进程是资源分配的基本单位，拥有独立的地址空间；线程是CPU调度的基本单位，同一进程的线程共享进程资源。', 3, 2, '["408","操作系统","进程"]', 5800, 66.40, 1, NOW(), NOW(), 0);

-- 答题记录表 t_answer_record
INSERT INTO t_answer_record (id, user_id, question_id, user_answer, is_correct, time_spent, submit_time, create_time) VALUES
-- 用户1的答题记录
(1, 1, 1, 'B', 1, 45, NOW(), NOW()),
(2, 1, 2, 'A', 0, 60, NOW(), NOW()),
(3, 1, 6, 'B', 1, 30, NOW(), NOW()),
(4, 1, 7, "f'(x) = 3x² + 4x - 5", 1, 120, NOW(), NOW()),
(5, 1, 9, 'C', 1, 55, NOW(), NOW()),

-- 用户2的答题记录
(6, 2, 1, 'B', 1, 35, NOW(), NOW()),
(7, 2, 2, 'B', 1, 50, NOW(), NOW()),
(8, 2, 6, 'B', 1, 25, NOW(), NOW()),
(9, 2, 8, 'A', 1, 45, NOW(), NOW()),
(10, 2, 10, 'C', 1, 70, NOW(), NOW()),

-- 用户3的答题记录
(11, 3, 1, 'A', 0, 80, NOW(), NOW()),
(12, 3, 6, 'C', 0, 65, NOW(), NOW());

-- 错题本表 t_wrong_question
INSERT INTO t_wrong_question (id, user_id, question_id, wrong_count, last_wrong_time, is_mastered, create_time, update_time) VALUES
(1, 1, 2, 2, NOW(), 0, NOW(), NOW()),
(2, 3, 1, 1, NOW(), 0, NOW(), NOW()),
(3, 3, 6, 1, NOW(), 0, NOW(), NOW());

-- =============================================
-- 5. 社区服务数据 (studyapp4_community)
-- =============================================
USE studyapp4_community;

-- 帖子表 t_post
INSERT INTO t_post (id, type, title, content, images, tags, author_id, view_count, like_count, comment_count, collect_count, is_top, is_official, status, create_time, update_time, deleted) VALUES
-- 官方资讯
(1, 'news', '2025年全国硕士研究生招生考试公告', '教育部发布2025年全国硕士研究生招生考试公告，报名时间为2024年10月5日至10月25日，初试时间为2024年12月21日至22日...', '["https://cos.example.com/post/news_1.jpg"]', '["考研公告","重要通知","2025考研"]', 1, 58600, 2350, 186, 1520, 1, 1, 1, NOW(), NOW(), 0),
(2, 'news', '考研数学大纲变化解读', '2025年考研数学大纲已发布，与去年相比主要变化如下：1.高等数学部分增加了...2.线性代数部分调整了...', '["https://cos.example.com/post/news_2.jpg", "https://cos.example.com/post/news_2_2.jpg"]', '["大纲解读","数学","变化"]', 1, 32500, 1580, 95, 890, 1, 1, 1, NOW(), NOW(), 0),

-- 用户经验分享
(3, 'experience', '三跨上岸北大计算机，我的备考经验分享', '本人本科双非，跨专业跨学校跨地区考研，最终成功上岸北京大学计算机专业。下面分享一下我的备考经验...\n\n一、为什么选择北大计算机\n二、各科复习规划\n三、心态调整方法\n四、复试准备建议', '["https://cos.example.com/post/exp_1.jpg"]', '["北大","计算机","三跨","经验"]', 2, 25800, 1890, 256, 2100, 0, 0, 1, NOW(), NOW(), 0),
(4, 'experience', '英语一从45到78，我的提分秘诀', '作为一个英语基础很差的理科生，从最初的45分提升到最终的78分，这个过程真的很艰难但也很值得。分享一下我的学习方法...', '[]', '["英语","提分","方法"]', 4, 18600, 1230, 145, 980, 0, 0, 1, NOW(), NOW(), 0),

-- 每日打卡
(5, 'checkin', '打卡Day45 | 今日学习8小时', '今天完成了高数极限部分的复习，做了50道选择题，正确率80%。继续加油！#考研打卡#', '[]', '["打卡","高数","学习记录"]', 1, 156, 23, 5, 2, 0, 0, 1, NOW(), NOW(), 0),
(6, 'checkin', '打卡Day120 | 政治开始二刷了', '政治选择题二刷进行中，感觉比第一遍清晰多了。马原部分的易错点整理完成。', '["https://cos.example.com/post/checkin_1.jpg"]', '["打卡","政治","二刷"]', 2, 89, 15, 3, 1, 0, 0, 1, NOW(), NOW(), 0);

-- 评论表 t_comment
INSERT INTO t_comment (id, post_id, parent_id, user_id, content, like_count, create_time, deleted) VALUES
-- 帖子1的评论
(1, 1, NULL, 2, '收到，已经开始准备报名材料了！', 35, NOW(), 0),
(2, 1, NULL, 3, '时间过得真快，又到报名季了', 18, NOW(), 0),
(3, 1, 1, 4, '同准备中，一起加油！', 12, NOW(), 0),

-- 帖子3的评论
(4, 3, NULL, 1, '太厉害了，三跨北大，膜拜大佬！', 89, NOW(), 0),
(5, 3, NULL, 3, '请问专业课是怎么准备的？', 45, NOW(), 0),
(6, 3, 5, 2, '专业课主要看王道四本书，然后刷真题，我在文章里有详细说', 28, NOW(), 0),
(7, 3, NULL, 5, '学长能分享一下复试的经验吗？', 33, NOW(), 0),

-- 帖子5的评论
(8, 5, NULL, 2, '坚持就是胜利，加油！', 8, NOW(), 0),
(9, 5, NULL, 3, '同打卡，我今天学了6小时', 5, NOW(), 0);

-- 点赞表 t_like
INSERT INTO t_like (id, target_type, target_id, user_id, create_time) VALUES
-- 帖子点赞
(1, 'post', 1, 2, NOW()),
(2, 'post', 1, 3, NOW()),
(3, 'post', 1, 4, NOW()),
(4, 'post', 3, 1, NOW()),
(5, 'post', 3, 3, NOW()),
(6, 'post', 3, 4, NOW()),
(7, 'post', 3, 5, NOW()),
(8, 'post', 5, 2, NOW()),

-- 评论点赞
(9, 'comment', 1, 3, NOW()),
(10, 'comment', 1, 4, NOW()),
(11, 'comment', 4, 1, NOW()),
(12, 'comment', 4, 3, NOW()),
(13, 'comment', 4, 5, NOW()),
(14, 'comment', 6, 1, NOW());

-- 收藏表 t_collect
INSERT INTO t_collect (id, target_type, target_id, user_id, create_time) VALUES
-- 帖子收藏
(1, 'post', 1, 2, NOW()),
(2, 'post', 1, 3, NOW()),
(3, 'post', 1, 4, NOW()),
(4, 'post', 1, 5, NOW()),
(5, 'post', 3, 1, NOW()),
(6, 'post', 3, 3, NOW()),
(7, 'post', 3, 4, NOW()),
(8, 'post', 3, 5, NOW()),
(9, 'post', 4, 1, NOW()),
(10, 'post', 4, 2, NOW()),
(11, 'post', 4, 5, NOW()),

-- 资源收藏（通过资源ID）
(12, 'resource', 5, 1, NOW()),
(13, 'resource', 9, 1, NOW());
