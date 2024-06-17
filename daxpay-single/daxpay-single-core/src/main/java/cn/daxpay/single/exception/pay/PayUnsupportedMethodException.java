package cn.daxpay.single.exception.pay;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.daxpay.single.code.DaxPayCommonErrorCode;

/**
 * 付款方式不支持异常
 *
 * @author xxm
 * @since 2020/12/9
 */
public class PayUnsupportedMethodException extends FatalException {

    public PayUnsupportedMethodException() {
        super(DaxPayCommonErrorCode.PAYMENT_METHOD_UNSUPPORT, "不支持的支付方式");
    }

}
