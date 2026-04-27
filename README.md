# LeadNews 微服务内容平台

## 1. 项目简介

`LeadNews` 是一个基于 Spring Cloud Alibaba 的内容平台后端项目，采用多模块 Maven + 微服务架构，覆盖了资讯分发场景中的核心能力：

- 用户体系（登录、关系链）
- 文章服务（发布、详情、静态化、热点计算）
- 行为服务（阅读、点赞、收藏、关注等）
- 评论服务
- 搜索服务（ES 检索与联想）
- 自媒体后台（素材、内容发布、审核流程）
- 网关鉴权与路由转发
- 定时任务与延迟任务调度

该项目适合用于面试中展示你在 **微服务拆分、异步消息、搜索、内容审核、网关鉴权、工程化组织** 方面的综合能力。

## 2. 技术栈

- Java 8
- Spring Boot 2.3.9.RELEASE
- Spring Cloud Hoxton.SR10
- Spring Cloud Alibaba 2.2.5.RELEASE
- MyBatis / MyBatis-Plus
- Nacos（注册中心 + 配置中心）
- Kafka（消息与流处理）
- Redis
- Elasticsearch
- MongoDB（部分搜索/历史场景）
- MinIO（对象存储）
- XXL-Job（任务调度）

## 3. 项目结构

根聚合工程：`leadnews`

- `leadnews-leadnews-common`：通用组件（异常、常量、Redis、Swagger、审核工具等）
- `leadnews-leadnews-utils`：工具类（JWT、加密、线程上下文、序列化等）
- `leadnews-leadnews-model`：统一 DTO / VO / POJO 模型层
- `leadnews-leadnews-feign-api`：Feign 接口定义层
- `leadnews-leadnews-basic`：基础 Starter（如文件上传 Starter）
- `leadnews-leadnews-service`：业务微服务集合
- `leadnews-leadnews-gateway`：网关服务集合
- `leadnews-leadnews-test`：各类技术验证 Demo（Kafka/ES/MinIO/Mongo/XXLJob 等）

### 3.1 业务微服务

位于 `leadnews-leadnews-service`：

- `leadnews-leadnews-user`（端口 `51801`）
- `leadnews-leadnews-article`（端口 `51802`）
- `leadnews-leadnews-wemedia`（端口 `51803`）
- `leadnews-leadnews-search`（端口 `51804`）
- `leadnews-leadnews-behavior`（端口 `51805`）
- `leadnews-leadnews-comment`（端口 `51806`）
- `leadnews-leadnews-schedule`（默认端口 `51701`）

### 3.2 网关服务

位于 `leadnews-leadnews-gateway`：

- `leadnews-leadnews-app-gateway`（端口 `51601`）
- `leadnews-leadnews-wemedia-gateway`（端口 `51602`）

## 4. 本地运行前准备

请先准备以下中间件（建议 Docker 方式）：

- Nacos（默认配置中示例地址为 `192.168.200.130:8848`）
- MySQL
- Redis
- Kafka
- Elasticsearch
- MongoDB（部分能力依赖）
- MinIO（素材上传场景）

说明：

- 当前项目大量服务使用 `bootstrap.yml` 从 Nacos 拉取配置。
- 你需要在 Nacos 中准备对应 Data ID（数据库、Redis、Kafka、ES 等连接配置）。
- 若你使用本机环境，请将 `bootstrap.yml` 的 Nacos 地址替换为本机可访问地址。

## 5. 编译与启动

### 5.1 编译

在根目录执行：

```bash
mvn clean install -DskipTests
```

### 5.2 推荐启动顺序

1. 启动基础中间件（Nacos / MySQL / Redis / Kafka / ES / MinIO）
2. 启动调度服务：`leadnews-leadnews-schedule`
3. 启动核心业务：`user -> article -> behavior -> comment -> search -> wemedia`
4. 启动网关：`app-gateway`、`wemedia-gateway`

## 6. 面试可讲亮点（建议）

- 微服务拆分边界：用户域、内容域、行为域、搜索域、自媒体域
- 鉴权链路：网关 + 服务拦截器 + ThreadLocal 用户上下文
- 内容处理链路：发布 -> 审核 -> 入库 -> 索引/静态化 -> 分发
- 热点计算：行为打分 + 定时任务 + 缓存
- 搜索链路：ES 索引同步与查询接口设计
- 工程组织：`common + utils + model + feign-api + service + gateway` 分层

## 7. 当前仓库说明（已去除原品牌标识）

本仓库已完成品牌字样替换，可直接作为你个人项目包装与展示基础。

如果你还希望进一步提升“面试观感”，建议下一步继续做：

- 统一模块命名（例如将 `leadnews-leadnews-*` 精简为 `leadnews-*`）
- 统一 Maven 坐标与包路径命名规范
- 增加一份架构图（服务依赖、调用关系、消息流）
- 补充接口文档与压测/监控指标

## 8. 常见问题

- 服务启动后注册不到 Nacos：优先检查 `bootstrap.yml` 的 `server-addr`
- 业务接口报数据库连接错误：检查 Nacos 中 Data ID 与数据库配置
- 消息消费未触发：检查 Kafka topic、consumer group 与 broker 连通性
- 搜索无结果：检查 ES 索引初始化与同步消息是否正常

---

如果你需要，我可以继续帮你做第二步：

1. 统一重命名模块目录为 `leadnews-*`
2. 自动修复所有 `pom.xml` 的父子依赖与模块路径
3. 验证一次 `mvn clean install` 可编译通过
