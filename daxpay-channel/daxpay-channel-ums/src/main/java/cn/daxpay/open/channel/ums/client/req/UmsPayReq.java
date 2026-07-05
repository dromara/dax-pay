package cn.daxpay.open.channel.ums.client.req;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import lombok.Data;

/// # 银联商务通道支付请求
@Data
public class UmsPayReq {

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品描述
    private String description;

    /// 支付方式
    private UmsPayMethod method;

    /// 异步通知地址
    private String notifyUrl;

    /// 客户端 IP(H5 场景用)
    private String clientIp;

    /// 是否限制信用卡支付
    private Boolean limitCreditCard;

    /// 微信 AppId(微信小程序收银台支付时必填)
    private String wxAppId;

    /// 通道调用凭证
    private UmsSdkCredential credential;
}
