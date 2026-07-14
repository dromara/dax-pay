package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # Nonce缺失异常
///
public class NonceMissingException extends BizInfoException {

    public NonceMissingException() {
        super(CommonErrorCode.NONCE_MISSING, "error.common.nonceMissing");
        initMessageKey("error.common.nonceMissing");
    }

    public NonceMissingException(String msg) {
        super(CommonErrorCode.NONCE_MISSING, msg);
    }

}
