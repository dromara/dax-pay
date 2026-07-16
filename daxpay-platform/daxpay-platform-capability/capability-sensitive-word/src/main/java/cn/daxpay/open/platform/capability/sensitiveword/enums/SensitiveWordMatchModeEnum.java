package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词匹配模式
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordMatchModeEnum implements I18nSupport {

    /// 子串包含（AC）
    CONTAINS("contains"),
    /// 整词精确（预留）
    EXACT("exact");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_match_mode";
    }

    public static Optional<SensitiveWordMatchModeEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}

