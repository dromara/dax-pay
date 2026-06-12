package org.dromara.daxpay.platform.core.exception;

import static org.dromara.daxpay.platform.core.code.CommonErrorCode.TIMESTAMP_EXPIRED;

/// # 请求时间戳超出允许范围异常
///
public class TimestampExpiredException extends BizInfoException {

    public TimestampExpiredException() {
        super(TIMESTAMP_EXPIRED, "请求时间戳已过期");
        initMessageKey("error.common.timestampExpired");
    }

    public TimestampExpiredException(String msg) {
        super(TIMESTAMP_EXPIRED, msg);
    }

}
