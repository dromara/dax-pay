package cn.bootx.platform.daxpay.service.core.payment.close.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayOrderService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WalletPayCloseStrategy extends AbsPayCloseStrategy {
    private final WalletPayService walletPayService;
    private final WalletPayOrderService walletPayOrderService;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        walletPayService.close(this.getOrder().getId());
        walletPayOrderService.updateClose(this.getOrder().getId());
    }
}
