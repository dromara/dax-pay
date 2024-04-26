package cn.bootx.platform.daxpay.exception.pay;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.DaxPayErrorCode;

/**
 * 支付错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayFailureException extends BizException {

    public PayFailureException(String message) {
        super(DaxPayErrorCode.PAY_FAILURE, message);
    }

    public PayFailureException() {
        super(DaxPayErrorCode.PAY_FAILURE, "支付失败");
    }

}
