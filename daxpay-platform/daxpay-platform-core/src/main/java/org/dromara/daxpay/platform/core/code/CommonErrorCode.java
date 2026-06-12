package org.dromara.daxpay.platform.core.code;

/// # 公用错误码常量 10000-19999
///
public interface CommonErrorCode {

    /// 系统错误
    int SYSTEM_ERROR = 10500;

    /// 认证失败
    int AUTHENTICATION_FAIL = 10401;

    /// 参数处理失败
    int PARSE_PARAMETERS_ERROR = 10505;

    /// 参数验证失败
    int VALIDATE_PARAMETERS_ERROR = 10506;

    /// 重复操作异常
    int REPETITIVE_OPERATION_ERROR = 10507;

    /// 资源不存在
    int SOURCES_NOT_EXIST = 10404;

    /// 数据不存在
    int DATA_NOT_EXIST = 10405;

    /// 不支持的操作
    int UN_SUPPORTED_OPERATE = 10415;

    /// 危险SQL异常,
    int DANGER_SQL = 10512;

    /// Nonce缺失
    int NONCE_MISSING = 10408;

    /// Nonce无效或已过期
    int NONCE_INVALID = 10409;

    /// 请求时间戳超出允许范围
    int TIMESTAMP_EXPIRED = 10410;

    /// 风控拒绝
    int RISK_REJECT = 10411;

}
