package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;

/// # Mock 通道关单响应
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockCloseResp` 镜像, 字段对齐。
/// 已支付订单关闭、订单不存在等场景由 mock 侧返回业务失败(DaxResult 透传)。
@Data
public class MockCloseResp {

    /// 商户订单号(透传 Req.outTradeNo)
    private String outTradeNo;

    /// Mock 通道交易号
    private String tradeNo;

    /// 是否已关闭(含幂等关闭)
    private Boolean closed;
}
