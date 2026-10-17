package cn.daxpay.open.channel.sand.client.req;

import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.enums.SandPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 杉德通道支付请求(主应用 → 子应用)
///
/// 金额单位为「分」(平台内部单位), 子应用侧换算为杉德要求的「元」。
@Data
public class SandPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private SandSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 outOrderNo 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(description)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定 payType/payMode 分支)
    @NotNull(message = "{validation.field.method.notNull}")
    private SandPayMethod method;

    /// 用户标识(JSAPI/小程序必填: 微信 openid / 支付宝 buyerId)
    private String openId;

    /// 通道应用 AppId(微信 JSAPI/小程序送 subAppId 用)
    private String channelAppId;

    /// 客户端IP(riskmgtInfo.sourceIp)
    private String clientIp;

    /// 异步通知地址(notifyUrl)
    private String notifyUrl;

    /// 支付完成前端回跳地址(payerInfo.frontUrl, 可空)
    private String returnUrl;
}
