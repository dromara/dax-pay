package org.dromara.daxpay.payment.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算状态
 * 字典: settle_status
 * @author xxm
 * @since 2025/6/18
 */
@Getter
@AllArgsConstructor
public enum SettleStatusEnum {
    NOT_SETTLE("not_settle","未结算"),
    SETTLED("settled","已结算"),
    ;
    private final String code;
    private final String name;

}
