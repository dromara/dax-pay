# DaxPay 生产部署指南

> 本文档涵盖后端(`dax-pay-open`)与前端(`dax-pay-ui`)的生产环境部署要点,
> 重点说明**部署模式 fail-fast 校验机制**与**必须配置的环境变量清单**。

---

## 一、后端部署模式(核心机制)

### 1.1 总开关 `daxpay.platform.deployment.mode`

| 值 | 行为 |
|---|---|
| `PROD` | 启动期强制校验一组"开发态功能"开关, 任一未关闭则**拒绝启动**(fail-fast) |
| `DEV` | 不做任何校验, 保持开发体验 |

**推断规则**(不显式配置时):

```
显式 daxpay.platform.deployment.mode  >  按 spring.profiles.active 推断
                                              ├─ 含 prod → PROD
                                              └─ 其他    → DEV
```

`application-prod.yml` 已显式设为 `PROD`, 正常生产部署无需额外操作。

### 1.2 fail-fast 校验清单(PROD 模式)

校验器: `DeploymentModeEnforcer`(SPI 注册于 `common-config` 模块的 `META-INF/spring.factories`)。
启动时若以下任一项不达标, 收集所有违规后一次性抛 `IllegalStateException` 拒绝启动:

#### ERROR 级(必须达标, 否则启动失败)

| # | 配置项 | 要求 | 说明 |
|---|---|---|---|
| 1 | `daxpay.platform.config.sandbox-enabled` | `false` | 沙箱环境必须关闭(Java 默认 `true`, 必须显式覆盖) |
| 2 | `daxpay.platform.starter.auth.enable-admin` | `false` | 超级管理员登录必须关闭 |
| 3 | `daxpay.platform.common.exception.show-full-message` | `false` | 异常详情不返回前端(避免泄露堆栈) |
| 4 | `springdoc.api-docs.enabled` | `false` | OpenAPI 文档关闭 |
| 5 | `springdoc.swagger-ui.enabled` | `false` | Swagger UI 关闭 |
| 6 | `daxpay.platform.common.spring.cors.enable` | `false` | 应用层 CORS 关闭(跨域走 nginx) |
| 7 | `management.endpoints.web.exposure.include` | 不含敏感端点 | 禁止暴露 `env`/`heapdump`/`threaddump`/`beans`/`loggers`/`configprops`/`shutdown` 等, 仅允许 `health,info,metrics` |
| 8 | `management.endpoint.health.show-details` | `never` 或 `when-authorized` | 不暴露 DB/Redis 内部状态 |
| 9 | `daxpay.platform.starter.auth.ignore-urls` | 不含 `/**` | 通配全开等于完全关闭认证 |

#### WARN 级(只打日志不阻断)

| # | 触发条件 | 说明 |
|---|---|---|
| W1 | PROD 模式但 `spring.profiles.active` 含 `dev` | 提醒可能误用 dev 配置启动生产 |
| W2 | `logging.level.cn.daxpay.open` = `DEBUG`/`TRACE` | 排查时可临时开启, 长期建议 INFO |

### 1.3 启动失败示例

若误将开发配置带入生产, 启动会看到:

```
----------------------------------------------------------
  生产部署模式(PROD)启动校验失败
  检测到 2 项开发态功能未关闭:
  - daxpay.platform.config.sandbox-enabled = (未配置, 框架默认 true)  [沙箱环境全局开关 生产环境必须为 false]
  - springdoc.api-docs.enabled = true  [OpenAPI 文档 生产环境必须为 false]
----------------------------------------------------------
  请在 application-prod.yml / 环境变量中修正上述配置后重启。
  如确需在当前环境开启(例如联调), 显式设置 daxpay.platform.deployment.mode=DEV
----------------------------------------------------------
```

**逃生口**: 联调/特殊场景需临时跳过校验, 显式设置环境变量 `DAXPAY_PLATFORM_DEPLOYMENT_MODE=DEV`。

---

## 二、后端部署 Checklist

### 2.1 必须配置的环境变量

