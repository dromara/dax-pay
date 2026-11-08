package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;

/// # Mock 通道支付请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockPayReq` 镜像, 经声明式 HTTP 客户端
/// [cn.daxpay.open.channel.mock.client.MockChannelClient] 序列化传输, 字段对齐;
/// 金额单位恒为分(Long), 禁止 BigDecimal/String 漂移。
@Data
public class MockPayReq {

    /// 商户订单号(主应用支付交易号透传)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 支付方式: qr=扫码(挂起, 返回确认页地址) / bar=付款码(即时定结果)
    /// 兼容字段, 首选 [way](qr→QR、bar→BARCODE 映射; 与 way 同时传时 way 优先)
    private String method;

    /// 支付渠道: WECHAT/ALIPAY/UNION/DOUYIN(子应用枚举名);
    /// 由统一下单 [cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum] 映射(UNION_PAY→UNION), 子应用无对应值时留空
    private String provider;

    /// 支付方式: QR/SCAN/BARCODE/H5/APP/MP/JSAPI/MINI_APP(子应用枚举名),
    /// 由统一下单 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 映射; 与 method 同时传时 way 优先
    private String way;

    /// 付款码码值(被扫场景透传 NormalPayParam.authCode; 子应用仅落脱敏留痕并按前缀识别渠道)
    private String authCode;

    /// 订单标题
    private String subject;

    /// 商户业务订单号(商户侧订单号透传, mock 侧留痕展示用)
    private String bizOrderNo;

    /// 支付描述(商户传入的订单描述, 可空)
    private String description;

    /// 商品明细(主应用序列化好的商品明细 JSON 数组串, 原样透传不解析不加工, 可空)
    private String goodsDetail;

    /// 商户附加参数(回调时原样返回, 可空)
    private String attach;

    /// 通道应用 AppId(优先请求显式指定的 channelAppId, 空则回落容器应用号快照, 可空)
    private String appId;

    /// 服务商应用 sp_appid(仅 MOCK_ISV 微信 JSAPI/小程序方式下传, 经
    /// [cn.daxpay.open.payment.wx.facade.WxAppFacade#resolveIsvPair] 解析; 直连不传)。
    /// 对齐商业版契约; one 侧镜像类与模拟网关支付契约暂无该字段(未知字段忽略), 承接前不影响现有链路
    private String spAppId;

    /// 子商户应用 sub_appid(同 sp_appid 场景下传, 未绑定商户档应用时不传)
    private String subAppId;

    /// 买家 openid(微信 JSAPI/小程序方式必填, 子应用校验链按 appId+openId 双维反查档案; 其余方式可空)
    private String openId;

    /// 订单过期时间(格式 yyyy-MM-dd HH:mm:ss, 可空)
    private String expireTime;

    /// 主应用回调地址(mock 扮演三方在支付终态时回推)
    private String notifyUrl;

    /// 商户号(透传, 可选, mock 侧留痕展示用)
    private String mchNo;

    /// 通道商户号(透传, 可选, mock 侧留痕展示用)
    private String channelMchNo;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
