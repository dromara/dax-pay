package cn.bootx.daxpay.exception.payment;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 付款记录不存在
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayNotExistedException extends BizException {

    public PayNotExistedException() {
        super(PaymentCenterErrorCode.PAYMENT_RECORD_NOT_EXISTED, "付款记录不存在");
    }

}
