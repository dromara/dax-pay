package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付签名类型
 * 字典 sign_type
 * @author xxm
 * @since 2023/12/24
 */
@Getter
@AllArgsConstructor
public enum SignTypeEnum {

    HMAC_SHA256("hmac_sha256", "HMAC_SHA256"),
    MD5("md5", "MD5"),
    SM3("sm3", "SM3"),;

    /** 支付方式 */
    private final String code;
    private final String name;

}
