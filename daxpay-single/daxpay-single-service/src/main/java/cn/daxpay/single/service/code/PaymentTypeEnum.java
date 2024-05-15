package cn.daxpay.single.service.code;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付系统中常见的操作类型, 如支付/退款/转账等
 * @author xxm
 * @since 2024/1/28
 */
@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {

    PAY("pay","支付"),
    REFUND("refund","退款"),
    TRANSFER("transfer","转账");

    private final String code;
    private final String name;

    public static PaymentTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("未找到对应的支付类型"));

    }
}
