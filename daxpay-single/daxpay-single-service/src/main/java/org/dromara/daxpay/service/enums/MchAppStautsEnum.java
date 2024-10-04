package org.dromara.daxpay.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户应用状态
 * 字典: mch_app_status
 * @author xxm
 * @since 2024/6/25
 */
@Getter
@AllArgsConstructor
public enum MchAppStautsEnum {
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
