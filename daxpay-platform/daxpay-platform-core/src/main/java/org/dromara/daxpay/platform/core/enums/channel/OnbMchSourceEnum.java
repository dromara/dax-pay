package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 进件商户来源
///
/// 字典: onb_mch_source
@Getter
@RequiredArgsConstructor
public enum OnbMchSourceEnum implements I18nSupport {

    /// 进件申请
    APPLY("apply"),
    /// 认证绑定
    AUTH_BIND("auth_bind"),
    /// 手动创建
    MANUAL("manual");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.onb_mch_source";
    }

}
