package cn.daxpay.single.exception.pay;

import cn.bootx.platform.common.core.exception.BizException;
import cn.daxpay.single.code.DaxPayErrorCode;

/**
 * 付款正在处理中
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayIsProcessingException extends BizException {

    public PayIsProcessingException() {
        super(DaxPayErrorCode.PAYMENT_IS_PROCESSING, "付款正在处理中");
    }

}
