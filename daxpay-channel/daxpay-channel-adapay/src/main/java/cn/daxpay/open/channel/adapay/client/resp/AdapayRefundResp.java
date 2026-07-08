package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 通道退款响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapayRefundResp {

    /// 退款单号(回显)
    private String outRefundNo;

    /// Adapay 退款对象 ID
    private String refundId;

    /// 退款状态(SUCCESS / PROCESSING / FAIL)
    private String refundStatus;

    /// 退款完成时间
    private String finishTime;
}
