package cn.daxpay.open.channel.leshua.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 乐刷通道退款查询响应(主应用侧镜像)
@Data
@Accessors(chain = true)
public class LeshuaRefundSyncResp {
    private String outRefundNo;
    private String leshuaRefundId;
    private String refundStatus;
    private Long amount;
    private OffsetDateTime finishTime;
    private String syncData;
}
