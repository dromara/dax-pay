package org.dromara.daxpay.platform.core.exception;

import static org.dromara.daxpay.platform.core.code.CommonErrorCode.NONCE_INVALID;

/// # Nonce无效或已过期异常
///
public class NonceInvalidException extends BizInfoException {

    public NonceInvalidException() {
        super(NONCE_INVALID, "Nonce无效或已过期");
        initMessageKey("error.common.nonceInvalid");
    }

    public NonceInvalidException(String msg) {
        super(NONCE_INVALID, msg);
    }

}
