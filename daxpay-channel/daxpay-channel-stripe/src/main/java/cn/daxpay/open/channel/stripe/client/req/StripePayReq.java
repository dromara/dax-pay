package cn.daxpay.open.channel.stripe.client.req;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import lombok.Data;

/// # Stripe 支付请求参数(主应用 → 子应用 channel-three)
@Data
public class StripePayReq {

    /// 通道凭证(主应用组装下发)
    private StripeSdkCredential credential;

    /// 平台业务单号(作为 Stripe metadata.orderNo)
    private String orderNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 支付标题
    private String title;

    /// 支付描述
    private String description;

    /// 金额(最小货币单位, 如 USD 100=1美元)
    private Long amount;

    /// 币种(ISO 4217 小写, 如 usd/jpy/eur/hkd/gbp)
    private String currency;

    /// 支付方式: stripe_checkout / stripe_payment_intent
    private String method;

    /// 同步跳转地址(Checkout Session 模式必填)
    private String returnUrl;

    /// 异步通知地址
    private String notifyUrl;

    /// 商户附加参数(透传到 metadata)
    private String attach;
}
