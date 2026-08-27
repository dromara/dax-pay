package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// # 分账回调处理服务
///
/// 处理抖音等通道的异步分账结果回调。
/// 支付宝/微信无分账回调(纯查询式), 不走本服务。
///
/// 参照 [cn.daxpay.open.payment.trade.transfer.runtime.service.TransferCallbackService] 设计：
/// - 锁外层反查分账单
/// - 持锁 + 事务内终态守卫 + 状态流转
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocCallbackService {

    private final AllocAssistService assistService;
    private final AllocOrderManager allocOrderManager;
    private final LockExecutor lockExecutor;

    /// 分账回调入口
    ///
    /// @param data 回调数据(tradeNo=allocNo 主定位, outTradeNo=outAllocNo 容错; 通道至少提供其一)
    /// @param detailResults 通道解析出的逐明细结果(由通道回调 Service 装配)
    public void allocCallback(CallbackData data, List<AllocResultBo.DetailResult> detailResults) {
        // allocNo 与通道分账号须至少一项非空(通道差异容忍: 部分通道通知仅含通道侧单号)
        if (data == null || (data.getTradeNo() == null && data.getOutTradeNo() == null)) {
            log.warn("分账回调: 分账单号与通道分账号均为空, 跳过");
            return;
        }
        // 按 allocNo 反查(忽略租户, 回调无 HTTP 上下文)
        AllocOrder allocOrder = data.getTradeNo() != null
                ? allocOrderManager.findByAllocNoNotTenant(data.getTradeNo()).orElse(null)
                : null;
        if (allocOrder == null && data.getOutTradeNo() != null) {
            // 容错: 按通道分账号反查
            allocOrder = allocOrderManager.findByOutAllocNo(data.getOutTradeNo()).orElse(null);
        }
        if (allocOrder == null) {
            log.warn("分账回调: 分账单不存在, allocNo={}, outAllocNo={}", data.getTradeNo(), data.getOutTradeNo());
            // 回传处理状态, 供外层落回调记录(本服务不落记录, 只审计不重放)
            data.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg("分账单不存在");
            return;
        }
        this.doAllocCallback(allocOrder, data, detailResults);
    }

    /// 锁内回调处理
    private void doAllocCallback(AllocOrder allocOrder, CallbackData data, List<AllocResultBo.DetailResult> detailResults) {
        lockExecutor.run(AllocAssistService.allocLockKey(allocOrder.getId()), () -> {
            // 锁内二次读: 须仍为 processing 才继续(终态幂等忽略)
            AllocOrder latest = allocOrderManager.findById(allocOrder.getId()).orElse(null);
            if (latest == null
                    || !Objects.equals(latest.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
                log.info("分账回调幂等: 分账单 {} 非处理中, 跳过", allocOrder.getAllocNo());
                // 回传处理状态: 非处理中不流转, 幂等忽略(供外层落回调记录)
                data.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("分账单已非处理中状态, 幂等忽略");
                return;
            }
            // 按逐明细结果聚合状态
            if (detailResults == null || detailResults.isEmpty()) {
                log.warn("分账回调: 明细结果为空, allocNo={}", latest.getAllocNo());
                // 回传处理状态: 通知数据异常, 不流转(供外层落回调记录)
                data.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg("分账回调明细结果为空");
                return;
            }
            long successCount = detailResults.stream()
                    .filter(d -> Objects.equals(d.getResult(), "success")).count();
            long failCount = detailResults.stream()
                    .filter(d -> Objects.equals(d.getResult(), "fail")).count();
            long total = detailResults.size();

            if (successCount == total) {
                assistService.success(latest, detailResults);
            } else if (failCount == total) {
                String errorMsg = detailResults.stream()
                        .map(AllocResultBo.DetailResult::getErrorMsg)
                        .filter(Objects::nonNull).findFirst().orElse(null);
                assistService.fail(latest, detailResults, errorMsg);
            } else {
                assistService.partial(latest, detailResults);
            }
        }, () -> new cn.daxpay.open.platform.core.exception.BizInfoException(
                cn.daxpay.open.platform.core.code.CommonCode.FAIL_CODE, "pay.error.alloc.syncProcessing"));
    }
}
