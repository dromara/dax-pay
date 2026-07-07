package cn.daxpay.open.channel.vbill.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class VbillRefundResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 随行付网关退款单号(uuid)
    private String outRefundOrderNo;

    /// 是否已终态完成(REFUNDSUC / REFUNDFAIL 时为 true)
    private Boolean complete;

    /// 退款完成时间
    private OffsetDateTime finishTime;
}
