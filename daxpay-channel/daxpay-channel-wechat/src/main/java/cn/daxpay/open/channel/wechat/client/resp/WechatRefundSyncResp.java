package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道退款同步响应
///
/// 与子应用 dax-pay-channel-one 的 `WechatRefundSyncResp` 镜像, 字段对齐。
@Data
public class WechatRefundSyncResp {
    /// 退款状态(SUCCESS / CLOSED / PROCESSING / ABNORMAL)
    private String status;
    /// 微信退款单号(refund_id)
    private String refundId;
    /// 退款单号(out_refund_no)
    private String outRefundNo;
    /// 微信支付订单号(transaction_id)
    private String transactionId;
    /// 商户订单号(out_trade_no)
    private String outTradeNo;
    /// 退款完成时间(success_time)
    private OffsetDateTime finishTime;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    /// 用户退款金额(单位: 分)
    private Long payerRefund;
}
