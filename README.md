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
- 提供商户端、运营端，支持多商户和服务商模式，
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和`云闪付`相关的接口
- 以支付扩展包的方式支持更多类型的通道：乐刷、海科、随行付、拉卡拉、斗拱等
- 以功能扩展包的方式提供网关支付：收银台、聚合支付、收款码牌等功能
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，保证交易安全可靠

## 文档和源码地址
### 文档地址
在 [DaxPay文档站](https://doc.daxpay.cn/) 可以进行查阅相关文档，具体模块说明地址如下：

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          | GITCODE                                        |
|---------|---------------------------------------------|-------------------------------------------------|------------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay) |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |                                                |
| 网关前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |                                                |


## 系统演示
### 开源版:
> 注：演示账号部分功能权限未开放。

运营端: https://admin.open.daxpay.cn/  演示用户: csadmin/123123

代理端: https://agent.open.daxpay.cn/   演示用户: csdls/123123

商户端: https://merchant.open.daxpay.cn/  普通商户演示: cssh/123123


### 商业版

运营端: https://admin.plus.daxpay.cn/  演示用户: csadmin/123123

代理端: https://agent.plus.daxpay.cn/   演示用户: csdls/123123

商户端: https://merchant.plus.daxpay.cn/  商户演示: csdl/123123

## 扩展包和商业版

### 扩展包
> 针对一些拓展性的功能和三四方通道的对接，提供付费扩展包，方便用户进行功能扩展，实现更多功能。
> 购买扩展包后一年内采购商业版时，提供购买扩展包的等额优惠金额。

| 名称            | 类型      | 价格  | 说明                     |
|---------------|---------|-----|------------------------|
| 支付网关(优化版)     | 功能拓展    | 99  | 可以实现码牌支付、收银台支付、聚合支付等功能 |
| 乐刷支付扩展包       | 支付通道扩展包 | 249 |                        |
| 海科融通扩展包       | 支付通道扩展包 | 249 |                        |
| 汇付(adapay)扩展包 | 支付通道扩展包 | 199 |                        |
| 随行付扩展包        | 支付通道扩展包 | 249 |                        |
| 斗拱支付扩展包       | 支付通道扩展包 | 249 | 待推出                    |
| 拉卡拉支付扩展包      | 支付通道扩展包 | 249 | 待推出                    |
| 快钱支付扩展包       | 支付通道扩展包 | 249 | 待推出                    |
| 富友支付扩展包       | 支付通道扩展包 | 249 | 待推出                    |
| 易宝支付扩展包       | 支付通道扩展包 | 249 | 待推出                    |
| 盛付通支付扩展包      | 支付通道扩展包 | 249 | 待推出                    |
| 银盛支付扩展包       | 支付通道扩展包 | 249 | 待推出                    |

### 商业版
> 针对一些需要较高的客户，提供商业版进行选择，相对于开源版，功能更强大，有专门的售后支持。

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

### 系统界面

<img height="570" alt="15ccf7650d7b05fb25654cfe669153c3_PicViewer" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/15ccf7650d7b05fb25654cfe669153c3_PicViewer.6t7gdqsgh7.webp"/>

<img height="570" src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/wechat_2025-04-27_204334_543.lvxlxz86a.webp" alt="wechat_2025-04-27_204334_543"  />

### 收银台

<img height="570" alt="微信图片_20250825232829_66" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_20250825232829_66.92qgx888jk.webp"/>

<img alt="微信图片_2025-08-25_232403_439" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_2025-08-25_232403_439.3d54ln66eh.webp" width="270" height="570"/>

### 聚合码牌

<img alt="7604af26dde4add3ff9aaea7a7d3be84" src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/7604af26dde4add3ff9aaea7a7d3be84.7axdaovomy.webp"  width = "270" height = "570" />

<img src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/20250427/1b7671d183f279751460d42234c6eadb.2rvc7pq7p4.webp" alt="1b7671d183f279751460d42234c6eadb"  width = "270" height = "570" />

### 小程序(商业版)

<img alt="微信图片_20250825232837_68" height="570" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_20250825232837_68.8hgtaxds8y.webp" width="270"/>

<img alt="微信图片_20250825232834_67" height="570" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/20250825/微信图片_20250825232834_67.8dx7d7kpj5.webp" width="270"/>

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
