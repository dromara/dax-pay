package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款修复方式枚举
 * @author xxm
 * @since 2024/1/26
 */
@Getter
@AllArgsConstructor
public enum RefundRepairWayEnum {

    SUCCESS("refund_success","退款成功"),
    FAIL("refund_fail","退款失败");

    private final String code;
    private final String name;
}
