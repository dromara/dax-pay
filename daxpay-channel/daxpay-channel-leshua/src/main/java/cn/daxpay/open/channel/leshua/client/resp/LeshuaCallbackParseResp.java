package cn.daxpay.open.channel.leshua.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 乐刷通道回调验签解析响应(主应用侧镜像)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaCallbackParseResp` 镜像, 字段对齐。
@Data
@Accessors(chain = true)
public class LeshuaCallbackParseResp {
    /// 验签是否通过 + 解析是否成功
    private Boolean success;
    /// 回调类型(PAY / REFUND)
    private String tradeType;
    /// 平台订单号(乐刷回调 third_order_id 回显)
    private String outTradeNo;
    /// 乐刷订单号
    private String leshuaOrderId;
    /// 交易状态
    private String tradeStatus;
    /// 金额(单位: 分)
    private Long amount;
    /// 实际金额(单位: 分)
    private Long realAmount;
    /// 完成时间
    private OffsetDateTime finishTime;
    /// 支付方式(仅支付回调)
    private String tradeWay;
    /// 买家标识(仅支付回调)
    private String buyerId;
    /// 通道外部交易号(仅支付回调)
    private String outTransNo;
    /// 错误描述
    private String errorMsg;
    /// 原始回调报文
    private String syncData;
}
