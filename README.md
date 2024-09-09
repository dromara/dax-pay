<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay%20multi-3.0.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-3.3.2-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# DaxPay(多商户前期预览版)
> 单商户版为DEV分支
## ❗使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## 🍈项目介绍

> DaxPay是一套开源支付网关系统，可以支持对接支付通道相关的接口。独立部署，提供接口供业务系统进行调用，不对原有系统产生影响

## 🧭 特色功能
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 支持多商户多应用模式，基于Maven多模块方式，方便后期增加扩展
- 已对接`微信支付`、`支付宝`和`云闪付(开发中)`相关的接口，后续版讲支持更多支付通道的接口
- 支持支付、退款、对账、转账等支付相关的能力
- 提供`HTTP`方式接口调用能力，封装`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求、响应和通知数据都支持启用签名机制，保证交易安全可靠
- 提供管理平台，方便运营人员进行管理和操作，不需要懂IT技术也可以轻松使用

## 基础环境
- Java: 21
- Spring Boot 3.3.x
- Maven: 3.9.x
- 数据库: Postgresql 16 / MySQL 8.x (适配中)
- 前端技术: Vite5 + Vue3 + Antd vue 4.x

## 📃 文档和源码地址
### 文档地址
> 待补充

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          |GITCODE                                          |
|---------|---------------------------------------------|-------------------------------------------------|---------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay)    |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |  |


## 🏬 系统演示
### 运营端
> 搭建中

地址：https://admin.web.daxpay.cn/

### 商户端
> 搭建中

地址：https://merchant.web.daxpay.cn/

##  🥪 关于我们

扫码加入QQ交流群

交流二群: 598644350
<p>
<img src="https://jsd.cdn.zzko.cn/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240513180310.2yy68aykip.webp" width = "330" height = "500"/>
</p>

扫码加入钉钉交流群: [加群连接](https://qr.dingtalk.com/action/joingroup?code=v1,k1,AzkcWLa8J/OHXi+nTWwNRc68IAJ0ckWXEEIvrJofq2A=&_dt_no_comment=1&origin=11)
<p>
<img src="https://jsd.cdn.zzko.cn/gh/xxm1995/picx-images-hosting@master/connect/png-(1).7egk526qnp.webp" width = "400" height = "400"/>
</p>

微信扫码加小助手拉群: sdcit2020
<p>
<img alt="微信图片_20240226144703" height="480" src="https://jsd.cdn.zzko.cn/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
</p>

## Star History

[![Stargazers over time](https://starchart.cc/dromara/dax-pay.svg?variant=adaptive)](https://starchart.cc/dromara/dax-pay)


## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](_doc/images/jetbrains.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0
