package cn.bootx.platform.daxpay.exception.pay;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.daxpay.code.DaxPayErrorCode;

/**
 * 异常金额
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayAmountAbnormalException extends FatalException {

    public PayAmountAbnormalException(String msg) {
        super(DaxPayErrorCode.PAYMENT_AMOUNT_ABNORMAL, msg);
    }

    public PayAmountAbnormalException() {
        super(DaxPayErrorCode.PAYMENT_AMOUNT_ABNORMAL, "异常金额");
    }

}
