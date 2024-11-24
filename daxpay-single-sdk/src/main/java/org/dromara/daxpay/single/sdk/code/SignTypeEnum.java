package org.dromara.daxpay.single.sdk.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付签名类型
 * @author xxm
 * @since 2023/12/24
 */
@Getter
@AllArgsConstructor
public enum SignTypeEnum {

    HMAC_SHA256("HMAC_SHA256"),
    MD5("MD5"),
    SM3("SM3"),
    ;

    /** 支付方式 */
    private final String code;


    /**
     * 根据编码获取枚举
     */
    public static SignTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("该枚举不存在"));
    }

}
