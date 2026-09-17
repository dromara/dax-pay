package cn.daxpay.open.channel.sheng.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 盛付通通道退款查询响应(主应用侧, 与子应用镜像)
@Data
public class ShengRefundSyncResp {

    private String outRefundNo;
    private String tradeNo;
    /// 退款状态: SUCCESS / FAIL / PROCESSING
    private String refundStatus;
    /// 退款金额(单位: 分)
    private Long refundAmount;
    private OffsetDateTime finishTime;
    private String syncData;
}
