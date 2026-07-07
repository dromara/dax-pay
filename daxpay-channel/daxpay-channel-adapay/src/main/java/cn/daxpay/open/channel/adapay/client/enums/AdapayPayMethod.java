package cn.daxpay.open.channel.adapay.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 汇付天下通道支付方式(主应用侧)
///
/// 与子应用 `cn.daxpay.open.channel.adapay.enums.AdapayPayMethod` 镜像,
/// 枚举 name 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 对齐,
/// 主应用 service 直接 valueOf(PayMethodEnum.name()) 即可完成映射。
@Getter
@AllArgsConstructor
public enum AdapayPayMethod {

    /// 微信扫码(动态二维码)
    WECHAT_QR("wechat_qr"),
    /// 微信公众号(JSAPI)
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信 APP
    WECHAT_APP("wechat_app"),
    /// 微信 H5
    WECHAT_H5("wechat_h5"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信付款码(条码)
    WECHAT_BARCODE("wechat_barcode"),

    /// 支付宝扫码
    ALIPAY_QR("alipay_qr"),
    /// 支付宝 JSAPI
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝 APP
    ALIPAY_APP("alipay_app"),
    /// 支付宝 H5
    ALIPAY_H5("alipay_h5"),
    /// 支付宝 PC
    ALIPAY_PC("alipay_pc"),
    /// 支付宝付款码(条码)
    ALIPAY_BARCODE("alipay_barcode"),

    /// 银联扫码(动态二维码)
    UNION_QR("union_qr"),
    /// 银联 JSAPI
    UNION_JSAPI("union_jsapi"),
    /// 银联 H5
    UNION_H5("union_h5"),
    /// 银联付款码(条码)
    UNION_PAY_BARCODE("union_pay_barcode");

    private final String code;
}
