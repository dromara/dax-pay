package cn.bootx.platform.daxpay.exception.payment;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 付款记录不存在
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayNotExistedException extends BizException {

    public PayNotExistedException() {
        super(PaymentErrorCode.PAYMENT_RECORD_NOT_EXISTED, "付款记录不存在");
    }

}
