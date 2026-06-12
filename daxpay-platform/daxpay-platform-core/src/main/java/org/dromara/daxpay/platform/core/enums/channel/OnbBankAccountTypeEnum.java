package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 进件商户结算卡类型
///
/// 字典: onb_bank_account_type
@Getter
@RequiredArgsConstructor
public enum OnbBankAccountTypeEnum implements I18nSupport {

    /// 对公账号收款
    COMPANY_OWNER("company_owner"),
    /// 公户授权
    COMPANY_NOT_OWNER("company_not_owner"),
    /// 私户且是法人
    PERSON_OWNER("person_owner"),
    /// 私户且不是法人
    PERSON_NOT_OWNER("person_not_owner"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.onb_bank_account_type";
    }
}
