package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词分类
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordCategoryEnum implements I18nSupport {

    /// 政治
    POLITIC("politic"),
    /// 色情
    PORN("porn"),
    /// 暴力
    VIOLENCE("violence"),
    /// 广告
    AD("ad"),
    /// 自定义
    CUSTOM("custom");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_category";
    }

    public static Optional<SensitiveWordCategoryEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}

