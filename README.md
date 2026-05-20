<p align="center">
	<img src="_doc/images/dax-pay.svg" width="45%">
</p>

<p align="center">
 <img src="https://img.shields.io/github/stars/dromara/dax-pay?style=flat&label=Github">
 <img src='https://gitee.com/bootx/dax-pay/badge/star.svg?theme=dark' alt='star'/>
 <img src="https://img.shields.io/badge/Dax%20Pay-3.1.0-success.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Author-Daxpay-orange.svg" alt="Build Status"/>
 <img src="https://img.shields.io/badge/Spring%20Boot-3.5.6-blue.svg" alt="Downloads"/>
 <img src="https://img.shields.io/badge/license-Apache%20License%202.0-green.svg"/>
</p>

# Dromara Dax-Pay(开源版)

## 使用须知

`DaxPay`开源版是一款基于`Apache License 2.0`协议分发的开源软件，受中华人民共和国相关法律法规的保护和限制，可以在符合[《用户授权使用协议》](用户授权使用协议.txt)和
[《Apache License 2.0》](LICENSE)开源协议情况下进行免费使用、学习和交流。**在使用前请阅读上述协议，如果不同意请勿进行使用。**

## 特色功能
- 支持支付、退款、转账等支付相关的核心能力
- 提供商户端、运营端，支持多商户和服务商模式
- 封装各通道为统一的`HTTP`接口，方便业务系统进行对接，简化对接多种支付方式的复杂度
- 接口请求和响应数据支持RSA公私钥签名机制，保证交易安全可靠


## 核心技术栈
| 名称        | 描述         | 版本要求            |
| ----------- | ------------ | ------------------- |
| JDK         | Java环境     | 21+                 |
| Spring Boot | 开发框架     | 3.5.x               |
| Redis       | 分布式缓存   | 7.x版本及以上       |
| Postgresql  | 数据库       | Postgresql 14及以上 |
| Vue         | 前端框架     | 3.x                 |
| UniApp      | 跨端开发工具 | 项目内定义          |

## 源码和演示地址

### 项目地址

> 在 [DaxPay开源文档站](https://yibeiguangnian.feishu.cn/wiki/space/7587280618229074889?ccm_open_type=lark_wiki_spaceLink&open_tab_from=wiki_home) 可以进行查阅相关文档


| 项目         | GITEE                                       | GITHUB                                          | GITCODE                                        |
| ------------ | ------------------------------------------- | ----------------------------------------------- | ---------------------------------------------- |
| 后端地址     | [GITEE](https://gitee.com/dromara/dax-pay)  | [GITHUB](https://github.com/dromara/dax-pay)    | [GITCODE](https://gitcode.com/dromara/dax-pay) |
| Web前端地址  | [GITEE](https://gitee.com/bootx/dax-pay-ui) | [GITHUB](https://github.com/xxm1995/dax-pay-ui) |                                                |
| 网关前端地址 | [GITEE](https://gitee.com/bootx/dax-pay-h5) | [GITHUB](https://github.com/xxm1995/dax-pay-h5) |                                                |

### 演示环境
> 注：演示账号部分功能权限未开放。
>
> - 启动后默认的账号密码为：bootx/123123
> - 演示环境暂时停用了，待后期更新

| 站点类型    | 地址 | 演示账号 |
|---------|------|---------|
| 运营端     | [https://admin.open.daxpay.cn/](https://admin.open.daxpay.cn/) | csadmin/123123 |
| 商户端     | [https://merchant.open.daxpay.cn/](https://merchant.open.daxpay.cn/) | cssh/123123 |
| 小程序(H5) | [https://mini.open.daxpay.cn/](https://mini.open.daxpay.cn/) | cssh/123123 |
| 收银台小程序 | 暂时未上架 | 无 |


## 商业版
> 官网地址: [https://plus.daxpay.cn](https://plus.daxpay.cn) 文档地址：[飞书文档站](https://yibeiguangnian.feishu.cn/wiki/space/7512817895487848452?ccm_open_type=lark_wiki_spaceLink&open_tab_from=wiki_home)
> 
> 针对一些需要较高的客户，提供商业版进行选择，相对于开源版，功能更强大。

### 功能模块

| 典型功能  | 描述                    |
|-------|-----------------------|
| 进件    | 支持发起进件申请和对已经进件的商户进行管理 |
| 分账    | 支持根据设置对支付订单进行分账       |
| 分润    | 根据设定的费率进行分润处理         |
| 代理商系统 | 支持代理商模式               |
| 终端报备  | 给通道侧进行辅助支付设备的宝贝       |
| 费率管理  | 管理商户进件和分润时所使用的费率      |

### 系统演示

> 注：演示账号部分功能权限未开放。

| 站点类型      | 地址                                                                       | 演示账号           |
|-----------|--------------------------------------------------------------------------|----------------|
| 运营端       | [https://admin.plus.daxpay.cn/](https://admin.plus.daxpay.cn/)           | csadmin/123123 |
| 代理端       | [https://agent.plus.daxpay.cn/](https://agent.plus.daxpay.cn/)           | csdl/123123    |
| 商户端       | [https://merchant.plus.daxpay.cn/](https://merchant.plus.daxpay.cn/)     | cssh/123123    |
| 代理小程序(H5) | [https://mini-agent.plus.daxpay.cn/](https://mini-agent.plus.daxpay.cn/) | csdl/123123    |
| 商户小程序(H5) | [https://mini-mch.plus.daxpay.cn/](https://mini-mch.plus.daxpay.cn/)     | cssh/123123    |
| 收银台小程序    | 暂时未上架                                                                    | 无              |

## 系统截图

<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/ScreenShot_2026-01-30_114018_880.4ubfwj80o3.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/image-(1).4ubfwj80nq.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/image.b9etk4rbh.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/image.6m4erfrdju.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/86fdffc1177a50638196482707da070c.5fl3iu2gyd.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/b1d35c4d2b44d610cf86de729a3e7c3f.4jom3dssip.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/channel-DswG0sBC.1lcbzvlbfb.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/70779c3d5c2810817b6e935acf18a1aa.szgi53xb3.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/f218d9ade6c2a283ad05a71a9cb26b0d.32ih1monry.webp" height="570" />
<img src="https://cdn.jsdmirror.cn/gh/xxm1995/picx-images-hosting@master/20260130/d9a4327a85e97e849534f09ea9c25966.7w7bxr9cv0.webp" height="570" />

##   关于我们

### QQ交流群

>  扫码加入QQ交流群: 879409917

<p>
<img src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/1733360741745_d.83a33entp3.webp" width = "330" height = "500"/>
</p>

### 微信交流群

> 微信扫码加小助手拉群: sdcit2020

<p>
<img alt="微信图片_20240226144703" height="480" src="https://cdn.jsdmirror.com/gh/xxm1995/picx-images-hosting@master/connect/微信图片_20240412152722.231nkeje2o.webp" width="330"/>
</p>
### 微信公众号

> 微信公众号会定期更新使用教程、版本更新记录和各种活动情况，欢迎关注

<p>
<img alt="微信图片_20240226144703" height="330" src="https://plus.daxpay.cn/assets/other/wxpub.webp" width="330" height="330"/>
</p>

### Star History

[![Stargazers over time](https://starchart.cc/dromara/dax-pay.svg?variant=adaptive)](https://starchart.cc/dromara/dax-pay)


##  鸣谢

感谢其他提供灵感和思路的开源项目

[部分参考的开源项目和开源许可列表](./_license/LICENSE.md)


## License

Apache License Version 2.0
