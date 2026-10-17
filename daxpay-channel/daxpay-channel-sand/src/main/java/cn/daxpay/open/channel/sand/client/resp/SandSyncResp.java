package cn.daxpay.open.channel.sand.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 杉德通道订单同步响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class SandSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 杉德订单流水号(sandSerialNo)
    private String tradeNo;

    /// 杉德订单状态(orderStatus 原始码)
    private String tradeState;

    /// 订单金额(单位: 分, 成功时返回)
    private Long amount;

    /// 完成时间(成功时返回)
    private OffsetDateTime finishTime;

    /// 买家支付账号(成功时返回)
    private String buyerId;

    /// 渠道方订单号(成功时返回)
    private String outTransNo;

    /// 渠道方流水号(成功时返回)
    private String outTransSerialNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
