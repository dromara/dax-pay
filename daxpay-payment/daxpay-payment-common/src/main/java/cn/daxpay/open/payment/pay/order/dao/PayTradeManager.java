package cn.daxpay.open.payment.pay.order.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.DangerSqlException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
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
