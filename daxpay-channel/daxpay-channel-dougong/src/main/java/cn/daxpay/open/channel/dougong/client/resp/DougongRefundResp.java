package cn.daxpay.open.channel.dougong.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 斗拱通道退款响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongRefundResp` 镜像, 字段对齐。
@Data
public class DougongRefundResp {

    /// 商户退款单号
    private String outRefundNo;

    /// 汇付退款流水号(hf_seq_id)
    private String tradeNo;

    /// 是否已完成
    private Boolean complete;

    /// 退款完成时间
    private OffsetDateTime finishTime;
}
