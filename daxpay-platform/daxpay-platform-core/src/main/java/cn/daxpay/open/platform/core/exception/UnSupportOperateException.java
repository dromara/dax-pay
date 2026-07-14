package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 不支持的操作异常
///
public class UnSupportOperateException extends BizInfoException {

    public UnSupportOperateException(String message) {
        super(CommonErrorCode.UN_SUPPORTED_OPERATE, message);
    }

    public UnSupportOperateException() {
        super(CommonErrorCode.UN_SUPPORTED_OPERATE, "不支持的操作异常");
        initMessageKey("error.common.unsupportedOperate");
    }

    public UnSupportOperateException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
