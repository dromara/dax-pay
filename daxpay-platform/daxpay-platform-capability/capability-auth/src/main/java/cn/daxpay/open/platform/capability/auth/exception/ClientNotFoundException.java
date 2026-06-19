package cn.daxpay.open.platform.capability.auth.exception;

import cn.daxpay.open.platform.core.exception.BizException;

/// # 终端不存在
///
public class ClientNotFoundException extends BizException {

    public ClientNotFoundException() {
        // 未找到对应的终端
        super("未找到对应的终端");
        initMessageKey("error.auth.auth.clientNotFound");
    }


    public ClientNotFoundException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
