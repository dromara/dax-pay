package cn.daxpay.open.channel.sheng.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 盛付通通道退款响应(主应用侧, 与子应用镜像)
@Data
public class ShengRefundResp {

    private String outRefundNo;
    private String tradeNo;
    /// 退款状态(盛付通原始码: REFUND_INIT/REFUND_ING/REFUND_SUCCESS/REFUND_FAIL, 诊断用)
    private String refundStatus;
    private Boolean complete;
    private OffsetDateTime finishTime;
}
