package cn.daxpay.open.channel.hmpay.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 河马付通道退款查询响应(主应用侧)
@Data
public class HmpayRefundSyncResp {

    /// 退款状态(SUCCESS / FAIL / 其他)
    private String refundState;

    /// 商户退款单号
    private String outRefundNo;

    /// 杉德退款流水号(refund_plat_trx_no)
    private String tradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
