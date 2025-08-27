package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务商状态
 * @author xxm
 * @since 2024/10/29
 */
@Getter
@AllArgsConstructor
public enum IsvStatusEnum {

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
