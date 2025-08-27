package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限制支付类型枚举
 * @author xxm
 * @since 2025/3/10
 */
@Getter
@AllArgsConstructor
public enum PayLimitPayEnum {

    NO_CREDIT("no_credit", "信用卡支付"),
    ;

    private final String code;
    private final String name;
}
