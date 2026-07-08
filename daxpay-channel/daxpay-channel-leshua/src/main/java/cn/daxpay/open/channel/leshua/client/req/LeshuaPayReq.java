package cn.daxpay.open.channel.leshua.client.req;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.enums.LeshuaPayBodyType;
import cn.daxpay.open.channel.leshua.client.enums.LeshuaPayMethod;
import lombok.Data;

/// # 乐刷通道支付请求(主应用侧)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaPayReq` 镜像, 经声明式 HTTP 客户端序列化传输, 字段对齐。
@Data
public class LeshuaPayReq {
    /// 通道调用凭证
    private LeshuaSdkCredential credential;
    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;
    /// 订单金额(单位: 分)
    private Long amount;
    /// 商品标题
    private String title;
    /// 商品描述
    private String description;
    /// 支付方式(UPLOAD_AUTHCODE 付款码 / GET_TDCODE 预下单)
    private LeshuaPayMethod method;
    /// 底层渠道(GET_TDCODE 必填: WXZF 微信 / ZFBZF 支付宝 / UPSMZF 云闪付)
    private String payWay;
    /// 支付形态(GET_TDCODE 必填: 0 扫码 / 1 JSAPI / 2 H5 / 3 小程序)
    private String jspayFlag;
    /// 用户标识(JSAPI/MINI: 微信 openid / 支付宝 buyerId)
    private String openId;
    /// 微信 AppId(微信 JSAPI/小程序场景透传)
    private String wxAppId;
    /// 付款码(UPLOAD_AUTHCODE 必填)
    private String authCode;
    /// 客户端IP
    private String clientIp;
    /// 异步通知地址
    private String notifyUrl;
    /// 支付内容类型(主应用预先计算好)
    private LeshuaPayBodyType payBodyType;
}
