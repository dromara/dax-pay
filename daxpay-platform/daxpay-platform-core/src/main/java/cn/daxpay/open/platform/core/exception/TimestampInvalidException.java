package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 请求时间戳格式非法异常
///
/// 区别于 [TimestampExpiredException]: 本异常表示时间戳请求头缺失或不是合法数字,
/// 属于客户端集成缺陷或攻击探测信号, 而非正常的时钟漂移/超时。
///
public class TimestampInvalidException extends BizInfoException {

    public TimestampInvalidException() {
        super(CommonErrorCode.TIMESTAMP_INVALID, "请求时间戳格式非法");
        initMessageKey("error.common.timestampInvalid");
    }

    public TimestampInvalidException(String msg) {
        super(CommonErrorCode.TIMESTAMP_INVALID, msg);
    }

}
