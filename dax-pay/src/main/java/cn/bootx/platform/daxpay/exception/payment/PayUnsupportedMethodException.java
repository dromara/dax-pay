package cn.bootx.platform.daxpay.exception.payment;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.daxpay.code.PaymentCenterErrorCode;

/**
 * 付款方式不支持异常
 *
 * @author xxm
 * @date 2020/12/9
 */
public class PayUnsupportedMethodException extends FatalException {

    public PayUnsupportedMethodException() {
        super(PaymentCenterErrorCode.PAYMENT_METHOD_UNSUPPORT, "不支持的支付方式");
    }

}
