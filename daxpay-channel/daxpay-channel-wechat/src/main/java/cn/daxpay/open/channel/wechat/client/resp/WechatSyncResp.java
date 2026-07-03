package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 微信通道同步响应
///
/// 与子应用 dax-pay-channel-one 的 `WechatSyncResp` 镜像, 字段对齐。
@Data
public class WechatSyncResp {
    /// 交易状态(SUCCESS / REFUND / NOTPAY / CLOSED / REVOKED / USERPAYING / PAYERROR / ACCEPT)
    private String tradeState;
    /// 交易状态描述
    private String tradeStateDesc;
    /// 微信支付订单号(transaction_id)
    private String transactionId;
    /// 商户订单号(out_trade_no)
    private String outTradeNo;
    /// 支付完成时间(success_time)
    private OffsetDateTime successTime;
    /// 订单总金额(单位: 分)
    private Long totalAmount;
    /// 用户支付金额(单位: 分)
    private Long payerTotal;
    /// 用户标识(openid)
    private String openId;
}
