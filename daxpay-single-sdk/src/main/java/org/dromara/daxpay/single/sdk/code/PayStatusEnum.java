package org.dromara.daxpay.single.sdk.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付状态
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@RequiredArgsConstructor
public enum PayStatusEnum {
    PROGRESS("progress","支付中"),
    SUCCESS("success","成功"),
    CLOSE("close","支付关闭"),
    REFUNDING("refunding","退款中"),
    PARTIAL_REFUND("partial_refund","部分退款"),
    REFUNDED("refunded","全部退款"),
    FAIL("fail","失败");

    /** 编码 */
    private final String code;

    /** 名称 */
    private final String name;

    /**
     * 根据编码获取枚举
     */
    public static PayStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("该枚举不存在"));
    }

}
