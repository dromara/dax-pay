package cn.daxpay.open.channel.lakala.client.req;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 拉卡拉通道支付请求(主应用侧, 与子应用镜像)
@Data
public class LakalaPayReq {

    /// 通道调用凭证
    private LakalaSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式(MICROPAY / PREORDER)
    private LakalaPayMethod method;

    /// 账户类型(PREORDER 必填: WECHAT / ALIPAY / UQRCODEPAY)
    private String accountType;

    /// 交易类型(PREORDER 必填: 41扫码 / 51 JSAPI / 61 APP / 71 小程序)
    private String transType;

    /// 用户标识(JSAPI/MINI 必填)
    private String openId;

    /// 付款码(MICROPAY 必填)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 订单过期时间
    private OffsetDateTime expireTime;

    /// 支付内容类型(主应用预先计算, 子应用透传到响应)
    private LakalaPayBodyType payBodyType;
}
