package cn.daxpay.open.channel.jeepay.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/// # Jeepay支付方式(主应用侧)
///
/// 一期 12 项(对齐 pay_md_product_capability 主数据: 微信/支付宝/云闪付的扫码+JSAPI 族),
/// 枚举 name 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 对齐,
/// 经 JSON 传输给子应用(与子应用侧 `cn.daxpay.open.channel.jeepay.enums.JeepayPayMethod` 镜像)。
@Getter
@AllArgsConstructor
public enum JeepayPayMethod {

    /// 支付宝扫码
    ALIPAY_QR("alipay_qr"),
    /// 支付宝生活号/JSAPI
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝PC网站
    ALIPAY_PC("alipay_pc"),
    /// 支付宝WAP/H5
    ALIPAY_H5("alipay_h5"),
    /// 支付宝APP
    ALIPAY_APP("alipay_app"),
    /// 微信扫码(Native)
    WECHAT_QR("wechat_qr"),
    /// 微信公众号JSAPI
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信H5
    WECHAT_H5("wechat_h5"),
    /// 微信APP
    WECHAT_APP("wechat_app"),
    /// 云闪付扫码
    UNION_QR("union_qr"),
    /// 云闪付JSAPI
    UNION_JSAPI("union_jsapi");

    /// 平台能力码(对齐 pay_md_product_capability)
    private final String code;

    /// 根据平台支付方式编码查找, 未知返回 null
    public static JeepayPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /// 是否支持的支付方式编码
    public static boolean supports(String code) {
        return Objects.nonNull(findByCodeOrNull(code));
    }

    /// 是否需要用户标识(jsapi 族: 微信公众号/小程序/支付宝生活号, 子应用凭此组装 channelExtra)
    public boolean requiresOpenId() {
        return this == ALIPAY_JSAPI || this == WECHAT_JSAPI || this == WECHAT_MINI;
    }
}