`application-prod.yml` 用 `${VAR:?missing}` 语法强制注入, 缺失直接报错:

| 环境变量 | 用途 | 示例 |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | 激活 prod profile | `prod` |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | PostgreSQL 地址 | `postgresql` / `5432` / `daxpay-prod` |
| `DB_USERNAME` / `DB_PASSWORD` | 数据库凭证 | — |
| `REDIS_HOST` / `REDIS_PORT` / `REDIS_DATABASE` | Redis 地址 | `redis` / `6379` / `0` |
| `REDIS_PASSWORD` | Redis 密码 | — |
| `ARTEMIS_BROKER_URL` / `ARTEMIS_USER` / `ARTEMIS_PASSWORD` | 消息队列 | `tcp://artemis:61616` |
| `RSA_PRIVATE_KEY` / `RSA_PUBLIC_KEY` | 平台 RSA 密钥(PEM 文本) | — |
| `ENCRYPT_KEY` | 业务字段 AES 加密密钥 | — |
| `CHANNEL_ONE_TRANSPORT_KEY` | 通道子应用1 传输密钥(32 字符) | — |
| `CHANNEL_TWO_TRANSPORT_KEY` | 通道子应用2 传输密钥(32 字符) | — |

### 2.2 可选环境变量

| 环境变量 | 默认值 | 说明 |
|---|---|---|
| `SERVER_PORT` | `9999` | 服务端口 |
| `CHANNEL_ONE_BASE_URL` | `http://channel-one:20100` | 子应用1 地址 |
| `CHANNEL_TWO_BASE_URL` | `http://channel-two:20200` | 子应用2 地址 |
| `IP2REGION_FILE_PATH` | `/data/ip/ip2region_v4.xdb` | IP 地理库路径 |

### 2.3 启动命令

```bash
# Docker 部署(推荐)
docker run -d \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgresql \
  -e DB_USERNAME=daxpay \
  -e DB_PASSWORD='强密码' \
  -e REDIS_HOST=redis \
  -e REDIS_PASSWORD='强密码' \
  -e ARTEMIS_BROKER_URL=tcp://artemis:61616 \
  -e ARTEMIS_USER=daxpay \
  -e ARTEMIS_PASSWORD='强密码' \
  -e RSA_PRIVATE_KEY='-----BEGIN PRIVATE KEY-----...' \
  -e RSA_PUBLIC_KEY='-----BEGIN PUBLIC KEY-----...' \
  -e ENCRYPT_KEY='32位AES密钥' \
  -e CHANNEL_ONE_TRANSPORT_KEY='32位传输密钥' \
  -e CHANNEL_TWO_TRANSPORT_KEY='32位传输密钥' \
  -p 9999:9999 \
  daxpay-open:latest

# JVM 参数启动
java -XX:MaxRAMPercentage=75 \
     -Dspring.profiles.active=prod \
     -jar daxpay-start.jar
```

---

## 三、前端部署 Checklist(dax-pay-ui)

### 3.1 构建命令

```bash
# 运营(管理)端
pnpm run build          # 默认 production 模式, 输出 dist/

# 商户端(同一仓库 monorepo)
pnpm run build          # 由 turbo 编排两端同时构建
```

### 3.2 必须配置项

`.env.production`(运营端 `apps/daxpay-admin/`、商户端 `apps/daxpay-merchant/`):

| 变量 | 说明 | 配置方式 |
|---|---|---|
| `VITE_GLOB_API_URL` | 后端 API 地址 | **构建时留空**, 部署时通过 nginx 反代 `/api` → 后端 9999 端口 |

已固化的生产开关(无需改动):

| 变量 | 值 | 说明 |
|---|---|---|
| `VITE_NITRO_MOCK` | `false` | Mock 服务关闭 |
| `VITE_DEVTOOLS` | `false` | Vue DevTools 关闭 |
| `VITE_COMPRESS` | `none` | gzip/brotli(交由 nginx 处理) |
| `VITE_PWA` | `false` | PWA 关闭 |
| `VITE_ARCHIVER` | `true` | 构建后生成 dist.zip |

