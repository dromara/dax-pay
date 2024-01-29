package cn.bootx.platform.daxpay.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.RefundSyncParam;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.factory.RefundSyncStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.bootx.platform.daxpay.service.func.AbsRefundSyncStrategy;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 支付退款同步服务类
 * @author xxm
 * @since 2024/1/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundSyncService {
    private final PayRefundOrderManager refundOrderManager;

    private final LockTemplate lockTemplate;

    /**
     * 退款同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sync(RefundSyncParam param){
        // 先获取退款单
        PayRefundOrder requestOrder;
        if (Objects.nonNull(param.getRefundId())){
            requestOrder = refundOrderManager.findById(param.getRefundId())
                    .orElseThrow(() -> new PayFailureException("未查询到退款订单"));
        } else {
            requestOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new PayFailureException("未查询到退款订单"));
        }
        // 如果不是异步支付, 直接返回返回
        if (!requestOrder.isAsyncPay()){
            // TODO 需要限制同步的请求不进行同步
            return;
        }
        this.syncPayOrder(requestOrder);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncPayOrder(PayRefundOrder refundOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:refund:" + refundOrder.getId());
        if (Objects.isNull(lock)) {
            throw new RepetitiveOperationException("退款同步处理中，请勿重复操作");
        }

        try {
            // 获取支付同步策略类
            AbsRefundSyncStrategy syncPayStrategy = RefundSyncStrategyFactory.create(refundOrder.getAsyncChannel());
            syncPayStrategy.initRefundParam(refundOrder);
            // 执行操作, 获取支付网关同步的结果
            RefundGatewaySyncResult syncResult = syncPayStrategy.doSyncStatus();

            // 判断是否同步成功
            if (Objects.equals(syncResult.getSyncStatus(), PayRefundSyncStatusEnum.FAIL)) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
//                return new PaySyncResult().setErrorMsg(syncResult.getErrorMsg());
                log.error("同步失败");
                return;
            }

            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态
//            boolean statusSync = this.checkAndAdjustSyncStatus(syncResult, payOrder);
//            PayRepairResult repairResult = new PayRepairResult();
//            try {
//                // 状态不一致，执行支付单修复逻辑
//                if (!statusSync) {
//                    repairResult = this.resultHandler(syncResult, payOrder);
//                }
//            } catch (PayFailureException e) {
//                // 同步失败, 返回失败响应, 同时记录失败的日志
//                syncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
//                this.saveRecord(payOrder, syncResult, false, null, e.getMessage());
//                return new PaySyncResult().setErrorMsg(e.getMessage());
//            }
//
//            // 同步成功记录日志
//            this.saveRecord(payOrder, syncResult, !statusSync, repairResult.getRepairId(), null);
//            return new PaySyncResult()
//                    .setGatewayStatus(syncResult.getSyncStatus()
//                            .getCode())
//                    .setSuccess(true)
//                    .setRepair(!statusSync)
//                    .setRepairId(repairResult.getRepairId());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
