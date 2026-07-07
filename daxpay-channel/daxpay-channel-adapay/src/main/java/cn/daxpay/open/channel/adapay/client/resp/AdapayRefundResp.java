package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下通道退款响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapayRefundResp {

    /// 退款单号(回显)
    private String outRefundNo;

    /// 汇付退款对象 ID
    private String refundId;

    /// 退款状态(SUCCESS / PROCESSING / FAIL)
    private String refundStatus;

    /// 退款完成时间
    private String finishTime;
}
