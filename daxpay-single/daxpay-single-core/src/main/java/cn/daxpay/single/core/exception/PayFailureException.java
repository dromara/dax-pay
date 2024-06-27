package cn.daxpay.single.core.exception;

import cn.bootx.platform.common.core.exception.BizException;
import cn.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 支付错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayFailureException extends BizException {


    public PayFailureException(int code, String message) {
        super(code, message);
    }

    public PayFailureException(String message) {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, message);
    }

    public PayFailureException() {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, "支付失败");
    }

}
