# Dromara Dax-Pay-Open

<p align="left">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-4.0.0--beta1-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-LGPL--3.0--or--later-green.svg"/>
</p>

`DaxPay` 开源版是一款基于 `GNU LGPL v3.0` 协议分发的开源支付系统,提供支付、退款、转账等支付相关的核心能力,面向支付服务商、多商户平台与跨境业务团队。

业务系统对接多种支付方式(支付宝、微信、银联等)通常面临:各通道接口规范、签名算法、回调机制各不相同,通道 SDK 依赖冲突、版本锁定与主业务耦合严重,多渠道资金对账、风控、终端设备管理分散。DaxPay 将各通道封装为**统一的 HTTP 接口**,业务系统只需对接一套标准协议即可接入多种支付方式;同时把第三方 SDK 隔离到**独立部署的通道子应用**中,显著降低对接、运维与升级的复杂度。

> 📌 **快捷导航**: [官方文档站](https://doc.open.daxpay.cn) · [部署指南](https://doc.open.daxpay.cn/deployment/run) · [交流群](#关于我们)

## 使用须知

`DaxPay` 开源版是一款基于 `GNU LGPL v3.0` 协议分发的开源软件,受中华人民共和国相关法律法规的保护和限制,可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《GNU LGPL v3.0 或更高版本》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议,如果不同意请勿进行使用。**

## 核心特性

- **支付核心能力** — 覆盖支付、退款、查询、回调、同步、关闭、商户通知等完整交易闭环,支持全额/部分退款与差错处理
- **通道编排与对接分离** — 主应用负责通道路由、策略编排与配置管理,第三方 SDK 下沉到独立子应用,支持**独立部署、独立升级、弹性伸缩**
- **Java + Go 双语言实现** — 通道子应用同时提供 Java(Spring Boot)与 Go(Gin)两套对等实现,按团队技术栈与性能诉求按需选用
- **统一 HTTP 接口** — 各通道封装为 RESTful 标准协议,业务系统一次对接、多通道通用,接口请求与响应支持 RSA 签名
- **多商户与服务商模式** — 运营端 + 商户端双入口架构,菜单与数据按 `client_code` 隔离,每个商户数据通过商户编号行级隔离
- **全端覆盖** — 运营/商户 Web 管理端 + PC 与移动双端 H5 网关;管理端小程序、商户端小程序、收银小程序为商业扩展包
- **安全签名与字段加密** — RSA 双向验签 + AES-256-GCM 字段级加密 + 双重 Nonce 防重放,Sa-Token 权限认证
- **国际化与时区** — 中日韩 + 东盟 10 语种支持,时间字段统一UTC 存储,适配各时区情况
- **沙箱 / 生产部署级隔离** — 通过部署隔离 + 全局开关让"沙箱"概念在生产部署中根本不存在,启动期 fail-fast 强制对齐

## 系统架构

> DaxPay 采用**主应用编排 + 子应用对接**的分层架构,这是其系统的核心设计:

| 层级                                             | 职责                                                      | 实现           |
|--------------------------------------------------|-----------------------------------------------------------|----------------|
| **编排层**(主应用 `dax-pay-open`)                | 通道声明、通道路由、支付策略编排、配置数据 CRUD、回调组装 | 不含第三方 SDK |
| **对接层**(子应用 `channel-one` / `channel-two`) | 第三方 SDK 直接调用、签名/验签、通道专属协议适配          | 独立部署       |

主应用通过声明式 HTTP 客户端(`@HttpExchange`)调用子应用,链路全程 **AES-GCM 传输加密**。这样设计优点如下:

- **SDK 依赖隔离** — 第三方 SDK 不污染主应用,避免版本冲突
- **独立升级** — 子应用可按通道单独发版,不影响主链路
- **弹性伸缩** — 高频通道(支付宝、微信)可多实例独立扩缩容

## 支付通道

| 通道模块                | 所属机构 | 对接模式           |
|-------------------------|----------|--------------------|
| `daxpay-channel-alipay` | 支付宝   | 直连 + ISV(服务商) |
| `daxpay-channel-wechat` | 微信支付 | 直连 / 服务商      |
| `daxpay-channel-ums`    | 银联商务 | 直连               |
| `daxpay-channel-douyin` | 抖音支付 | 直连               |

> 聚合通道(拉卡拉、海科融通、斗拱、乐刷、随行付、河马付、Adapay、富友、易宝等 )属于商业扩展包,由 `dax-pay-channel-two` 子应用承载,详见 [版本清单](https://doc.open.daxpay.cn/extension/pricing/overview)。

## 安全机制

DaxPay 在接口、传输、存储、认证多个层面构建安全防护:

- **接口签名防篡改** — 开放支付 API 采用 RSA(`SHA256withRSA`)双向验签:商户私钥签名、平台公钥验签,平台私钥签名响应、商户公钥验签,核心入口由 `PaymentVerifyAspect` 切面统一拦截
- **字段级加密** — 数据库敏感字段(通道密钥、API 凭证、OAuth clientSecret 等)采用 AES-256-GCM 透明加密(MyBatis-Plus TypeHandler),支持多版本密钥轮换,即使数据库被拖走也无法解密
- **传输加密** — 主应用与通道子应用之间全程 AES-GCM 加密,生产环境密钥强制通过环境变量注入
- **双重 Nonce 防重放** — 管理端 API(`capability-nonce`)由平台签发一次性 nonce + 时间窗口校验;开放支付 API 由商户自生成 `nonceStr`,基于 Redis SETNX 语义防重放 + 双向时间窗口校验
- **权限认证** — Sa-Token权限框架 + 菜单数据隔离,支持 TOTP 两步验证与社交登录(微信/支付宝/抖音/QQ/飞书/钉钉/GitHub 等)
- **生产部署 fail-fast 校验** — 通过 `DeploymentModeEnforcer`(SPI 注册)在容器启动前按 `daxpay.platform.deployment.mode` 强制校验一组"开发态功能"开关(沙箱、超管登录、Swagger、actuator 端点等),任一未关闭则**拒绝启动**,避免开发配置误带入生产。详见 [部署指南](./_doc/DEPLOYMENT.md)

## 插件机制

通过 SPI 扩展点实现可插拔业务增强,核心层 `PayPluginAssistService` 作为插件辅助接入点:

| 插件                    | 说明                                                                                  |
|-------------------------|---------------------------------------------------------------------------------------|
| `daxpay-plugin-risk`    | 支付风控插件 — 黑名单管理 + 命中记录 + 默认风控检查器,在支付流程中提供风险拦截        |
| `daxpay-plugin-easypay` | 易支付协议插件 — 实现易支付的订单/退款/凭证/配置对接,可直接与支持易支付协议的系统互通 |

## 核心技术栈

| 名称           | 描述       | 版本要求   |
|----------------|------------|------------|
| JDK            | Java 环境  | 25+        |
| Spring Boot    | 开发框架   | 4.1.x      |
| PostgreSQL     | 数据库     | 14 及以上  |
| Redis          | 分布式缓存 | 7.x 及以上 |
| Apache Artemis | 消息队列   | 2.55+      |
| OpenTelemetry  | 链路追踪   | 内置       |
| Go             | GoLang环境 | 1.22+      |

## 项目结构

```
dax-pay-open/                       # 主后端应用
├── daxpay-platform/                # 基础层
│   ├── daxpay-platform-core/       #   通用契约
│   ├── daxpay-platform-common/     #   技术设施
│   ├── daxpay-platform-capability/ #   平台能力
│   └── daxpay-platform-service/    #   业务服务
├── daxpay-payment/                 # 支付核心
│   ├── daxpay-payment-core/        #   领域内核:下单 / 退款 / 同步 / 回调 / 通知 / 路由
│   ├── daxpay-payment-unipay/      #   统一开放 API
│   ├── daxpay-payment-admin/       #   运营端 API
│   ├── daxpay-payment-merchant/    #   商户端 API
│   ├── daxpay-payment-app-admin/   #   运营小程序 API
│   └── daxpay-payment-app-merchant/#   商户小程序 API
├── daxpay-channel/                 # 通道路由与编排
├── daxpay-plugin/                  # 插件机制(风控/易支付)
├── daxpay-demo/                    # 功能演示模块
└── daxpay-start/                   # 启动入口
```

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/1280X1280-(2).1vzcvhe692.webp" width="800" alt="DaxPay 系统架构图">
</p>

## 源码和演示地址

### 项目地址

| 项目 | GITEE | GITHUB |
| -------------- | ------------------------------------------- | ----------------------------------------------- |
| 后端地址 | [GITEE](https://gitee.com/dromara/dax-pay) | [GITHUB](https://github.com/dromara/dax-pay) |
| Web 前端地址 | [GITEE](https://gitee.com/opendaxpay/dax-pay-ui) | [GITHUB](https://github.com/opendaxpay/dax-pay-ui) |
| 通道子应用（Java） | [GITEE](https://gitee.com/opendaxpay/dax-pay-channel-one) | [GITHUB](https://github.com/opendaxpay/dax-pay-channel-one) |
| 通道子应用（Go） | [GITEE](https://gitee.com/opendaxpay/dax-pay-channel-one-go) | [GITHUB](https://github.com/opendaxpay/dax-pay-channel-one-go) |
| 网关前端地址 | [GITEE](https://gitee.com/opendaxpay/dax-pay-h5) | [GITHUB](https://github.com/opendaxpay/dax-pay-h5) |

### 关联子项目

>  DaxPay 开源版由多个独立 git 仓库组成,本仓库为主后端应用。配套子项目按交付形态分为两类:

#### 核心组件(开源版直接交付)

| 子项目 | 说明 |
|--------|------|
| `dax-pay-channel-one` | 通道适配子应用(Java),承载支付宝/微信/抖音/银联商务等直连通道,SDK 依赖隔离 / 独立升级 / 弹性伸缩 |
| `dax-pay-channel-one-go` | 通道适配子应用(Go 对等实现),更高吞吐与更低内存占用,**勿与 Java 版同时启动** |
| `dax-pay-ui` | Web 管理端 monorepo(运营端 + 商户端同源编译,Vben Admin 5 + antdv-next) |
| `dax-pay-h5` | 移动 H5 网关端(单应用承载 PC + 移动两套页面,Vant 4 + UnoCSS) |

#### 扩展包(商业增强版组件)

>  下列项目为 DaxPay 商业版扩展组件**不在开源版交付范围内**,需购买对应版本授权使用。详见 [版本清单](https://doc.open.daxpay.cn/extension/pricing/overview)。

| 子项目 | 说明 | 对应版本 |
|--------|------|---------|
| `dax-pay-channel-two` | 通道适配子应用(Java),承载拉卡拉/海科融通/斗拱/乐刷/随行付/河马付/Adapay/富友/易宝等聚合通道 | 旗舰版 |
| `dax-pay-app-admin` | 运营管理小程序(unibest 4 + uni-app + wot-ui v2,H5/微信/支付宝/抖音/安卓App 多端编译) | 增强版 |
| `dax-pay-app-merchant` | 商户管理小程序(规划中,技术栈同 `dax-pay-app-admin`) | 增强版 |
| `dax-pay-cashier` | 收银小程序(unibest 4 + uni-app + wot-ui v2,微信/支付宝/抖音) | 增强版 |

### 演示环境

> 演示账号部分功能权限未开放,完整功能请参考本地部署。启动后默认的本地账号密码为 `bootx / 123123`。

| 站点类型 | 地址 | 演示账号 |
|---------|------|---------|
| 运营端 | [https://admin.open.daxpay.cn/](https://admin.open.daxpay.cn/) | csadmin / 123123 |
| 商户端 | [https://merchant.open.daxpay.cn/](https://merchant.open.daxpay.cn/) | cssh / 123123 |

## 系统截图

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134631_746.7lkp72gaua.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134752_374.8vnmddya4o.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134652_881.7ehhbmu5es.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134735_540.wj9ibj64l.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_152315_016.1lcj2df8zo.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_152337_157.9gx9zq1a9y.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134710_519.92qu8vc9l6.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134851_582.8z78b3rcul.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_152700_495.8dxkou9kkt.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_145448_013.64ek5ckpxk.webp" width="800" alt="">
</p>
<table align="left">
  <tr>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727141720_489_69.92qu8tkfkw.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727151707_493_69.3yf5jkkngh.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727141719_488_69.6m4ltwdjod.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727153019_494_69.7ehhboclbz.webp" width="240" alt=""></td>
  </tr>
  <tr>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727141719_487_69.77e9g77zyz.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727145731_491_69.lwfp6j58j.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727150056_492_69.70b1ks11s5.webp" width="240" alt=""></td>
    <td align="center"><img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/微信图片_20260727153639_496_69.ictri0pz9.webp" width="240" alt=""></td>

  </tr>
</table>

## 关于我们

### QQ 交流群

> 扫码加入 QQ 交流群: 839738244

<p>
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260611/qrcode_1781170853115.1e99dci0rp.webp" width = "330" height = "500"/>
</p>

### 微信交流群

> 微信扫码加小助手拉群: sdcit2020

<p>
<img alt="微信图片_20240226144703" height="480" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
</p>

### 微信公众号

> 微信公众号会定期更新使用教程、版本更新记录和各种活动情况,欢迎关注

<p>
<img alt="微信公众号" height="330" src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
</p>

## 鸣谢

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)

## License

GNU LGPL v3.0 或更高版本 ([GNU Lesser General Public License v3.0 or later](LICENSE))
