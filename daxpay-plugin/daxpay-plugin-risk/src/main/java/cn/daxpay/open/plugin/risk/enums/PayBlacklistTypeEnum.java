package cn.daxpay.open.plugin.risk.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 黑名单类型
///
@Getter
@RequiredArgsConstructor
public enum PayBlacklistTypeEnum implements I18nSupport {

    /// IP
    IP("ip"),
    /// 支付宝用户（userId，通道内全局）
    ALIPAY_USER("alipay_user"),
    /// 微信 OpenId（绑定平台支付应用）
    WECHAT_OPENID("wechat_openid"),
    /// 海外 IP（地域策略命中, 非黑名单来源）
    OVERSEAS_IP("overseas_ip");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_blacklist_type";
    }

    public static Optional<PayBlacklistTypeEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }

    /// 是否为用户标识类名单（支付宝 / 微信）
    public boolean isUserIdentity() {
        return this == ALIPAY_USER || this == WECHAT_OPENID;
    }
}
