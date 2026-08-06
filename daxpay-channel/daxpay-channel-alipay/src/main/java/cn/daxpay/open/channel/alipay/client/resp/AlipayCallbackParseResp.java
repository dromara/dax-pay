package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付宝回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayCallbackParseResp` 镜像。
/// 仅承载支付/退款回调; 转账回调见 [AlipayTransferCallbackParseResp]。通过 tradeType 区分。
@Data
@Accessors(chain = true)
public class AlipayCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户订单号(支付回调: out_trade_no = 平台支付交易号; 退款回调: out_request_no = 退款单号)
    private String outTradeNo;

    /// 支付宝交易号(trade_no, 支付回调)
    private String tradeNo;

    /// 支付宝退款流水号(退款回调)
    private String outRefundNo;

    /// 交易状态(抽象态 SUCCESS / FAIL)
    private String tradeStatus;

    /// 金额(单位: 分, 元转分)
    private Long amount;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
