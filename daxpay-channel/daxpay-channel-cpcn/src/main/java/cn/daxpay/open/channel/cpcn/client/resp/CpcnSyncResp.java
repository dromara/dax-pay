package cn.daxpay.open.channel.cpcn.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 中金通道订单同步响应(主应用侧)
///
/// 子应用调用中金 5016 查单后回传给主应用, tradeState 为中金原始交易状态
/// (Status: 10 处理中 / 30 成功 / 40 失败)。
@Data
@Accessors(chain = true)
public class CpcnSyncResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 中金订单号(OrderNo)
    private String outOrderNo;

    /// 中金交易状态(Status 原始码)
    private String tradeState;

    /// 交易金额(单位: 分, 成功时返回)
    private Long realAmount;

    /// 完成时间(ResponseTime, 成功时返回)
    private OffsetDateTime finishTime;

    /// 买家标识(PayerID, 成功时返回)
    private String buyerId;

    /// 中金流水号(TraceNo, 成功时返回)
    private String outTransNo;

    /// 原始响应明文(用于对账/排障)
    private String syncData;
}
