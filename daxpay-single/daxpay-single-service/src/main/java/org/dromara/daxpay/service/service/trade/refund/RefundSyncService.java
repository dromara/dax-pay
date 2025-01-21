package org.dromara.daxpay.service.service.trade.refund;

import cn.bootx.platform.core.exception.RepetitiveOperationException;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.param.trade.refund.RefundSyncParam;
import org.dromara.daxpay.core.result.trade.refund.RefundSyncResult;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.service.order.refund.RefundOrderQueryService;
import org.dromara.daxpay.service.service.record.sync.TradeSyncRecordService;
import org.dromara.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.dromara.daxpay.core.enums.TradeStatusEnum.FAIL;

/**
 * 支付退款同步服务类
 * @author xxm
 * @since 2024/1/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundSyncService {
    private final RefundOrderManager refundOrderManager;

    private final RefundOrderQueryService refundOrderQueryService;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final RefundAssistService refundAssistService;

    private final LockTemplate lockTemplate;

    /**
     * 退款同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RefundSyncResult sync(RefundSyncParam param){
        // 先获取退款单
        RefundOrder refundOrder = refundOrderQueryService.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo(),param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("未查询到退款订单"));
        return this.syncRefundOrder(refundOrder);
    }

    /**
     * 退款订单信息同步
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RefundSyncResult syncRefundOrder(RefundOrder refundOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:refund:" + refundOrder.getId(),10000,200);
        if (Objects.isNull(lock)) {
            throw new RepetitiveOperationException("退款同步处理中，请勿重复操作");
        }
        // 获取支付同步策略类
        AbsSyncRefundOrderStrategy syncPayStrategy = PaymentStrategyFactory.create(refundOrder.getChannel(),AbsSyncRefundOrderStrategy.class);
        syncPayStrategy.setRefundOrder(refundOrder);
        // 同步前处理, 主要预防请求过于迅速
        syncPayStrategy.doBeforeHandler();
        try {

            // 执行操作, 获取支付网关同步的结果
            RefundSyncResultBo syncResult = syncPayStrategy.doSync();

            // 订单的通道交易号是否一致, 不一致进行更新
            if (Objects.nonNull(syncResult.getOutRefundNo()) && !Objects.equals(syncResult.getOutRefundNo(), refundOrder.getOutRefundNo())){
                refundOrder.setOutRefundNo(syncResult.getOutRefundNo());
                refundOrderManager.updateById(refundOrder);
            }
            // 判断网关状态是否和支付单一致
            boolean statusSync = this.checkStatus(syncResult, refundOrder);
            try {
                // 状态不一致，执行退款单调整逻辑
                if (!statusSync) {
                    // 如果没有支付来源, 设置支付来源为同步
                    this.adjustHandler(syncResult, refundOrder);
                }
            } catch (Exception e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncSuccess(false).setSyncErrorMsg(e.getMessage());
            }
            // 判断是否同步成功
            if (!syncResult.isSyncSuccess()) {
                // 同步成功记录日志
                this.saveRecord(refundOrder, syncResult, !statusSync);
            } else {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(refundOrder, syncResult, true);
            }
            return new RefundSyncResult()
                    .setOrderStatus(refundOrder.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 检查状态是否一致
     * @see RefundStatusEnum 退款单状态
     */
    private boolean checkStatus(RefundSyncResultBo syncResult, RefundOrder order){
        var syncStatus = syncResult.getRefundStatus();
        String orderStatus = order.getStatus();
        // 如果本地订单为失败时, 直接返回需要进行调整
        if (orderStatus.equals(FAIL.getCode())){
            return false;
        }

        // 如果订单为退款中, 对状态进行比较
        if (Objects.equals(orderStatus, RefundStatusEnum.SUCCESS.getCode())){
            // 退款完成
            if (Objects.equals(syncStatus, RefundStatusEnum.SUCCESS)) {
                return true;
            }
            // 退款失败
            if (Objects.equals(syncStatus, RefundStatusEnum.FAIL)) {
                return true;
            }
            // 退款中
            if (Objects.equals(syncStatus, RefundStatusEnum.PROGRESS)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行退款订单和支付订单的调整
     */
    private void adjustHandler(RefundSyncResultBo syncResult, RefundOrder order){
        var refundStatus = syncResult.getRefundStatus();
        // 对支付网关同步的结果进行处理
        switch (refundStatus) {
            case SUCCESS -> refundAssistService.success(order, syncResult.getFinishTime());
            case PROGRESS -> {}
            case CLOSE-> refundAssistService.close(order);
            case FAIL -> refundAssistService.updateOrderByError(order, syncResult.getSyncErrorMsg());
            default -> log.error("退款同步结果未知, 退款单号:{}, 错误信息:{}", order.getRefundNo(), syncResult.getSyncErrorMsg());
        }
    }

    /**
     * 保存同步记录, 使用新事务进行保存
     * @param refundOrder 支付单
     * @param syncResult 同步结果
     */
    private void saveRecord(RefundOrder refundOrder, RefundSyncResultBo syncResult, boolean adjust){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setTradeNo(refundOrder.getRefundNo())
                .setBizTradeNo(refundOrder.getBizRefundNo())
                .setOutTradeNo(syncResult.getOutRefundNo())
                .setTradeType(TradeTypeEnum.REFUND.getCode())
                .setChannel(refundOrder.getChannel())
                .setSyncInfo(syncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(syncResult.getSyncErrorCode())
                .setErrorMsg(syncResult.getSyncErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        if (syncResult.isSyncSuccess()){
            tradeSyncRecord.setOutTradeStatus(syncResult.getRefundStatus().getCode());
        }
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}
