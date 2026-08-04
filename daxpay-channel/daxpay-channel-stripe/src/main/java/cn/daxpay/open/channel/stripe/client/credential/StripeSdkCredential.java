package cn.daxpay.open.channel.stripe.client.credential;

import lombok.Data;

/// # Stripe SDK 凭证
///
/// 与子应用 dax-pay-channel-three 的 `StripeSdkCredential` 镜像, 跨 HTTP 传输时字段对齐。
/// 主应用从进件实体(stripe_key_config / stripe_channel_merchant)提取后组装,
/// 下发给子应用构建 Stripe SDK Client。
@Data
public class StripeSdkCredential {

    /// Stripe Secret Key(sk_test_xxx 沙箱 / sk_live_xxx 生产)
    private String secretKey;

    /// Stripe Publishable Key(pk_test_xxx 沙箱 / pk_live_xxx 生产, 前端 Elements 用)
    private String publishableKey;

    /// Webhook 签名密钥(whsec_xxx, 回调验签用)
    private String webhookSecret;
}
