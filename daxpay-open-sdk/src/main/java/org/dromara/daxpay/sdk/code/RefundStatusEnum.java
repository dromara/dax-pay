package org.dromara.daxpay.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款状态枚举
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {

    /** 退款中 */
    PROGRESS("progress"),
    /** 成功 */
    SUCCESS("success"),
    /** 关闭 */
    CLOSE("close"),
    /** 失败 */
    FAIL("fail");

    /** 编码 */
    private final String code;
}
