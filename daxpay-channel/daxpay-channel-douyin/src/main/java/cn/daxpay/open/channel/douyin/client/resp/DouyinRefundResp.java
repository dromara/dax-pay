package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;

/// # 抖音通道退款响应
@Data
public class DouyinRefundResp {
    /// 退款单号(回显)
    private String outRefundNo;
    /// 抖音退款单号(refundId)
    private String refundId;
    /// 退款状态(SUCCESS / PROCESSING / CLOSED / ABNORMAL)
    private String refundStatus;
    /// 退款完成时间(RFC3339)
    private String finishTime;
}
