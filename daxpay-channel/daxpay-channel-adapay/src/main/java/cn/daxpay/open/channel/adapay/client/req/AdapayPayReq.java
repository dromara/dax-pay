package cn.daxpay.open.channel.adapay.client.req;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.enums.AdapayPayMethod;
import lombok.Data;

/// # Adapay 通道支付请求(主应用侧)
@Data
public class AdapayPayReq {

    /// 商户订单号(主应用支付交易号, 作为Adapay order_no)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题(Adapay goods_title)
    private String title;

    /// 商品描述(Adapay goods_desc)
    private String description;

    /// 支付方式
    private AdapayPayMethod method;

    /// 用户标识(微信 openId / 支付宝 buyerId, JSAPI/小程序必填)
    private String openId;

    /// 微信应用 AppId(微信 JSAPI/APP/小程序支付上送 wx_app_id)
    private String wxAppId;

    /// 付款码(条码支付必填)
    private String authCode;

    /// 客户端 IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 是否限制信用卡支付
    private Boolean limitCreditCard;

    /// 通道调用凭证
    private AdapaySdkCredential credential;
}
