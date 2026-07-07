package cn.daxpay.open.channel.hkrt.client.req;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.enums.HkrtPayBodyType;
import cn.daxpay.open.channel.hkrt.client.enums.HkrtPayMethod;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 海科融通通道支付请求(主应用侧, 与子应用镜像)
///
/// 海科融通的支付方式由 method 单独决定, 不需要 accountType/transType/payBodyType 三要素。
/// amount 单位为分, 透传给子应用, 由子应用负责分→元转换。
@Data
public class HkrtPayReq {

    /// 通道调用凭证
    private HkrtSdkCredential credential;

    /// 商户订单号(主应用支付交易号)
    private String outTradeNo;

    /// 订单金额(单位: 分)
    private Long amount;

    /// 商品标题
    private String title;

    /// 商品描述
    private String description;

    /// 支付方式(WECHAT_JSAPI / ALIPAY_QR / ALIPAY_JSAPI / UNION_QR / BARCODE)
    private HkrtPayMethod method;

    /// 用户标识(JSAPI/MINI 必填)
    private String openId;

    /// 付款码(BARCODE 必填)
    private String authCode;

    /// 客户端IP
    private String clientIp;

    /// 异步通知地址
    private String notifyUrl;

    /// 订单过期时间
    private OffsetDateTime expireTime;

    /// 支付内容类型(主应用预先计算, 子应用透传到响应)
    private HkrtPayBodyType payBodyType;
}
