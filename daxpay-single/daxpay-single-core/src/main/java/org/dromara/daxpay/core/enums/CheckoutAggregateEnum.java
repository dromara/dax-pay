package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.daxpay.core.exception.ConfigNotExistException;

import java.util.Arrays;
import java.util.Objects;

/**
 * 收银台聚合支付类型
 * 字典: checkout_aggregate
 * @author xxm
 * @since 2024/11/27
 */
@Getter
@AllArgsConstructor
public enum CheckoutAggregateEnum {

    WECHAT("wechat_pay", "微信支付"),
    ALIPAY("alipay", "支付宝"),
    ;

    private final String code;
    private final String name;

    public CheckoutAggregateEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的聚合支付类型"));
    }
}
