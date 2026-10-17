package cn.daxpay.open.channel.sand.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 杉德通道退款响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class SandRefundResp {

    /// 商户退款单号(透传)
    private String outRefundNo;

    /// 杉德退款订单流水号(sandSerialNo)
    private String tradeNo;

    /// 是否已终态完成(受理恒 false, 等待异步通知)
    private Boolean complete;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
