package cn.daxpay.open.channel.yeepay.client.req;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.enums.YeepayPayMethod;
import lombok.Data;

/// # 易宝通道支付请求(主应用 → 子应用)
@Data
public class YeepayPayReq {

    /// 商户订单号(平台支付交易号, 作为易宝 orderId)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题(易宝 goodsName)
    private String title;

    /// 商品描述(易宝 memo)
    private String description;

    /// 支付方式
    private YeepayPayMethod method;

    /// 客户端 IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步返回地址(H5 场景用)
    private String returnUrl;

    /// 是否限制信用卡支付
    private Boolean limitCreditCard;

    /// 通道调用凭证
    private YeepaySdkCredential credential;
}
