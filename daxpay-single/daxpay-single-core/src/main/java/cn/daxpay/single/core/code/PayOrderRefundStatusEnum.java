package cn.daxpay.single.core.code;

import cn.bootx.platform.common.core.exception.DataNotExistException;
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
public enum PayOrderRefundStatusEnum {
    NO_REFUND("no_refund","未退款"),
    REFUNDING("refunding","退款中"),
    PARTIAL_REFUND("partial_refund","部分退款"),
    REFUNDED("refunded","全部退款"),
    ;
    private final String code;
    private final String name;

    /**
     * 根据编码获取枚举
     */
    public static PayOrderRefundStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("该枚举不存在"));
    }

}
