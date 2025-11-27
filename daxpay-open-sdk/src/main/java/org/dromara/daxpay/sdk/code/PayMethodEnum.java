package org.dromara.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式
 * @author xxm
 * @since 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayMethodEnum {

    /* 通用支付方式 */
    QRCODE("qrcode",  "通用扫码支付"),
    BARCODE("barcode", "通用付款码支付"),
    WECHAT_CASHIER("wechat_cashier", "微信小程序收银台"),
    /* 通道支付方式 */
    WECHAT_QR("wechat_qr", "微信扫码"),
    WECHAT_JSAPI("wechat_jsapi", "微信jsapi"),
    WECHAT_MINI("wechat_mini", "微信小程序"),
    WECHAT_H5("wechat_h5", "微信H5"),
    WECHAT_APP("wechat_app", "微信应用支付"),
    ALIPAY_QR("alipay_qr", "支付宝扫码"),
    ALIPAY_JSAPI("alipay_jsapi", "支付宝jsapi"),
    ALIPAY_MINI("alipay_mini", "支付宝小程序"),
    ALIPAY_PC("alipay_pc", "支付宝电脑支付"),
    ALIPAY_H5("alipay_h5", "支付宝H5"),
    ALIPAY_APP("alipay_app", "支付宝应用支付"),
    UNION_QR("union_qr", "银联扫码"),
    UNION_JSAPI("union_jsapi", "银联jsapi"),
    UNION_H5("union_h5", "银联H5"),
    OTHER("other",  "其他支付方式"),
    ;

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;
}
