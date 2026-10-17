package cn.daxpay.open.channel.sand.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 杉德通道回调验签解析响应(子应用 → 主应用)
///
/// 支付回调与退款回调共用本对象, 由 [#tradeType] 区分(PAY/REFUND)。
@Data
@Accessors(chain = true)
public class SandCallbackParseResp {

    /// 验签是否通过 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 商户侧单号(支付回调 = 原支付交易号; 退款回调 = 退款单号)
    private String outTradeNo;

    /// 杉德订单流水号(sandSerialNo)
    private String tradeNo;

    /// 订单状态(orderStatus: success / fail / 其他)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 买家支付账号
    private String buyerId;

    /// 渠道方订单号
    private String outTransNo;

    /// 渠道方流水号
    private String outTransSerialNo;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调明文(bizData JSON)
    private String syncData;
}
