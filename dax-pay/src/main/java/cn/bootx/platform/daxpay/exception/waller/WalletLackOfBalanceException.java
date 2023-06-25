package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 余额不足异常
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletLackOfBalanceException extends BizException {

    public WalletLackOfBalanceException() {
        super(PaymentErrorCode.WALLET_BALANCE_NOT_ENOUGH, "余额不足异常");
    }

}
