package cn.bootx.daxpay.exception.payment;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 付款正在处理中
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayIsProcessingException extends BizException {

    public PayIsProcessingException() {
        super(PaymentCenterErrorCode.PAYMENT_IS_PROCESSING, "付款正在处理中");
    }

}
