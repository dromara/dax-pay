package cn.daxpay.open.channel.xingyi.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 星驿付通道退款响应(主应用侧)
///
/// 星驿付退款为同步受理即成功(无退款异步通知), 受理成功即置终态。
@Data
@Accessors(chain = true)
public class XingyiRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 星驿付退款流水号(orderFlowNo)
    private String tradeNo;

    /// 是否已终态完成(受理成功恒 true)
    private Boolean complete;

    /// 实际退款金额(单位: 分, realRefundAmt)
    private Long refundAmount;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
