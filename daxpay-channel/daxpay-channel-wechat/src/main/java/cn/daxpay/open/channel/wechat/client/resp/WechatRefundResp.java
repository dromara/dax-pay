package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道退款响应
///
/// 与子应用 dax-pay-channel-one 的 `WechatRefundResp` 镜像, 字段对齐。
@Data
public class WechatRefundResp {
    /// 商户订单号
    private String outTradeNo;
    /// 微信支付订单号(transaction_id)
    private String transactionId;
    /// 退款单号(透传 WechatRefundReq.outRefundNo)
    private String outRefundNo;
    /// 微信退款单号(refund_id)
    private String refundId;
    /// 退款状态(SUCCESS / CLOSED / PROCESSING / ABNORMAL)
    private String status;
    /// 是否已终态完成(SUCCESS / CLOSED 为 true)
    private Boolean complete;
    /// 退款完成时间
    private OffsetDateTime finishTime;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    /// 用户退款金额(单位: 分)
    private Long payerRefund;
}
