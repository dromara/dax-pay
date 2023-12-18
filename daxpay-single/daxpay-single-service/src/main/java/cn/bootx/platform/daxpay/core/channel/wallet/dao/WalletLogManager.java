package cn.bootx.platform.daxpay.core.channel.wallet.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletLog;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletLogQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 钱包日志
 *
 * @author xxm
 * @since 2020/12/8
 */
@Repository
@RequiredArgsConstructor
public class WalletLogManager extends BaseManager<WalletLogMapper, WalletLog> {

    /**
     * 分页查询指定用户的钱包日志
     */
    public Page<WalletLog> pageByUserId(PageParam pageParam, WalletLogQueryParam param, Long userId) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);
        return this.lambdaQuery().orderByDesc(MpIdEntity::getId).eq(WalletLog::getUserId, userId).page(mpPage);
    }

    /**
     * 分页查询
     */
    public Page<WalletLog> page(PageParam pageParam, WalletLogQueryParam query) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);
        return this.lambdaQuery()
            .orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(query.getUserId()), WalletLog::getUserId, query.getUserId())
            .like(Objects.nonNull(query.getWalletId()), WalletLog::getWalletId, query.getWalletId())
            .page(mpPage);
    }

    /**
     * 分页查询 根据钱包id
     */
    public Page<WalletLog> pageByWalletId(PageParam pageParam, WalletLogQueryParam param) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);
        return this.lambdaQuery()
            .orderByDesc(MpIdEntity::getId)
            .eq(WalletLog::getWalletId, param.getWalletId())
            .page(mpPage);
    }

}
