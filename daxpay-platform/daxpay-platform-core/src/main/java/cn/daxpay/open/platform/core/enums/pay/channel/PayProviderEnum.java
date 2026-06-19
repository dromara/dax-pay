package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 支付渠道
///
/// 付款方使用的钱包或支付渠道（C 端），非支付通道
/// 字典: pay_provider
@Getter
@RequiredArgsConstructor
public enum PayProviderEnum implements I18nSupport {
    /// 聚合支付（收银台/一码多付等）
    AGGREGATE_PAY("aggregate_pay"),
    /// 微信（C 端钱包渠道）
    WECHAT("wechat"),
    /// 支付宝
    ALIPAY("alipay"),
    /// 银联
    UNION_PAY("union_pay"),
    /// Visa 卡组织
    VISA("visa"),
    /// 万事达卡组织
    MASTERCARD("mastercard"),
    /// 抖音支付
    DOUYIN("douyin"),
    ;
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_provider";
    }

    public static PayProviderEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
