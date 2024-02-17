package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.code.WalletCode;
import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.param.channel.wallet.CreateWalletParam;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletRechargeParam;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalleteeDductParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 钱包管理接口
 * @author xxm
 * @since 2023/6/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletManager walletManager;

    /**
     * 创建钱包操作，默认为启用状态， 不传输余额则默认为0
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateWalletParam param) {
        // 判断钱包是否已开通
        if (walletManager.existsByUser(param.getUserId())) {
            throw new BizException("钱包已经开通");
        }
        int balance = Objects.isNull(param.getBalance()) ? 0 : param.getBalance();
        Wallet wallet = new Wallet()
                .setUserId(param.getUserId())
                .setName(param.getName())
                .setBalance(balance)
                .setStatus(WalletCode.STATUS_NORMAL);
        walletManager.save(wallet);
    }

    /**
     * 锁定钱包
     */
    @Transactional(rollbackFor = Exception.class)
    public void lock(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        wallet.setStatus(WalletCode.STATUS_FORBIDDEN);
        walletManager.updateById(wallet);
    }

    /**
     * 解锁钱包
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlock(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        wallet.setStatus(WalletCode.STATUS_NORMAL);
        walletManager.updateById(wallet);
    }

    /**
     * 余额充值
     */
    @Transactional(rollbackFor = Exception.class)
    public void recharge(WalletRechargeParam param) {
        Wallet wallet = null;

        if (Objects.nonNull(param.getWalletId())){
            wallet =  walletManager.findById(param.getWalletId()).orElseThrow(DataNotExistException::new);
        }
        if (Objects.isNull(wallet)){
            wallet = walletManager.findByUser(param.getUserId()).orElseThrow(DataNotExistException::new);
        }
        wallet.setBalance(wallet.getBalance() + param.getAmount());
        walletManager.updateById(wallet);
    }

    /**
     * 余额扣减
     */
    @Transactional(rollbackFor = Exception.class)
    public void deduct(WalleteeDductParam param) {
        Wallet wallet = null;
        if (Objects.nonNull(param.getWalletId())){
            wallet =  walletManager.findById(param.getWalletId()).orElseThrow(DataNotExistException::new);
        }
        if (Objects.isNull(wallet)){
            wallet = walletManager.findByUser(param.getUserId()).orElseThrow(DataNotExistException::new);
        }
        if (wallet.getBalance() > param.getAmount()){
            throw new BizException("余额不足");
        }
        wallet.setBalance(wallet.getBalance() - param.getAmount());
        walletManager.updateById(wallet);
    }
}
