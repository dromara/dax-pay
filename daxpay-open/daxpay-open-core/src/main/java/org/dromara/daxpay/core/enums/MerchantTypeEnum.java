package org.dromara.daxpay.core.enums;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商户类型
 * 字典: merchant_type
 * @author xxm
 * @since 2024/10/29
 */
@Getter
@AllArgsConstructor
public enum MerchantTypeEnum {

    /** 普通商户 */
    COMMON("common", "普通商户"),

    /** 特约商户 */
    PARTNER("partner", "特约商户"),

    /** 代理商 */
    AGENT("agent", "代理商"),
    ;

    private final String code;
    private final String name;

    /**
     * 根据编码查找
     */
    public static MerchantTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ConfigNotExistException("未找到对应的商户类型"));
    }
}
