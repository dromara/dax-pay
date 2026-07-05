package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;

/// # 抖音回调验签解析响应
@Data
public class DouyinCallbackParseResp {
    /// 回调类型(PAY / REFUND)
    private String tradeType;
    /// 商户订单号
    private String outTradeNo;
    /// 抖音交易号(支付回调)
    private String transactionId;
    /// 退款单号(退款回调)
    private String outRefundNo;
    /// 抖音退款单号(退款回调)
    private String refundId;
    /// 交易状态(支付回调)
    private String tradeState;
    /// 退款状态(退款回调)
    private String refundStatus;
    /// 金额(单位: 分)
    private Long amount;
    /// 成功时间
    private String successTime;
    /// 买家 openid
    private String openid;
    /// 验签是否通过
    private boolean verified;
}
