package cn.daxpay.single.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单的分账状态
 * @author xxm
 * @since 2024/4/16
 */
@Getter
@AllArgsConstructor
public enum PayOrderAllocStatusEnum {
    WAITING("waiting", "待分账"),
    ALLOCATION("allocation", "已分账"),
    ;

    final String code;
    final String name;
}
