
<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-2.0.4-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Boot%20Platform-1.3.6-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Bootx-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(开源支付系统)
## ❗使用须知

`DaxPay`是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和 
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**


## 🍈项目介绍

> DaxPay是一套基于Bootx-Platform脚手架构建的开源支付网关系统，已经对接支付宝、微信支付相关的接口，以及扩展了钱包支付、储值卡支付、现金支付等新的支付方式。
> 可以独立部署，提供接口供业务系统进行调用，不对原有系统产生影响

## 🧭 特色功能
- 封装各类支付通道的接口为统一的接口，方便业务系统进行调用，简化对接多种支付方式的复杂度
- 已对接`微信支付`、`支付宝`和云闪付相关的接口，后续版本将支持`V3`版本的接口
- 支持组合支付，满足用户系统需要多种方式同时进行支付的场景。
- 支持支付、退款、对账、分账等支付相关的能力
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
                .signType(SignTypeEnum.HMAC_SHA256)
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
### 收银台演示
![微信截图_20240326141126](https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20240326141126.es9yupxd3.webp)
### 驾驶舱
![QQ截图20240326141912](https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/daxpay/QQ截图20240326141912.60u0cpvjg5.webp)
### H5收银台演示
![h5](https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/daxpay/h5.839t0s61xr.webp)
### 支付通道配置
![微信截图_20240326142208](https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/daxpay/微信截图_20240326142208.6bgu5vdv60.webp)
## 🛣️ 路线图
> 当前处于功能开发阶段，部分功能可能会有调整，`V2.1.0`时将作为正式生产可用版本进行发布，之后会保证系统版本非大版本升级时，API接口和数据接口向前兼容

[**当前开发进度和任务池**](/_doc/Task.md)

[**历史更新记录**](/_doc/ChangeLog.md)

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
<img alt="微信图片_20240226144703" height="500" src="https://cdn.jsdelivr.net/gh/xxm1995/picx-images-hosting@master/connect/7a1d9bc7257d1115063666cf2845a95.4cknp77ari.webp" width="330"/>
</p>

## 🍻 鸣谢
感谢 JetBrains 提供的免费开源 License：

