# 微信Starter模块使用说明

## 模块简介

微信Starter模块为支付系统提供微信公众号和小程序的核心功能，包括：
- 消息发送（公众号模板消息、小程序统一服务消息）
- 用户认证（OpenId获取、用户信息获取、手机号获取）
- AccessToken管理（自动刷新、多副本部署支持）
- 消息记录管理（历史查询、状态追踪、失败重发）

## 依赖说明

模块依赖以下核心库：
- weixin-java-mp 4.8.1.B（微信公众号SDK）
- weixin-java-miniapp 4.8.1.B（微信小程序SDK）
- lock4j 2.2.7（分布式锁）
- Redis（缓存和分布式锁）

## 配置说明

### 数据库配置

执行数据库迁移脚本创建消息记录表：
```sql
-- 位置：src/main/resources/db/migration/V1.0.0__wechat_message_record.sql
```

### Redis配置

模块使用Redis进行：
- AccessToken缓存（支持多副本部署）
- 分布式锁（Token刷新互斥）

确保项目中已配置Redis连接。

## 功能使用

### 1. 公众号模板消息发送

```java
@Autowired
private WechatMpMessageService mpMessageService;

// 构建消息参数
TemplateMessageParam param = new TemplateMessageParam();
param.setAppId("your_app_id");
param.setAppSecret("your_app_secret");
param.setOpenId("user_open_id");
param.setTemplateId("template_id");

// 设置模板数据
Map<String, String> data = new HashMap<>();
data.put("first", "您的订单已支付成功");
data.put("keyword1", "订单号123456");
data.put("keyword2", "100.00元");
data.put("remark", "感谢您的购买");
param.setData(data);

// 设置跳转链接（可选）
param.setUrl("https://example.com/order/123456");

// 设置业务场景（可选，用于记录）
param.setScene("payment_success");

// 发送消息
MessageSendResult result = mpMessageService.sendTemplateMessage(param);
if (result.getSuccess()) {
    System.out.println("消息发送成功，msgId: " + result.getMsgId());
}
```

### 2. 小程序统一服务消息发送

```java
@Autowired
private WechatMaMessageService maMessageService;

// 构建消息参数
UniformMessageParam param = new UniformMessageParam();
param.setAppId("your_mini_app_id");
param.setAppSecret("your_mini_app_secret");
param.setOpenId("user_open_id");
param.setTemplateId("template_id");

// 设置模板数据
Map<String, String> data = new HashMap<>();
data.put("thing1", "订单已发货");
data.put("thing2", "顺丰快递");
data.put("character_string3", "SF123456789");
param.setData(data);

// 设置小程序页面路径（可选）
param.setPage("pages/order/detail?id=123456");

// 设置业务场景（可选）
param.setScene("order_shipped");

// 发送消息
MessageSendResult result = maMessageService.sendUniformMessage(param);
```

### 3. 批量发送消息

```java
// 批量发送公众号模板消息
List<TemplateMessageParam> params = new ArrayList<>();
// ... 添加多个消息参数
List<MessageSendResult> results = mpMessageService.batchSendTemplateMessage(params);

// 批量发送小程序统一服务消息
List<UniformMessageParam> maParams = new ArrayList<>();
// ... 添加多个消息参数
List<MessageSendResult> maResults = maMessageService.batchSendUniformMessage(maParams);
```

### 4. 公众号用户认证

```java
@Autowired
private WechatMpAuthService mpAuthService;

// 生成静默授权链接
WechatAuthUrlResult authUrl = mpAuthService.generateAuthUrl(
    "https://example.com/callback",
    "your_app_id",
    "your_app_secret"
);

// 生成用户信息授权链接
WechatAuthUrlResult userInfoAuthUrl = mpAuthService.generateUserInfoAuthUrl(
    "https://example.com/callback",
    "your_app_id",
    "your_app_secret"
);

// 通过授权码获取OpenId
WechatAuthResult authResult = mpAuthService.getTokenAndOpenId(
    "auth_code",
    "your_app_id",
    "your_app_secret"
);

// 通过授权码获取用户信息
WechatUserInfoResult userInfo = mpAuthService.getUserInfoByAuthCode(
    "auth_code",
    "your_app_id",
    "your_app_secret"
);
```

