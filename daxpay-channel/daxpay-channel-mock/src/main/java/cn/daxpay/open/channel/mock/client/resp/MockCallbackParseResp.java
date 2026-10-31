package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # Mock 通道回调解析响应(mock → 主应用)
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockCallbackParseResp` 镜像, 字段对齐。
/// 回调由 one 侧按平台公钥验签(失败抛业务异常不构成本响应), 解析通过 success 为 true;
/// 支付/退款回调共用本形态, outRefundNo/refundId 仅退款回调有值。
@Data
public class MockCallbackParseResp {

    /// 是否解析通过(验签与字段校验通过即 true)
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(mock 侧存主应用支付交易号)
    private String outTradeNo;

    /// Mock 支付通道交易号(原支付单 tradeNo)
    private String tradeNo;

    /// 商户退款号(退款回调)
    private String outRefundNo;

    /// Mock 通道退款流水号(退款回调)
    private String refundId;

    /// 交易状态(SUCCESS / FAIL)
    private String status;

    /// 金额(单位: 分; 支付回调=订单金额, 退款回调=退款金额)
    private Long amount;

    /// 完成时间(带偏移的 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 商户业务订单号(下单透传回传, 报文未携带为 null)
    private String bizOrderNo;

    /// 商户附加参数(下单透传原样回传, 报文未携带为 null)
    private String attach;

    /// 买家 openid(微信 JSAPI/小程序方式下单才有值, 供主应用回填订单 buyerId;
    /// 其余方式/退款回调/旧报文为 null)
    private String openId;

    /// 子商户号(ISV 模式下单留痕回传, 报文未携带为 null)
    private String subMchId;

    /// 实际付款钱包(聚合扫码单才有值: WECHAT/ALIPAY/UNION/DOUYIN; 分钱包单/退款回调/旧报文为 null)
    ///
    /// 拟真聚合通道回调的 buyer_pay_type: 一码多付付款完成后回传实际钱包,
    /// 平台侧回填订单/资金凭证的 provider(按付款钱包维度看资金)。
    private String provider;
}
