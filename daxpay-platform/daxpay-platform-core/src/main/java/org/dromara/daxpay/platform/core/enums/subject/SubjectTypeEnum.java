package org.dromara.daxpay.platform.core.enums.subject;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 主体类型枚举
///
///
/// 适用于商户、代理商、服务商等经营主体
@Getter
@RequiredArgsConstructor
public enum SubjectTypeEnum implements I18nSupport {

    /// 小微商户
    MICRO("micro"),
    /// 个体工商户
    INDIVIDUAL("individual"),
    /// 企业
    ENTERPRISE("enterprise");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.subject_type";
    }

}
