package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizException;

/// # 终端方式被停用
///
public class ClientNotEnableException extends BizException {

    public ClientNotEnableException() {
        // 指定终端方式已被停用
        super("指定终端方式已被停用");
        initMessageKey("error.auth.auth.clientNotEnable");
    }


    public ClientNotEnableException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
