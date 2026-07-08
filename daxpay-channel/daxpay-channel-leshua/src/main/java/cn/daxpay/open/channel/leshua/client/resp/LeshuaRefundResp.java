package cn.daxpay.open.channel.leshua.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 乐刷通道退款响应(主应用侧镜像)
@Data
@Accessors(chain = true)
public class LeshuaRefundResp {
    private String outRefundNo;
    private String leshuaRefundId;
    private String refundStatus;
    private Boolean complete;
    private OffsetDateTime finishTime;
}
