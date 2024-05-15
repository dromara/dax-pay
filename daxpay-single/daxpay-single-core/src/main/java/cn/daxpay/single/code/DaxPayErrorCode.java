package cn.daxpay.single.code;

/**
 * 错误码
 *
 * @author xxm
 * @since 2020/12/7
 */
public interface DaxPayErrorCode {

    /**
     * 支付失败
     */
    int PAY_FAILURE = 20000;

    /**
     * 支付金额异常
     */
    int PAYMENT_AMOUNT_ABNORMAL = 28100;

    /**
     * 支付记录不存在
     */
    int PAYMENT_RECORD_NOT_EXISTED = 28101;

    /**
     * 支付在进行中
     */
    int PAYMENT_IS_PROCESSING = 28102;

    /**
     * 支付已经存在
     */
    int PAYMENT_HAS_EXISTED = 28104;

    /**
     * 支付手动取消
     */
    int PAYMENT_CANCEL = 28105;

    /**
     * 不支持的支付方式
     */
    int PAYMENT_METHOD_UNSUPPORT = 28106;
    /**
     * 钱包已存在
     */
    int WALLET_ALREADY_EXISTS = 28814;

    /**
     * 钱包不存在
     */
    int WALLET_NOT_EXISTS = 28815;

    /**
     * 钱包已被禁用
     */
    int WALLET_BANNED = 28816;

    /**
     * 钱包余额不足
     */
    int WALLET_BALANCE_NOT_ENOUGH = 28817;

    /**
     * wallet 信息不存在
     */
    int WALLET_INFO_NOT_EXISTS = 28819;

    /**
     * 钱包日志异常(类型不正确，或者充值金额小于0等场景)
     */
    int WALLET_LOG_ERROR = 28827;



}
