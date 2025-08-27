package org.dromara.daxpay.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户角色枚举
 * @author xxm
 * @since 2025/1/4
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    MERCHANT_ROLE("merchant_admin","商户管理员"),
    ;

    private final String code;
    private final String name;
}
