package cn.daxpay.multi.core.enums;

import cn.daxpay.multi.core.exception.StatusNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付订单的退款状态
 * @author xxm
 * @since 2024/6/7
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum {
    NO_REFUND("no_refund"),
    REFUNDING("refunding"),
    PARTIAL_REFUND("partial_refund"),
    REFUNDED("refunded"),
    ;
    private final String code;

    /**
     * 根据编码获取枚举
     */
    public static PayRefundStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new StatusNotExistException("该退款状态不存在"));
    }

}
