package cn.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付修复方式, 包含退款的修复方式, 退款修复方式是支付修复的子集
 * @author xxm
 * @since 2023/12/28
 */
@Getter
@AllArgsConstructor
public enum PayAdjustWayEnum {

    SUCCESS("pay_success","支付成功"),
    CLOSE_LOCAL("pay_close_local","关闭本地支付"),
    /** 同时也会关闭本地支付 */
    CLOSE_GATEWAY("pay_close_gateway","关闭网关支付");

    private final String code;
    private final String name;
}
