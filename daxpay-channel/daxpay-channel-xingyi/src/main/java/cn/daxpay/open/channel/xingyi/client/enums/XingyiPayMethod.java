package cn.daxpay.open.channel.xingyi.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 星驿付支付方式(主应用侧)
///
/// 与子应用 XingyiPayMethod 对齐(枚举 name 一致, 经 JSON 传输按 name 反序列化):
/// 官方码扫码(支付宝/云闪付)、星驿码聚合(微信)、JSAPI(微信/支付宝)、微信小程序、付款码(微信/支付宝/云闪付)。
@Getter
@AllArgsConstructor
public enum XingyiPayMethod {

    /// 支付宝扫码(官方码二维码)
    ALIPAY_QR("alipay_qr"),
    /// 云闪付扫码(官方码二维码)
    UNION_QR("union_qr"),
    /// 微信扫码(星驿码聚合二维码)
    WECHAT_QR("wechat_qr"),
    /// 支付宝 JSAPI(openid=买家用户ID)
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 微信 JSAPI(openid + wxAppid)
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信小程序(openid + wxAppid + traType=8)
    WECHAT_MINI("wechat_mini"),
    /// 微信付款码(B扫C)
    WECHAT_BARCODE("wechat_barcode"),
    /// 支付宝付款码(B扫C)
    ALIPAY_BARCODE("alipay_barcode"),
    /// 云闪付付款码(B扫C)
    UNION_BARCODE("union_barcode");

    /// 平台支付方式编码(对齐 PayMethodEnum.code)
    private final String code;
}