### 3.3 生产构建已内置的优化

- `console.*` 调用移除(`dropConsole: true`, 见 `internal/vite-config/src/config/application.ts`)
- `debugger` 语句移除(`dropDebugger: true`)
- sourcemap 关闭
- rolldown minify 开启

### 3.4 nginx 配置示例

```nginx
server {
    listen 80;
    server_name admin.daxpay.example.com;

    # 前端静态资源
    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    # API 反代到后端(配合前端 VITE_GLOB_API_URL=/api)
    location /api/ {
        proxy_pass http://backend:9999/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

> 前端 `VITE_GLOB_API_URL` 留空时, 请求走相对路径(当前 origin), nginx 通过 `/api/` 反代到后端。

---

## 四、通道子应用(channel-one / channel-two)

本次部署模式校验**未覆盖**通道子应用(它们有独立的 yml 与密钥体系)。
通道子应用的部署要点:

- 启动时必须传 `SPRING_PROFILES_ACTIVE=prod`
- 通道传输密钥 `${CHANNEL_TRANSPORT_KEY}` 必须由环境变量注入(dev yml 中为硬编码占位密钥)
- 默认 profile 同样为 `dev`, 切勿遗漏

---

## 五、常见误用案例与排错

### 5.1 忘记切换 profile(最高频)

**现象**: 以 `dev` profile 启动生产, 沙箱/超管/Swagger 全开, 但 DeploymentModeEnforcer **不会拦截**(因 dev profile 推断为 DEV 模式)。

**预防**: 部署脚本/Dockerfile 必须显式 `-e SPRING_PROFILES_ACTIVE=prod`。

**兜底**: PROD 模式下若检测到 active profile 含 `dev`, 会打 WARN 日志提醒(但不阻断)。

### 5.2 漏配 sandbox-enabled

**现象**: `application-prod.yml` 已显式 `sandbox-enabled: false`, 但若运维通过自定义 yml 覆盖且漏写此项, Java 默认值 `true` 生效, 沙箱误开。

**预防**: DeploymentModeEnforcer 在 PROD 模式会拦截(校验项 #1), 启动直接失败。

### 5.3 dev 密钥带入生产

**现象**: `application-dev.yml` 中硬编码了 RSA/AES/通道传输密钥, 若误用 dev profile 启动生产, 密钥泄露。

**预防**: `application-prod.yml` 全部用 `${VAR:?missing}` 强制环境变量注入, 不传值直接报错。
**切勿**将 dev 的密钥值复制到生产环境变量。

### 5.4 actuator 敏感端点暴露

**现象**: 误配 `management.endpoints.web.exposure.include=*` 或含 `env`/`heapdump`, 泄露环境变量与堆内存。

**预防**: DeploymentModeEnforcer 校验项 #7, PROD 模式下含敏感端点直接拒绝启动。

### 5.5 前端 API 地址未配置

**现象**: 前端构建后 API 请求 404 或打到错误地址。

**排查**: 确认 `VITE_GLOB_API_URL` 在 `.env.production` 中留空(走 nginx 反代), 或通过运行时配置注入。
**禁止**保留 Vben 默认的 `https://mock-napi.vben.pro/api`(已清理)。

---

## 六、相关源码索引

| 文件 | 说明 |
|---|---|
| `daxpay-platform/daxpay-platform-common/common-config/.../DeploymentModeEnforcer.java` | fail-fast 校验器(EnvironmentPostProcessor) |
| `daxpay-platform/daxpay-platform-common/common-config/.../properties/DeploymentProperties.java` | 部署模式配置属性类 |
| `daxpay-platform/daxpay-platform-common/common-config/src/main/resources/META-INF/spring.factories` | SPI 注册 |
| `daxpay-start/src/main/resources/application-prod.yml` | 生产环境配置模板 |
| `daxpay-start/src/main/resources/application-dev.yml` | 开发环境配置(对照参考) |
