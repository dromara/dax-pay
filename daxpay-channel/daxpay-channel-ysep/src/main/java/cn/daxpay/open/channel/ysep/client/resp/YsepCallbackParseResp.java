package cn.daxpay.open.channel.ysep.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 银盛回调验签解析响应(子应用 → 主应用)
///
/// 与子应用 dax-pay-channel-two 的 `YsepCallbackParseResp` 镜像。
/// 银盛两产品退款无独立异步通知, 仅支付回调使用本对象。
@Data
@Accessors(chain = true)
public class YsepCallbackParseResp {

    /// 是否验签通过
    private Boolean success;

    /// 平台订单号(= 下单时传入的 tradeNo)
    private String outTradeNo;

    /// 银盛交易单号
    private String tradeNo;

    /// 交易状态(银盛原始状态)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 买家标识
    private String buyerId;

    /// 渠道接收流水号
    private String channelRecvSn;

    /// 渠道发送流水号
    private String channelSendSn;

    /// 完成时间(东八区 OffsetDateTime)
    private OffsetDateTime finishTime;

    /// 错误描述(验签失败或解析异常时填充)
    private String errorMsg;

    /// 原始回调参数
    private String syncData;
}
