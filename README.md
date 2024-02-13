# Dax-Pay(开源支付系统)

<p>
 <img src='https://gitee.com/bootx/bootx-platform/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Boot%20Platform-1.3.6-success.svg" alt="Build Status"/>
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-2.0.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Bootx-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

## 🍈项目介绍

> DaxPay是一套基于Bootx-Platform脚手架构建的一套开源支付网关系统，已经对接支付宝、微信支付相关的接口，以及扩展了钱包支付、储值卡支付、现金支付等新的支付方式。
> 可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响

## 🧭 特色功能
- 已对接`微信支付`相关的接口，目前已经支持`V2`版本的接口，后续版本将支持`V3`版本的接口
- 已对接`支付宝`相关的接口，目前已经支持`V2`版本的接口，后续版本将支持`V3`版本的接口
- 支持组合支付，满足用户系统需要多种方式同时进行支付的场景。
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，可根据实际需要进行开关，保证交易安全可靠
- 提供管理平台，方便运营人员进行管理和操作，不需要懂IT技术也可以轻松使用
- 提供`聚合支付`、`电脑收银台`和`手机收银台`的演示模块，供开发者参考其实现支付功能的逻辑

## 🍒 文档和源码地址
### 文档地址
在 [Bootx开源文档站](https://bootx.gitee.io/) 下的支付网关(DaxPay)模块下可以进行查阅相关文档，具体链接地址如下：
[快速指南](https://bootx.gitee.io/daxpay/guides/overview/项目介绍.html)、
[支付对接](https://bootx.gitee.io/daxpay/gateway/overview/接口清单.html)、
[平台配置](https://bootx.gitee.io/daxpay/admin/config/平台配置.html)

### 项目地址
后端地址：[https://gitee.com/bootx/dax-pay](https://gitee.com/bootx/dax-pay)

Web前端地址：[https://gitee.com/bootx/dax-pay-ui](https://gitee.com/bootx/dax-pay-ui)

H5前端地址：[https://gitee.com/bootx/dax-pay-h5](https://gitee.com/bootx/dax-pay-h5)

## 🏬 系统演示
### 管理平台:
> 注：演示账号部分功能修改删除权限未开放。

地址：https://daxpay.demo.bootx.cn

账号：daxpay

密码：123456

### 网关接口
> 注：接口平台只开放支付网关相关的接口，不开放系统其他接口。

地址: https://daxpay.server.bootx.cn/doc.html

账号: daxpay

密码: 123456

### 收银台演示
> 请勿大额支付，可以通过后台管理端进行退款

电脑收银台地址: https://daxpay.demo.bootx.cn/#/cashier

手机收银台地址: https://daxpay.demo.bootx.cn/h5/#/cashier/uniCashier

## 🥞 核心技术栈
| 名称          | 描述       | 版本要求                       |
|-------------|----------|----------------------------|
| Jdk         | Java环境   | 1.8+，11版本可以正常使用，但17+版本暂不支持 |
| Spring Boot | 开发框架     | 2.7.x                      |
| Redis       | 分布式缓存    | 5.x版本及以上                   |
| MySQL       | 数据库      | 基于5.7.X版本开发，基本支持8.x版本      |
| Vue         | 前端框架     | 3.x                        |
| IJpay       | 支付SDK开发包 | 项目自动管理，不需要额外处理             |


## 🍎 系统截图

### H5收银台演示
![h5.png](https://s11.ax1x.com/2024/02/12/pF8nPMV.png)
### 支付演示
![pay.png](https://s11.ax1x.com/2024/02/12/pF8np2q.png)
### 收银台演示
![pc.jpg](https://s11.ax1x.com/2024/02/12/pF8n9x0.jpg)
### 支付通道配置
[](https://s11.ax1x.com/2024/02/13/pF8s2VS.jpg)

## 💾 路线图
[**开发进度和任务池**](/_doc/Task.md)

[**更新记录**](/_doc/ChangeLog.md)

### 2.0.X版本:
- [ ] 对账功能剩余比对功能实现
- [ ] 支持转账操作
- [ ] 微信增加V3版本接口支持
- [ ] 支付宝增加V3版本接口支持
- [ ] 钱包功能完善
- [ ] 储值卡功能完善
- [ ] 现金支付功能完善
- [ ] 支付宝进行关闭时，支持通过撤销模式进行订单关闭
- [ ] 消息通知支持消息中间件模式
- [ ] 增加验签调试等功能的页面

### 2.1.X版本:
- [ ] 增加账户金额表
- [ ] 增加统计管理
- [ ] 支持微信消息通知
- [ ] 支持钉钉消息通知
- [ ] 新增支付单预警功能, 处理支付单与网关状态不一致且无法自动修复的情况


##  🥂 Bootx 项目合集
- Bootx-Platform：单体版脚手架 [Gitee地址](https://gitee.com/bootx/bootx-platform)
- Bootx-Cloud：微服务版脚手架 [Gitee地址](https://gitee.com/bootx/bootx-cloud)
- dax-pay：开源支付系统 [Gitee地址](https://gitee.com/bootx/daxpay)
- mybatis-table-modify：数据表结构管理 [Gitee地址](https://gitee.com/bootx/mybatis-table-modify)

##  🥪 关于我们

QQ扫码加入QQ交流群
<p>
<img src="_doc/images/qq_qun.jpg" width = "330" height = "500"/>
</p>


## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](_doc/images/jetbrains.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0
