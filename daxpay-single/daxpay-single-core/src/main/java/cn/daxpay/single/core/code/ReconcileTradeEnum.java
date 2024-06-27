package cn.daxpay.single.core.code;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对账交易类型
 * @author xxm
 * @since 2024/1/23
 */
@Getter
@AllArgsConstructor
public enum ReconcileTradeEnum {

    PAY("pay","支付"),
    REFUND("refund","退款"),
    REVOKED("revoked","撤销");

    private final String code;
    private final String name;

    public static ReconcileTradeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("未找到对应的交易类型"));
    }
}
