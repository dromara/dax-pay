package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.DangerSqlException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    /// 根据实际上送串(relationOrderNo/submitNo)反查
    public Optional<PayTrade> findByRelationOrderNo(String relationOrderNo) {
        return lambdaQuery()
                .eq(PayTrade::getRelationOrderNo, relationOrderNo)
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
    ///
    /// expiredTime 在容器(pay_gateway_order)上, SQL 见 [PayTradeMapper#findGatewayTimeoutTrades]。
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量(分页插件生成方言 limit)。
    @IgnoreTenant
    public List<PayTrade> findGatewayTimeoutTrades(OffsetDateTime now) {
        Page<PayTrade> page = new Page<>(1, 500);
        page.setSearchCount(false);
        return getBaseMapper().findGatewayTimeoutTrades(page, now).getRecords();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<PayTrade> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayTrade> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 按状态 + 创建时间窗口扫描(定时同步任务用)
    ///
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    /// 支持 processing(常规同步)和 close(CLOSE→SUCCESS 纠正)两种状态扫描。
    /// 命中索引 idx_pay_trade_status_create_time。
    @IgnoreTenant
    public List<PayTrade> findSyncTrades(String status, OffsetDateTime start, OffsetDateTime end) {
        return listLimit(500, q -> q
                .eq(PayTrade::getStatus, status)
                .ge(PayTrade::getCreateTime, start)
                .le(PayTrade::getCreateTime, end)
                .orderByAsc(PayTrade::getCreateTime));
    }

    /// 按状态 + close_time 窗口扫描(CLOSE→SUCCESS 纠正专用)
    ///
    /// 与 [findSyncTrades](按 create_time)的区别: 默认 30min 到期的订单超时关单时,
    /// create_time 已落在 30min 窗口下限之外会被永久漏扫; 改按 close_time 扫描可覆盖默认到期单。
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    @IgnoreTenant
    public List<PayTrade> findSyncTradesByCloseTime(String status, OffsetDateTime start, OffsetDateTime end) {
        return listLimit(500, q -> q
                .eq(PayTrade::getStatus, status)
                .ge(PayTrade::getCloseTime, start)
                .le(PayTrade::getCloseTime, end)
                .orderByAsc(PayTrade::getCloseTime));
    }

    /// 查询普通支付已超时但仍处理中的资金交易(兜底定时任务用)
    ///
    /// 条件: tradeType=NORMAL 且 status=PROCESSING 且容器 expiredTime < now。
    /// expiredTime 在容器(pay_normal_order)上, SQL 见 [PayTradeMapper#findNormalTimeoutTrades]。
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量(分页插件生成方言 limit)。
    @IgnoreTenant
    public List<PayTrade> findNormalTimeoutTrades(OffsetDateTime now) {
        Page<PayTrade> page = new Page<>(1, 500);
        page.setSearchCount(false);
        return getBaseMapper().findNormalTimeoutTrades(page, now).getRecords();
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

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// 用途：替代回调/同步/关单路径中"先读后盲写"的 [updateById]，消除并发竞态。
    /// SQL 语义：`UPDATE pay_trade SET status=?, pay_time=?, ... WHERE id=? AND status IN (...)`。
    ///
    /// @param trade      已设置目标状态与关联字段的实体（从数据库加载后修改）
    /// @param expectFrom 合法的前置状态编码集合（由调用方根据业务路径决定子集）
    /// @return true=更新成功；false=状态已被其他线程改变，调用方应幂等退出或重试
    public boolean casUpdateStatus(PayTrade trade, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(PayTrade::getId, trade.getId())
                .in(PayTrade::getStatus, expectFrom)
                .set(PayTrade::getStatus, trade.getStatus())
                .set(PayTrade::getPayTime, trade.getPayTime())
                .set(PayTrade::getCloseTime, trade.getCloseTime())
                .set(PayTrade::getOutOrderNo, trade.getOutOrderNo())
                .set(PayTrade::getPostedAmount, trade.getPostedAmount())
                .set(PayTrade::getRefundableBalance, trade.getRefundableBalance())
                .set(PayTrade::getRelationOrderNo, trade.getRelationOrderNo())
                .set(PayTrade::getProvider, trade.getProvider())
                .update();
    }
}
