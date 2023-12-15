package cn.bootx.platform.daxpay.code;

/**
 * 错误码
 *
 * @author xxm
 * @since 2020/12/7
 */
public interface DaxPayErrorCode {

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
     * 支付失败
     */
    int PAY_FAILURE = 28103;

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


}
