package cn.bootx.daxpay.exception.waller;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 钱包被禁用
 *
 * @author xxm
 * @date 2020/12/8
 */
public class WalletBannedException extends BizException {

    public WalletBannedException() {
        super(PaymentCenterErrorCode.WALLET_BANNED, "钱包被禁用");
    }

}
