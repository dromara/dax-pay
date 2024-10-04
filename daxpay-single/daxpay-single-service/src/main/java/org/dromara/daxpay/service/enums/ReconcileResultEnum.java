package org.dromara.daxpay.service.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 对账结果
 * 字典: reconcile_result
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

    public static ReconcileResultEnum findByCode(String code) {
        return Arrays.stream(ReconcileResultEnum.values())
                .filter(value -> value.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的枚举"));
    }
}
