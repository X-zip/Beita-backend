# 贝塔驿站后端系统

## 项目简介（以下内容基于cursor生成）

贝塔驿站是一个基于Spring Boot的校园社交平台后端系统，为大学生提供校园生活服务、社交互动、任务管理等功能。系统采用前后端分离架构，提供RESTful API接口服务。

## 技术栈

### 后端技术
- **框架**: Spring Boot 2.5.10
- **数据库**: MySQL 5.7+
- **ORM**: MyBatis
- **模板引擎**: Thymeleaf
- **WebSocket**: Spring WebSocket
- **构建工具**: Maven

### 主要依赖
- **数据库连接**: mysql-connector-java
- **JSON处理**: fastjson, gson
- **HTTP客户端**: httpclient, unirest-java
- **文件上传**: 七牛云SDK
- **微信支付**: wechatpay-java
- **敏感词过滤**: sensitive-word
- **邮件服务**: spring-boot-starter-mail
- **工具库**: lombok, commons-lang3

## 项目结构

```
Beita-backend-master/
├── src/main/java/com/example/demo/
│   ├── controller/          # 控制器层
│   │   ├── BeitaController.java      # 主要业务控制器
│   │   ├── QuanziController.java     # 圈子功能控制器
│   │   ├── UserController.java       # 用户管理控制器
│   │   ├── WXController.java         # 微信相关控制器
│   │   ├── XiaoyuanController.java  # 校园功能控制器
│   │   └── WebController.java        # Web页面控制器
│   ├── service/             # 服务层
│   ├── dao/                 # 数据访问层
│   ├── model/               # 实体模型
│   └── websocket/           # WebSocket相关
├── src/main/resources/
│   ├── application.properties    # 配置文件
│   ├── static/                  # 静态资源
│   └── templates/               # 模板文件
├── beita.sql                   # 数据库脚本
└── pom.xml                     # Maven配置
```

## 功能特性

### 核心功能
- **用户管理**: 用户注册、登录、信息管理
- **校园社交**: 圈子功能、评论互动、点赞系统
- **任务系统**: 任务发布、接取、完成管理
- **内容管理**: 帖子发布、图片上传、内容审核
- **微信集成**: 微信登录、支付功能
- **实时通信**: WebSocket实时消息推送
- **敏感词过滤**: 内容安全审核
- **邮件通知**: 系统邮件推送

### 管理功能
- **用户管理**: 用户信息查看、权限管理
- **内容审核**: 帖子审核、评论管理
- **黑名单管理**: 违规用户处理
- **系统设置**: 基础配置管理
- **数据统计**: 用户活跃度、内容统计

## 环境要求

- **JDK**: 1.8+
- **MySQL**: 5.7+
- **Maven**: 3.6+
- **内存**: 建议2GB+

## 快速开始

### 1. 环境准备

确保已安装以下软件：
- JDK 1.8
- MySQL 5.7+
- Maven 3.6+

### 2. 数据库配置

1. 创建MySQL数据库：
```sql
CREATE DATABASE beita_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入数据库脚本：
```bash
mysql -u root -p beita_test < beita.sql
```

### 3. 配置文件修改

编辑 `src/main/resources/application.properties`：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/beita_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password

# 邮箱配置（可选）
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=your_email@qq.com
spring.mail.password=your_email_password
spring.mail.from=your_email@qq.com

# 七牛云配置（可选）
# 在相关工具类中配置七牛云参数
```

### 4. 编译运行

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

或者使用IDE直接运行 `BeitaApplication.java`

### 5. 访问应用

- **后端API**: http://localhost:8802
- **管理后台**: http://localhost:8802/admin

## API文档

### 主要接口

#### 用户相关
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/info` - 获取用户信息

#### 内容相关
- `POST /api/post/create` - 发布帖子
- `GET /api/post/list` - 获取帖子列表
- `POST /api/comment/add` - 添加评论

#### 圈子相关
- `POST /api/quanzi/join` - 加入圈子
- `GET /api/quanzi/list` - 获取圈子列表

## 部署说明

### 开发环境
```bash
# 开发模式运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 生产环境
```bash
# 打包
mvn clean package -Dmaven.test.skip=true

# 运行jar包
java -jar target/beita-0.0.1-SNAPSHOT.jar
```

### Docker部署
```dockerfile
FROM openjdk:8-jre-alpine
COPY target/beita-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8802
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 配置说明

### 数据库连接池配置
```properties
spring.datasource.hikari.max-lifetime=30000
spring.datasource.hikari.connection-timeout=10000
spring.datasource.hikari.maximum-pool-size=300
```

### 文件上传配置
```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=1000MB
```

### SSL证书配置
```properties
server.ssl.key-store=classpath:yqtech.ltd.jks
server.ssl.key-store-password=123456
```

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一异常处理
- 规范API返回格式

### 数据库设计
- 使用utf8mb4字符集
- 主键使用自增ID
- 时间字段使用bigint存储时间戳
- 软删除使用is_delete字段

### 安全考虑
- 敏感词过滤
- 用户权限控制
- 黑名单机制
- 内容审核

## 常见问题

### Q: 启动时数据库连接失败
A: 检查数据库配置是否正确，确保MySQL服务正常运行

### Q: 文件上传失败
A: 检查七牛云配置，确保AccessKey和SecretKey正确

### Q: 微信功能无法使用
A: 检查微信相关配置，确保AppID和AppSecret正确

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者: [您的姓名]
- 邮箱: [您的邮箱]
- 项目地址: [项目GitHub地址]

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 基础用户管理功能
- 校园社交功能
- 任务管理系统
- 微信集成功能

---

**注意**: 在生产环境部署前，请务必修改默认密码和敏感配置信息。
