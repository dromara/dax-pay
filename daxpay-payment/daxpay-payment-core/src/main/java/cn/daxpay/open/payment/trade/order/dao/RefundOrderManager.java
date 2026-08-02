package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/// # 退款订单管理器
///
@Repository
public class RefundOrderManager extends BaseManager<RefundOrderMapper, RefundOrder> {

    /// 根据退款号查询
    public Optional<RefundOrder> findByRefundNo(String refundNo) {
        return findByField(RefundOrder::getRefundNo, refundNo);
    }

    /// 根据商户退款号查询(按商户号自动租户隔离)
    public Optional<RefundOrder> findByBizRefundNo(String bizRefundNo) {
        return lambdaQuery()
                .eq(RefundOrder::getBizRefundNo, bizRefundNo)
                .oneOpt();
    }

    /// 根据商户退款号和应用号查询(统一接口主路径, 避免同商户多应用串单)
    public Optional<RefundOrder> findByBizRefundNo(String bizRefundNo, String appId) {
        return lambdaQuery()
                .eq(RefundOrder::getBizRefundNo, bizRefundNo)
                .eq(RefundOrder::getAppId, appId)
                .oneOpt();
    }

    /// 根据退款号查询(忽略租户, 定时任务引导读用)
    @IgnoreTenant
    public Optional<RefundOrder> findByRefundNoNotTenant(String refundNo) {
        return findByField(RefundOrder::getRefundNo, refundNo);
    }

    /// 按创建时间窗口扫描退款中订单(定时同步任务用)
    ///
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    /// 固定 status=progress, 命中索引 idx_refund_order_status_create_time。
    @IgnoreTenant
    public List<RefundOrder> findProgressRefunds(OffsetDateTime start, OffsetDateTime end) {
        return listLimit(500, q -> q
                .eq(RefundOrder::getStatus, "progress")
                .ge(RefundOrder::getCreateTime, start)
                .le(RefundOrder::getCreateTime, end)
                .orderByAsc(RefundOrder::getCreateTime));
    }

    /// 根据实际上送串查询(回调容错: 特殊通道仅回传变形号)
    public Optional<RefundOrder> findByRelationOrderNo(String relationOrderNo) {
        return findByField(RefundOrder::getRelationOrderNo, relationOrderNo);
    }

    /// 根据通道退款流水号查询(回调容错: 部分通道仅回传其内部退款号)
    public Optional<RefundOrder> findByOutRefundNo(String outRefundNo) {
        return findByField(RefundOrder::getOutRefundNo, outRefundNo);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<RefundOrder> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<RefundOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// 用途：替代结算路径中"先读后盲写"的 updateById，消除并发竞态。
    /// SQL 语义：`UPDATE refund_order SET status=?, ... WHERE id=? AND status IN (...)`。
    ///
    /// @param refundOrder 已设置目标状态与关联字段的实体（从数据库加载后修改）
    /// @param expectFrom  合法的前置状态编码集合
    /// @return true=更新成功；false=状态已被其他线程改变，调用方应幂等退出
    public boolean casUpdateStatus(RefundOrder refundOrder, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(RefundOrder::getId, refundOrder.getId())
                .in(RefundOrder::getStatus, expectFrom)
                .set(RefundOrder::getStatus, refundOrder.getStatus())
                .set(RefundOrder::getFinishTime, refundOrder.getFinishTime())
                .set(RefundOrder::getOutRefundNo, refundOrder.getOutRefundNo())
                .set(RefundOrder::getRelationOrderNo, refundOrder.getRelationOrderNo())
                .set(RefundOrder::getErrorMsg, refundOrder.getErrorMsg())
                .update();
    }
}
