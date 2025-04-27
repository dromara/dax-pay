package org.dromara.daxpay.service.enums;

import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对账单文件
 * @author xxm
 * @since 2024/5/4
 */
@Getter
@AllArgsConstructor
public enum ReconcileFileTypeEnum {

    TRADE("trade", "交易对账单"),
    ZIP("zip", "压缩包"),
    ;

    private final String code;
    private final String name;

    public static ReconcileFileTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotEnableException("未知的对账文件类型"));
    }
}
