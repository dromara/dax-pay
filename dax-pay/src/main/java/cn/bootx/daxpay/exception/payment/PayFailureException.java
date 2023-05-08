package cn.bootx.daxpay.exception.payment;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 付款错误
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayFailureException extends BizException {

    public PayFailureException(String message) {
        super(PaymentCenterErrorCode.PAY_FAILURE, message);
    }

    public PayFailureException() {
        super(PaymentCenterErrorCode.PAY_FAILURE, "支付失败");
    }

}
