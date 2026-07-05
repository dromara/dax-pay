package cn.daxpay.open.channel.ums.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 银联商务通道支付方式
///
/// 与子应用 `cn.daxpay.open.channel.ums.enums.UmsPayMethod` 镜像。
@Getter
@AllArgsConstructor
public enum UmsPayMethod {

    /// 扫码支付(主扫, 返回 billQRCode 二维码)
    QRCODE("QRCODE"),
    /// 支付宝 H5 支付(返回跳转链接)
    ALIPAY_H5("ALIPAY_H5"),
    /// 微信 H5 支付(返回跳转链接)
    WECHAT_H5("WECHAT_H5"),
    /// 微信小程序收银台支付(H5 转小程序, 返回跳转链接)
    WECHAT_CASHIER("WECHAT_CASHIER"),
    /// 银联云闪付 H5/JSAPI 支付(返回跳转链接)
    UNION_JSAPI("UNION_JSAPI");

    private final String code;
}
