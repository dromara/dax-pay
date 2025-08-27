package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商户应用状态
 * 字典: mch_app_status
 * @author xxm
 * @since 2024/6/25
 */
@Getter
@AllArgsConstructor
public enum MchAppStatusEnum {
    /**
     * 禁用
     */
    DISABLED("disabled", "禁用"),
    /**
     * 启用
     */
    ENABLE("enable", "启用");

    private final String code;
    private final String name;

    /**
     * 根据编码查找
     */
    public static MchAppStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的商户应用状态类型"));
    }
}
