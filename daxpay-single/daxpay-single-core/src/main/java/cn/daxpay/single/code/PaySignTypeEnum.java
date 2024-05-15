package cn.daxpay.single.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付签名类型
 * @author xxm
 * @since 2023/12/24
 */
@Getter
@AllArgsConstructor
public enum PaySignTypeEnum {

    HMAC_SHA256("HMAC_SHA256"),
    MD5("MD5"),;

    /** 支付方式 */
    private final String code;

}
