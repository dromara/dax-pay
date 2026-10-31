package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;

import java.time.OffsetDateTime;

/// # Mock 通道同步响应
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockSyncResp` 镜像, 字段对齐;
/// 金额单位恒为分(Long)。tradeStatus 取值: SUCCESS / FAIL / WAIT_BUYER_PAY / CLOSED,
/// 状态映射由 P2 的 MockSyncService 完成。
@Data
public class MockSyncResp {

    /// 商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;

    /// Mock 通道交易号
    private String tradeNo;

    /// 交易状态(SUCCESS / FAIL / WAIT_BUYER_PAY / CLOSED)
    private String tradeStatus;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 终态时间(成功/失败时返回)
    private OffsetDateTime finishTime;

    /// 实际付款钱包(聚合扫码单才有值: WECHAT/ALIPAY/UNION/DOUYIN; 其余方式与未选择钱包时为 null)
    ///
    /// 聚合支付一码多付语义: 下单渠道为聚合(aggregate_pay), 付款完成后回传实际使用的钱包,
    /// 平台侧回填订单/资金凭证的 provider(按付款钱包维度看资金)。
    private String provider;
}
