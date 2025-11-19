<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-3.1.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-3.5.6-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(开源版)

## 使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## 项目介绍

> DaxPay是一套开源支付系统，可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响。

## 特色功能
- 支持支付、退款等支付相关的核心能力
- 提供商户端、运营端，支持多商户和服务商模式，
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和`云闪付`相关的接口
- 以支付扩展包的方式支持更多类型的通道：乐刷、海科、随行付、拉卡拉、斗拱、富友、易宝等
- 支持商户小程序扩展包，可以通过小程序来查询订单和管理商户相关的功能
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，保证交易安全可靠


## 核心技术栈
| 名称          | 描述     | 版本要求             |
|-------------|--------|------------------|
| Jdk         | Java环境 | 21+              |
| Spring Boot | 开发框架   | 3.5.x            |
| Redis       | 分布式缓存  | 7.x版本及以上         |
| Postgresql  | 数据库    | Postgresql 12及以上 |
| Vue         | 前端框架   | 3.x              |

## 文档和源码地址
### 文档地址
在 [DaxPay开源文档站](https://doc.daxpay.cn/) 可以进行查阅相关文档，具体模块说明地址如下：

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          | GITCODE                                        |
|---------|---------------------------------------------|-------------------------------------------------|------------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay) |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |                                                |
| 网关前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |                                                |
| 小程序地址   | 无                                           | 无                                               |                                                |


## 系统演示
### 开源版:
> 注：演示账号部分功能权限未开放。

| 端点类型 | 地址 | 演示账号 |
|---------|------|---------|
| 运营端 | [https://admin.open.daxpay.cn/](https://admin.open.daxpay.cn/) | csadmin/123123 |
| 商户端 | [https://merchant.open.daxpay.cn/](https://merchant.open.daxpay.cn/) | cssh/123123 |
| 小程序(H5) | [https://mini.open.daxpay.cn/](https://mini.open.daxpay.cn/) | cssh/123123 |


### 商业版

> 官网: [https://plus.daxpay.cn/](https://plus.daxpay.cn/)

| 端点类型      | 地址                                                                       | 演示账号           |
|-----------|--------------------------------------------------------------------------|----------------|
| 运营端       | [https://admin.plus.daxpay.cn/](https://admin.plus.daxpay.cn/)           | csadmin/123123 |
| 代理端       | [https://agent.plus.daxpay.cn/](https://agent.plus.daxpay.cn/)           | csdl/123123    |
| 商户端       | [https://merchant.plus.daxpay.cn/](https://merchant.plus.daxpay.cn/)     | cssh/123123    |
| 代理小程序(H5) | [https://mini-agent.plus.daxpay.cn/](https://mini-agent.plus.daxpay.cn/) | csdl/123123    |
| 商户小程序(H5) | [https://mini-mch.plus.daxpay.cn/](https://mini-mch.plus.daxpay.cn/)     | cssh/123123    |

## 扩展包和商业版

### 扩展包
> 针对一些拓展性的功能和三四方通道的对接，提供付费扩展包，方便用户进行功能扩展，实现更多功能。扩展包地址: [https://plus.daxpay.cn/plugins/](https://plus.daxpay.cn/plugins/)

| 名称            | 类型      | 说明  |
|---------------|---------|-----|
| 乐刷支付扩展包       | 支付通道扩展包 |     |
| 海科融通扩展包       | 支付通道扩展包 |     |
| 汇付(adapay)扩展包 | 支付通道扩展包 |     |
| 随行付扩展包        | 支付通道扩展包 |     |
| 斗拱支付扩展包       | 支付通道扩展包 |     |
| 拉卡拉支付扩展包      | 支付通道扩展包 |     |
| 快钱支付扩展包       | 支付通道扩展包 | 待推出 |
| 富友支付扩展包       | 支付通道扩展包 |     |
| 易宝支付扩展包       | 支付通道扩展包 |     |
| 盛付通支付扩展包      | 支付通道扩展包 | 待推出 |
| 银盛支付扩展包       | 支付通道扩展包 | 待推出 |


### 商业版
> 针对一些需要较高的客户，提供商业版进行选择，相对于开源版，功能更强大。官网地址: [https://plus.daxpay.cn/](https://plus.daxpay.cn/plugins/)

## 系统截图

### WEB端

<img alt="97c03499-d554-4e55-bb12-ddea42158551" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/97c03499-d554-4e55-bb12-ddea42158551.1vz2vjqzaj.webp" width="" height="800"/>

<img alt="ScreenShot_2025-11-17_170633_696" height="570" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/ScreenShot_2025-11-17_170633_696.8s3qfq3bgn.webp"/>

<img alt="ScreenShot_2025-11-17_170415_265" height="570" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/ScreenShot_2025-11-17_170415_265.b9bw2kzp8.webp"/>

### 网关H5端

<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_20250825232829_66.92qgx888jk.webp" height="570"/>

<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_2025-08-25_232403_439.3d54ln66eh.webp" width="300"/>

<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250427/7604af26dde4add3ff9aaea7a7d3be84.7axdaovomy.webp"  width="300"  />

<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250427/1b7671d183f279751460d42234c6eadb.2rvc7pq7p4.webp" width="300" />

### 商户小程序端(扩展包)

<img alt="微信图片_20251117161043_127_724" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161043_127_724.1hsn4mdm51.webp" width="300"/>

<img alt="微信图片_20251117161049_135_724" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161049_135_724.esxtqhs9e.webp" width="300"/>

<img alt="微信图片_20251117161047_132_724" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161047_132_724.1vz2vhlx0c.webp" width="300"/>

<img alt="微信图片_20251117161045_130_724" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161045_130_724.7ppyavmu1.webp" width="300"/>

<img alt="微信图片_20251117161047_132_721" height="666" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161048_134_724.5q7ueg3uxq.webp" width="300"/>)

<img alt="微信图片_20251117161044_129_724" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20251117/微信图片_20251117161044_129_724.58hspv2hcn.webp" width="300"/>

##  🥪 关于我们

扫码加入QQ交流群

交流三群: 879409917
<p>
<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/1733360741745_d.83a33entp3.webp" width = "330" height = "500"/>
</p>

微信扫码加小助手拉群: sdcit2020
<p>
<img alt="微信图片_20240226144703" height="480" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
</p>

## Star History

[![Stargazers over time](https://starchart.cc/dromara/dax-pay.svg?variant=adaptive)](https://starchart.cc/dromara/dax-pay)


## 🍻 鸣谢

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0
