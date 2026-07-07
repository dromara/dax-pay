package cn.daxpay.open.channel.vbill.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VbillRefundSyncResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 随行付网关退款单号(uuid)
    private String outRefundOrderNo;

    /// 退款状态(REFUNDSUC / REFUNDFAIL / REFUNDING)
    private String refundStatus;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 同步原始数据
    private String syncData;
}
