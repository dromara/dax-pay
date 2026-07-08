package cn.daxpay.open.channel.hmpay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 河马付通道退款响应(主应用侧)
@Data
public class HmpayRefundResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 杉德退款流水号(refund_plat_trx_no)
    private String tradeNo;

    /// 是否已完成
    private Boolean complete;

    /// 退款完成时间
    private OffsetDateTime finishTime;
}
