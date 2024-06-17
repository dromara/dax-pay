package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付回调处理状态
 * @author xxm
 * @since 2023/12/20
 */
@Getter
@AllArgsConstructor
public enum CallbackStatusEnum {

    SUCCESS("success","成功"),
    FAIL("fail","失败"),
    IGNORE("ignore","忽略"),
    EXCEPTION("exception","异常"),
    NOT_FOUND("not_found","未找到");

    private final String code;
    private final String name;
}
