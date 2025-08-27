package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收银台类型
 * 字典: gateway_cashier_type
 * @author xxm
 * @since 2025/3/27
 */
@Getter
@AllArgsConstructor
public enum GatewayCashierTypeEnum {
    H5("h5", "H5收银台"),
    PC("pc", "PC收银台"),
    MINI_APP("mini_app", "小程序收银台"),
    ;

    private final String code;
    private final String name;
}
