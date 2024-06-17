package cn.daxpay.single.exception.pay;

import cn.bootx.platform.common.core.exception.BizException;
import cn.daxpay.single.code.DaxPayCommonErrorCode;

/**
 * 付款已存在
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayHasExistedException extends BizException {

    public PayHasExistedException() {
        super(DaxPayCommonErrorCode.PAYMENT_HAS_EXISTED, "付款已存在");
    }

}
