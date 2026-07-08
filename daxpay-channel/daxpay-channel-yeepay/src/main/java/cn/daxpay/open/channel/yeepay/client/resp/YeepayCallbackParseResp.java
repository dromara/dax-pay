package cn.daxpay.open.channel.yeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易宝回调验签解析响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepayCallbackParseResp {

    /// 验签(解密)是否通过
    private boolean verified;

    /// 回调类型(PAY / REFUND)
    private String tradeType;

    /// 商户订单号
    private String outTradeNo;

    /// 退款单号(退款回调)
    private String outRefundNo;

    /// 统一交易状态(SUCCESS / FAIL / PROGRESS)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 实付/实退金额(单位: 分)
    private Long realAmount;

    /// 支付/退款完成时间
    private OffsetDateTime finishTime;

    /// 买家标识(支付回调)
    private String buyerId;

    /// 支付厂商(支付回调)
    private String targetSys;

    /// 第三方订单号
    private String targetOrderId;
}
