package cn.daxpay.open.channel.dougong.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 斗拱通道回调验签解析响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongCallbackParseResp` 镜像, 字段对齐。
@Data
public class DougongCallbackParseResp {

    /// 验签 + 解析是否成功
    private Boolean success;

    /// 回调类型(PAY / REFUND)
    private String tradeType;

    /// 商户单号(支付回调: 商户订单号; 退款回调: 退款号)
    private String outTradeNo;

    /// 汇付流水号(hf_seq_id)
    private String tradeNo;

    /// 交易状态(S 成功 / F 失败)
    private String tradeStatus;

    /// 金额(单位: 分)
    private Long amount;

    /// 实际金额(单位: 分)
    private Long realAmount;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 支付方式(仅支付回调: trans_type)
    private String tradeWay;

    /// 买家标识(仅支付回调)
    private String buyerId;

    /// 通道外部交易号(仅支付回调)
    private String outTransNo;

    /// 错误描述
    private String errorMsg;

    /// 原始 resp_data JSON
    private String syncData;
}
