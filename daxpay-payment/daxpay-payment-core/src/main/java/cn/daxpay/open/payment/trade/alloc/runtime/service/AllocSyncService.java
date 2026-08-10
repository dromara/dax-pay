package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.payment.trade.record.dao.PaySyncRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PaySyncRecord;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyFactory;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

/// # 分账同步编排服务
///
/// 参照 [cn.daxpay.open.payment.trade.transfer.runtime.service.TransferSyncService] 设计。
/// 处理中(processing)的分账单通过同步查询通道状态, 推进到终态。
/// 触发路径: 延迟 MQ / 定时任务 / 手动同步。
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocSyncService {

    private final AllocAssistService assistService;
    private final AllocOrderManager allocOrderManager;
    private final PaySyncRecordManager paySyncRecordManager;
    private final LockExecutor lockExecutor;

    /// 手动同步(管理端/开放API, 有 HTTP 上下文)
    public void sync(String allocNo) {
        AllocOrder allocOrder = allocOrderManager.findByAllocNo(allocNo).orElse(null);
        if (allocOrder == null) {
            log.warn("分账同步: 分账单不存在, allocNo={}", allocNo);
            return;
        }
        this.syncWithLock(allocOrder);
    }

    /// 自动同步(延迟消息/定时任务入口, 无 HTTP 上下文)
    public void autoSync(String allocNo) {
        // 跨租户查询(定时任务无 HTTP 上下文)
        AllocOrder allocOrder = allocOrderManager.findByAllocNoNotTenant(allocNo).orElse(null);
        if (allocOrder == null) {
            log.warn("分账同步: 分账单不存在, allocNo={}", allocNo);
            return;
        }
        this.syncWithLock(allocOrder);
    }

    /// 锁内同步编排(无事务, 状态变更由 Assist 各方法独立事务提交)
    private void syncWithLock(AllocOrder allocOrder) {
        lockExecutor.run(AllocAssistService.allocLockKey(allocOrder.getId()), () -> {
            // 锁内二次读: 须仍为 processing 才继续
            AllocOrder latest = allocOrderManager.findById(allocOrder.getId()).orElse(null);
            if (latest == null
                    || !Objects.equals(latest.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
                log.info("分账同步幂等: 分账单 {} 非处理中, 跳过", allocOrder.getAllocNo());
                return;
            }
            // 装配策略上下文
            AllocStrategyContext context = assistService.loadContext(latest).orElse(null);
            if (context == null) {
                log.warn("分账同步: 上下文装配失败, allocNo={}", latest.getAllocNo());
                return;
            }
            // 通道查询
            AbsAllocStrategy strategy = AllocStrategyFactory.create(latest.getChannel());
            AllocResultBo result;
            try {
                result = strategy.doSync(context);
            } catch (Exception e) {
                // 通道查单失败: 保持处理中, 落同步记录, 由定时任务兜底
                log.warn("分账同步查单失败: allocNo={}, 保持处理中", latest.getAllocNo(), e);
                this.saveSyncRecord(latest, null, false, resolveErrorMsg(e));
                return;
            }
            // 回写通道分账号(如有)
            if (result.getOutAllocNo() != null && !Objects.equals(result.getOutAllocNo(), latest.getOutAllocNo())) {
                assistService.processing(latest, result.getOutAllocNo());
            }
            // 按逐明细结果聚合状态
            if (result.getDetails() != null && !result.getDetails().isEmpty()) {
                long successCount = result.getDetails().stream()
                        .filter(d -> Objects.equals(d.getResult(), "success")).count();
                long failCount = result.getDetails().stream()
                        .filter(d -> Objects.equals(d.getResult(), "fail")).count();
                long total = result.getDetails().size();

                if (successCount == total) {
                    assistService.success(latest, result.getDetails());
                } else if (failCount == total) {
                    String errorMsg = result.getDetails().stream()
                            .map(AllocResultBo.DetailResult::getErrorMsg)
                            .filter(Objects::nonNull).findFirst().orElse(null);
                    assistService.fail(latest, result.getDetails(), errorMsg);
                } else if (successCount + failCount == total) {
                    // 全部明细已有终态结果(部分成功)
                    assistService.partial(latest, result.getDetails());
                }
                // 否则仍有 pending, 不流转, 等下次同步
            }
            this.saveSyncRecord(latest, result, result.isSyncSuccess(), null);
        }, () -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.alloc.syncProcessing"));
    }

    /// 落同步记录
    private void saveSyncRecord(AllocOrder allocOrder, AllocResultBo result, boolean adjust, String errorMsg) {
        PaySyncRecord record = new PaySyncRecord()
                .setTradeNo(allocOrder.getAllocNo())
                .setBizTradeNo(allocOrder.getBizAllocNo())
                .setOutTradeNo(allocOrder.getOutAllocNo())
                .setTradeType(TradeTypeEnum.ALLOC.getCode())
                .setChannel(allocOrder.getChannel())
                .setAdjust(adjust)
                .setErrorMsg(errorMsg);
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        record.setMchNo(allocOrder.getMchNo());
        paySyncRecordManager.save(record);
    }

    /// 异常本地化解析
    private String resolveErrorMsg(Throwable e) {
        if (e instanceof BizException biz) {
            String key = biz.resolveMessageKey();
            if (key != null) {
                return I18nUtil.get(key, Locale.CHINA, biz.getArgs());
            }
        }
        return e.getMessage();
    }
}
