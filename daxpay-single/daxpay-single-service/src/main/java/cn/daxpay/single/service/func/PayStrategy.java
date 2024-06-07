package cn.daxpay.single.service.func;

/**
 * 支付相关策略标识接口
 * @author xxm
 * @since 2023/12/27
 */
public interface PayStrategy {

    /**
     * 策略标识
     * @see cn.daxpay.single.param.payment.pay.PayCancelParam
     */
    String getChannel();
}