### 5. 小程序用户认证

```java
@Autowired
private WechatMaAuthService maAuthService;

// 获取OpenId
WechatAuthResult authResult = maAuthService.getOpenId(
    "js_code",
    "your_mini_app_id",
    "your_mini_app_secret"
);
```

### 6. 获取用户信息

```java
@Autowired
private WechatUserService userService;

// 获取公众号用户信息
WechatUserInfoResult userInfo = userService.getUserInfo(
    "user_open_id",
    "your_app_id",
    "your_app_secret"
);

// 获取小程序用户手机号
WechatPhoneResult phoneResult = userService.getPhoneNumber(
    "phone_code",
    "your_mini_app_id",
    "your_mini_app_secret"
);
```

### 7. 消息记录管理

```java
@Autowired
private WechatMessageRecordService recordService;

// 查询消息记录
List<WechatMessageRecord> records = recordService.queryRecords(
    "user_open_id",  // OpenId（可选）
    "template",      // 消息类型（可选）
    "success",       // 发送状态（可选）
    startTime,       // 开始时间（可选）
    endTime          // 结束时间（可选）
);

// 统计消息状态
Map<String, Long> statusCount = recordService.countByStatus();

// 重发失败的消息
MessageSendResult result = recordService.resendMessage(recordId);
```

### 8. AccessToken管理

```java
@Autowired
private WechatTokenService tokenService;

// 获取AccessToken（自动刷新）
String accessToken = tokenService.getAccessToken(
    "your_app_id",
    "your_app_secret"
);

// 手动刷新AccessToken
String newToken = tokenService.refreshAccessToken(
    "your_app_id",
    "your_app_secret"
);

// 检查Token是否即将过期
boolean expiring = tokenService.isTokenExpiringSoon("your_app_id");
```

## 重要说明

### 配置管理

所有服务方法都需要调用方传入配置参数（appId和appSecret），不从数据库或配置中心获取。这样设计的好处：
- 灵活性高，支持多个公众号/小程序
- 配置由调用方管理，模块只负责功能实现
- 便于集成到不同的系统中

### 多副本部署

模块支持多副本部署，通过以下机制保证安全：
- AccessToken使用Redis缓存，所有副本共享
- Token刷新使用lock4j分布式锁，确保只有一个副本执行刷新
- 消息记录使用数据库存储，所有副本共享

### 异步处理

- 消息记录保存使用@Async异步处理，不阻塞主流程
- 批量消息发送使用CompletableFuture异步处理，提高性能

### 错误处理

所有服务方法都有完善的错误处理：
- 配置参数验证：appId和appSecret为空时立即抛出异常
- 微信API调用失败：捕获WxErrorException并转换为业务异常
- 详细的错误日志：记录错误码、错误信息和请求参数
- 敏感信息脱敏：日志中AppSecret只记录前4位和后4位

## 注意事项

1. **配置参数必填**：所有服务方法的appId和appSecret参数都是必填的
2. **模板ID管理**：模板ID由调用方在发送消息时直接指定，不需要预先配置
3. **消息重发限制**：只能重发状态为failed的消息
4. **Token过期时间**：AccessToken有效期为7200秒，系统会提前5分钟自动刷新
5. **Redis依赖**：模块依赖Redis，确保Redis服务正常运行
6. **数据库表**：首次使用前需要执行数据库迁移脚本创建消息记录表

## 编译说明

使用mvnd命令进行编译：
```bash
mvnd clean install
```

## 版本信息

- 模块版本：4.0.0-beta3
- weixin-java-mp：4.8.1.B
- weixin-java-miniapp：4.8.1.B
- lock4j：2.2.7
