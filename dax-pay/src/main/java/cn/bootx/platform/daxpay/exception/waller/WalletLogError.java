package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.daxpay.code.PaymentCenterErrorCode;

/**
 * 钱包日志错误
 *
 * @author xxm
 * @date 2020/12/8
 */
public class WalletLogError extends FatalException {

    public WalletLogError() {
        super(PaymentCenterErrorCode.WALLET_LOG_ERROR, "钱包日志错误");
    }

}
