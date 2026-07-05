package cn.daxpay.open.channel.ums.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务回调验签解析响应
@Data
@Accessors(chain = true)
public class UmsCallbackParseResp {

    /// 验签是否通过
    private boolean verified;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号
    private String outTradeNo;

    /// 退款单号(退款回调)
    private String outRefundNo;

    /// 统一交易状态
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 实付/实退金额(单位: 分)
    private Long realAmount;

    /// 支付/退款完成时间
    private String finishTime;

    /// 买家标识
    private String buyerId;

    /// 支付厂商
    private String targetSys;

    /// 第三方订单号
    private String targetOrderId;
}
