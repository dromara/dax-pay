package cn.daxpay.multi.core.code;

/**
 * 公共错误码
 * @author xxm
 * @since 2024/6/17
 */
public interface DaxPayCommonErrorCode {
    /** 未归类的错误 */
    int UNCLASSIFIED_ERROR = 10000;

    /** 不存在的支付通道 */
    int PAYMENT_CHANNEL_NOT_EXIST = 10002;

    /** 不存在的支付方式 */
    int PAYMENT_METHOD_NOT_EXIST = 10001;

    /** 不存在的支付同步状态 */
    int PAYMENT_SYNC_STATUS_NOT_EXIST = 10003;

    /** 参数校验未通过 */
    int PARAM_VALIDATION_FAILED = 10004;

    /** 支付通道未启用 */
    int PAYMENT_CHANNEL_NOT_ENABLED_ERROR = 10005;

    /** 退款失败 */
    int REFUND_FAILED = 10006;

    /** 金额超过限额 */
    int AMOUNT_EXCEED_LIMIT = 10007;

    /** 未开启分账配置 */
    int ALLOCATION_CONFIG_NOT_ENABLED = 10008;

    /** 不支持该能力 */
    int UNSUPPORTED_ABILITY = 10009;

    /** 获取对账文件失败 */
    int GET_RECONCILIATION_FILE_FAILED = 10010;

    /** 验签失败 */
    int VERIFY_SIGN_FAILED = 10011;

    /** 对账交易账单不存在 */
    int RECONCILIATION_TRADE_BILL_NOT_EXIST = 10012;

    /** 对账交易账单未生成 */
    int RECONCILIATION_TRADE_BILL_NOT_GENERATED = 10013;

    /** 对账失败 */
    int RECONCILIATION_FAILED = 10014;

    /** 支付订单已撤销 */
    int PAYMENT_ORDER_CANCELED = 10015;

    /** 支付订单已关闭 */
    int PAYMENT_ORDER_CLOSED = 10016;

    /** 交易处理中, 请勿重新操作 */
    int TRADE_PROCESSING = 10017;

    /** 交易状态错误 */
    int TRADE_STATUS_ERROR = 10018;

    /** 系统无法处理, 未知异常 */
    int SYSTEM_UNKNOWN_ERROR = 10019;
}
