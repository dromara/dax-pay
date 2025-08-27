package org.dromara.daxpay.sdk.code;

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

    /** 信用卡支付 */
    NO_CREDIT("no_credit"),
    ;

    private final String code;
}
