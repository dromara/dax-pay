<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-3.0.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-3.3.4-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(单商户多应用版)

## ❗使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## 🍈项目介绍

> DaxPay是一套开源支付网关系统，已经对接支付宝、微信支付、云闪付相关的接口。可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响。

## 🧭 特色功能
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和`云闪付`相关的接口，并以扩展包的方式支持更多类型的通道
- 支持多应用配置，可以同时对接多个支付通道账号，方便多个业务系统对接
- 支持支付、退款、对账、分账等支付相关的能力
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，保证交易安全可靠
- 提供管理端，方便运营人员进行管理和操作

## 📃 文档和源码地址
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
| H5前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |                                                |


## 🏬 系统演示
### 管理平台:
> 注：演示账号部分功能权限未开放。

地址：https://single.web.daxpay.cn

账号：daxpay

密码：daxpay123

## 🥞 核心技术栈
| 名称               | 描述     | 版本要求                         |
|------------------|--------|------------------------------|
| Jdk              | Java环境 | 21+                          |
| Spring Boot      | 开发框架   | 3.3.x                        |
| Redis            | 分布式缓存  | 5.x版本及以上                     |
| MySQL/Postgresql | 数据     | MySQL8.x及以上/Postgresql 10及以上 |
| Vue              | 前端框架   | 3.x                          |

## 🛠️ 业务系统接入
> 业务系统想接入支付网关的话，不需要集成到业务系统里，只需要单独部署一份支付系统，然后业务系统通过接口调用即可拥有对应的支付能力，
不会对原业务系统的架构产生影响。如果是Java项目，可以使用SDK简化接入流程， 其他语言可以参照中的说明使用HTTP接口方式接入。

### Java客户端SDK
> SDK版本号与支付网关的版本保持一致，如果需要使用，请在pom.xml中添加如下依赖。SDK使用方式参考[SDK使用说明](https://daxpay.dromara.org/single/gateway/overview/SDK使用说明.html)。

```xml
 <!-- 支付SDK -->
<dependency>
    <groupId>org.dromara.daxpay</groupId>
    <artifactId>daxpay-single-sdk</artifactId>
    <version>${latest.version}</version>
</dependency>
```
### SDK调用示例
```java
package org.dromara.daxpay.single.sdk.test.trade;

import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.code.PayMethodEnum;
import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.model.trade.pay.PayResultModel;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.channel.AlipayParam;
import org.dromara.daxpay.single.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.single.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.dromara.daxpay.single.sdk.util.PaySignUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 统一支付接口
 * @author xxm
 * @since 2024/2/5
 */
public class PayOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("123")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 微信支付(二维码扫码)
     */
    @Test
    public void wxQrPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信扫码支付");
        param.setDescription("这是支付备注");
        param.setAmount(BigDecimal.valueOf(1.00));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.hmacSha256Sign(execute, "123456"));
    }
}

```

## 🍎 系统截图
### PC收银台演示(旧版)
![](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20240513192801.2ruycydkl6.webp)
### 移动端收银演示

<img height="900" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信图片_20241012172346.41y1kcemrf.webp" width="390"/>

### 支付通道配置
![](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20241012170024.5tr0f8xzn9.webp)
### 开发联调功能
![](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20241012165858.231uu094fm.webp)
### 对账单文件
![](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20241012170315.6wqpq4ttix.webp)
## 🛣️ 路线图

[**当前开发进度和任务池**](/_doc/Task.md)

[**历史更新记录**](/_doc/Changelog.md)

##  🥪 关于我们

扫码加入QQ交流群

交流三群: 879409917
<p>
<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/1733360741745_d.83a33entp3.webp" width = "330" height = "500"/>
</p>

扫码加入钉钉交流群: [加群连接](https://qr.dingtalk.com/action/joingroup?code=v1,k1,AzkcWLa8J/OHXi+nTWwNRc68IAJ0ckWXEEIvrJofq2A=&_dt_no_comment=1&origin=11)
<p>
<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/png-(1).7egk526qnp.webp" width = "400" height = "400"/>
</p>

微信扫码加小助手拉群: sdcit2020
<p>
<img alt="微信图片_20240226144703" height="480" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
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
