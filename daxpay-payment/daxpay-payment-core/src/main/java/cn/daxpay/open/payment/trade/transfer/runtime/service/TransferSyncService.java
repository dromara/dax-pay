package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyFactory;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.record.dao.PaySyncRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PaySyncRecord;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

/// # 转账同步服务
///
/// 管理端手动同步 / 延迟任务 / 定时任务共用一套锁内流程：
/// 持锁二次读凭证（终态幂等）→ 装载策略上下文 → 通道查询 → 按通道结果双表 CAS + 落同步记录。
/// 容器读写收敛在 [TransferAssistService]，本服务只面向凭证与策略上下文。
/// 锁键 `payment:transfer-trade:{id}` 与回调/关闭路径互斥。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferSyncService {

    private final TransferAssistService assistService;
    private final TransferTradeManager transferTradeManager;
    private final PaySyncRecordManager paySyncRecordManager;
    private final LockExecutor lockExecutor;

    /// 管理端手动同步（按通道+转账单ID）
    public void sync(String channel, Long id) {
        TransferTrade trade = assistService.findTradeByContainer(channel, id)
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.notFound"));
        this.syncWithLock(channel, trade);
    }

    /// 按平台转账单号同步（延迟消息/定时任务入口, 无 HTTP 上下文）
    public void autoSync(String transferNo) {
        TransferTrade trade = transferTradeManager.findByTradeNoNotTenant(transferNo).orElse(null);
        if (trade == null) {
            log.warn("转账同步: 凭证不存在, tradeNo={}", transferNo);
            return;
        }
        this.syncWithLock(trade.getContainerChannel(), trade);
    }

    /// 锁内同步编排（无事务, 状态变更由 Assist 各方法独立事务提交）
    private void syncWithLock(String channel, TransferTrade trade) {
        lockExecutor.run(TransferAssistService.tradeLockKey(trade.getId()), () -> {
            // 锁内二次读: 凭证须仍为 processing 才继续(容器 CAS 由 Assist 的 expectFrom 兜底幂等)
            TransferTrade latestTrade = transferTradeManager.findById(trade.getId()).orElse(null);
            if (latestTrade == null
                    || !Objects.equals(latestTrade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
                log.info("转账同步幂等: 凭证 {} 非处理中, 跳过", trade.getTradeNo());
                return;
            }
            // 装载容器并装配策略上下文(容器缺失视为同步失败, 保持处理中由定时任务兜底)
            TransferStrategyContext context = assistService.loadContext(channel, latestTrade.getContainerId()).orElse(null);
            if (context == null) {
                log.warn("转账同步: 容器不存在, tradeNo={}", latestTrade.getTradeNo());
                return;
            }
            // 通道查询
            AbsTransferStrategy strategy = TransferStrategyFactory.create(channel);
            TransferResultBo result;
            try {
                result = strategy.doSync(context);
            } catch (Exception e) {
                // 通道查单失败: 保持处理中, 落同步记录, 由定时任务兜底重试
                log.warn("转账同步查单失败: tradeNo={}, 保持处理中", latestTrade.getTradeNo(), e);
                this.saveSyncRecord(channel, latestTrade, null, false, resolveErrorMsg(e));
                return;
            }
            // 按通道结果调整状态
            if (Objects.equals(result.getStatus(), PayFundStatusEnum.SUCCESS)) {
                assistService.success(channel, latestTrade,
                        result.getOutTransferNo(), result.getFinishTime(), result.getRelationNo(), null);
            } else if (Objects.equals(result.getStatus(), PayFundStatusEnum.FAIL)) {
                assistService.fail(channel, latestTrade, result.getSyncErrorMsg());
            } else if (Objects.equals(result.getStatus(), PayFundStatusEnum.CLOSE)) {
                assistService.close(channel, latestTrade, result.getSyncErrorMsg());
            }
            // 落同步记录
            this.saveSyncRecord(channel, latestTrade, result, result.isSyncSuccess(), null);
        }, () -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.syncProcessing"));
    }

    /// 落同步记录（无 HTTP 上下文, 显式设置商户/应用）
    private void saveSyncRecord(String channel, TransferTrade trade, TransferResultBo result,
                                boolean adjust, String errorMsg) {
        PaySyncRecord record = new PaySyncRecord()
                .setTradeNo(trade.getTradeNo())
                .setBizTradeNo(trade.getBizTransferNo())
                .setOutTradeNo(trade.getOutTransferNo())
                .setOutTradeStatus(result == null ? null : result.getStatus().getCode())
                .setTradeType(TradeTypeEnum.TRANSFER.getCode())
                .setChannel(channel)
                .setSyncInfo(null)
                .setAdjust(adjust)
                .setErrorMsg(errorMsg);
        // 商户号独立赋值(父类 setter 返回 MchBaseEntity, 禁止链式)
        record.setMchNo(trade.getMchNo());
        paySyncRecordManager.save(record);
    }

    /// 解析异常为本地化错误消息
    ///
    /// [BizException] 的 getMessage() 返回 i18n messageKey(未经 I18nUtil 解析), 直接记录会导致同步记录
    /// errorMsg 存 key 字符串。本方法按固定中文(Locale.CHINA)解析, 保证落库文案不随请求语言变化。
    /// 非 BizException 的 getMessage() 已是真实文案, 直接使用。
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
