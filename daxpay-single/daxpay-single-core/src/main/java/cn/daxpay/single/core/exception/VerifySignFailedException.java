package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 验签失败
 * @author xxm
 * @since 2024/6/17
 */
public class VerifySignFailedException extends PayFailureException{

    public VerifySignFailedException(String message) {
        super(DaxPayCommonErrorCode.VERIFY_SIGN_FAILED,message);
    }

    public VerifySignFailedException() {
        super(DaxPayCommonErrorCode.VERIFY_SIGN_FAILED,"验签失败");
    }
}
