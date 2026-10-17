package cn.daxpay.open.channel.xingyi.client.req;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.client.enums.XingyiPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 星驿付通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 按支付方式走对应驿云付接口下单。
@Data
public class XingyiPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private XingyiSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 orderNo 上送, 异步回调凭 THREE_ORDER_NO 反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 星驿付 txamt 同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(决定接口形态与渠道参数)
    @NotNull(message = "{validation.field.method.notNull}")
    private XingyiPayMethod payMethod;

    /// 用户标识(JSAPI/小程序必填: 微信 openid / 支付宝买家用户ID)
    private String openId;

    /// 通道应用 AppId(微信 JSAPI/小程序上送 wxAppid, 可选)
    private String channelAppId;

    /// 付款码(被扫 B扫C 必填: auth_code)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(asyncNotify, 由子应用透传给星驿付)
    private String notifyUrl;
}
