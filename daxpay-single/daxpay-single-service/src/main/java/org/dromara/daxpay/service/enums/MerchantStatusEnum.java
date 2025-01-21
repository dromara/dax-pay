package org.dromara.daxpay.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户状态枚举
 * 字典 merchant_status
 * @author xxm
 * @since 2024/6/24
 */
@Getter
@AllArgsConstructor
public enum MerchantStatusEnum {
    /**
     * 禁用
     */
    DISABLED("disabled", "禁用"),
    /**
     * 启用
     */
    ENABLE("enable", "启用");

    private final String code;
    private final String name;
}
