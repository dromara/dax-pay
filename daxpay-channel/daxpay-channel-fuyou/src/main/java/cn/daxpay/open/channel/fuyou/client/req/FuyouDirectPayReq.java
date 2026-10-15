package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友直连支付请求
///
/// 由主应用经声明式 HTTP 客户端转发到子应用, 走富友「商户移动支付前置接口」(aggregatePay)。
/// 支付方式由 [#method] 决定, 不同方式下额外参数:
/// - WECHAT_JSAPI / WECHAT_MINI: 需传 `openId`(微信用户标识) + 可选 `wxAppId`
/// - ALIPAY_JSAPI: 需传 `openId`(支付宝 buyerId / 小程序 openId)
/// - *_QR: 仅需基础参数(返回二维码链接)
/// - BARCODE: 需传 `authCode`(付款码)
@Data
public class FuyouDirectPayReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouDirectSdkCredential credential;

    /// 商户订单号(主应用支付交易号, 作为富友 mchnt_order_no, 回调凭此反查)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 订单金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 商品标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式
    @NotNull(message = "{validation.field.method.notNull}")
    private FuyouPayMethod method;

    /// 用户标识(微信 openid / 支付宝 buyerId, JSAPI/小程序必填)
    private String openId;

    /// 微信 AppId(JSAPI/小程序场景透传 sub_appid, 可选)
    private String wxAppId;

    /// 付款码(BARCODE 必填)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址(由子应用透传给富友 notify_url)
    private String notifyUrl;

    /// 是否限制信用卡支付
    private Boolean limitCredit;

    /// 订单过期时间(子应用格式化为 yyyyMMddHHmmss; 为空不传)
    private OffsetDateTime expireTime;

    /// 支付内容类型(主应用预先计算好, 子应用透传到响应)
    private FuyouPayBodyType payBodyType;
}
