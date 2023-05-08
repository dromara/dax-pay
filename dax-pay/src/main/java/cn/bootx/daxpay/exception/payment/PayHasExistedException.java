package cn.bootx.daxpay.exception.payment;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 付款已存在
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayHasExistedException extends BizException {

    public PayHasExistedException() {
        super(PaymentCenterErrorCode.PAYMENT_HAS_EXISTED, "付款已存在");
    }

}
