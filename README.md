# Dax-Pay(开源支付系统)

<p>
 <img src='https://gitee.com/bootx/bootx-platform/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Boot%20Platform-1.2.3-success.svg" alt="Build Status"/>
<img src="https://img.shields.io/badge/Dax%20Pay-1.0-success.svg" alt="Build Status"/>
<img src="https://img.shields.io/badge/Author-Bootx-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.10-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

## 🙏🙏🙏 求star呀，走过路过留个star吧，非常非常感谢。🙏🙏🙏

## 🍈项目介绍

Dax-Pay是Bootx-Platform的子项目之一，主要是对支付收单和账务进行优化，精简与支付无关的模块，专注支付领域，对微信和支付宝的各种支付方式进行封装，
同时扩展了更多支付方式，如储值卡、现金卡等，可以作为一个简单的四方支付进行使用。

## 🛠️功能亮点
- 支持单通道支付、聚合支付、组合支付、部分和全部退款等支付功能
- 支持支付宝、微信、云闪付、现金、钱包、储值卡等多种支付方式
- 支持退款
- 支持对账

结算台演示地址：[http://daxpay.demo.bootx.cn/cashier](http://daxpay.demo.bootx.cn/cashier)

![](https://oscimg.oschina.net/oscnet/up-9f0044b76071d5a7f598ceab591c5fedb02.png)

## 🍒文档
- 系统演示地址：[Dax支付系统](http://v3.platform.bootx.cn/)
- 前端项目地址：[前端项目(vue3)](https://gitee.com/bootx/bootx-platform-vue3)
- 项目文档：[项目文档(GITEE)](https://bootx.gitee.io/)、[项目文档(备用)](https://daxpay.doc.bootx.cn/)
- 更新日志：[更新日志](./_doc/ChangeLog.md)

## 🥂 Quick Start

- 后端: [后端启动流程](https://https://daxpay.doc.bootx.cn/)
- 前端: [Vue3前端启动流程](https://https://daxpay.doc.bootx.cn/)
- 支付通道配置: [支付通道配置](https://https://daxpay.doc.bootx.cn/)

## 名词解释

| 名词   | 英文或简写    | 备注                                             |
|------|----------|------------------------------------------------|
| 支付通道 | Channel  | 主要包括第三方支付平台，如支付宝、微信、云闪付等                       |
| 支付方式 | PayWay   | 主要是进行支付时的方式，如扫码支付、H5支付、APP支付等，一种支付通道通常会有多种支付方式 |
| 支付策略 | Strategy | 对支付通道和支付方式进行封装，可以完成一种支付操作                      |
| 聚合支付 |          | 通常是扫码或收款时，根据客户使用应用的不同，自动识别是哪种支付通道，并进行支付        |
| 组合支付 |          | 同时使用多种支付通道进行支付，如同时使用余额+现金+储值卡+微信支付进行支付         |
| 商户   | merchant |                                                |
| 商户应用 | mchApp   |                                                |
| 分账   |          |                                                |
| 对账   |          |                                                |


## 🥞项目结构(dax-pay)
```lua
dax-pay
 ├──java
    ├── code -- 项目相关配置
    ├── configuration -- 项目文档
    ├── controller -- 使用外部项目对应开源协议
    ├── core -- 核心包
       ├── aggregate -- Quartz定时任务模块
       ├── cashier -- 微信对接模块
       ├── pay -- 企业微信对接模块
       ├── payment -- 企业微信对接模块
       ├── paymodel -- 企业微信对接模块
       ├── refund -- 企业微信对接模块
       ├── order -- 企业微信对接模块
       ├── bill -- 账单
    ├── dto -- 业务实体类
    ├── event -- 事件
    ├── exception -- 异常
    ├── mq -- 消息队列
    ├── param -- 参数
    ├── task -- 定时任务
    ├── util -- 工具类
    ├── DaxPayApplication -- 启动类
 ├── resources
    ├── mapper -- MyBatis映射文件
    ├── templates -- 静态网页
    ├── logback-spring -- 日志打印配置
    
```
##  🥂 Bootx 项目合集
- Bootx-Platform：单体版脚手架 [Gitee地址](https://gitee.com/bootx/bootx-platform)
- Bootx-Cloud：微服务版脚手架 [Gitee地址](https://gitee.com/bootx/bootx-cloud)
- dax-pay：开源支付系统 [Gitee地址](https://gitee.com/bootx/daxpay)
- bpm-plus：协同办公系统 [Gitee地址](https://gitee.com/bootx/bpm-plus)
- mybatis-table-modify：数据表结构管理 [Gitee地址](https://gitee.com/bootx/mybatis-table-modify)

##  🥪 关于我们

QQ扫码加入QQ交流群
<p>

<img src="https://oscimg.oschina.net/oscnet/up-ac1a8f8221203de2b5cbc6a461a26199b95.jpg" width = "330" height = "500"/>
</p>

## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](https://oscimg.oschina.net/oscnet/up-4aab9fa8bc769295b48c888d93e71320d93.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0_
