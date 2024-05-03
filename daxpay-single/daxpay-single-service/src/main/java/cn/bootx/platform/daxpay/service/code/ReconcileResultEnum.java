package cn.bootx.platform.daxpay.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对账结果
 * @author xxm
 * @since 2024/5/3
 */
@Getter
@AllArgsConstructor
public enum ReconcileResultEnum {

    /** 一致 */
    CONSISTENT("consistent","一致"),

    /** 不一致 */
    INCONSISTENT("inconsistent","不一致");

    private final String code;
    private final String name;
}