[![JetBrains](_doc/images/jetbrains.png)](https://www.jetbrains.com/?from=bootx)

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## 🍷License

Apache License Version 2.0

# 🚀Dromara成员项目

<style>
    .member-project {
        display: flex;
        flex-wrap: wrap;
    }
    .member-project a {
        padding: 10px;
    }
    .member-project a img {
        height: 40px;
    }

</style>

<div class="member-project">
    <a href="https://gitee.com/dromara/TLog" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/tlog.png" alt="TLog" title="一个轻量级的分布式日志标记追踪神器，10分钟即可接入，自动对日志打标签完成微服务的链路追踪">
    </a>
    <a href="https://gitee.com/dromara/liteFlow" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/liteflow.png" alt="liteFlow" title="轻量，快速，稳定，可编排的组件式流程引擎">
    </a>
    <a href="https://hutool.cn/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/hutool.jpg" alt="hutool" title="小而全的Java工具类库，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。">
    </a>
    <a href="https://sa-token.cc/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/sa-token.png" alt="sa-token" title="一个轻量级 java 权限认证框架，让鉴权变得简单、优雅！">
    </a>
    <a href="https://gitee.com/dromara/hmily" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/hmily.png" alt="hmily" title="高性能一站式分布式事务解决方案。">
    </a>
    <a href="https://gitee.com/dromara/Raincat" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/raincat.png" alt="Raincat" title="强一致性分布式事务解决方案。">
    </a>
    <a href="https://gitee.com/dromara/myth" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/myth.png" alt="myth" title="可靠消息分布式事务解决方案。">
    </a>
    <a href="https://cubic.jiagoujishu.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/cubic.png" alt="cubic" title="一站式问题定位平台，以agent的方式无侵入接入应用，完整集成arthas功能模块，致力于应用级监控，帮助开发人员快速定位问题">
    </a>
    <a href="https://maxkey.top/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/maxkey.png" alt="maxkey" title="业界领先的身份管理和认证产品">
    </a>
    <a href="http://forest.dtflyx.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/forest-logo.png" alt="forest" title="Forest能够帮助您使用更简单的方式编写Java的HTTP客户端">
    </a>
    <a href="https://jpom.top/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/jpom.png" alt="jpom" title="一款简而轻的低侵入式在线构建、自动部署、日常运维、项目监控软件">
    </a>
    <a href="https://su.usthe.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/sureness.png" alt="sureness" title="面向 REST API 的高性能认证鉴权框架">
    </a>
    <a href="https://easy-es.cn/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/easy-es2.png" alt="easy-es" title="傻瓜级ElasticSearch搜索引擎ORM框架">
    </a>
    <a href="https://gitee.com/dromara/northstar" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/northstar_logo.png" alt="northstar" title="Northstar盈富量化交易平台">
    </a>
    <a href="https://hertzbeat.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/hertzbeat-brand.svg" alt="hertzbeat" title="易用友好的云监控系统">
    </a>
    <a href="https://dromara.gitee.io/fast-request/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/fast-request.png" alt="fast-request" title="Idea 版 Postman，为简化调试API而生">
    </a>
    <a href="https://www.jeesuite.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/mendmix.png" alt="mendmix" title="开源分布式云原生架构一站式解决方案">
    </a>
    <a href="https://gitee.com/dromara/koalas-rpc" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/koalas-rpc2.png" alt="koalas-rpc" title="企业生产级百亿日PV高可用可拓展的RPC框架。">
    </a>
    <a href="https://async.sizegang.cn/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/gobrs-async.png" alt="gobrs-async" title="配置极简功能强大的异步任务动态编排框架">
    </a>
    <a href="https://dynamictp.cn/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/dynamic-tp.png" alt="dynamic-tp" title="基于配置中心的轻量级动态可监控线程池">
    </a>
    <a href="https://www.x-easypdf.cn" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/x-easypdf.png" alt="x-easypdf" title="一个用搭积木的方式构建pdf的框架（基于pdfbox）">
    </a>
    <a href="http://dromara.gitee.io/image-combiner" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/image-combiner.png" alt="image-combiner" title="一个专门用于图片合成的工具，没有很复杂的功能，简单实用，却不失强大">
    </a>
    <a href="https://www.herodotus.cn/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/dante-cloud2.png" alt="dante-cloud" title="Dante-Cloud 是一款企业级微服务架构和服务能力开发平台。">
    </a>
    <a href="http://www.mtruning.club" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/go-view.png" alt="go-view" title="低代码数据可视化开发平台">
    </a>
    <a href="https://tangyh.top/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/lamp-cloud.png" alt="lamp-cloud" title="微服务中后台快速开发平台，支持租户(SaaS)模式、非租户模式">
    </a>
    <a href="https://www.redisfront.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/redis-front.png" alt="redis-front" title="RedisFront 是一款开源免费的跨平台 Redis 桌面客户端工具, 支持单机模式, 集群模式, 哨兵模式以及 SSH 隧道连接, 可轻松管理Redis缓存数据.">
    </a>
    <a href="https://www.yuque.com/u34495/mivcfg" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/electron-egg.png" alt="electron-egg" title="一个入门简单、跨平台、企业级桌面软件开发框架">
    </a>
    <a href="https://gitee.com/dromara/open-capacity-platform" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/open-capacity-platform.jpg" alt="open-capacity-platform" title="简称ocp是基于Spring Cloud的企业级微服务框架(用户权限管理，配置中心管理，应用管理，....)">
    </a>
    <a href="http://easy-trans.fhs-opensource.top/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/easy_trans.png" alt="Easy-Trans" title="Easy-Trans 一个注解搞定数据翻译,减少30%SQL代码量">
    </a>
    <a href="https://gitee.com/dromara/neutrino-proxy" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/neutrino-proxy.svg" alt="neutrino-proxy" title="一款基于 Netty 的、开源的内网穿透神器。">
    </a>
    <a href="https://chatgpt.cn.obiscr.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/chatgpt.png" alt="chatgpt" title="一个支持在 JetBrains 系列 IDE 上运行的 ChatGPT 的插件。">
    </a>
    <a href="https://gitee.com/dromara/zyplayer-doc" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/zyplayer-doc.png" alt="zyplayer-doc" title="zyplayer-doc是一款适合团队和个人使用的WIKI文档管理工具，同时还包含数据库文档、Api接口文档。">
    </a>
    <a href="https://gitee.com/dromara/payment-spring-boot" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/payment-spring-boot.png" alt="payment-spring-boot" title="最全最好用的微信支付V3 Spring Boot 组件。">
    </a>
    <a href="https://www.j2eefast.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/j2eefast.png" alt="j2eefast" title="J2eeFAST 是一个致力于中小企业 Java EE 企业级快速开发平台,我们永久开源!">
    </a>
    <a href="https://gitee.com/dromara/data-compare" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/dataCompare.png" alt="data-compare" title="数据库比对工具：hive 表数据比对，mysql、Doris 数据比对，实现自动化配置进行数据比对，避免频繁写sql 进行处理，低代码(Low-Code) 平台">
    </a>
    <a href="https://gitee.com/dromara/open-giteye-api" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/open-giteye-api.svg" alt="open-giteye-api" title="giteye.net 是专为开源作者设计的数据图表服务工具类站点，提供了包括 Star 趋势图、贡献者列表、Gitee指数等数据图表服务。">
    </a>
    <a href="https://gitee.com/dromara/RuoYi-Vue-Plus" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/RuoYi-Vue-Plus.png" alt="RuoYi-Vue-Plus" title="后台管理系统 重写 RuoYi-Vue 所有功能 集成 Sa-Token + Mybatis-Plus + Jackson + Xxl-Job + SpringDoc + Hutool + OSS 定期同步">
    </a>
    <a href="https://gitee.com/dromara/RuoYi-Cloud-Plus" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/RuoYi-Cloud-Plus.png" alt="RuoYi-Cloud-Plus" title="微服务管理系统 重写RuoYi-Cloud所有功能 整合 SpringCloudAlibaba Dubbo3.0 Sa-Token Mybatis-Plus MQ OSS ES Xxl-Job Docker 全方位升级 定期同步">
    </a>
    <a href="https://gitee.com/dromara/stream-query" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/stream-query.png" alt="stream-query" title="允许完全摆脱 Mapper 的 mybatis-plus 体验！封装 stream 和 lambda 操作进行数据返回处理。">
    </a>
    <a href="https://wind.kim/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/sms4j.png" alt="sms4j" title="短信聚合工具，让发送短信变的更简单。">
    </a>
    <a href="https://cloudeon.top/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/cloudeon.png" alt="cloudeon" title="简化kubernetes上大数据集群的运维管理">
    </a>
    <a href="https://github.com/dromara/hodor" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/hodor.png" alt="hodor" title="Hodor是一个专注于任务编排和高可用性的分布式任务调度系统。">
    </a>
    <a href="http://nsrule.com/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/test-hub.png" alt="test-hub" title="流程编排，插件驱动，测试无限可能">
    </a>
    <a href="https://gitee.com/dromara/disjob" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/disjob-2.png" alt="disjob" title="Disjob是一个分布式的任务调度框架">
    </a>
    <a href="https://gitee.com/dromara/binlog4j" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/Binlog4j.png" alt="binlog4j" title="轻量级 Mysql Binlog 客户端, 提供宕机续读, 高可用集群等特性">
    </a>
    <a href="https://gitee.com/dromara/yft-design" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/yft-design.png" alt="yft-design" title="基于 Canvas 的开源版 创客贴 支持导出json，svg, image文件。">
    </a>
    <a href="https://x-file-storage.xuyanwu.cn" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/logo3.svg" alt="x-file-storage" title="">
    </a>
    <a href="https://wemq.nicholasld.cn" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/WeMQ.png" alt="WeMQAQ" title="开源、高性能、安全、功能强大的物联网调试和管理解决方案。">
    </a>
    <a href="https://www.yuque.com/may-fly/mayfly-go" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/mayfly-go.png" alt="Mayfly-Go" title="web版 linux、数据库、redis、mongo统一管理操作平台。">
    </a>
    <a href="https://akali.yomahub.com" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/Akali.png" alt="Akali" title="Akali(阿卡丽)，轻量级本地化热点检测/降级框架，10秒钟即可接入使用！大流量下的神器">
    </a>
    <a href="https://dbswitch.gitee.io/docs-site/#/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/dbswitch.png" alt="dbswitch" title="异构数据库迁移同步工具，dbswitch提供源端数据库向目的端数据的全量与增量迁移同步功能">
    </a>
    <a href="https://gitee.com/dromara/easyAi" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/easyAi.png" alt="easyAi" title="java傻瓜ai框架，无需任何算法知识，通过简单的api调用就可以实现常用的图像内物体的识别，定位等图像ai服务，及自然语言分类处理服务。面向java开发程序员，不依赖任何第三方库，第三方接口，独立包。">
    </a>
    <a href="https://gitee.com/dromara/tianai-captcha" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/tianai-captcha.png" alt="tianai-captcha" title="可能是java界最好的开源行为验证码[滑块验证码、点选验证码、行为验证码、旋转验证码， 滑动验证码]">
    </a>
    <a href="https://dromara.org/zh/projects/" target="_blank">
        <img src="https://x-file-storage.xuyanwu.cn/assets/link/dromara.png" alt="dromara" title="让每一位开源爱好者，体会到开源的快乐。">
    </a>
</div>

