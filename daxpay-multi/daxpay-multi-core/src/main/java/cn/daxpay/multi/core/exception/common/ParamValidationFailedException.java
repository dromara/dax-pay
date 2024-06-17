package cn.daxpay.multi.core.exception.common;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 *
 * @author xxm
 * @since 2024/6/17
 */
public class ParamValidationFailedException extends PayFailureException{

    public ParamValidationFailedException(String message) {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAILED,message);
    }

    public ParamValidationFailedException() {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAILED,"不存在的支付通道");
    }
}
