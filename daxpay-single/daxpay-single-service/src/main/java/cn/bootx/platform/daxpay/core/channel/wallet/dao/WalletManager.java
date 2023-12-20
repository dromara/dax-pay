package cn.bootx.platform.daxpay.core.channel.wallet.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletQueryParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 钱包管理
 *
 * @author xxm
 * @since 2020/12/8
 */
@Repository
@RequiredArgsConstructor
public class WalletManager extends BaseManager<WalletMapper, Wallet> {

    private final WalletMapper walletMapper;

    /**
     * 预占额度
     * @param walletId 钱包ID
     * @param amount 金额
     * @return 更新数量
     */
    public int freezeBalance(Long walletId, BigDecimal amount){
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.freezeBalance(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 解冻金额
     * @param walletId 钱包ID
     * @param amount 金额
     * @return 更新数量
     */
    public int unfreezeBalance(Long walletId, BigDecimal amount){
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.unfreezeBalance(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 扣减余额
     * @param walletId 钱包ID
     * @param amount 扣减金额
     * @return 操作条数
     */
    public int reduceBalance(Long walletId, BigDecimal amount) {
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.reduceBalance(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 扣减余额同时解除预冻结的额度
     * @param walletId 钱包ID
     * @param amount 扣减金额
     * @return 操作条数
     */
    public int reduceAndUnfreezeBalance(Long walletId, BigDecimal amount) {
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.reduceAndUnfreezeBalance(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 增加余额
     * @param walletId 钱包
     * @param amount 金额
     * @return 更新数量
     */
    public int increaseBalance(Long walletId, BigDecimal amount) {
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.increaseBalance(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 更改余额-允许扣成负数
     * @param walletId 钱包ID
     * @param amount 更改的额度, 正数增加,负数减少
     * @return 剩余条数
     */
    public int reduceBalanceUnlimited(Long walletId, BigDecimal amount) {
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.reduceBalanceUnlimited(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 用户钱包是否存在
     */
    public boolean existsByUser(Long userId) {
        return lambdaQuery()
                .eq(Wallet::getUserId,userId)
                .exists();
    }

    /**
     * 查询用户的钱包
     */
    public Optional<Wallet> findByUser(Long userId) {
        return lambdaQuery()
                .eq(Wallet::getUserId,userId)
                .oneOpt();
    }

    /**
     * 分页查询
     */
    public Page<Wallet> page(PageParam pageParam, WalletQueryParam param) {
        QueryWrapper<Wallet> wrapper = QueryGenerator.generator(param);
        Page<Wallet> mpPage = MpUtil.getMpPage(pageParam, Wallet.class);
        wrapper.select(this.getEntityClass(), MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(Wallet::getId));
        return this.page(mpPage, wrapper);
    }
}
