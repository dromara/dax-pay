package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # Mock 通道退款同步响应
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockRefundResp` 形态一致
/// (子应用 /refund-sync 端点返回退款单同形态), 主应用侧单独建类以便同步链路独立演进;
/// 金额单位恒为分(Long)。refundStatus 取值: SUCCESS / FAIL / PROCESSING,
/// 状态映射由 P2 的 MockRefundSyncService 完成。
@Data
public class MockRefundSyncResp {

    /// 原支付单商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;

    /// 商户退款号(透传 Req.outRefundNo)
    private String outRefundNo;

    /// Mock 通道退款流水号(MOCKR 前缀)
    private String refundId;

    /// 退款状态(SUCCESS / FAIL / PROCESSING)
    private String refundStatus;

    /// 是否已终态完成(仅 refundStatus=SUCCESS 为 true)
    private Boolean complete;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 终态时间(成功/失败时返回)
    private OffsetDateTime finishTime;
}
