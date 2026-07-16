package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词状态
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordStatusEnum implements I18nSupport {

    /// 启用
    ENABLE("enable"),
    /// 禁用
    DISABLE("disable");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_status";
    }

    public static Optional<SensitiveWordStatusEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}

