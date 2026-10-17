package cn.daxpay.open.channel.fubei.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 付呗通道订单同步响应(主应用侧)
///
/// 子应用调用付呗 fbpay.order.query 后回传给主应用,
/// tradeState 为付呗原始订单状态(order_status: SUCCESS 成功 / 其他处理中)。
@Data
@Accessors(chain = true)
public class FubeiSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 付呗订单号(order_sn)
    private String outOrderNo;

    /// 付呗订单状态(order_status 原始码)
    private String tradeState;

    /// 用户实付金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(finish_time, 成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(user_id, 成功时返回)
    private String buyerId;

    /// 渠道外部交易号(channel_order_sn, 成功时返回)
    private String outTransNo;

    /// 原始响应(用于对账/排障)
    private String syncData;
}
