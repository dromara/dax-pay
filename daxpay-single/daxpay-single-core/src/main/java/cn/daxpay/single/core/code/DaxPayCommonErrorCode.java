package cn.daxpay.single.core.code;

/**
 * 公共错误码
 * @author xxm
 * @since 2024/6/17
 */
public interface DaxPayCommonErrorCode {
    /** 未归类的错误 */
    int UNCLASSIFIED_ERROR = 10000;

    /** 不存在的支付通道 */
    int CHANNEL_NOT_EXIST = 10011;

    /** 不存在的支付方式 */
    int METHOD_NOT_EXIST = 10012;

    /** 不存在的状态 */
    int STATUS_NOT_EXIST = 10013;

    /** 支付通道未启用 */
    int CHANNEL_NOT_ENABLED = 10021;

    /** 支付方式未启用 */
    int METHOD_NOT_ENABLED = 10022;

    /** 配置未启用 */
    int CONFIG_NOT_ENABLED = 10023;

    /** 不支持该能力 */
    int UNSUPPORTED_ABILITY = 10030;

    /** 交易不存在 */
    int TRADE_NOT_EXIST = 10041;

    /** 交易已关闭 */
    int TRADE_CLOSED = 10042;

    /** 交易处理中, 请勿重复操作 */
    int TRADE_PROCESSING = 10043;

    /** 交易状态错误 */
    int TRADE_STATUS_ERROR = 10044;

    /** 交易失败 */
    int TRADE_FAILED = 10045;

    /** 参数校验未通过 */
    int PARAM_VALIDATION_FAILED = 10051;

    /** 验签失败 */
    int VERIFY_SIGN_FAILED = 10052;

    /** 金额超过限额 */
    int AMOUNT_EXCEED_LIMIT = 10060;

    /** 对账文件获取失败 */
    int RECONCILE_GET_FAILED = 10071;

    /** 对账交易账单不存在 */
    int RECONCILE_NOT_EXIST = 10072;

    /** 对账交易账单未生成 */
    int RECONCILE_NOT_GENERATED = 10073;

    /** 对账失败 */
    int RECONCILIATION_FAILED = 10074;

    /** 操作失败 */
    int OPERATION_FAILED = 10080;


    /** 未知异常，系统无法处理 */
    int SYSTEM_UNKNOWN_ERROR = 20000;
}
