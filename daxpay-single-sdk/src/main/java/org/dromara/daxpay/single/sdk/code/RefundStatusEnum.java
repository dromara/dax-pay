package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 退款状态枚举
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {

    /**
     * 接口调用成功不代表成功
     */
    PROGRESS("progress","退款中"),
    SUCCESS("success","成功"),
    CLOSE("close","关闭"),
    FAIL("fail","失败");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    /**
     * 根据编码获取枚举
     */
    public static RefundStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(statusEnum -> Objects.equals(statusEnum.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("该枚举不存在"));
    }
}
