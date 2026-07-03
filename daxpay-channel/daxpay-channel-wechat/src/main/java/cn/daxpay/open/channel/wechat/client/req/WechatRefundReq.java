package cn.daxpay.open.channel.wechat.client.req;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import lombok.Data;

/// # 微信通道退款请求
///
/// 与子应用 dax-pay-channel-one 的 `WechatRefundReq` 镜像, 字段对齐。
@Data
public class WechatRefundReq {
    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;
    /// 微信支付订单号(transaction_id, 优先使用)
    private String transactionId;
    /// 退款单号(对应微信 out_refund_no)
    private String outRefundNo;
    /// 原订单总金额(单位: 分)
    private Long totalAmount;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    /// 退款原因(对应微信 reason)
    private String reason;
    /// 退款异步通知地址(可选)
    private String notifyUrl;
    /// 通道调用凭证
    private WechatSdkCredential credential;
}
