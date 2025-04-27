package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 网关支付类型
 * 字典 gateway_pay_type
 * @author xxm
 * @since 2024/11/26
 */
@Getter
@AllArgsConstructor
public enum GatewayPayTypeEnum {

    H5("h5", "H5收银台支付"),
    PC("pc", "PC收银台支付"),
    MINI_APP("mini_app", "小程序收银台支付"),
    AGGREGATE("aggregate", "网关聚合支付"),
    ;

    private final String code;
    private final String name;

    public static GatewayPayTypeEnum findBuyCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("不支持的收银台类型"));

    }
}
