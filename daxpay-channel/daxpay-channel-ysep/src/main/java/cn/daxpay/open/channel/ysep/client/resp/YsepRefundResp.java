package cn.daxpay.open.channel.ysep.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 银盛通道退款响应(主应用侧, 与子应用镜像)
@Data
@Accessors(chain = true)
public class YsepRefundResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 银盛退款单号
    private String tradeNo;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 是否已完成(退款成功时为 true)
    private Boolean complete;

    /// 退款状态原始码(诊断用)
    private String refundStatus;

    /// 退款完成时间
    private OffsetDateTime finishTime;
}
