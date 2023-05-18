package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletLogManager;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletLog;
import cn.bootx.platform.daxpay.exception.waller.WalletBannedException;
import cn.bootx.platform.daxpay.exception.waller.WalletLogError;
import cn.bootx.platform.daxpay.exception.waller.WalletNotExistsException;
import cn.bootx.platform.daxpay.param.paymodel.wallet.WalletRechargeParam;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 钱包的相关操作
 *
 * @author xxm
 * @date 2020/12/8
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
        Wallet wallet = new Wallet().setUserId(userId).setBalance(BigDecimal.ZERO).setStatus(WalletCode.STATUS_NORMAL);
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
    public void createWalletBatch(List<Long> userIds) {
        // 查询出
        List<Long> existUserIds = walletManager.findExistUserIds(userIds);
        userIds.removeAll(existUserIds);
        List<Wallet> wallets = userIds.stream()
            .map(userId -> new Wallet().setUserId(userId)
                .setStatus(WalletCode.STATUS_NORMAL)
                .setBalance(BigDecimal.ZERO))
            .collect(Collectors.toList());
        walletManager.saveAll(wallets);
        List<WalletLog> walletLogs = wallets.stream()
            .map(wallet -> new WalletLog().setWalletId(wallet.getId())
                .setUserId(wallet.getUserId())
                .setAmount(BigDecimal.ZERO)
                .setType(WalletCode.LOG_ACTIVE)
                .setRemark("激活钱包")
                .setOperationSource(WalletCode.OPERATION_SOURCE_USER))
            .collect(Collectors.toList());
        walletLogManager.saveAll(walletLogs);
    }

    /**
     * 锁定钱包
     */
    public void lock(Long walletId) {
        walletManager.setUpStatus(walletId, WalletCode.STATUS_FORBIDDEN);
    }

    /**
     * 解锁钱包
     */
    public void unlock(Long walletId) {
        walletManager.setUpStatus(walletId, WalletCode.STATUS_NORMAL);
    }

    /**
     * 充值操作 也可以扣款
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

    /**
     * 根据支付单对钱包充值的余额进行扣减
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductedBalanceByPaymentId(Long paymentId, Long orderId, String remark, Boolean isThrowError) {

        // 根据支付记录ID查询交易的金额和交易的钱包ID
        WalletLog walletLog = walletLogManager.findFirstByPayment(paymentId).orElseThrow(DataNotExistException::new);
        if (walletLog == null) {
            return;
        }

        // 充值类型
        List<Integer> chargeLogType = Lists.newArrayList(WalletCode.LOG_RECHARGE, WalletCode.LOG_AUTO_RECHARGE,
                WalletCode.LOG_ADMIN_CHANGER);

        // 保证是充值类型 且充值金额大于0
        if (!chargeLogType.contains(walletLog.getType())
                || BigDecimalUtil.compareTo(walletLog.getAmount(), BigDecimal.ZERO) < 0) {
            log.warn("退款 发现非充值交易，日志ID:{},交易类型:{}", walletLog.getId(), walletLog.getType());
            if (isThrowError) {
                throw new WalletLogError();
            }
            return;
        }

        // 获取钱包ID 并扣减对应金额(允许扣成负数)
        walletManager.reduceBalanceUnlimited(walletLog.getWalletId(), walletLog.getAmount());

        // 记录日志
        WalletLog log = new WalletLog().setWalletId(walletLog.getWalletId())
            .setUserId(walletLog.getUserId())
            .setPaymentId(paymentId)
            .setAmount(walletLog.getAmount())
            .setType(WalletCode.LOG_SYSTEM_REDUCE_BALANCE)
            .setRemark(String.format("系统减少余额 %.2f (" + remark + ")", walletLog.getAmount()))
            .setOperationSource(WalletCode.OPERATION_SOURCE_SYSTEM)
            .setPaymentId(paymentId)
            .setBusinessId(String.valueOf(orderId));
        walletLogManager.save(log);

    }

    /**
     * 查询钱包，如果钱包不存在或者钱包被禁用将抛出异常
     */
    public Wallet getNormalWalletById(Long walletId) {
        // 查询Wallet
        Wallet wallet = walletManager.findById(walletId).orElseThrow(WalletNotExistsException::new);
        // 是否被禁用
        if (Objects.equals(WalletCode.STATUS_FORBIDDEN, wallet.getStatus())) {
            throw new WalletBannedException();
        }
        return wallet;
    }

    /**
     * 查询钱包，如果钱包不存在或者钱包被禁用将抛出异常
     */
    public Wallet getNormalWalletByUserId(Long userId) {
        // 查询Wallet
        Wallet wallet = walletManager.findByUser(userId).orElseThrow(WalletNotExistsException::new);
        // 是否被禁用
        if (Objects.equals(WalletCode.STATUS_FORBIDDEN, wallet.getStatus())) {
            throw new WalletBannedException();
        }
        return wallet;
    }

}
