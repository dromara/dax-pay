package cn.daxpay.open.channel.cpcn.client.req;

import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.enums.CpcnPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 中金通道支付请求(主应用侧)
///
/// 由主应用组装, 经声明式 HTTP 客户端转发到子应用, 走中金 5011 统一下单。
@Data
public class CpcnPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private CpcnSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为 TxSN 上送, 异步回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分, 中金接口金额单位同为分, 直接透传)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 支付方式(中金三级码映射)
    @NotNull(message = "{validation.field.method.notNull}")
    private CpcnPayMethod payMethod;

    /// 用户标识(微信 openid / 支付宝买家用户ID, JSAPI 族必填, 上送 SubOpenID)
    private String openId;

    /// 微信公众号/小程序 AppId(微信 JSAPI 必填, 上送 SubAppID)
    private String appId;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(NoticeURL, 由子应用透传给中金)
    private String notifyUrl;

    /// 同步跳转地址(PageURL, 可空)
    private String returnUrl;
}
