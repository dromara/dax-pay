package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信回调验签解析响应
///
/// 子应用使用微信 SDK 验签并解密回调后, 返回结构化业务数据。
/// 仅承载支付/退款回调; 转账回调见 [WechatTransferCallbackParseResp]。通过 tradeType 区分支付与退款。
@Data
@Accessors(chain = true)
public class WechatCallbackParseResp {

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 微信交易号(transactionId, 支付回调)
    private String transactionId;

    /// 退款单号(主应用退款单号, 退款回调)
    private String outRefundNo;

    /// 微信退款单号(refundId, 退款回调)
    private String refundId;

    /// 交易状态(支付回调: SUCCESS/REFUND/NOTPAY/CLOSED/REVOKED/USERPAYING/PAYERROR)
    private String tradeState;

    /// 退款状态(退款回调: SUCCESS/PROCESSING/CLOSED/ABNORMAL)
    private String refundStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 成功时间(RFC3339)
    private String successTime;

    /// 买家 openid
    private String openid;

    /// 验签是否通过
    private boolean verified;
}
