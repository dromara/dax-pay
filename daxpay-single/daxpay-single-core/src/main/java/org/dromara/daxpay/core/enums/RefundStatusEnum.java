package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 退款状态枚举
 * 字典: refund_status
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
    SUCCESS("success","退款成功"),
    CLOSE("close","退款关闭"),
    FAIL("fail","退款失败");

    /** 编码 */
    private final String code;
    /** 名称 */
    private final String name;

    public static RefundStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("退款状态不存在"));
    }

}
