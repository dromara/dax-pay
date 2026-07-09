package cn.daxpay.open.channel.vbill.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 随行付回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-two 的 `VbillCallbackParseResp` 镜像。
/// 兼容支付与退款两种回调: 通过 tradeType 区分。
@Data
@Accessors(chain = true)
public class VbillCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(支付回调: ordNo = 平台支付交易号; 退款回调: 退款单号)
    private String outTradeNo;

    /// 随行付网关订单号(支付回调 uuid)
    private String tradeNo;

    /// 通道退款流水号(退款回调)
    private String outRefundNo;

    /// 交易状态(抽象态 SUCCESS / FAIL)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
