package cn.bootx.daxpay.exception.payment;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 异常金额
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayAmountAbnormalException extends FatalException {

    public PayAmountAbnormalException(String msg) {
        super(PaymentCenterErrorCode.PAYMENT_AMOUNT_ABNORMAL, msg);
    }

    public PayAmountAbnormalException() {
        super(PaymentCenterErrorCode.PAYMENT_AMOUNT_ABNORMAL, "异常金额");
    }

}
