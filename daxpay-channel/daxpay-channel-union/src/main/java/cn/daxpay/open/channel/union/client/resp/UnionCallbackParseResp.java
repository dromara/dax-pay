package cn.daxpay.open.channel.union.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付回调验签解析响应
@Data
@Accessors(chain = true)
public class UnionCallbackParseResp {

    /// 验签是否通过
    private boolean verified;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(支付回调: orderId)
    private String outTradeNo;

    /// 退款单号(退款回调: orderId)
    private String outRefundNo;

    /// 统一交易状态(SUCCESS / PROGRESS / CLOSED)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 实付/实退金额(单位: 分)
    private Long realAmount;

    /// 支付/退款完成时间(yyyyMMddHHmmss, 东八区)
    private String finishTime;

    /// 银联交易查询凭证(支付回调必填, 退款时作为 origQryId)
    private String queryId;

    /// 买家标识
    private String buyerId;
}
