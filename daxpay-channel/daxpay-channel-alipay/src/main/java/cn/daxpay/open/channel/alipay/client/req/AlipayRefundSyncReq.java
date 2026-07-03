package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;

/// # 支付宝通道退款同步请求
///
/// 与子应用 dax-pay-channel-one 的 `AlipayRefundSyncReq` 镜像, 经声明式 HTTP 客户端序列化传输, 字段对齐。
@Data
public class AlipayRefundSyncReq {

    /// 商户订单号(主应用支付交易号, 对应支付宝 out_trade_no)
    private String outTradeNo;

    /// 支付宝交易号(trade_no, 可选)
    private String tradeNo;

    /// 退款请求号(对应支付宝 out_request_no, 必传)
    private String outRequestNo;

    /// 通道调用凭证
    private AlipaySdkCredential credential;
}
