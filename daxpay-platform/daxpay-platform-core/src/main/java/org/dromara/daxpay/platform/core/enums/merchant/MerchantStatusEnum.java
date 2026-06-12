package org.dromara.daxpay.platform.core.enums.merchant;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户状态枚举
///
/// 字典: merchant_status
@Getter
@RequiredArgsConstructor
public enum MerchantStatusEnum implements I18nSupport {

    /// 禁用
    DISABLED("disabled"),
    /// 启用
    ENABLE("enable");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.merchant_status";
    }

}
