package cn.daxpay.single.sdk.code;

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
    WAITING("waiting", "待分账"),
    ALLOCATION("allocation", "已分账"),
    ;

    private final String code;
    private final String name;
}
