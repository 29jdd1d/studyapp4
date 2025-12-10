-- ================================================
-- 考研学习小程序数据库初始化脚本
-- ================================================

-- 创建用户数据库
CREATE DATABASE IF NOT EXISTS studyapp4_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建资源数据库
CREATE DATABASE IF NOT EXISTS studyapp4_resource DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建学习计划数据库
CREATE DATABASE IF NOT EXISTS studyapp4_plan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建题库数据库
CREATE DATABASE IF NOT EXISTS studyapp4_exam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建社区数据库
CREATE DATABASE IF NOT EXISTS studyapp4_community DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE studyapp4_user;

-- ================================================
-- 用户表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(128) NOT NULL COMMENT '微信openid',
  `unionid` VARCHAR(128) DEFAULT NULL COMMENT '微信unionid',
  `nickname` VARCHAR(100) NOT NULL COMMENT '用户昵称',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `gender` TINYINT DEFAULT 0 COMMENT '性别（0-未知 1-男 2-女）',
  `target_university` VARCHAR(100) DEFAULT NULL COMMENT '目标院校',
  `target_major` VARCHAR(100) DEFAULT NULL COMMENT '目标专业',
  `exam_year` INT DEFAULT NULL COMMENT '考试年份',
  `current_level` TINYINT DEFAULT NULL COMMENT '当前基础（1-零基础 2-基础薄弱 3-基础一般 4-基础较好 5-基础优秀）',
  `vip_status` TINYINT DEFAULT 0 COMMENT '会员状态（0-普通用户 1-VIP）',
  `vip_expire_time` DATETIME DEFAULT NULL COMMENT '会员过期时间',
  `total_study_time` INT DEFAULT 0 COMMENT '累计学习时长（分钟）',
  `continuous_days` INT DEFAULT 0 COMMENT '连续打卡天数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_phone` (`phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

USE studyapp4_resource;

-- ================================================
-- 资源表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_resource` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `title` VARCHAR(200) NOT NULL COMMENT '资源标题',
  `type` VARCHAR(20) NOT NULL COMMENT '资源类型（video-视频 document-文档 question-题目）',
  `category` VARCHAR(50) NOT NULL COMMENT '分类（politics-政治 english-英语 math-数学 professional-专业课）',
  `sub_category` VARCHAR(100) DEFAULT NULL COMMENT '子分类（章节）',
  `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件URL',
  `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '封面图URL',
  `duration` INT DEFAULT NULL COMMENT '时长（秒，仅视频）',
  `file_size` BIGINT DEFAULT NULL COMMENT '文件大小（字节）',
  `description` TEXT DEFAULT NULL COMMENT '资源描述',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（JSON数组）',
  `difficulty` TINYINT DEFAULT 1 COMMENT '难度（1-5）',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `is_free` TINYINT DEFAULT 1 COMMENT '是否免费（0-付费 1-免费）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-下架 1-上架）',
  `publisher_id` BIGINT NOT NULL COMMENT '发布者ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type_category` (`type`, `category`),
  KEY `idx_publisher` (`publisher_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源表';

USE studyapp4_plan;

-- ================================================
-- 学习计划表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_study_plan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `target_university` VARCHAR(100) NOT NULL COMMENT '目标院校',
  `target_major` VARCHAR(100) NOT NULL COMMENT '目标专业',
  `exam_date` DATE NOT NULL COMMENT '考试日期',
  `current_level` TINYINT NOT NULL COMMENT '当前基础',
  `total_days` INT NOT NULL COMMENT '总天数',
  `plan_content` TEXT NOT NULL COMMENT '计划内容（JSON）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-已完成 1-进行中 2-已放弃）',
  `completion_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成率',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习计划表';

-- ================================================
-- 学习任务表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_study_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `plan_id` BIGINT NOT NULL COMMENT '计划ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `task_date` DATE NOT NULL COMMENT '任务日期',
  `category` VARCHAR(50) NOT NULL COMMENT '科目分类',
  `task_content` TEXT NOT NULL COMMENT '任务内容（JSON）',
  `resource_ids` VARCHAR(500) DEFAULT NULL COMMENT '关联资源ID（JSON数组）',
  `status` TINYINT DEFAULT 0 COMMENT '状态（0-未开始 1-进行中 2-已完成）',
  `completion_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `study_duration` INT DEFAULT 0 COMMENT '学习时长（分钟）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_user_date` (`user_id`, `task_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习任务表';

USE studyapp4_exam;

-- ================================================
-- 题目表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `type` VARCHAR(20) NOT NULL COMMENT '题目类型（choice-选择题 judge-判断题 blank-填空题 essay-简答题）',
  `category` VARCHAR(50) NOT NULL COMMENT '科目分类',
  `chapter` VARCHAR(100) DEFAULT NULL COMMENT '章节',
  `knowledge_point` VARCHAR(200) DEFAULT NULL COMMENT '知识点',
  `year` INT DEFAULT NULL COMMENT '年份（真题）',
  `content` TEXT NOT NULL COMMENT '题目内容',
  `options` TEXT DEFAULT NULL COMMENT '选项（JSON，仅选择题）',
  `answer` TEXT NOT NULL COMMENT '答案',
  `analysis` TEXT DEFAULT NULL COMMENT '解析',
  `difficulty` TINYINT DEFAULT 1 COMMENT '难度（1-5）',
  `score` INT DEFAULT 1 COMMENT '分值',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（JSON数组）',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `correct_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-下架 1-上架）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_category_chapter` (`category`, `chapter`),
  KEY `idx_knowledge_point` (`knowledge_point`),
  KEY `idx_year` (`year`),
  KEY `idx_difficulty` (`difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- ================================================
-- 答题记录表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_answer_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `question_id` BIGINT NOT NULL COMMENT '题目ID',
  `user_answer` TEXT DEFAULT NULL COMMENT '用户答案',
  `is_correct` TINYINT DEFAULT 0 COMMENT '是否正确（0-错误 1-正确）',
  `time_spent` INT DEFAULT 0 COMMENT '用时（秒）',
  `submit_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`),
  KEY `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='答题记录表';

-- ================================================
-- 错题本表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_wrong_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `question_id` BIGINT NOT NULL COMMENT '题目ID',
  `wrong_count` INT DEFAULT 1 COMMENT '错误次数',
  `last_wrong_time` DATETIME NOT NULL COMMENT '最后错误时间',
  `is_mastered` TINYINT DEFAULT 0 COMMENT '是否掌握（0-未掌握 1-已掌握）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_question` (`user_id`, `question_id`),
  KEY `idx_user_mastered` (`user_id`, `is_mastered`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错题本表';

USE studyapp4_community;

-- ================================================
-- 帖子表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `type` VARCHAR(20) NOT NULL COMMENT '类型（news-资讯 experience-经验 checkin-打卡）',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `images` TEXT DEFAULT NULL COMMENT '图片URL（JSON数组）',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（JSON数组）',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT DEFAULT 0 COMMENT '评论数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶（0-否 1-是）',
  `is_official` TINYINT DEFAULT 0 COMMENT '是否官方发布（0-否 1-是）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-待审核 1-已发布 2-已下架）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type_status` (`type`, `status`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- ================================================
-- 评论表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` BIGINT NOT NULL COMMENT '帖子ID',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID（二级评论）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除 1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ================================================
-- 点赞表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型（post-帖子 comment-评论）',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_target_user` (`target_type`, `target_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞表';

-- ================================================
-- 收藏表
-- ================================================
CREATE TABLE IF NOT EXISTS `t_collect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型（post-帖子 resource-资源）',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_target_user` (`target_type`, `target_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';
