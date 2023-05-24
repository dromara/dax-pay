package cn.bootx.platform.daxpay.core.channel.wallet.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 钱包管理
 *
 * @author xxm
 * @date 2020/12/8
 */
@Repository
@RequiredArgsConstructor
public class WalletManager extends BaseManager<WalletMapper, Wallet> {

    private final WalletMapper walletMapper;

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
     * 扣减余额-允许扣成负数
     * @param walletId 钱包ID
     * @param amount 扣减金额
     * @return 剩余条数
     */
    public int reduceBalanceUnlimited(Long walletId, BigDecimal amount) {
        Long userId = SecurityUtil.getUserIdOrDefaultId();
        return walletMapper.reduceBalanceUnlimited(walletId, amount, userId, LocalDateTime.now());
    }

    /**
     * 更新钱包状态
     */
    public void setUpStatus(Long walletId, int status) {
        lambdaUpdate().eq(Wallet::getId, walletId).set(Wallet::getStatus, status).update();
    }

    /**
     * 用户钱包是否存在
     */
    public boolean existsByUser(Long userId) {
        return existedByField(Wallet::getUserId, userId);
    }

    /**
     * 查询用户的钱包
     */
    public Optional<Wallet> findByUser(Long userId) {
        return findByField(Wallet::getUserId, userId);
    }

    /**
     * 分页查询
     */
    public Page<Wallet> page(PageParam pageParam, WalletPayParam param) {

        Page<Wallet> mpPage = MpUtil.getMpPage(pageParam, Wallet.class);
        return this.lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
    }

    /**
     * 待开通钱包的用户列表
     */
    public Page<UserInfo> pageByNotWallet(PageParam pageParam, UserInfoParam userInfoParam) {
        Page<UserInfo> mpPage = MpUtil.getMpPage(pageParam, UserInfo.class);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.isNull("w.id")
            .orderByDesc("w.id")
            .like(StrUtil.isNotBlank(userInfoParam.getUsername()), "w.username", userInfoParam.getUsername())
            .like(StrUtil.isNotBlank(userInfoParam.getName()), "w.name", userInfoParam.getName());
        return walletMapper.pageByNotWallet(mpPage, wrapper);
    }

    /**
     * 查询已经存在钱包的用户id
     */
    public List<Long> findExistUserIds(List<Long> userIds) {
        return this.lambdaQuery()
            .select(Wallet::getUserId)
            .in(Wallet::getUserId, userIds)
            .list()
            .stream()
            .map(Wallet::getUserId)
            .collect(Collectors.toList());

    }

}
