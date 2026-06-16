package org.dromara.daxpay.payment.pay.order.dao;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.exception.DangerSqlException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
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

    /// 根据交易号+应用ID查询
    public Optional<PayTrade> findByTradeNo(String tradeNo, String appId) {
        return lambdaQuery()
                .eq(PayTrade::getTradeNo, tradeNo)
                .eq(PayTrade::getAppId, appId)
                .oneOpt();
    }

    /// 根据通道订单号+应用ID查询
    public Optional<PayTrade> findByOutOrderNo(String outOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayTrade::getOutOrderNo, outOrderNo)
                .eq(PayTrade::getAppId, appId)
                .oneOpt();
    }

    /// 根据容器ID查询
    public Optional<PayTrade> findByContainerId(Long containerId, String appId) {
        return lambdaQuery()
                .eq(PayTrade::getContainerId, containerId)
                .eq(PayTrade::getAppId, appId)
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
