# 考研学习小程序 StudyApp4

## 项目简介

这是一个面向考研学生的学习小程序后端系统，提供资源管理、学习计划、题库练习、社区交流等核心功能。采用 Spring Cloud 微服务架构，支持高并发、高可用场景。

## 技术栈

- **后端框架**: Spring Boot 3.2.0 + Spring Cloud 2023.0.0
- **微服务组件**: Spring Cloud Alibaba 2023.0.0.0-RC1
  - Nacos: 服务注册与发现、配置中心
  - Gateway: API 网关、统一鉴权
  - OpenFeign: 服务间调用
- **数据库**: MySQL 8.0 + MyBatis-Plus 3.5.5
- **缓存**: Redis
- **消息队列**: RabbitMQ
- **对象存储**: 腾讯云 COS
- **认证**: JWT
- **工具库**: Hutool, Fastjson2

## 项目结构

```
studyapp4/
├── studyapp4-gateway/          # Spring Cloud Gateway 网关（端口 8080）
├── studyapp4-common/           # 公共模块（统一响应、异常处理、工具类）
├── studyapp4-user/             # 用户服务（端口 8081）
├── studyapp4-resource/         # 资源服务（端口 8082）
├── studyapp4-plan/             # 学习计划服务（端口 8083）
├── studyapp4-exam/             # 考试服务（端口 8084）
├── studyapp4-community/        # 社区服务（端口 8085）
├── studyapp4-admin/            # 后台管理服务（端口 8086）
└── scripts/                    # 数据库初始化脚本
    └── init.sql                # 完整建表语句（5个数据库，12张表）
```

## 核心功能

### 1. 用户服务 (studyapp4-user) ✅
- ✅ 微信小程序登录（wx.login + 后端换取 openid）
- ✅ JWT 认证（7天有效期）
- ✅ 用户信息管理
- ✅ 学习数据看板（打卡、连续打卡天数、累计学习时长）
- ✅ Redis 缓存用户信息

**核心 API**:
- `POST /api/v1/user/wx-login` - 微信登录
- `GET /api/v1/user/info` - 获取用户信息
- `POST /api/v1/user/check-in` - 每日打卡
- `POST /api/v1/user/study-time` - 添加学习时长
- `GET /api/v1/user/dashboard` - 学习数据看板

### 2. 资源服务 (studyapp4-resource) ✅
- ✅ 腾讯云 COS 集成（临时密钥上传）
- ✅ 资源分类管理（视频、文档）
- ✅ 上传凭证获取（STS 临时凭证）
- ✅ 资源列表分页查询
- ✅ RabbitMQ 视频转码队列

**核心 API**:
- `POST /api/v1/resource/upload-credential` - 获取上传凭证
- `POST /api/v1/resource` - 创建资源记录
- `GET /api/v1/resource/list` - 资源列表
- `GET /api/v1/resource/{id}` - 资源详情

### 3. 学习计划服务 (studyapp4-plan) ✅
- ✅ 个性化计划生成（基于目标院校、专业、考试日期）
- ✅ 学习任务管理（每日任务）
- ✅ 进度跟踪（完成率计算）
- ✅ RabbitMQ 异步计划生成
- ✅ 难度系数调整算法（985/211/双非）

**核心 API**:
- `POST /api/v1/plan` - 创建学习计划
- `GET /api/v1/plan` - 获取我的计划
- `GET /api/v1/plan/tasks/today` - 今日任务
- `POST /api/v1/plan/tasks/{taskId}/complete` - 完成任务

**算法特性**:
- 985 院校：政治20%, 英语30%, 数学30%, 专业课20%
- 211 院校：政治20%, 英语25%, 数学25%, 专业课30%
- 双非院校：政治20%, 英语25%, 数学25%, 专业课30%

### 4. 考试服务 (studyapp4-exam) ✅
- ✅ 题库管理（按科目、章节、知识点分类）
- ✅ 答题记录（自动判分）
- ✅ 错题本（自动收录）
- ✅ 智能练习推荐（知识点薄弱推荐）
- ✅ 真题年份标记

**核心 API**:
- `GET /api/v1/exam/questions` - 题库列表
- `POST /api/v1/exam/submit` - 提交答案
- `GET /api/v1/exam/wrong-questions` - 我的错题本
- `GET /api/v1/exam/recommend` - 智能推荐

**智能推荐算法**:
1. 统计用户各知识点错误率
2. 优先推荐错误率 > 30% 的知识点
3. 按正确率升序排序
4. 每次推荐 10 题

### 5. 社区服务 (studyapp4-community) ✅
- ✅ 资讯发布（需管理员审核）
- ✅ 经验分享（UGC 内容）
- ✅ 打卡功能（简短记录）
- ✅ 评论互动（支持二级评论）
- ✅ 点赞、收藏
- ✅ Redis 缓存热门帖子
- ✅ 浏览数去重（每人每天只计数一次）

