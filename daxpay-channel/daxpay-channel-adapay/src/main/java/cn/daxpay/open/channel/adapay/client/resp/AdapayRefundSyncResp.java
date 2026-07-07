package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下通道退款同步响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapayRefundSyncResp {

    /// 退款单号(回显)
    private String outRefundNo;

    /// 统一退款状态(SUCCESS / PROGRESS / CLOSED)
    private String refundStatus;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 退款完成时间
    private String finishTime;
}
