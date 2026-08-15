package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.convert.AllocOrderConvert;
import cn.daxpay.open.payment.trade.alloc.dao.AllocDetailManager;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.TradeAllocStatusEnum;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/// # 分账辅助服务
///
/// 分账单(主表) + 明细表的读写唯一事实源。
/// 所有状态变更方法都要求调用方持分布式锁([AllocAssistService#allocLockKey]),
/// 与同步/回调路径互斥, 保证并发安全。
///
/// 与转账不同: 分账无"容器+凭证"双层, 主表直接携带通道凭证快照,
/// 明细表通过 allocNo 关联, 状态变更只 CAS 主表 + 更新明细。
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocAssistService {

    /// 分账状态变更互斥锁前缀(与分账单主键组合)
    public static final String ALLOC_LOCK_PREFIX = "payment:alloc-trade:";

    private final AllocOrderManager allocOrderManager;
    private final AllocDetailManager allocDetailManager;
    private final PayTradeManager payTradeManager;
    private final TradeNoticeBridge tradeNoticeBridge;

    /// 分账状态变更互斥锁 key
    public static String allocLockKey(Long allocOrderId) {
        return ALLOC_LOCK_PREFIX + allocOrderId;
    }

    /// 根据商户分账单号查找已有分账单(幂等查重主路径)
    public Optional<AllocOrder> findByBizAllocNo(String bizAllocNo, String mchNo) {
        return allocOrderManager.findByBizAllocNo(bizAllocNo, mchNo);
    }

    /// 装配策略上下文(主单 + 明细)
    public Optional<AllocStrategyContext> loadContext(AllocOrder allocOrder) {
        if (allocOrder == null) {
            return Optional.empty();
        }
        List<AllocDetail> details = allocDetailManager.findAllByAllocNo(allocOrder.getAllocNo());
        AllocStrategyContext context = new AllocStrategyContext()
                .setAllocOrder(allocOrder)
                .setDetails(details)
                .setChannel(allocOrder.getChannel())
                .setMchNo(allocOrder.getMchNo())
                .setChannelMchNo(allocOrder.getChannelMchNo())
                .setChannelAppId(allocOrder.getChannelAppId())
                .setOutOrderNo(allocOrder.getOutOrderNo())
                .setNotifyUrl(allocOrder.getNotifyUrl());
        return Optional.of(context);
    }

    /// 建单(主表 + 明细, status=processing, 明细 result=pending)
    ///
    /// 调用方须持发起锁, 此方法独立事务。
    @Transactional(rollbackFor = Exception.class)
    public AllocOrder createOrder(AllocOrder allocOrder, List<AllocDetail> details) {
        allocOrderManager.save(allocOrder);
        for (AllocDetail detail : details) {
            detail.setAllocNo(allocOrder.getAllocNo());
            // 明细表继承 MpBaseEntity, 不参与 mch 行级隔离, 无需 setMchNo
            allocDetailManager.save(detail);
        }
        // 标记原支付交易分账中
        markTradeAllocStatus(allocOrder.getTradeNo(), TradeAllocStatusEnum.PROCESSING.getCode());
        return allocOrder;
    }

    /// 分账成功(全部明细成功): CAS 主表 processing → success, 更新明细, 注册通知
    ///
    /// @return true=本次流转成功; false=CAS 竞争失败/终态幂等
    @Transactional(rollbackFor = Exception.class)
    public boolean success(AllocOrder allocOrder, List<AllocResultBo.DetailResult> detailResults) {
        if (!Objects.equals(allocOrder.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
            log.warn("分账成功忽略: allocNo={} 状态为 {} 非 processing", allocOrder.getAllocNo(), allocOrder.getStatus());
            return false;
        }
        // 更新明细
        applyDetailResults(allocOrder.getAllocNo(), detailResults);
        // CAS 主表
        allocOrder.setStatus(AllocOrderStatusEnum.SUCCESS.getCode());
        allocOrder.setFinishTime(OffsetDateTime.now());
        boolean updated = allocOrderManager.casUpdateStatus(allocOrder, Set.of(AllocOrderStatusEnum.PROCESSING.getCode()));
        if (!updated) {
            log.warn("分账成功CAS竞争失败: allocNo={}", allocOrder.getAllocNo());
            return false;
        }
        // 标记原支付交易已分账
        markTradeAllocStatus(allocOrder.getTradeNo(), TradeAllocStatusEnum.DONE.getCode());
        // 注册商户通知
        tradeNoticeBridge.dispatchAlloc(allocOrder, NoticeEventEnum.ALLOC_SUCCESS);
        return true;
    }

    /// 分账部分成功: CAS 主表 processing → partial, 更新明细, 注册通知
    ///
    /// 单次分账终态, 不可再追加。
    @Transactional(rollbackFor = Exception.class)
    public boolean partial(AllocOrder allocOrder, List<AllocResultBo.DetailResult> detailResults) {
        if (!Objects.equals(allocOrder.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
            log.warn("分账部分成功忽略: allocNo={} 状态为 {} 非 processing", allocOrder.getAllocNo(), allocOrder.getStatus());
            return false;
        }
        applyDetailResults(allocOrder.getAllocNo(), detailResults);
        allocOrder.setStatus(AllocOrderStatusEnum.PARTIAL.getCode());
        allocOrder.setFinishTime(OffsetDateTime.now());
        boolean updated = allocOrderManager.casUpdateStatus(allocOrder, Set.of(AllocOrderStatusEnum.PROCESSING.getCode()));
        if (!updated) {
            log.warn("分账部分成功CAS竞争失败: allocNo={}", allocOrder.getAllocNo());
            return false;
        }
        // 部分成功也标记原支付交易已分账(单次分账语义)
        markTradeAllocStatus(allocOrder.getTradeNo(), TradeAllocStatusEnum.DONE.getCode());
        tradeNoticeBridge.dispatchAlloc(allocOrder, NoticeEventEnum.ALLOC_SUCCESS);
        return true;
    }

    /// 分账失败(全部明细失败): CAS 主表 processing → fail, 更新明细, 注册通知
    @Transactional(rollbackFor = Exception.class)
    public boolean fail(AllocOrder allocOrder, List<AllocResultBo.DetailResult> detailResults, String errorMsg) {
        if (!Objects.equals(allocOrder.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
            log.warn("分账失败忽略: allocNo={} 状态为 {} 非 processing", allocOrder.getAllocNo(), allocOrder.getStatus());
            return false;
        }
        if (detailResults != null) {
            applyDetailResults(allocOrder.getAllocNo(), detailResults);
        }
        allocOrder.setStatus(AllocOrderStatusEnum.FAIL.getCode());
        allocOrder.setFinishTime(OffsetDateTime.now());
        allocOrder.setErrorMsg(errorMsg);
        boolean updated = allocOrderManager.casUpdateStatus(allocOrder, Set.of(AllocOrderStatusEnum.PROCESSING.getCode()));
        if (!updated) {
            log.warn("分账失败CAS竞争失败: allocNo={}", allocOrder.getAllocNo());
            return false;
        }
        // 失败回退原支付交易分账状态为 none(允许重新发起分账)
        markTradeAllocStatus(allocOrder.getTradeNo(), TradeAllocStatusEnum.NONE.getCode());
        tradeNoticeBridge.dispatchAlloc(allocOrder, NoticeEventEnum.ALLOC_FAIL);
        return true;
    }

    /// 处理中回写: 更新通道分账号(不改状态)
    ///
    /// 用于发起返回处理中时回写通道分账号(outAllocNo), 供后续同步/回调反查。
    @Transactional(rollbackFor = Exception.class)
    public void processing(AllocOrder allocOrder, String outAllocNo) {
        allocOrder.setOutAllocNo(outAllocNo);
        allocOrderManager.updateById(allocOrder);
    }

    /// 按通道结果更新明细(逐条 CAS, 仅 pending 来源)
    private void applyDetailResults(String allocNo, List<AllocResultBo.DetailResult> detailResults) {
        if (detailResults == null || detailResults.isEmpty()) {
            return;
        }
        List<AllocDetail> details = allocDetailManager.findAllByAllocNo(allocNo);
        for (AllocResultBo.DetailResult dr : detailResults) {
            if (dr.getReceiverAccount() == null) {
                continue;
            }
            // 按接收方账号匹配明细
            details.stream()
                    .filter(d -> Objects.equals(d.getReceiverAccount(), dr.getReceiverAccount()))
                    .findFirst()
                    .ifPresent(detail -> {
                        detail.setResult(dr.getResult());
                        detail.setOutDetailId(dr.getOutDetailId());
                        detail.setErrorCode(dr.getErrorCode());
                        detail.setErrorMsg(dr.getErrorMsg());
                        detail.setFinishTime(dr.getFinishTime());
                        // CAS 明细: 仅 pending 来源可更新
                        allocDetailManager.casUpdateResult(detail, Set.of(AllocDetailResultEnum.PENDING.getCode()));
                    });
        }
    }

    /// 标记原支付交易的分账状态
    private void markTradeAllocStatus(String tradeNo, String allocStatus) {
        Optional<PayTrade> tradeOpt = payTradeManager.findByTradeNo(tradeNo);
        if (tradeOpt.isEmpty()) {
            log.warn("原支付交易不存在, 跳过分账状态标记: tradeNo={}", tradeNo);
            return;
        }
        PayTrade trade = tradeOpt.get();
        trade.setAllocStatus(allocStatus);
        payTradeManager.updateById(trade);
    }
}
