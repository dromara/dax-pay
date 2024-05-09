package cn.bootx.platform.common.core.code;

/**
 * 公用错误码常量 10001-10100
 */
public interface CommonErrorCode {

    /** 系统错误 */
    int SYSTEM_ERROR = 10001;

    /** 远程调用错误 */
    int REMOTE_CALL_ERROR = 10002;

    /** 认证失败 */
    int AUTHENTICATION_FAIL = 10004;

    /** 参数处理失败 */
    int PARSE_PARAMETERS_ERROR = 10005;

    /** 参数验证失败 */
    int VALIDATE_PARAMETERS_ERROR = 10006;

    /** 重复操作异常 */
    int REPETITIVE_OPERATION_ERROR = 10007;

    /** 数据过期 */
    int DATA_OUT_OF_DATE = 10008;

    /** 不支持的下载 */
    int UN_SUPPORTED_READ = 10009;

    /** 数据不存在 */
    int DATA_NOT_EXIST = 10010;

    /** 不支持的操作 */
    int UN_SUPPORTED_OPERATE = 10011;

    /** 危险SQL异常, */
    int DANGER_SQL = 10012;

    // 未知异常
    int UNKNOWN_ERROR = -1;

}
