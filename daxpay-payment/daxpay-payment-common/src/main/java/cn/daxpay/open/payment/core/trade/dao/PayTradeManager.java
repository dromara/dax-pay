package cn.daxpay.open.payment.core.trade.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.DangerSqlException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.param.PayTradeQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 资金交易凭证管理器
///
@Repository
public class PayTradeManager extends BaseManager<PayTradeMapper, PayTrade> {

    /// 根据交易号查询
    public Optional<PayTrade> findByTradeNo(String tradeNo) {
        return findByField(PayTrade::getTradeNo, tradeNo);
    }

    /// 根据交易号查询（忽略租户）
    @IgnoreTenant
    public Optional<PayTrade> findByTradeNoNotTenant(String tradeNo) {
        return findByField(PayTrade::getTradeNo, tradeNo);
    }

    /// 根据通道订单号查询（按商户号自动租户隔离）
    public Optional<PayTrade> findByOutOrderNo(String outOrderNo) {
        return lambdaQuery()
                .eq(PayTrade::getOutOrderNo, outOrderNo)
                .oneOpt();
    }

    /// 根据容器ID查询（按商户号自动租户隔离）
    public Optional<PayTrade> findByContainerId(Long containerId) {
        return lambdaQuery()
                .eq(PayTrade::getContainerId, containerId)
                .oneOpt();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<PayTrade> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayTrade> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 根据id进行更新，失败时抛出异常
    @Override
    public int updateById(PayTrade entity) {
        int i = super.updateById(entity);
        if (i < 1) {
            throw new DangerSqlException(CommonCode.DANGER_SQL, "pay.error.pay.updateTradeFailed", entity.getTradeNo());
        }
        return i;
    }
}
