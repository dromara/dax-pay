package cn.daxpay.open.channel.lakala.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉通道退款查询响应(主应用侧, 与子应用镜像)
@Data
public class LakalaRefundSyncResp {

    private String outRefundNo;
    private String tradeNo;
    /// 退款状态: SUCCESS / FAIL / PROCESSING
    private String refundStatus;
    private OffsetDateTime finishTime;
    private String syncData;
}
