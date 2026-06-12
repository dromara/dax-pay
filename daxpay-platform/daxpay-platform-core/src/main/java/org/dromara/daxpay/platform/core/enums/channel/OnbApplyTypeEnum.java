package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 进件类型
///
/// 字典:onb_apply_type
@Getter
@RequiredArgsConstructor
public enum OnbApplyTypeEnum implements I18nSupport {
    /// 商户进件
    MERCHANT("merchant"),
    /// 企业进件
    ENTERPRISE("enterprise"),
    /// 小微进件
    MICRO("micro"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.apply_type";
    }
}
