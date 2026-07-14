package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 请求时间戳超出允许范围异常
///
public class TimestampExpiredException extends BizInfoException {

    public TimestampExpiredException() {
        super(CommonErrorCode.TIMESTAMP_EXPIRED, "请求时间戳已过期");
        initMessageKey("error.common.timestampExpired");
    }

    public TimestampExpiredException(String msg) {
        super(CommonErrorCode.TIMESTAMP_EXPIRED, msg);
    }

}
