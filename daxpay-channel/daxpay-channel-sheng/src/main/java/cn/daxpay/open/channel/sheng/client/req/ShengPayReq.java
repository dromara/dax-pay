package cn.daxpay.open.channel.sheng.client.req;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.enums.ShengPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 盛付通通道支付请求(主应用侧, 与子应用镜像)
///
/// 主应用只透传 payMethod + authCode, 由子应用按支付方式分流 authPay(被扫) / unifiedorderOffline(其余)。
@Data
public class ShengPayReq {

    /// 通道调用凭证
    private ShengSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 支付方式(15 项, code 与平台 PayMethodEnum 一致)
    private ShengPayMethod payMethod;

    /// 付款码(被扫 wechat/alipay/union_barcode 必填)
    private String authCode;

    /// 用户标识(jsapi/mini 族必填: 微信 openId / 支付宝买家ID)
    private String openId;

    /// 渠道应用 appId(jsapi/mini 族: 微信公众号/小程序 appId / 支付宝小程序 appId)
    private String appId;

    /// 商品标题(作为盛付通下单 body 字段)
    private String title;

    /// 附加数据(透传回显)
    private String attach;

    /// 异步通知地址
    private String notifyUrl;

    /// 订单过期时间(子应用转东八区 yyyyMMddHHmmss 上送)
    private OffsetDateTime expireTime;

    /// 客户端IP
    private String clientIp;
}
