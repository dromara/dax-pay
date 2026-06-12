package org.dromara.daxpay.platform.core.enums.unipay;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 限制支付类型枚举
///
@Getter
@RequiredArgsConstructor
public enum PayLimitPayEnum implements I18nSupport {

    /// 信用卡支付
    NO_CREDIT("no_credit");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_limit_pay";
    }

}
