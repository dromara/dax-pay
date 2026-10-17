package cn.daxpay.open.channel.duolabao.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/// # 哆啦宝支付方式(主应用侧)
///
/// 与子应用 DuolabaoPayMethod 对齐(枚举 name 一致, 经 JSON 传输反序列化)。
@Getter
@AllArgsConstructor
public enum DuolabaoPayMethod {

    /// 支付宝扫码(聚合码)
    ALIPAY_QR("alipay_qr"),
    /// 微信扫码(聚合码)
    WECHAT_QR("wechat_qr"),
    /// 云闪付扫码(聚合码)
    UNION_QR("union_qr"),
    /// 云闪付 H5(聚合码链接跳转)
    UNION_H5("union_h5"),
    /// 支付宝 JS(买家用户ID)
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 微信 JS(openid + appId/subAppId)
    WECHAT_JSAPI("wechat_jsapi");

    /// 平台支付方式编码(对齐 PayMethodEnum.code)
    private final String code;

    /// 根据平台支付方式编码查找, 未知返回 null
    public static DuolabaoPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
