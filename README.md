<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-3.0.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-3.5.4-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(开源版)

## 使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## 项目介绍

> DaxPay是一套开源支付网关系统，已经对接支付宝、微信支付、云闪付相关的接口。可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响。
> 同时与商业版使用同样的底层代码，保证统一接口尽量兼容，可以方便的升级为商业版。

## 特色功能
- 支持支付、退款等支付相关的核心能力
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和`云闪付`相关的接口，并以扩展包的方式支持更多类型的通道
- 支持多应用配置，可以同时对接多个支付通道账号，方便多个业务系统对接
- 提供网关支付功能：收银台、聚合支付、收款码牌等功能
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，保证交易安全可靠
- 提供管理端，方便运营人员进行管理和操作

## 文档和源码地址
### 文档地址
在 [DaxPay文档站](https://daxpay.dromara.org/) 下的支付网关(DaxPay)模块下可以进行查阅相关文档，具体链接地址如下：
[快速指南](https://daxpay.dromara.org/single/guides/overview/项目介绍.html)、
[支付对接](https://daxpay.dromara.org/single/gateway/overview/接口清单.html)、
[操作手册](https://daxpay.dromara.org/single/admin/config/平台配置.html)

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          | GITCODE                                        |
|---------|---------------------------------------------|-------------------------------------------------|------------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay) |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |                                                |
| 网关前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |                                                |


## 系统演示
### 开源版:
> 注：演示账号部分功能权限未开放。

地址：https://admin.web.daxpay.cn

账号：daxpay

密码：daxpay123

### 商业版

运营端
https://admin.web.daxpay.cn/
代理端
https://agent.web.daxpay.cn/
商户端
https://merchant.web.daxpay.cn/

运营端演示用户: csadmin/123123

代理端演示用户: csdls/123123

商户端普通商户演示: cspt/123123

商户端特约商户演示: csdl/123123


## 核心技术栈
| 名称          | 描述     | 版本要求             |
|-------------|--------|------------------|
| Jdk         | Java环境 | 21+              |
| Spring Boot | 开发框架   | 3.5.x            |
| Redis       | 分布式缓存  | 7.x版本及以上         |
| Postgresql  | 数据库    | Postgresql 12及以上 |
| MySQL       | 数据库    | MySQL 8.0及以上     |
| Vue         | 前端框架   | 3.x              |

## 系统截图
### 通道配置
<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/wechat_2025-04-27_204334_543.lvxlxz86a.webp" alt="wechat_2025-04-27_204334_543"  />

### 收银台
<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/wechat_2025-04-27_203920_863.7phv2q931.webp" alt="wechat_2025-04-27_203920_863" />

<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/wechat_2025-04-27_204208_069.6bh9xisxha.webp" alt="wechat_2025-04-27_204208_069" width = "270" height = "570" />

### 聚合码牌

<img alt="7604af26dde4add3ff9aaea7a7d3be84" src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/7604af26dde4add3ff9aaea7a7d3be84.7axdaovomy.webp"  width = "270" height = "570" />

<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/1b7671d183f279751460d42234c6eadb.2rvc7pq7p4.webp" alt="1b7671d183f279751460d42234c6eadb"  width = "270" height = "570" />

### 小程序快捷收银
<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/cbe6e332c55b241215787254951dc7ec.969y3b848r.webp" alt="cbe6e332c55b241215787254951dc7ec" width = "270" height = "570" />


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
