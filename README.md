# Dromara Dax-Pay-Open

<p align="left">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-4.0.0--beta11-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-4.1.2-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-LGPL--3.0--or--later-green.svg"/>
</p>

`DaxPay` 开源版支付平台核心采用 `GNU LGPL v3.0 或更高版本(LGPL-3.0-or-later)` 发布,提供支付、退款、转账等支付相关的核心能力,面向支付服务商、多商户平台与跨境业务团队。

业务系统只需对接一套**统一的 HTTP 标准协议**即可接入支付宝、微信、银联等多种支付方式;第三方 SDK 全部隔离到**独立部署的通道子应用**,对接、升级、扩容互不干扰。

> 📌 **快捷导航**: [官方文档站](https://doc.open.daxpay.cn) · [一键部署](https://doc.open.daxpay.cn/getting-started/deployment/installer) · [部署指南](https://doc.open.daxpay.cn/getting-started/deployment/run) · [配套工具](#配套工具) · [交流群](#关于我们)

## 源码与演示

### 源码地址

| 项目 | Gitee | GitHub |
|------|-------|--------|
| 后端主应用(本仓库) | [dax-pay](https://gitee.com/dromara/dax-pay) | [dax-pay](https://github.com/dromara/dax-pay) |
| Web 管理端(运营端 + 商户端) | [dax-pay-ui](https://gitee.com/opendaxpay/dax-pay-ui) | [dax-pay-ui](https://github.com/opendaxpay/dax-pay-ui) |
| 通道子应用 | [dax-pay-channel-one](https://gitee.com/opendaxpay/dax-pay-channel-one) | [dax-pay-channel-one](https://github.com/opendaxpay/dax-pay-channel-one) |
| H5 网关端(PC + 移动) | [dax-pay-h5](https://gitee.com/opendaxpay/dax-pay-h5) | [dax-pay-h5](https://github.com/opendaxpay/dax-pay-h5) |

> 聚合通道(`dax-pay-channel-two`,拉卡拉 / 海科融通 / 斗拱 / 乐刷 / 随行付等)、运营 / 商户 / 收银小程序等属于商业扩展包,详见 [官网定价页](https://www.daxpay.cn/pricing/extension)。

### 演示环境

> 演示账号即点即用,数据定期重置,请勿录入真实业务数据与敏感信息。本地部署的种子账号和初始密码见 [_config/sql/README.md](./_config/sql/README.md),首次登录后请立即修改。

| 站点类型 | 地址 | 演示账号 |
|---------|------|---------|
| 运营端 | https://admin.open.daxpay.cn | `admin` / `121212` |
| 商户端 | https://merchant.open.daxpay.cn | `csqysh` / `121212` |

## 核心特性

- **完整交易闭环** — 支付、退款、转账、分账、查询、回调、同步、关闭、商户通知,支持全额/部分退款
- **通道编排与对接分离** — 主应用负责路由与策略编排,第三方 SDK 下沉到独立子应用,独立部署、独立升级、弹性伸缩
- **统一 HTTP 接口** — 各通道封装为 RESTful 标准协议,一次对接、多通道通用,请求与响应 RSA 签名
- **多商户与服务商模式** — 运营端 + 商户端双入口,菜单与数据按 `client_code` 隔离,商户数据行级隔离
- **全端覆盖** — 运营/商户 Web 管理端 + PC 与移动双端 H5 网关;小程序后端 API 随本仓库开源,小程序前端为商业扩展包
- **收款终端与设备** — 虚拟收款设备(收款播报终端)接入与台账管理,支付成功经 MQTT 推送语音播报与小票打印,支持通道终端登记与报备
- **收款精灵** — PC 旁挂式收款场景的商户收银接口(`daxpay-payment-elf`),配套桌面端收款精灵使用
- **基础风险检查** — IP 黑名单、海外 IP 拦截、省/市地区名单与支付前校验,支持风险命中记录
- **国际化与时区** — 中日韩 + 东盟 10 语种支持,时间字段统一 UTC 存储
- **沙箱 / 生产部署级隔离** — 部署隔离 + 全局开关,启动期 fail-fast 强制对齐

## 系统架构

> DaxPay 采用**主应用编排 + 子应用对接**的分层架构,这是其系统的核心设计:

| 层级                                             | 职责                                                      | 实现           |
|--------------------------------------------------|-----------------------------------------------------------|----------------|
| **编排层**(主应用 `dax-pay-open`)                | 通道声明、通道路由、支付策略编排、配置数据 CRUD、回调组装 | 不含第三方 SDK |
| **对接层**(子应用 `channel-one` / `channel-two` / `channel-three`·国际通道) | 第三方 SDK 直接调用、签名/验签、通道专属协议适配          | 独立部署       |

主应用通过声明式 HTTP 客户端(`@HttpExchange`)调用子应用,链路全程 **AES-GCM 传输加密**,SDK 依赖隔离、按通道独立升级、高频通道可多实例弹性伸缩。

## 支付通道

| 通道模块                | 所属机构 | 对接模式           |
|-------------------------|----------|--------------------|
| `daxpay-channel-alipay` | 支付宝   | 直连 + ISV(服务商) |
| `daxpay-channel-wechat` | 微信支付 | 直连 / 服务商      |
| `daxpay-channel-ums`    | 银联商务 | 直连               |
| `daxpay-channel-douyin` | 抖音支付 | 直连               |

> 聚合通道(拉卡拉、海科融通、斗拱、乐刷、随行付、河马付、Adapay、富友、易宝等 )属于商业扩展包,由 `dax-pay-channel-two` 子应用承载,详见 [官网定价页](https://www.daxpay.cn/pricing/extension)。

## 安全机制

DaxPay 在接口、传输、存储、认证多个层面构建安全防护:

- **接口签名防篡改** — 开放支付 API 全链路 RSA(`SHA256withRSA`)双向验签,核心入口切面统一拦截
- **字段级加密** — 通道密钥、API 凭证等敏感字段 AES-256-GCM 透明加密,支持密钥轮换,拖库也无法解密
- **传输加密** — 主应用与通道子应用间全程 AES-GCM,生产环境密钥强制环境变量注入
- **双重 Nonce 防重放** — 管理端一次性 nonce 签发 + 开放 API 商户 `nonceStr`,Redis SETNX + 时间窗口校验
- **权限认证** — Sa-Token + 菜单数据隔离,支持 TOTP 两步验证与社交登录
- **生产 fail-fast 校验** — 启动前强制校验开发态开关(沙箱、超管、Swagger 等),未关闭则拒绝启动,详见 [部署指南](./_doc/DEPLOYMENT.md)

## 插件机制

通过 SPI 扩展点实现可插拔业务增强,核心层 `PayPluginAssistService` 作为插件辅助接入点:

| 插件                    | 说明                                                                                  |
|-------------------------|---------------------------------------------------------------------------------------|
| `daxpay-plugin-risk`    | 支付风控插件 — 黑名单(IP / 省市地区)+ 海外 IP 拦截 + 支付前风险校验与命中记录 |
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
│   ├── daxpay-payment-app-merchant/#   商户小程序 API
│   └── daxpay-payment-elf/         #   收款精灵 API
├── daxpay-channel/                 # 通道路由与编排
├── daxpay-device/                  # 收款播报设备域(虚拟设备)
├── daxpay-plugin/                  # 插件机制(风控/易支付)
├── daxpay-demo/                    # 功能演示模块
└── daxpay-start/                   # 启动入口
```

## 配套工具

围绕部署上线、开发联调、柜台播报与门店收银,DaxPay 提供配套工具。

### 运维面板

**从源码到运行,一个面板搞定**。Go单文件程序,一行命令装到 Linux 服务器,在浏览器里按向导完成部署;日常运维也可以在面板进行操作。

- **向导式部署** — 环境检测、Docker 安装、PostgreSQL / Redis / Artemis 部署,到源码构建、各端应用上线,都在向导里完成
- **统一网关** — 域名绑定、CDN 接入、MQTT配置、对外接口按需开关
- **日常运维** — 服务重新部署、容器监控、任务日志与操作审计

获取:免费安装使用,需 Linux x86_64 · [产品介绍](https://www.daxpay.cn/products/panel/) · [部署文档](https://doc.open.daxpay.cn/getting-started/deployment/installer)

```bash
curl -fsSL https://download.daxpay.top/open/install/install.sh | sudo bash
```

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/ScreenShot_2026-11-09_113046_11.1ow95xnvw2.webp" width="800" alt="运维面板">
</p>

### 模拟联调通道

**不接真实通道,把支付全链路跑通**。一套独立部署的模拟支付服务,扮演一个"三方支付平台":支付、退款、分账、转账、预授权、进件、终端报备与微信授权全链路可模拟练,不需要任何真实商户号与密钥。

- **全链路模拟** — 覆盖微信、支付宝、银联、抖音渠道的扫码、H5、JSAPI、付款码等多种方式;
- **结果可控** — 场景规则让订单按预期成功、失败、延迟到账或不发回调,异常分支更方便测试;
- **开放网关协议** — 对外提供 DaxPay 开放接口风格的 RSA 双向签名网关,可用于验收自己的签名与验签代码

获取:源码随[商业扩展包](https://www.daxpay.cn/pricing/extension)基础版起附带 · [在线演示](https://mockpay.open.daxpay.cn/)(账号 `mock` / `mock`) · [产品介绍](https://www.daxpay.cn/products/mock/)

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/authcode.C_FqeJPK.2ksqldxkbt.webp" width="400" alt="模拟联调通道手机付款码">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/pay-confirm.DjInEy1b.83av1j5sfj.webp" width="400" alt="模拟联调通道模拟收银台">
</p>

### 收款播报 APP

**通过APP,失效收款播报和小票打印**。安卓手机模拟硬件, 顾客付款成功播报到账金额,同时支持热敏打印机打印小票。

- **到账播报** — 支付成功实时推送到手机,语音报出收款金额
- **小票打印** — 蓝牙、USB、网络连接 58 / 80mm 热敏打印机
- **扫码激活** — 运营端生成设备后,手机扫码或输入设备号即可激活;

需 Android 8.0+ · [产品介绍](https://www.daxpay.cn/products/voice/) · [使用文档](https://doc.open.daxpay.cn/operation-guide/mobile/virtual-device/overview)

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/device-light.W8tju9fT.232owsw6r0.webp" width="240" alt="收款播报 App 设备页">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/print-light.CTfqEeQL.32is9yyxwx.webp" width="240" alt="收款播报 App 打印设置">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/check-light.Btb69Ogy.6m4pzs1nor.webp" width="240" alt="收款播报 App 一键检查">
</p>

### 收款精灵

**不更换收银软件,实现插件收款**。装在收银电脑上的扫码收款工具:金额从收银软件屏幕上读,付款码用扫码盒扫,收完自动播报、打印小票。

- **屏幕识别金额** — 框选一次收银软件的金额区域,之后每笔自动识别,专用小模型本机识别j呢额
- **扫码盒收款** — 扫码盒读取顾客付款码收款;没有收银软件时也能手输金额简易收款
- **播报与小票** — 到账语音播报,支持网口或 USB 热敏打印机自动打印小票;

 Windows 下运行,需 Windows 10 / 11(64 位) · [产品介绍](https://www.daxpay.cn/products/elf/)

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/home.BP2mH-ut.4joxbq32ni.webp" width="800" alt="收款精灵首页">
</p>

## 系统截图

<p align="left">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134631_746.7lkp72gaua.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134752_374.8vnmddya4o.webp" width="800" alt="">
  <img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20260727/ScreenShot_2026-07-27_134652_881.7ehhbmu5es.webp" width="800" alt="">
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
<img alt="微信公众号" height="440" src="https://www.daxpay.cn/assets/other/wxpub.webp" width="330"/>
</p>

## 鸣谢

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)

## License

`DaxPay` 支付平台核心基于 `GNU LGPL v3.0 或更高版本` 协议开源([LICENSE](LICENSE)),受中华人民共和国相关法律法规的保护和限制。核心软件不收取授权费,可在符合 [《用户授权使用协议》](用户授权使用协议.txt) 与开源协议的前提下用于商业项目、学习和交流;修改、分发及第三方组件或支付通道的使用仍须遵守适用条款,**使用前请阅读上述协议,如不同意请勿使用**。
