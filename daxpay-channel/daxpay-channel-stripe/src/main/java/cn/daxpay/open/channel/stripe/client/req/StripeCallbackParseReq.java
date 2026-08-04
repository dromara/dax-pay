package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 回调解析请求参数
///
/// 主应用接收 Stripe Webhook 原始 body + Stripe-Signature 头后, 转发到子应用验签解析。
@Data
public class StripeCallbackParseReq {

    /// 通道凭证(webhookSecret 用于验签)
    private StripeSdkCredential credential;

    /// Webhook 原始 payload(Stripe 发送的 raw body, 验签必须用原始字节)
    private String payload;

    /// Stripe-Signature 头(t=xxx,v1=xxx)
    private String signature;
}
