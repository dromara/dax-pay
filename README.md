# Dax-Pay(开源支付系统)

<p>
 <img src='https://gitee.com/bootx/bootx-platform/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Boot%20Platform-1.3.2-success.svg" alt="Build Status"/>
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-1.0.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Bootx-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.x-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

## 🙏🙏🙏 求star呀，走过路过留个star吧，非常非常感谢。🙏🙏🙏

## 🍈项目介绍

Dax-Pay是Bootx-Platform的子项目之一，主要是对支付收单和账务进行优化，精简与支付无关的模块，专注支付领域，对微信和支付宝的各种支付方式进行封装，
同时扩展了更多支付方式，如储值卡、现金卡等，可以作为一个简单的四方支付进行使用。

## 🛠️功能亮点
- 支持单通道支付、聚合支付、组合支付、退款、对账等支付功能
- 单通道支付：支持支付宝、微信、现金、钱包、储值卡等多种支付方式
- 聚合支付：支持微信或支付宝使用同一个码
- 组合支付：支持多种同步支付和一个异步支付（微信、支付宝）进行组合支付
- 支持退款：部分对款、全部退款等方式
- 储值卡：支持单卡支付、多卡支付，退款时支持退款到原储值卡中，也支持将余额退到同一个卡上
- 支付宝：支持web支付、wap支付、扫码支付、付款码支付、APP支付
- 微信：wap支付、扫码支付、付款码支付、APP支付、公众号/小程序支付

结算台演示地址：[http://daxpay.demo.bootx.cn/cashier](http://daxpay.demo.bootx.cn/cashier)

![](https://oscimg.oschina.net/oscnet/up-9f0044b76071d5a7f598ceab591c5fedb02.png)

## 🍒文档
- 系统演示地址：[Dax支付系统](http://daxpay.demo.bootx.cn/)
- 前端项目地址：[前端项目(vue3)](https://gitee.com/bootx/dax-pay-ui)
- 项目文档：[项目文档(GITEE)](https://bootx.gitee.io/)、[项目文档(备用)](https://daxpay.doc.bootx.cn/)
- 更新日志：[更新日志](./_doc/ChangeLog.md)

## 🍎 路线图
1.0.x
- 支持微信V3接口，可通过配置进行切换
- 新增储值卡多卡使用演示

## 🥂 Quick Start

- 后端: [后端启动流程](https://https://daxpay.doc.bootx.cn/)
- 前端: [Vue3前端启动流程](https://https://daxpay.doc.bootx.cn/)
- 支付通道配置: [支付通道配置](https://https://daxpay.doc.bootx.cn/)

## 名词解释

| 名词   | 英文或简写          | 备注                                             |
|------|----------------|------------------------------------------------|
| 支付通道 | Channel        | 主要包括第三方支付平台，如支付宝、微信、云闪付等                       |
| 支付方式 | PayWay         | 主要是进行支付时的方式，如扫码支付、H5支付、APP支付等，一种支付通道通常会有多种支付方式 |
| 支付策略 | Strategy       | 对支付通道和支付方式进行封装，可以完成一种支付操作                      |
| 聚合支付 | AggregationPay | 通常是扫码或收款时，根据客户使用应用的不同，自动识别是哪种支付通道，并进行支付        |
| 组合支付 | CombinationPay | 同时使用多种支付通道进行支付，如同时使用余额+现金+储值卡+微信支付进行支付         |
| 商户   | Merchant       | 系统中的一种单元                                       |
| 商户应用 | mchApp         | 一个商户可以有多个应用，一个应用可以分别进行各种支付通道配置                 |

## 🥞项目结构(dax-pay)
```lua
dax-pay
 ├──java
    ├── code -- 项目相关配置
    ├── configuration -- 项目文档
    ├── controller -- 使用外部项目对应开源协议
    ├── core -- 核心包
       ├── aggregate -- Quartz定时任务模块
       ├── cashier -- 结算台
       ├── channel -- 支付通道
       ├── merchant -- 商户和应用
       ├── pay -- 支付
       ├── payment -- 支付单
       ├── notify -- 回调通知
       ├── refund -- 退款
       ├── order -- 订单
       ├── bill -- 账单
    ├── dto -- 业务实体类
    ├── event -- 事件
    ├── exception -- 异常
    ├── mq -- 消息队列
    ├── param -- 参数
    ├── task -- 定时任务
    ├── util -- 工具类
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

## 💾 系统截图
|  ![](https://oscimg.oschina.net/oscnet/up-8be45e5ae3fb03db65345c48406fd03d351.png) |  ![](https://oscimg.oschina.net/oscnet/up-640fc184bab96c843c0b22fb3750687ff8b.png) |
|---|---|
|  ![](https://oscimg.oschina.net/oscnet/up-6b5457b481896129aab9c00c2b3b7a4a227.png) |  ![](https://oscimg.oschina.net/oscnet/up-3e79040cf91ebafa098bdea2198400a61fc.png) |
|  ![](https://oscimg.oschina.net/oscnet/up-be5961c8e88c4167f427bb70964458d8a68.png) |  ![](https://oscimg.oschina.net/oscnet/up-4e72e3f971cbe5fada18dfdf7d8960e4eec.png) |
|  ![](https://oscimg.oschina.net/oscnet/up-8c1774be39b72f6db111a2a66e39e74733e.png) |  ![](https://oscimg.oschina.net/oscnet/up-bb20d6fc035a908e4fdf86fb04830996fcc.png) |

## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](https://oscimg.oschina.net/oscnet/up-4aab9fa8bc769295b48c888d93e71320d93.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0
