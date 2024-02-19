package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 钱包支付操作
 *
 * @author xxm
 * @since 2021/2/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletPayService {

    private final WalletManager walletManager;


    /**
     * 支付
     */
    public void pay(Integer amount, Wallet wallet){
        // 支付余额
        wallet.setBalance(wallet.getBalance() -amount);
        walletManager.updateById(wallet);
    }

    /**
     * 关闭支付, 将支付成功的金额进行返还
     */
    public void close(PayChannelOrder channelOrder, Wallet wallet) {
        // 将订单的金额退款到钱包
        wallet.setBalance(wallet.getBalance() + channelOrder.getAmount());
        walletManager.updateById(wallet);
    }

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(Wallet wallet, int amount) {
        // 将金额退款到钱包中
        wallet.setBalance(wallet.getBalance() + amount);
        walletManager.updateById(wallet);
    }
}
