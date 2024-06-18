package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 参数校验未通过
 * @author xxm
 * @since 2024/6/17
 */
public class ParamValidationFailedException extends PayFailureException{

    public ParamValidationFailedException(String message) {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAILED,message);
    }

    public ParamValidationFailedException() {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAILED,"参数校验未通过");
    }
}
