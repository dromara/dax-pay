package cn.daxpay.open.channel.fuyou.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 富友回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-two 的 `FuyouCallbackParseResp` 镜像。
/// 兼容支付与退款两种回调: 通过 tradeType 区分。
///
/// 注意: outTradeNo 为富友 `mchnt_order_no`(关联订单号), 非平台交易号,
/// 主应用需凭 relationOrderNo 反查 PayTrade 得到真实 tradeNo。
@Data
@Accessors(chain = true)
public class FuyouCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 回调类型(PAY 支付回调 / REFUND 退款回调)
    private String tradeType;

    /// 关联订单号(mchnt_order_no, 主应用据此反查平台 tradeNo)
    private String outTradeNo;

    /// 富友交易号(transaction_id)
    private String tradeNo;

    /// 通道退款流水号(退款回调)
    private String outRefundNo;

    /// 交易状态(抽象态 SUCCESS / FAIL, 富友回调仅成功时通知, 验签通过即 SUCCESS)
    private String tradeStatus;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;
}
