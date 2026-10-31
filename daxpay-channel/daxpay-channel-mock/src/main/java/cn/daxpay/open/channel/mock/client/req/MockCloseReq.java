package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;

/// # Mock 通道关单请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockCloseReq` 镜像, 字段对齐。
@Data
public class MockCloseReq {

    /// 商户订单号(主应用支付交易号透传)
    private String outTradeNo;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
