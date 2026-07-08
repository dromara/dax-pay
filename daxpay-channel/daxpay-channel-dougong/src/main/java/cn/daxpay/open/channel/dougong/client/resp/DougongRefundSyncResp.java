package cn.daxpay.open.channel.dougong.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 斗拱通道退款查询响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongRefundSyncResp` 镜像, 字段对齐。
@Data
public class DougongRefundSyncResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 汇付退款流水号(hf_seq_id)
    private String tradeNo;

    /// 退款状态(S / F / 其他)
    private String refundState;

    /// 退款金额(单位: 分)
    private Long amount;

    /// 实际退款金额(单位: 分)
    private Long realAmount;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
