package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;

/// # 抖音通道退款同步响应
@Data
public class DouyinRefundSyncResp {
    private String outRefundNo;
    /// 抖音退款单号
    private String refundId;
    /// 退款状态
    private String refundStatus;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    /// 退款完成时间
    private String finishTime;
    /// 查询失败错误码
    private String errorCode;
    /// 查询失败错误信息
    private String errorMsg;
}
