package cn.daxpay.open.channel.xingyi.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 星驿付通道订单同步响应(主应用侧)
///
/// tradeState 为星驿付原始订单状态(orderStatus: 1=支付成功, 其他=未支付/处理中)。
@Data
@Accessors(chain = true)
public class XingyiSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 星驿付订单号(orderNo)
    private String outOrderNo;

    /// 星驿付订单状态原始码(orderStatus)
    private String tradeState;

    /// 用户实付金额(单位: 分, 成功时返回 txamt)
    private Long realAmount;

    /// 完成时间(成功时返回 orderTime, 东八区)
    private OffsetDateTime finishTime;

    /// 买家标识(成功时返回 openId)
    private String buyerId;

    /// 通道支付流水号(成功时返回 payNo)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
