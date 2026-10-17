package cn.daxpay.open.channel.fubei.client.req;

import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.client.enums.FubeiPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 付呗通道支付请求(主应用侧)
///
/// 由主应用组装后经声明式 HTTP 客户端转发到子应用, 走付呗开放平台统一下单。
@Data
public class FubeiPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FubeiSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 merchant_order_sn 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 付呗接口金额单位为「元」, 子应用自动换算)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题(body)
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(白名单: 云闪付扫码/微信JSAPI/支付宝JSAPI/支付宝H5)
    @NotNull(message = "{validation.field.payMethod.notNull}")
    private FubeiPayMethod payMethod;

    /// JSAPI 族用户标识(微信=openid / 支付宝=买家用户ID, JSAPI 族必填)
    private String openId;

    /// 通道应用 AppId(微信 JSAPI/小程序上送 sub_appid)
    private String channelAppId;

    /// 客户端IP(H5 下单必传 user_ip)
    private String clientIp;

    /// 异步通知地址(notify_url, 由子应用透传给付呗)
    private String notifyUrl;

    /// 支付完成后前端跳转地址(return_url, 支付宝 H5 专用)
    private String returnUrl;
}
