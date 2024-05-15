package cn.daxpay.single.exception.waller;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.daxpay.single.code.DaxPayErrorCode;

/**
 * 钱包信息不存在
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletInfoNotExistException extends FatalException {

    public WalletInfoNotExistException() {
        super(DaxPayErrorCode.WALLET_INFO_NOT_EXISTS, "钱包信息不存在");
    }

}
