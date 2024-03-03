# Dromara Dax-Pay(开源支付系统)

<p>
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-2.0.1-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Boot%20Platform-1.3.6-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Bootx-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

## 🍈项目介绍

> DaxPay是一套基于Bootx-Platform脚手架构建的开源支付网关系统，已经对接支付宝、微信支付相关的接口，以及扩展了钱包支付、储值卡支付、现金支付等新的支付方式。
> 可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响

> 当前处于功能开发阶段，部分功能可能会有调整，`V2.1.0`时将作为正式生产可用版本进行发布，之后会保证系统版本非大版本升级时，API接口和数据接口向前兼容

## 🧭 特色功能
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`和`支付宝`相关的接口，目前已经支持`V2`版本的接口，后续版本将支持`V3`版本的接口
- 支持组合支付，满足用户系统需要多种方式同时进行支付的场景。
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，可根据实际需要进行开关，保证交易安全可靠
- 提供管理平台，方便运营人员进行管理和操作，不需要懂IT技术也可以轻松使用
- 提供`聚合支付`、`电脑收银台`和`手机收银台`的演示模块，供开发者参考其实现支付功能的逻辑

## 📃 文档和源码地址
### 文档地址
在 [Bootx开源文档站](https://bootx.gitee.io/) 下的支付网关(DaxPay)模块下可以进行查阅相关文档，具体链接地址如下：
[快速指南](https://bootx.gitee.io/daxpay/guides/overview/项目介绍.html)、
[支付对接](https://bootx.gitee.io/daxpay/gateway/overview/接口清单.html)、
[操作手册](https://bootx.gitee.io/daxpay/admin/config/平台配置.html)

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          |
|---------|---------------------------------------------|-------------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |
| H5前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |


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

## 🛠️ 业务系统接入
> 业务系统想接入支付网关的话，不需要集成到业务系统里，只需要单独部署一份支付系统，然后业务系统通过接口调用即可拥有对应的支付能力，
不会对原业务系统的架构产生影响。如果是Java项目，可以使用SDK简化接入流程， 其他语言可以参照中的说明使用HTTP接口方式接入。

### Java客户端SDK
> SDK版本号与支付网关的版本保持一致，如果需要使用，请在pom.xml中添加如下依赖。SDK使用方式参考[SDK使用说明](https://bootx.gitee.io/daxpay/gateway/overview/SDK使用说明.html)。

```xml
 <!-- 支付SDK -->
<dependency>
    <groupId>cn.bootx.platform</groupId>
    <artifactId>daxpay-single-sdk</artifactId>
    <version>${latest.version}</version>
</dependency>
```
### SDK调用示例
> 此处以简单支付接口为例，演示业务系统如何调用支付网关进行支付，其他接口的调用方式类似，具体请参考[支付对接](https://bootx.gitee.io/daxpay/gateway/overview/接口清单.html)。

```java
package cn.bootx.platform.daxpay.sdk;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import org.junit.Before;
import org.junit.Test;

/**
 * 简单支付
 * @author xxm
 * @since 2024/2/2
 */
public class SimplePayOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9000")
                // 需要跟网关中配置一致
                .signSecret("123456")
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void simplePay() {
        // 简单支付参数
        SimplePayParam param = new SimplePayParam();
        param.setBusinessNo("P0001");
        param.setAmount(1);
        param.setTitle("测试支付宝支付");
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setPayWay(PayWayEnum.QRCODE.getCode());

        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param, true);
        System.out.println(execute);
        PayOrderModel data = execute.getData();
        System.out.println(data);
    }
}
```

## 🍎 系统截图

### H5收银台演示
![h5.png](https://s11.ax1x.com/2024/02/12/pF8nPMV.png)
### 支付演示
![pay.png](https://s11.ax1x.com/2024/02/12/pF8np2q.png)
### 收银台演示
![pc.jpg](https://s11.ax1x.com/2024/02/12/pF8n9x0.jpg)
### 支付通道配置
![](https://s11.ax1x.com/2024/02/13/pF8s2VS.jpg)

## 🛣️ 路线图
[**开发进度和任务池**](/_doc/Task.md)

[**更新记录**](/_doc/ChangeLog.md)

### 2.0.X版本:
- [ ] 对账比对功能实现
- [ ] 支持转账、分账操作
- [ ] 云闪付支付支持
- [ ] 支付宝和微信增加V3版本接口支持
- [ ] 消息通知支持消息中间件模式

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

微信扫码加入微信交流群
<p>
<img alt="微信图片_20240226144703" height="500" src="https://jsd.cdn.zzko.cn/gh/xxm1995/picx-images-hosting@master/connect/3bf09e365c43146b158d01c97f00d55.8aczzvuovi.webp" width="330"/>
</p>


## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](_doc/images/jetbrains.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0