**核心 API**:
- `POST /api/v1/community/post` - 发布帖子
- `GET /api/v1/community/post/list` - 帖子列表
- `GET /api/v1/community/post/{id}` - 帖子详情
- `POST /api/v1/community/post/{id}/like` - 点赞
- `POST /api/v1/community/comment` - 添加评论
- `GET /api/v1/community/my/collects` - 我的收藏

**内容类型**:
- `news`: 官方资讯（需审核）
- `experience`: 经验分享
- `checkin`: 每日打卡

### 6. 后台管理服务 (studyapp4-admin) ✅
- ✅ 用户管理（状态管理）
- ✅ 资源审核（视频、文档审核）
- ✅ 内容审核（帖子审核、置顶、精华）
- ✅ 数据统计（用户数、资源数、帖子数等）
- ✅ OpenFeign 调用各服务接口

**核心 API**:
- `GET /api/v1/admin/statistics` - 统计数据
- `GET /api/v1/admin/users` - 用户列表
- `PUT /api/v1/admin/resources/{id}/review` - 审核资源
- `PUT /api/v1/admin/posts/{id}/review` - 审核帖子
- `PUT /api/v1/admin/posts/{id}/top` - 置顶帖子

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- RabbitMQ 3.8+
- Nacos 2.2.0+

### 1. 数据库初始化

```bash
# 执行初始化脚本（自动创建 5 个数据库和 12 张表）
mysql -u root -p < scripts/init.sql
```

**数据库清单**:
- `studyapp4_user`: 用户信息
- `studyapp4_resource`: 学习资源
- `studyapp4_plan`: 学习计划、任务
- `studyapp4_exam`: 题库、答题记录、错题本
- `studyapp4_community`: 帖子、评论、点赞、收藏

### 2. 启动 Nacos

```bash
# 下载 Nacos 2.2.0+
# 启动 Nacos（单机模式）
cd nacos/bin
sh startup.sh -m standalone

# 访问控制台：http://localhost:8848/nacos
# 默认账号密码：nacos/nacos
```

### 3. 配置 Nacos

在 Nacos 控制台添加配置（命名空间: dev）:

**微信小程序配置** (studyapp4-user-dev.yml):
```yaml
wechat:
  appid: wx1234567890abcdef
  secret: your_wechat_secret
```

**腾讯云 COS 配置** (studyapp4-resource-dev.yml):
```yaml
tencent:
  cos:
    secret-id: your_tencent_secret_id
    secret-key: your_tencent_secret_key
    bucket: studyapp4-1234567890
    region: ap-guangzhou
```

### 4. 启动 Redis & RabbitMQ

```bash
# Redis
redis-server

# RabbitMQ
rabbitmq-server

# RabbitMQ 管理界面：http://localhost:15672
# 默认账号密码：guest/guest
```

### 5. 启动微服务

**方式一：逐个启动**
```bash
# 1. 启动网关
cd studyapp4-gateway
mvn spring-boot:run

# 2. 启动用户服务
cd studyapp4-user
mvn spring-boot:run

# 3. 启动其他服务（resource, plan, exam, community, admin）
```

**方式二：批量编译**
```bash
# 在项目根目录
mvn clean package -DskipTests

# 启动各服务
java -jar studyapp4-gateway/target/studyapp4-gateway-1.0.0.jar
java -jar studyapp4-user/target/studyapp4-user-1.0.0.jar
# ...
```

### 6. 验证服务注册

访问 Nacos 控制台查看服务列表：http://localhost:8848/nacos

应看到以下服务：
- studyapp4-gateway
- studyapp4-user
- studyapp4-resource
- studyapp4-plan
- studyapp4-exam
- studyapp4-community
- studyapp4-admin

## 服务端口

| 服务 | 端口 | 数据库 | Redis DB |
|------|------|--------|----------|
| Gateway | 8080 | - | - |
| User | 8081 | studyapp4_user | 0 |
| Resource | 8082 | studyapp4_resource | 1 |
| Plan | 8083 | studyapp4_plan | 2 |
| Exam | 8084 | studyapp4_exam | 3 |
| Community | 8085 | studyapp4_community | 4 |
| Admin | 8086 | studyapp4_user | 5 |

## API 文档

### 统一响应格式

```json
{
  "code": 200,
  "message": "成功",
  "data": {},
  "timestamp": 1702188000
}
```

**状态码说明**:
- `200`: 成功
- `400`: 参数错误
- `401`: 未认证
- `403`: 无权限
- `404`: 资源不存在
- `500`: 服务器错误

### 认证方式

所有需要登录的接口，在请求头中携带：

```
Authorization: Bearer {jwt_token}
```

网关会自动解析 JWT，并在请求头中添加 `X-User-Id` 传递给下游服务。

