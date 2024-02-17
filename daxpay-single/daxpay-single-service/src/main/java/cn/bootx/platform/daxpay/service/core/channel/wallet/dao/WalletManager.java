package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletQueryParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    /**
     * 用户钱包是否存在
     */
    public boolean existsByUser(String userId) {
        return lambdaQuery()
                .eq(Wallet::getUserId,userId)
                .exists();
    }

    /**
     * 查询用户的钱包
     */
    public Optional<Wallet> findByUser(String userId) {
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
        return this.page(mpPage, wrapper);
    }
}
