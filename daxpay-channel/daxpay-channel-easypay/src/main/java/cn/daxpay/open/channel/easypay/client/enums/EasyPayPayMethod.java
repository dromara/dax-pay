package cn.daxpay.open.channel.easypay.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/// # 易支付支付方式(主应用侧)
///
/// 一期仅扫码两类(2026-09-18 移植拍板), 枚举 name 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 对齐,
/// 经 JSON 传输给子应用(与子应用侧 `cn.daxpay.open.channel.easypay.enums.EasyPayPayMethod` 镜像)。
@Getter
@AllArgsConstructor
public enum EasyPayPayMethod {

    /// 支付宝扫码(动态二维码/跳转)
    ALIPAY_QR("alipay_qr"),
    /// 微信扫码(动态二维码/跳转)
    WECHAT_QR("wechat_qr");

    /// 平台能力码(对齐 pay_md_product_capability)
    private final String code;

    /// 根据平台支付方式编码查找, 未知返回 null
    public static EasyPayPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /// 是否支持的支付方式编码
    public static boolean supports(String code) {
        return Objects.nonNull(findByCodeOrNull(code));
    }
}
