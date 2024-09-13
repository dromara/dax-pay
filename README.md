<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-2.0.8-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(开源支付系统-单商户版)
## ❗使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## ⚠️多商户版本开发中
> 当前单商户各种BUG问题将不会做特意的修复，使用者可以自行进行debug进行修改，后续新版单商户将基于多商户相同架构进行实现。

目前多商户版本已经进入开发中状态，为了更好的听取大家的建议，特建立一个征集需求建议的issues，欢迎提出各种功能需求和建议，填写地址：[功能和建议填写](https://gitee.com/dromara/dax-pay/issues/I9F3EO)


## 🍈项目介绍

> DaxPay是一套开源支付网关系统，已经对接支付宝、微信支付、云闪付相关的接口。可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响

## 🧭 特色功能
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和`云闪付`相关的接口，后续版本将支持`V3`版本的接口
- 支持支付、退款、对账、分账等支付相关的能力
- 提供`HTTP`方式接口调用能力，和`Java`版本的`SDK`，方便业务系统进行对接
- 接口请求和响应数据支持启用签名机制，保证交易安全可靠
- 提供管理平台，方便运营人员进行管理和操作，不需要懂IT技术也可以轻松使用
- 提供`聚合支付`、`电脑收银台`和`手机收银台`的演示模块，供开发者参考其实现支付功能的逻辑

## 📃 文档和源码地址
### 文档地址
在 [DaxPay文档站](https://doc.daxpay.cn/) 下的支付网关(DaxPay)模块下可以进行查阅相关文档，具体链接地址如下：
[快速指南](https://doc.daxpay.cn/single/guides/overview/项目介绍.html)、
[支付对接](https://doc.daxpay.cn/single/gateway/overview/接口清单.html)、
[操作手册](https://doc.daxpay.cn/single/admin/config/平台配置.html)

### 项目地址

| 项目      | GITEE                                       | GITHUB                                          |GITCODE                                          |
|---------|---------------------------------------------|-------------------------------------------------|---------------------------------------------|
| 后端地址    | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay)    |
| Web前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) | [GITCODE](https://github.com/daxpay/dax-pay-ui) |
| H5前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) | [GITCODE](https://github.com/daxpay/dax-pay-h5) |


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

## 🛠️ 业务系统接入
> 业务系统想接入支付网关的话，不需要集成到业务系统里，只需要单独部署一份支付系统，然后业务系统通过接口调用即可拥有对应的支付能力，
不会对原业务系统的架构产生影响。如果是Java项目，可以使用SDK简化接入流程， 其他语言可以参照中的说明使用HTTP接口方式接入。

### Java客户端SDK
> SDK版本号与支付网关的版本保持一致，如果需要使用，请在pom.xml中添加如下依赖。SDK使用方式参考[SDK使用说明](https://doc.daxpay.cn/single/gateway/overview/SDK使用说明.html)。

```xml
 <!-- 支付SDK -->
<dependency>
    <groupId>cn.daxpay.single</groupId>
    <artifactId>daxpay-single-sdk</artifactId>
    <version>${latest.version}</version>
</dependency>
```
### SDK调用示例
> 此处以支付接口为例，演示业务系统如何调用支付网关进行支付，其他接口的调用方式类似，具体请参考[支付对接](https://doc.daxpay.cn/single/gateway/overview/接口清单.html)。

```java
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
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 支付
     */
    @Test
    public void pay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("P0004");
        param.setTitle("测试接口支付");
        param.setChannel(PayChannelEnum.ALI.getCode());

        DaxPayResult<PayModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
        
    }
}
```

## 🍎 系统截图
### 收银台演示
![微信截图_20240326141126](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20240513192801.2ruycydkl6.webp)
### 驾驶舱
![QQ截图20240326141912](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/QQ截图20240326141912.60u0cpvjg5.webp)
### H5收银台演示
![h5](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/h5.839t0s61xr.webp)
### 支付通道配置
![微信截图_20240326142208](https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20240326142208.6bgu5vdv60.webp)
## 🛣️ 路线图
> dev为开发分支，本地运行请使用master或dev分支进行测试，当前正在进行整个系统的优化重构工作，各种功能都会有可能调整，
`V2.1.0`时将作为正式生产可用版本进行发布，之后会保证系统版本非大版本升级时，API接口和数据接口向前兼容。
**请勿在生产环境中使用，请等待生产可用的版本发布。如在使用，需要自己来保证应用的安全**

- 支持支付宝和微信V3版本接口
- 支持撤销、转账等更多支付接口
- 增加微信通知、钉钉通知、飞书通知能力
- 支持服务商模式，以及一些间连通道，如通联支付、易宝支付等，更好适应小微收单场景

[**当前开发进度和任务池**](/_doc/Task.md)

[**历史更新记录**](/_doc/ChangeLog.md)

##  🥪 关于我们

扫码加入QQ交流群

交流二群: 598644350
<p>
<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240513180310.2yy68aykip.webp" width = "330" height = "500"/>
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
