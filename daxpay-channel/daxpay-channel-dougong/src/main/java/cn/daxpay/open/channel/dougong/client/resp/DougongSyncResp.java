package cn.daxpay.open.channel.dougong.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # 斗拱通道订单查询响应(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `DougongSyncResp` 镜像, 字段对齐。
@Data
public class DougongSyncResp {

    /// 汇付流水号(hf_seq_id)
    private String tradeNo;

    /// 商户订单号(req_seq_id)
    private String outTradeNo;

    /// 交易状态(SUCCESS / FAIL / CLOSE / 其他)
    private String tradeState;

    /// 支付方式(trans_type)
    private String tradeWay;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 实付金额(单位: 分)
    private Long realAmount;

    /// 买家标识
    private String buyerId;

    /// 通道外部交易号
    private String outTransNo;

    /// 完成时间
    private OffsetDateTime finishTime;

    /// 原始响应数据
    private String syncData;
}
