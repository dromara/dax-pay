package cn.daxpay.open.payment.core.trade.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.DangerSqlException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.param.PayTradeQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
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

    /// 根据容器ID + 交易形态查询
    public Optional<PayTrade> findByContainerId(Long containerId, String tradeType) {
        return lambdaQuery()
                .eq(PayTrade::getContainerId, containerId)
                .eq(PayTrade::getTradeType, tradeType)
                .oneOpt();
    }

    /// 查询网关支付已超时但仍处理中的资金交易(兜底)
    @IgnoreTenant
    public List<PayTrade> findGatewayTimeoutTrades(OffsetDateTime now) {
        return lambdaQuery()
                .eq(PayTrade::getTradeType, PayTradeTypeEnum.GATEWAY.getCode())
                .eq(PayTrade::getStatus, PayFundStatusEnum.PROCESSING.getCode())
                .lt(PayTrade::getExpiredTime, now)
                .orderByAsc(PayTrade::getExpiredTime)
                .last("limit 500")
                .list();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<PayTrade> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayTrade> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 查询普通支付已超时但仍处理中的资金交易(兜底定时任务用)
    ///
    /// 条件: tradeType=NORMAL 且 status=PROCESSING 且 expiredTime < now
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    @IgnoreTenant
    public List<PayTrade> findNormalTimeoutTrades(OffsetDateTime now) {
        return lambdaQuery()
                .eq(PayTrade::getTradeType, PayTradeTypeEnum.NORMAL.getCode())
                .eq(PayTrade::getStatus, PayFundStatusEnum.PROCESSING.getCode())
                .lt(PayTrade::getExpiredTime, now)
                .orderByAsc(PayTrade::getExpiredTime)
                .last("limit 500")
                .list();
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
