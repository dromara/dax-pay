package cn.bootx.platform.daxpay.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayOrderService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WalletPayRepairStrategy extends AbsPayRepairStrategy {

    private final WalletPayOrderService walletPayOrderService;

    private final WalletPayService walletPayService;

    /**
     * 取消支付
     */
    @Override
    public void doCloseHandler() {
        walletPayService.close(this.getOrder().getId());
        walletPayOrderService.updateClose(this.getOrder().getId());
    }
}
