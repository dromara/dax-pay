package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletLogManager;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletLog;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletRechargeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author xxm
 * @since 2023/6/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletManager walletManager;

    private final WalletLogManager walletLogManager;

    /**
     * 开通操作 创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void createWallet(Long userId) {
        // 判断钱包是否已开通
        if (walletManager.existsByUser(userId)) {
            throw new BizException("钱包已经开通");
        }
        Wallet wallet = new Wallet().setUserId(userId)
                .setBalance(BigDecimal.ZERO)
                .setFreezeBalance(BigDecimal.ZERO)
                .setStatus(WalletCode.STATUS_NORMAL);
        walletManager.save(wallet);
        // 激活 log
        WalletLog activeLog = new WalletLog().setWalletId(wallet.getId())
                .setUserId(wallet.getUserId())
                .setType(WalletCode.LOG_ACTIVE)
                .setRemark("激活钱包")
                .setOperationSource(WalletCode.OPERATION_SOURCE_USER);
        walletLogManager.save(activeLog);
    }

    /**
     * 批量开通
     */
    @Transactional(rollbackFor = Exception.class)
    public void createWalletBatch(List<Long> userIds) {
        // 查询出
        List<Long> existUserIds = walletManager.findExistUserIds(userIds);
        userIds.removeAll(existUserIds);
        List<Wallet> wallets = userIds.stream()
                .map(userId -> new Wallet().setUserId(userId)
                        .setStatus(WalletCode.STATUS_NORMAL)
                        .setFreezeBalance(BigDecimal.ZERO)
                        .setBalance(BigDecimal.ZERO))
                .collect(Collectors.toList());
        walletManager.saveAll(wallets);
        List<WalletLog> walletLogs = wallets.stream()
                .map(wallet -> new WalletLog().setWalletId(wallet.getId())
                        .setUserId(wallet.getUserId())
                        .setAmount(BigDecimal.ZERO)
                        .setType(WalletCode.LOG_ACTIVE)
                        .setRemark("批量开通钱包")
                        .setOperationSource(WalletCode.OPERATION_SOURCE_ADMIN))
                .collect(Collectors.toList());
        walletLogManager.saveAll(walletLogs);
    }

    /**
     * 锁定钱包
     */
    @Transactional(rollbackFor = Exception.class)
    public void lock(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        wallet.setStatus(WalletCode.STATUS_FORBIDDEN);
        walletManager.updateById(wallet);
        // 激活 log
        WalletLog log = new WalletLog().setWalletId(wallet.getId())
                .setUserId(wallet.getUserId())
                .setType(WalletCode.LOG_LOCK)
                .setRemark("锁定钱包")
                .setOperationSource(WalletCode.OPERATION_SOURCE_ADMIN);
        walletLogManager.save(log);
    }

    /**
     * 解锁钱包
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlock(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        wallet.setStatus(WalletCode.STATUS_NORMAL);
        walletManager.updateById(wallet);
        // 激活 log
        WalletLog log = new WalletLog().setWalletId(wallet.getId())
                .setUserId(wallet.getUserId())
                .setType(WalletCode.LOG_UNLOCK)
                .setRemark("解锁钱包")
                .setOperationSource(WalletCode.OPERATION_SOURCE_ADMIN);
        walletLogManager.save(log);
    }

    /**
     * 更改余额 也可以扣款
     */
    @Transactional(rollbackFor = Exception.class)
    public void changerBalance(WalletRechargeParam param) {
        if (BigDecimalUtil.compareTo(param.getAmount(), BigDecimal.ZERO) == 1) {
            walletManager.increaseBalance(param.getWalletId(), param.getAmount());
        }
        else if (BigDecimalUtil.compareTo(param.getAmount(), BigDecimal.ZERO) == -1) {
            walletManager.reduceBalanceUnlimited(param.getWalletId(), param.getAmount());
        }
        else {
            return;
        }
        Wallet wallet = walletManager.findById(param.getWalletId()).orElseThrow(DataNotExistException::new);
        WalletLog walletLog = new WalletLog().setAmount(param.getAmount())
                .setWalletId(wallet.getId())
                .setType(WalletCode.LOG_ADMIN_CHANGER)
                .setUserId(wallet.getUserId())
                .setRemark(String.format("系统变动余额 %.2f ", param.getAmount()))
                .setOperationSource(WalletCode.OPERATION_SOURCE_ADMIN);
        walletLogManager.save(walletLog);
    }
}
