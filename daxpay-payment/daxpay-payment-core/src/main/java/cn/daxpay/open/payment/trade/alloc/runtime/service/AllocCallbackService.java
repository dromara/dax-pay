package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
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
    /// @param data 回调数据(tradeNo=allocNo, outTradeNo=outAllocNo)
    /// @param detailResults 通道解析出的逐明细结果(由通道回调 Service 装配)
    public void allocCallback(CallbackData data, List<AllocResultBo.DetailResult> detailResults) {
        if (data == null || data.getTradeNo() == null) {
            log.warn("分账回调: 分账单号为空, 跳过");
            return;
        }
        // 按 allocNo 反查(忽略租户, 回调无 HTTP 上下文)
        AllocOrder allocOrder = allocOrderManager.findByAllocNoNotTenant(data.getTradeNo()).orElse(null);
        if (allocOrder == null && data.getOutTradeNo() != null) {
            // 容错: 按通道分账号反查
            allocOrder = allocOrderManager.findByOutAllocNo(data.getOutTradeNo()).orElse(null);
        }
        if (allocOrder == null) {
            log.warn("分账回调: 分账单不存在, allocNo={}, outAllocNo={}", data.getTradeNo(), data.getOutTradeNo());
            return;
        }
        this.doAllocCallback(allocOrder, detailResults);
    }

    /// 锁内回调处理
    private void doAllocCallback(AllocOrder allocOrder, List<AllocResultBo.DetailResult> detailResults) {
        lockExecutor.run(AllocAssistService.allocLockKey(allocOrder.getId()), () -> {
            // 锁内二次读: 须仍为 processing 才继续(终态幂等忽略)
            AllocOrder latest = allocOrderManager.findById(allocOrder.getId()).orElse(null);
            if (latest == null
                    || !Objects.equals(latest.getStatus(), AllocOrderStatusEnum.PROCESSING.getCode())) {
                log.info("分账回调幂等: 分账单 {} 非处理中, 跳过", allocOrder.getAllocNo());
                return;
            }
            // 按逐明细结果聚合状态
            if (detailResults == null || detailResults.isEmpty()) {
                log.warn("分账回调: 明细结果为空, allocNo={}", latest.getAllocNo());
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
