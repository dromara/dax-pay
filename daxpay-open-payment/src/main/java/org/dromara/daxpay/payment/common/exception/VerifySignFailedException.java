package org.dromara.daxpay.payment.common.exception;

import org.dromara.daxpay.payment.common.code.DaxPayErrorCode;

/**
 * 验签失败
 * @author xxm
 * @since 2024/6/17
 */
public class VerifySignFailedException extends PayFailureException{

    public VerifySignFailedException(String message) {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED,message);
    }

    public VerifySignFailedException() {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED,"验签失败");
    }
}
