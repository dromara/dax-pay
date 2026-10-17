package cn.daxpay.open.channel.jeepay.client.req;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.enums.JeepayPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # Jeepay通道支付请求(主应用侧)
///
/// jsapi 族(微信公众号/小程序/支付宝生活号)须携带 openId, 由子应用组装 channelExtra。
///
/// Jeepay 金额单位为「分」(与平台内部单位一致, 直传不换算)。
@Data
public class JeepayPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JeepaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 Jeepay mchOrderNo)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(作为 Jeepay subject/body 字段)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 wayCode 与 channelExtra 组装)
    @NotNull(message = "{validation.field.method.notNull}")
    private JeepayPayMethod payMethod;

    /// 用户标识(jsapi 族必填: 微信 openid / 支付宝 buyerUserId, 其他方式忽略)
    private String openId;

    /// 客户端IP(可空)
    private String clientIp;

    /// 异步通知地址(由子应用透传给 Jeepay 平台)
    private String notifyUrl;

    /// 支付完跳转地址(可空)
    private String returnUrl;
}
