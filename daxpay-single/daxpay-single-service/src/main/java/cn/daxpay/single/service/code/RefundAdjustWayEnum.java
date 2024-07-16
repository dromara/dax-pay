package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款调整方式枚举
 * @author xxm
 * @since 2024/1/26
 */
@Getter
@AllArgsConstructor
public enum RefundAdjustWayEnum {

    SUCCESS("success","退款成功"),
    FAIL("fail","退款失败");

    private final String code;
    private final String name;
}
