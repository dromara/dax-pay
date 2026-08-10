package cn.daxpay.open.payment.trade.alloc.dao;

import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.param.AllocOrderQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/// # 分账订单管理器
///
@Repository
public class AllocOrderManager extends BaseManager<AllocOrderMapper, AllocOrder> {

    /// 根据平台分账单号查询
    public Optional<AllocOrder> findByAllocNo(String allocNo) {
        return findByField(AllocOrder::getAllocNo, allocNo);
    }

    /// 根据商户分账单号查询(按商户号自动租户隔离, 幂等主路径)
    public Optional<AllocOrder> findByBizAllocNo(String bizAllocNo, String mchNo) {
        return lambdaQuery()
                .eq(AllocOrder::getBizAllocNo, bizAllocNo)
                .eq(AllocOrder::getMchNo, mchNo)
                .oneOpt();
    }

    /// 根据平台分账单号查询(忽略租户, 定时任务引导读用)
    @IgnoreTenant
    public Optional<AllocOrder> findByAllocNoNotTenant(String allocNo) {
        return findByField(AllocOrder::getAllocNo, allocNo);
    }

    /// 根据通道分账单号查询(回调容错: 部分通道仅回传其内部分账号)
    public Optional<AllocOrder> findByOutAllocNo(String outAllocNo) {
        return findByField(AllocOrder::getOutAllocNo, outAllocNo);
    }

    /// 根据原支付交易号查询分账单(校验是否已分账)
    public Optional<AllocOrder> findByTradeNo(String tradeNo) {
        return findByField(AllocOrder::getTradeNo, tradeNo);
    }

    /// 按状态 + 创建时间窗口扫描(定时同步任务用)
    ///
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    /// 命中索引 idx_pay_alloc_order_status_time。
    @IgnoreTenant
    public List<AllocOrder> findSyncAllocs(String status, OffsetDateTime start, OffsetDateTime end) {
        return listLimit(500, q -> q
                .eq(AllocOrder::getStatus, status)
                .ge(AllocOrder::getCreateTime, start)
                .le(AllocOrder::getCreateTime, end)
                .orderByAsc(AllocOrder::getCreateTime));
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<AllocOrder> page(PageParam pageParam, AllocOrderQuery query) {
        Page<AllocOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<AllocOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// SQL 语义：`UPDATE pay_alloc_order SET status=?, ... WHERE id=? AND status IN (...)`。
    ///
    /// @param allocOrder 已设置目标状态与关联字段的实体（从数据库加载后修改）
    /// @param expectFrom 合法的前置状态编码集合
    /// @return true=更新成功；false=状态已被其他线程改变，调用方应幂等退出
    public boolean casUpdateStatus(AllocOrder allocOrder, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(AllocOrder::getId, allocOrder.getId())
                .in(AllocOrder::getStatus, expectFrom)
                .set(AllocOrder::getStatus, allocOrder.getStatus())
                .set(AllocOrder::getFinishTime, allocOrder.getFinishTime())
                .set(AllocOrder::getOutAllocNo, allocOrder.getOutAllocNo())
                .set(AllocOrder::getErrorCode, allocOrder.getErrorCode())
                .set(AllocOrder::getErrorMsg, allocOrder.getErrorMsg())
                .update();
    }
}
