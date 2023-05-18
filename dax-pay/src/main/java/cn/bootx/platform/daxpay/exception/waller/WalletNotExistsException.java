package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 钱包不存在
 *
 * @author xxm
 * @date 2020/12/8
 */
public class WalletNotExistsException extends BizException {

    public WalletNotExistsException() {
        super(PaymentErrorCode.WALLET_NOT_EXISTS, "钱包不存在");
    }

}
