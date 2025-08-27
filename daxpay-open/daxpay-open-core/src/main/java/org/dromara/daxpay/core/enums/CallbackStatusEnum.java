package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付回调处理状态
 * 字典: callback_status
 * @author xxm
 * @since 2023/12/20
 */
@Getter
@AllArgsConstructor
public enum CallbackStatusEnum {
    /** 成功 */
    SUCCESS("success"),
    /** 失败 */
    FAIL("fail"),
    /** 忽略 */
    IGNORE("ignore"),
    /** 异常 */
    EXCEPTION("exception"),
    /** 未找到 */
    NOT_FOUND("not_found");

    private final String code;
}
