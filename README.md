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