### 分页响应格式

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "total": 100,
    "current": 1,
    "pages": 10,
    "size": 10,
    "records": []
  }
}
```

## 开发规范

### 代码规范

- Controller 层使用 `@RestController`
- 路径前缀统一为 `/api/v1/{module}`
- 使用 `@Valid` 进行参数校验
- Service 接口 + 实现类分离
- 使用 Lombok 简化代码

### 数据库规范

- 表名统一前缀 `t_`
- 主键使用 `id`，自增
- 逻辑删除字段 `deleted` (0=未删除, 1=已删除)
- 时间字段 `create_time`, `update_time` (自动填充)

### Redis 规范

- Key 命名格式：`{module}:{function}:{id}`
- 不同服务使用不同的 database（避免 Key 冲突）
- 热点数据缓存 30 分钟

### 命名规范

- 请求 DTO: `XxxRequest`
- 响应 VO: `XxxVO` 或 `XxxResponse`
- Mapper 接口: `XxxMapper`
- Service 接口: `XxxService`
- Service 实现: `XxxServiceImpl`

## 核心组件说明

### 1. Gateway 网关

**功能**:
- 统一路由（路由到各微服务）
- JWT 认证（解析 token，提取 userId）
- 跨域配置（CORS）
- 限流（未实现，可扩展）

**路由规则**:
```yaml
/api/v1/user/**     → studyapp4-user
/api/v1/resource/** → studyapp4-resource
/api/v1/plan/**     → studyapp4-plan
/api/v1/exam/**     → studyapp4-exam
/api/v1/community/** → studyapp4-community
/api/v1/admin/**    → studyapp4-admin
```

### 2. Common 公共模块

**组件**:
- `Result<T>`: 统一响应封装
- `PageResult<T>`: 分页响应封装
- `BusinessException`: 业务异常
- `GlobalExceptionHandler`: 全局异常处理
- `JwtUtil`: JWT 工具类
- `CommonConstants`: 公共常量
- `RabbitMQConstants`: RabbitMQ 常量

### 3. RabbitMQ 队列设计

| 队列名 | Exchange | 用途 |
|--------|----------|------|
| studyapp4.resource.video.transcode | studyapp4.resource.exchange | 视频转码任务 |
| studyapp4.plan.generate | studyapp4.plan.exchange | 学习计划生成 |
| studyapp4.exam.analysis | studyapp4.exam.exchange | 答题数据分析 |

**特性**:
- 持久化队列
- 死信队列（DLX）
- 失败重试 3 次

## 配置说明

### JWT 配置

```yaml
jwt:
  secret: your_jwt_secret_key_min_256_bits
  expire-time: 604800000  # 7天（单位：毫秒）
```

### Nacos 配置

需要在 Nacos 配置中心添加以下配置：

#### 微信小程序配置 (studyapp4-user-dev.yml)
```yaml
wechat:
  appid: wx1234567890abcdef
  secret: your_wechat_secret_from_mp_admin
```

#### 腾讯云 COS 配置 (studyapp4-resource-dev.yml)
```yaml
tencent:
  cos:
    secret-id: AKIDxxxxxxxxxxxxxxxxxxxxx
    secret-key: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    bucket: studyapp4-1234567890
    region: ap-guangzhou  # 广州
    domain: https://studyapp4-1234567890.cos.ap-guangzhou.myqcloud.com
```

## 测试接口

### 1. 微信登录测试

```bash
curl -X POST http://localhost:8080/api/v1/user/wx-login \
  -H "Content-Type: application/json" \
  -d '{"code": "test_wx_code"}'
```

### 2. 获取用户信息

```bash
curl -X GET http://localhost:8080/api/v1/user/info \
  -H "Authorization: Bearer {your_jwt_token}"
```

### 3. 获取资源列表

```bash
curl -X GET "http://localhost:8080/api/v1/resource/list?pageNum=1&pageSize=10"
```

### 4. 获取帖子列表

```bash
curl -X GET "http://localhost:8080/api/v1/community/post/list?type=experience&pageNum=1&pageSize=10"
```

## 常见问题

### 1. Nacos 连接失败

检查 Nacos 是否启动：
```bash
ps -ef | grep nacos
```

### 2. MySQL 连接失败

检查数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/studyapp4_user?...
    username: root
    password: your_password
```

### 3. JWT 认证失败

检查 token 是否有效：
- token 是否过期（7天）
- JWT secret 配置是否一致

## 待实现功能

- [ ] 微信支付集成（VIP 会员）
- [ ] AI 学习助手（基于 OpenAI API 答疑）
- [ ] 直播课堂（WebRTC 或云服务商方案）
- [ ] 数据导出（学习报告 PDF 生成）
- [ ] 接口文档（Swagger / Knife4j）
- [ ] 容器化部署（Docker Compose）
- [ ] CI/CD 流水线（GitHub Actions）

## 许可证

MIT License

---

**开发者**: AI Coding Assistant  
**版本**: v1.0.0  
**最后更新**: 2025-01-15
