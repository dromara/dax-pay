package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款修复方式枚举, 支付修复方式包含退款的修复方式, 退款修复方式是支付修复的子集
 * @author xxm
 * @since 2024/1/26
 */
@Getter
@AllArgsConstructor
public enum RefundRepairWayEnum {

    REFUND_SUCCESS("refund_success","退款成功"),
    REFUND_FAIL("refund_fail","退款失败");

    private final String code;
    private final String name;
}
