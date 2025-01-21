package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款同步状态枚举
 * @author xxm
 * @since 2024/1/29
 */
@Getter
@AllArgsConstructor
public enum RefundSyncStatusEnum {
    SUCCESS("refund_success","退款成功"),
    FAIL("refund_fail","退款失败"),
    PROGRESS("refund_progress","退款中"),
    NOT_FOUND("pay_not_found", "交易不存在");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;
}
