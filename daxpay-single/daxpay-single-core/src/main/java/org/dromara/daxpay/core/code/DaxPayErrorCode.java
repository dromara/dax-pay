package org.dromara.daxpay.core.code;

/**
 * 公共错误码
 * @author xxm
 * @since 2024/6/17
 */
public interface DaxPayErrorCode {
    /** 未归类的错误 */
    int UNCLASSIFIED_ERROR = 20000;

    /** 不存在的支付通道 */
    int CHANNEL_NOT_EXIST = 20011;

    /** 不存在的支付方式 */
    int METHOD_NOT_EXIST = 20012;

    /** 不存在的状态 */
    int STATUS_NOT_EXIST = 20013;

    /** 支付通道未启用 */
    int CHANNEL_NOT_ENABLE = 20021;

    /** 支付方式未启用 */
    int METHOD_NOT_ENABLE = 20022;

    /** 配置未启用 */
    int CONFIG_NOT_ENABLE = 20023;

    /** 配置错误 */
    int CONFIG_ERROR = 20024;

    /** 配置不存在 */
    int CONFIG_NOT_EXIST = 20025;

    /** 不支持该能力 */
    int UNSUPPORTED_ABILITY = 20030;

    /** 交易不存在 */
    int TRADE_NOT_EXIST = 20041;

    /** 交易已关闭 */
    int TRADE_CLOSED = 20042;

    /** 交易处理中, 请勿重复操作 */
    int TRADE_PROCESSING = 20043;

    /** 交易状态错误 */
    int TRADE_STATUS_ERROR = 20044;

    /** 交易失败 */
    int TRADE_FAIL = 20045;

    /** 验签失败 */
    int VERIFY_SIGN_FAILED = 20052;

    /** 金额超过限额 */
    int AMOUNT_EXCEED_LIMIT = 20060;

    /** 对账失败 */
    int RECONCILE_FAIL = 20071;

    /** 操作失败 */
    int OPERATION_FAIL = 20080;

    /** 操作处理中, 请勿重复操作 */
    int OPERATION_PROCESSING = 20081;

    /** 不支持的操作 */
    int OPERATION_UNSUPPORTED = 20082;

    /** 数据错误 */
    int DATA_ERROR = 20091;

    /** 未知异常，系统无法处理 */
    int SYSTEM_UNKNOWN_ERROR = 30000;
}
