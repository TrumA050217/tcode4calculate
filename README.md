# 小学生算数系统

一款专为小学生设计的算数练习系统，支持生成加减乘除题目、错题记录、答题统计等功能，帮助小学生提升算数能力。

微信小程序端：https://github.com/AOWUkawaii/tcode4calculate-wechat

## 📋 功能特点

- **用户管理**：支持账号密码登录、微信小程序授权登录
- **题库管理**：生成自定义题库、手动添加题目、标记完成状态、批量删除
- **题目生成**：支持100以内加减乘除运算，可生成混合题型
- **答题记录**：提交作答结果、查询历史答题记录
- **错题本**：自动记录错题、查询错题详情
- **数据统计**：计算正确率、统计错题总数

## 🛠️ 技术栈

- **后端**：Spring Boot
- **数据库**：MySQL
- **API文档**：SpringDoc OpenAPI (Swagger)
- **微信登录**：weixin-java-miniapp
- **安全认证**：JWT
- **构建工具**：Maven

## 📁 项目结构

```
MyCalculator/
├── src/
│   ├── main/
│   │   ├── java/com/zzuli/
│   │   │   ├── controller/    # 控制器
│   │   │   ├── service/       # 服务层
│   │   │   ├── config/        # 配置类
│   │   │   ├── util/          # 工具类
│   │   │   └── Main.java      # 启动类
│   │   └── resources/         # 资源文件
│   └── test/                  # 测试代码
├── pom.xml                    # Maven配置
└── README.md                  # 项目说明
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 部署步骤

1. 克隆仓库
```bash
git clone https://github.com/TrumA050217/tcode4calculate.git
cd tcode4calculate
```

2. 配置数据库
   - 创建数据库并执行 `src/project.sql` 中的SQL脚本
   - 修改配置文件中的数据库连接信息

3. 配置微信小程序（可选）
   - 在 `application.properties` 中配置微信小程序的 `appId` 和 `secret`

4. 构建并运行
```bash
mvn clean package
java -jar target/tcode4calculate-0.0.1-SNAPSHOT.jar
```

5. 访问接口文档
   - 启动后访问 http://localhost:8866/swagger-ui.html 查看API文档

## 🔌 核心接口

| 接口 | 功能 |
|------|------|
| `POST /user/register` | 用户注册 |
| `POST /user/login` | 用户登录 |
| `GET /calculate/generate` | 生成题库 |
| `GET /calculate/generate/questions` | 生成题目 |
| `POST /calculate/manual` | 手动添加题目 |
| `POST /calculate/submit` | 提交作答 |
| `GET /calculate/wrong` | 查询错题本 |
| `GET /calculate/all` | 查询历史记录 |

## 📝 版本历史

- v1.0 基础版本发布，包含题库生成、答题、错题记录等核心功能

## 🤝 贡献指南

1. Fork 本仓库
2. 创建分支 (`git checkout -b feature/amazing-feature`)
3. 提交修改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📧 联系方式

如有问题或建议，请联系：truma050217@gmail.com
