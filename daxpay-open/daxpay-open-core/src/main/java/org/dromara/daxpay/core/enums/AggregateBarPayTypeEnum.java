package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 聚合付款码支付类型
 * 字典: aggregate_bar_pay_type
 * @author xxm
 * @since 2025/3/24
 */
@Getter
@AllArgsConstructor
public enum AggregateBarPayTypeEnum {
    WECHAT("wechat_pay", "微信支付"),
    ALIPAY("alipay", "支付宝"),
    UNION_PAY("union_pay", "银联支付"),
    ;

    private final String code;
    private final String name;


    public AggregateBarPayTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的聚合付款码支付类型"));
    }
}
