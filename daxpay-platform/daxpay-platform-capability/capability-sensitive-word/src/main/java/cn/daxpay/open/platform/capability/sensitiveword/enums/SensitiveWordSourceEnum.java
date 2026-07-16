package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词请求来源
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordSourceEnum implements I18nSupport {

    /// 运营端
    ADMIN("admin"),
    /// 商户端
    MERCHANT("merchant"),
    /// 开放支付
    UNIPAY("unipay"),
    /// 管理端小程序
    APP_ADMIN("app_admin"),
    /// 未知
    UNKNOWN("unknown");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_source";
    }

    public static Optional<SensitiveWordSourceEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}

