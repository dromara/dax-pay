package org.dromara.daxpay.platform.core.enums.role;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 角色编码枚举
///
@Getter
@RequiredArgsConstructor
public enum RoleCodeEnum implements I18nSupport {

    /// 商户管理员
    MERCHANT_ADMIN("merchant_admin");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.role_code";
    }

}
