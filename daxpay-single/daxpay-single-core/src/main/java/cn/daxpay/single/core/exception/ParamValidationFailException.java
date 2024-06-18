package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 参数校验未通过
 * @author xxm
 * @since 2024/6/17
 */
public class ParamValidationFailException extends PayFailureException{

    public ParamValidationFailException(String message) {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAIL,message);
    }

    public ParamValidationFailException() {
        super(DaxPayCommonErrorCode.PARAM_VALIDATION_FAIL,"参数校验未通过");
    }
}
