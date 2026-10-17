package cn.daxpay.open.channel.ysep.client.req;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.enums.YsepPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 银盛通道支付请求(主应用侧, 与子应用镜像)
@Data
public class YsepPayReq {

    /// 通道调用凭证
    private YsepSdkCredential credential;

    /// 产品编码(ysep_pay / yseqt_pay, 决定报文族)
    private String product;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 支付方式
    private YsepPayMethod payMethod;

    /// 用户标识(微信 openid / 支付宝买家ID / 云闪付 userId, jsapi 族必填)
    private String openId;

    /// 渠道应用 appId(微信公众号/小程序 appId, 微信 jsapi/mini 必填)
    private String appId;

    /// 商品标题
    private String title;

    /// 异步通知地址
    private String notifyUrl;

    /// 订单过期时间
    private OffsetDateTime expireTime;

    /// 客户端IP
    private String clientIp;
}
