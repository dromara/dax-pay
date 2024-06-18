package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 不支持该能力
 * @author xxm
 * @since 2024/6/17
 */
public class UnsupportedAbilityException extends PayFailureException{

    public UnsupportedAbilityException(String message) {
        super(DaxPayCommonErrorCode.UNSUPPORTED_ABILITY,message);
    }

    public UnsupportedAbilityException() {
        super(DaxPayCommonErrorCode.UNSUPPORTED_ABILITY,"不支持该能力");
    }
}
