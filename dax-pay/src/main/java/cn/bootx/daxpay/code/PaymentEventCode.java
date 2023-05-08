package cn.bootx.daxpay.code;

public interface PaymentEventCode {

    /** 支付中心交换机 */
    String EXCHANGE_PAYMENT = "service.payment";

    /** 支付完成 */
    String PAY_COMPLETE = "pay.complete";

    /** 支付取消 */
    String PAY_CANCEL = "pay.cancel";

    /** 支付退款 */
    String PAY_REFUND = "pay.refund";

    /** 支付单超时 */
    String PAYMENT_EXPIRED_TIME = "payment.expired:time";

}
