# 考研学习小程序 - AI 编码助手指南

## 项目概述
这是一个面向考研学生的学习小程序，提供资源管理、学习计划、题库练习、社区交流等核心功能。

**技术栈**：
- 后端：Spring Cloud Alibaba + Spring Boot 3.x + MyBatis-Plus
- 微服务组件：Nacos（注册中心/配置中心）+ OpenFeign（服务调用）+ Gateway（网关）
- 数据库：MySQL 8.0
- 缓存：Redis
- 消息队列：RabbitMQ
- 对象存储：腾讯云 COS（视频、文档资源）
- 小程序端：微信小程序原生框架

## 架构设计

### 核心模块划分
```
studyapp4/
├── studyapp4-gateway/          # Spring Cloud Gateway 网关（路由、鉴权、限流）
├── studyapp4-common/           # 公共模块（工具类、通用响应、常量）
├── studyapp4-user/             # 用户服务（微信登录、个人信息、学习数据）
├── studyapp4-resource/         # 资源服务（视频、文档、题库资源管理）
├── studyapp4-plan/             # 学习计划服务（个性化推荐、进度跟踪）
├── studyapp4-exam/             # 考试服务（答题、错题本、智能练习）
├── studyapp4-community/        # 社区服务（资讯、打卡、评论）
└── studyapp4-admin/            # 后台管理服务（统一管理入口）
```

### 微服务通信
- **服务注册与发现**：所有服务启动时注册到 Nacos
- **服务间调用**：使用 OpenFeign，接口定义在各模块的 `api` 包中
  - 示例：`studyapp4-user` 提供 `UserFeignClient` 供其他服务调用
- **配置管理**：敏感配置（微信密钥、COS密钥、数据库密码）统一存储在 Nacos 配置中心
- **消息异步**：使用 RabbitMQ，Exchange 命名规范 `studyapp4.{module}.exchange`

### 数据库设计要点
- **用户表** (`t_user`)：微信 openid、unionid、学习目标（院校、专业）
- **资源表** (`t_resource`)：支持多类型（video/document/question），分类字段（科目、章节）
- **学习计划** (`t_study_plan`)：关联用户、目标、动态调整
- **题库** (`t_question`)：知识点标签、难度分级、年份标记
- **错题本** (`t_wrong_question`)：自动收录、关联原题
- **社区内容** (`t_post`)：类型区分（资讯/经验/打卡）

## 开发约定

### 命名规范
- **Controller 层**：使用 `@RestController`，路径前缀 `/api/v1/{module}`
  - 示例：`/api/v1/user/login`, `/api/v1/exam/submit`
- **Service 层**：接口 + 实现类分离（`XxxService` + `XxxServiceImpl`）
- **Entity**：数据库表实体类使用 `@TableName` 注解，统一前缀 `t_`
- **DTO/VO**：请求用 `XxxRequest`，响应用 `XxxResponse` 或 `XxxVO`

### 微信小程序集成
- **登录流程**：
  1. 小程序调用 `wx.login()` 获取 code
  2. 后端 `/api/v1/user/wx-login` 接收 code，调用微信 API 换取 openid/session_key
  3. 生成 JWT token 返回给小程序
- **配置**：微信 AppID/Secret 存储在 Nacos 配置中心

### 腾讯云 COS 对象存储
- **用途**：存储视频、文档、图片等学习资源
- **上传流程**：
  1. 前端请求后端获取临时上传凭证（STS）
  2. 前端直传 COS，完成后回调后端记录资源信息
- **文件命名**：`{module}/{year}/{month}/{uuid}.{ext}`
  - 示例：`resource/2025/12/abc123.mp4`
- **配置**：COS SecretId/SecretKey/Bucket/Region 存储在 Nacos

### 通用响应格式
```java
{
  "code": 200,        // 业务状态码
  "message": "成功",
  "data": {...},      // 业务数据
  "timestamp": 1702188000
}
```

### 分页查询标准
- 使用 MyBatis-Plus 的 `Page<T>` 对象
- 请求参数：`pageNum`（从 1 开始）、`pageSize`（默认 10）
- 响应字段：`total`、`pages`、`current`、`records`

### 学习计划生成逻辑
- **输入**：目标院校、专业、考试日期、当前基础
- **算法**：
  1. 根据目标院校难度系数调整学习强度
  2. 按科目（政治/英语/数学/专业课）分配时间权重
  3. 生成每日/每周任务清单，关联到具体资源
- **动态调整**：每周根据完成度自动优化后续计划

### RabbitMQ 消息队列
- **队列设计**：
  - `studyapp4.resource.video.transcode`：视频转码任务
  - `studyapp4.plan.generate`：学习计划生成任务
  - `studyapp4.exam.analysis`：答题数据分析任务
- **Exchange 类型**：Topic，支持灵活路由
- **重试机制**：失败任务自动重试 3 次，超时进入死信队列

### 题库刷题功能
- **分类维度**：科目 > 章节 > 知识点、年份（真题）
- **智能练习**：优先推送错题相关知识点、薄弱环节
- **答题记录**：保存用户答案、用时、正确率，用于数据分析

### 社区交流模块
- **内容类型**：
  - 资讯（官方发布）：需管理员审核
  - 经验分享（用户 UGC）：支持富文本、图片
  - 打卡（每日学习记录）：简短文本 + 标签
- **互动**：点赞、评论（二级评论）、收藏

## 关键命令

### 本地开发
```bash
# 启动后端服务（需先启动 MySQL/Redis）
mvn spring-boot:run -Dspring.profiles.active=dev

# 运行单元测试
mvn test

# 构建生产包
mvn clean package -Dmaven.test.skip=true
```

### 数据库管理
```bash
# 直接执行 SQL 脚本（开发环境）
mysql -u root -p studyapp4 < scripts/init.sql

# 生成 MyBatis-Plus 代码
# 配置在 src/test/java/.../CodeGenerator.java
```

## 注意事项

1. **敏感信息保护**：微信密钥、数据库密码使用 Nacos 配置中心，禁止硬编码
2. **文件上传限制**：视频单文件 ≤ 500MB，文档 ≤ 50MB，配置在 Gateway 网关
3. **缓存策略**：热门资源、学习计划使用 Redis 缓存，过期时间 30 分钟
4. **异步处理**：视频转码、计划生成使用 RabbitMQ 消息队列
5. **日志规范**：关键操作（登录/支付/内容发布）使用 INFO 级别，异常用 ERROR + 堆栈

## 待实现功能（优先级）
- [ ] 微信支付集成（VIP 会员）
- [ ] AI 学习助手（基于 OpenAI API 答疑）
- [ ] 直播课堂（WebRTC 或云服务商方案）
- [ ] 数据导出（学习报告 PDF 生成）
