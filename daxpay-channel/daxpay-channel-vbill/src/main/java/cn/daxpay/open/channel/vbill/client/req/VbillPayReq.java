package cn.daxpay.open.channel.vbill.client.req;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.enums.VbillPayBodyType;
import cn.daxpay.open.channel.vbill.client.enums.VbillPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 随行付通道支付请求(主应用侧)
///
/// 字段与子应用 [cn.daxpay.open.channel.vbill.req.VbillPayReq] 对称。
@Data
public class VbillPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private VbillSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 商品描述(随行付接口不使用, 仅 DTO 对称保留)
    private String description;

    /// 支付方式
    @NotNull(message = "{validation.field.method.notNull}")
    private VbillPayMethod method;

    /// 聚合支付底层渠道标识(UNI_PAY 必填): WECHAT / ALIPAY / UNIONPAY
    private String payType;

    /// 聚合支付方式(UNI_PAY 必填): 02 公众号/JSAPI, 03 微信小程序
    private String payWay;

    /// 微信 AppId(UNI_PAY 微信场景, 可选)
    private String wxAppId;

    /// 用户标识(UNI_PAY 必填): 微信 openid / 支付宝 buyerId
    private String openId;

    /// 付款码(BAR_CODE 必填)
    private String authCode;

    /// 小程序收银台类型(APPLET_CASHIER 用): 00 小程序支付插件 / 01 半屏小程序收银台
    private String appletSource;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(由子应用透传给随行付)
    private String notifyUrl;

    /// 订单过期时间
    private OffsetDateTime expireTime;

    /// 支付内容类型(主应用预先计算好, 子应用透传到响应)
    private VbillPayBodyType payBodyType;
}
