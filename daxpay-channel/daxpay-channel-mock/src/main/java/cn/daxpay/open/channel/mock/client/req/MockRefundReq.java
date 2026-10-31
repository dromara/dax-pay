package cn.daxpay.open.channel.mock.client.req;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import lombok.Data;

/// # Mock 通道退款请求
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.req.MockRefundReq` 镜像, 经声明式 HTTP 客户端
/// [cn.daxpay.open.channel.mock.client.MockChannelClient] 序列化传输, 字段对齐;
/// 金额单位恒为分(Long)。
@Data
public class MockRefundReq {

    /// 原支付单商户订单号(主应用支付交易号透传, 与 MockPayReq.outTradeNo 同值)
    private String outTradeNo;

    /// 商户退款号(主应用退款单 relationOrderNo 透传, 同一订单多次退款不可重复)
    private String outRefundNo;

    /// 退款金额(单位: 分)
    private Long refundAmount;

    /// 退款原因(商户传入, 可空, mock 侧留痕展示用)
    private String reason;

    /// 主应用退款回调地址(mock 扮演三方在退款终态时回推)
    private String notifyUrl;

    /// 通道调用凭证(占位空壳)
    private MockSdkCredential credential;
}
