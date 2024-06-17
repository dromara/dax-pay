package cn.daxpay.single.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.daxpay.single.code.DaxPayCommonErrorCode;

/**
 * 余额不足异常
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletLackOfBalanceException extends BizException {

    public WalletLackOfBalanceException() {
        super(DaxPayCommonErrorCode.WALLET_BALANCE_NOT_ENOUGH, "余额不足异常");
    }

}
