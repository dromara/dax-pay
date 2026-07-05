package cn.daxpay.open.channel.ums.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务通道退款响应
@Data
@Accessors(chain = true)
public class UmsRefundResp {

    /// 退款单号(回显)
    private String outRefundNo;

    /// 退款状态(SUCCESS / PROCESSING / FAIL)
    private String refundStatus;

    /// 退款完成时间(yyyy-MM-dd HH:mm:ss)
    private String finishTime;
}
