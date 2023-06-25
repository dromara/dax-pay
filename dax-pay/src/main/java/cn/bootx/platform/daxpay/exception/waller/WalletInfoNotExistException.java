package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 钱包信息不存在
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletInfoNotExistException extends FatalException {

    public WalletInfoNotExistException() {
        super(PaymentErrorCode.WALLET_INFO_NOT_EXISTS, "钱包信息不存在");
    }

}
