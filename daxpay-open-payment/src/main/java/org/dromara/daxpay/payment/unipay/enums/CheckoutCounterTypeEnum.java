package org.dromara.daxpay.payment.unipay.enums;

import org.dromara.daxpay.payment.common.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 收银台类型
 * 字典: gateway_cashier_type
 * @author xxm
 * @since 2025/3/27
 */
@Getter
@AllArgsConstructor
public enum CheckoutCounterTypeEnum {
    H5("h5", "H5收银台"),
    PC("pc", "PC收银台"),
    MINI_APP("mini_app", "小程序收银台"),
    ;

    private final String code;
    private final String name;

    public static CheckoutCounterTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("不支持的收银台类型"));

    }
}
