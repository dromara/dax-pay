package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 收银支付场景类型
 * 字典: cashier_code_type -> cashier_scene
 * @author xxm
 * @since 2024/9/28
 */
@Getter
@AllArgsConstructor
public enum CashierSceneEnum {

    WECHAT_PAY("wechat_pay", "微信支付"),
    ALIPAY("alipay", "支付宝支付"),
    UNION_PAY("union_pay", "云闪付支付"),
    ;

    private final String code;
    private final String name;

    public static CashierSceneEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("退款状态不存在"));
    }
}
