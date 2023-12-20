package cn.bootx.platform.daxpay.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付回调通知状态
 * @author xxm
 * @since 2023/12/20
 */
@Getter
@AllArgsConstructor
public enum PayNotifyStatusEnum {

    // 回调处理状态
    /** 失败 */
    FAIL("fail","失败"),

    /** 成功 */
    SUCCESS("success","成功"),

    /** 忽略 */
    IGNORE("ignore","忽略");

    /** 状态 */
    private final String code;

    /** 状态描述 */
    private final String desc;
}
