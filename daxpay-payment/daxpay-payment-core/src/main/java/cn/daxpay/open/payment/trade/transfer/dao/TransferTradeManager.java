package cn.daxpay.open.payment.trade.transfer.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.param.TransferTradeQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/// # 转账资金凭证管理器
///
@Repository
public class TransferTradeManager extends BaseManager<TransferTradeMapper, TransferTrade> {

    /// 根据平台转账交易号查询
    public Optional<TransferTrade> findByTradeNo(String tradeNo) {
        return findByField(TransferTrade::getTradeNo, tradeNo);
    }

    /// 根据平台转账交易号查询(忽略租户, 定时任务引导读用)
    @IgnoreTenant
    public Optional<TransferTrade> findByTradeNoNotTenant(String tradeNo) {
        return findByField(TransferTrade::getTradeNo, tradeNo);
    }

    /// 根据通道转账单号查询(回调容错: 部分通道仅回传其内部转账号)
    public Optional<TransferTrade> findByOutTransferNo(String outTransferNo) {
        return findByField(TransferTrade::getOutTransferNo, outTransferNo);
    }

    /// 根据实际上送串查询(回调容错: 特殊通道仅回传变形号)
    public Optional<TransferTrade> findByRelationNo(String relationNo) {
        return findByField(TransferTrade::getRelationNo, relationNo);
    }

    /// 根据容器ID + 通道查询
    public Optional<TransferTrade> findByContainerId(Long containerId, String containerChannel) {
        return lambdaQuery()
                .eq(TransferTrade::getContainerId, containerId)
                .eq(TransferTrade::getContainerChannel, containerChannel)
                .oneOpt();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<TransferTrade> page(PageParam pageParam, TransferTradeQuery query) {
        Page<TransferTrade> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<TransferTrade> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 按状态 + 创建时间窗口扫描(定时同步任务用)
    ///
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 500 防积压爆量。
    /// 命中索引 idx_pay_transfer_trade_status_time。
    @IgnoreTenant
    public List<TransferTrade> findSyncTransfers(String status, OffsetDateTime start, OffsetDateTime end) {
        return listLimit(500, q -> q
                .eq(TransferTrade::getStatus, status)
                .ge(TransferTrade::getCreateTime, start)
                .le(TransferTrade::getCreateTime, end)
                .orderByAsc(TransferTrade::getCreateTime));
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// SQL 语义：`UPDATE pay_transfer_trade SET status=?, ... WHERE id=? AND status IN (...)`。
    ///
    /// @param trade      已设置目标状态与关联字段的实体（从数据库加载后修改）
    /// @param expectFrom 合法的前置状态编码集合
    /// @return true=更新成功；false=状态已被其他线程改变，调用方应幂等退出
    public boolean casUpdateStatus(TransferTrade trade, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(TransferTrade::getId, trade.getId())
                .in(TransferTrade::getStatus, expectFrom)
                .set(TransferTrade::getStatus, trade.getStatus())
                .set(TransferTrade::getFinishTime, trade.getFinishTime())
                .set(TransferTrade::getOutTransferNo, trade.getOutTransferNo())
                .set(TransferTrade::getRelationNo, trade.getRelationNo())
                .update();
    }
}
