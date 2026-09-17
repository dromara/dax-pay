package cn.daxpay.open.channel.sheng.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 盛付通回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-two 的 `ShengCallbackParseResp` 镜像。
/// 兼容支付与退款两种回调: 通过 tradeType 区分(PAY / REFUND)。
@Data
@Accessors(chain = true)
public class ShengCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 原商户订单号(支付/退款回调均为原支付单号=平台支付交易号)
    private String outTradeNo;

    /// 商户退款单号(退款回调=平台退款关联单号; 支付回调为空)
    private String outRefundNo;

    /// 盛付通交易号(支付回调=transactionId / 退款回调=refundId)
    private String tradeNo;

    /// 交易金额(单位: 分)
    private Long amount;

    /// 交易状态(标准化: SUCCESS / FAIL / PROCESSING)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
