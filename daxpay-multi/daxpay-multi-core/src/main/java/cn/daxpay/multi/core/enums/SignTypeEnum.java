package cn.daxpay.multi.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付签名类型
 * @author xxm
 * @since 2023/12/24
 */
@Getter
@AllArgsConstructor
public enum SignTypeEnum {

    HMAC_SHA256("HMAC_SHA256"),
    SM3("SM3"),;

    /** 支付方式 */
    private final String code;

}
