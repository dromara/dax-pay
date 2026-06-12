package org.dromara.daxpay.platform.core.exception;

import static org.dromara.daxpay.platform.core.code.CommonErrorCode.NONCE_MISSING;

/// # Nonce缺失异常
///
public class NonceMissingException extends BizInfoException {

    public NonceMissingException() {
        super(NONCE_MISSING, "Nonce缺失");
        initMessageKey("error.common.nonceMissing");
    }

    public NonceMissingException(String msg) {
        super(NONCE_MISSING, msg);
    }

}
