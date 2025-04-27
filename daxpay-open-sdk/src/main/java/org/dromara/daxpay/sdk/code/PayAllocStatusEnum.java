package org.dromara.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单分账状态
 * @author xxm
 * @since 2024/4/16
 */
@Getter
@AllArgsConstructor
public enum PayAllocStatusEnum {
    /** 待分账 */
    WAITING("waiting"),
    /** 已分账 */
    ALLOCATION("allocation"),
    ;

    private final String code;
}
