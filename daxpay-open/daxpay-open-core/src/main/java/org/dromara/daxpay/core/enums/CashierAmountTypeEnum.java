package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收银码金额类型枚举
 * @author xxm
 * @since 2025/7/5
 */
@Getter
@AllArgsConstructor
public enum CashierAmountTypeEnum {
    FIXED("fixed","固定金额"),
    RANDOM("random","任意金额"),
    ;
    private final String code;
    private final String name;
}
