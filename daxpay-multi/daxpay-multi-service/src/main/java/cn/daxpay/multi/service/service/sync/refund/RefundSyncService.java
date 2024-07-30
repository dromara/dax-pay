package cn.daxpay.multi.service.service.sync.refund;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.daxpay.multi.core.enums.PayRefundStatusEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.core.exception.PayFailureException;
import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.core.param.trade.refund.RefundSyncParam;
import cn.daxpay.multi.core.result.trade.refund.RefundSyncResult;
import cn.daxpay.multi.service.bo.sync.RefundSyncResultBo;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.record.sync.TradeSyncRecord;
import cn.daxpay.multi.service.enums.RefundSyncResultEnum;
import cn.daxpay.multi.service.service.notice.ClientNoticeService;
import cn.daxpay.multi.service.service.order.pay.PayOrderService;
import cn.daxpay.multi.service.service.order.refund.RefundOrderQueryService;
import cn.daxpay.multi.service.service.record.flow.TradeFlowRecordService;
import cn.daxpay.multi.service.service.record.sync.TradeSyncRecordService;
import cn.daxpay.multi.service.strategy.AbsSyncRefundOrderStrategy;
import cn.daxpay.multi.service.util.PaymentStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

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

    private final LockTemplate lockTemplate;

    private final PayOrderService payOrderService;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final ClientNoticeService clientNoticeService;

    /**
     * 退款同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RefundSyncResult sync(RefundSyncParam param){
        // 先获取退款单
        RefundOrder refundOrder = refundOrderQueryService.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo())
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
            RefundSyncResultBo syncResultBo = syncPayStrategy.doSync();

            // 判断是否同步成功
            if (Objects.equals(syncResultBo.getSyncStatus(), RefundSyncResultEnum.SYNC_FAIL)) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(refundOrder, syncResultBo, false);
                throw new OperationFailException(syncResultBo.getSyncErrorMsg());
            }
            // 订单的通道交易号是否一致, 不一致进行更新
            if (Objects.nonNull(syncResultBo.getOutRefundNo()) && !Objects.equals(syncResultBo.getOutRefundNo(), refundOrder.getOutRefundNo())){
                refundOrder.setOutRefundNo(syncResultBo.getOutRefundNo());
                refundOrderManager.updateById(refundOrder);
            }
            // 判断网关状态是否和支付单一致
            boolean statusSync = this.checkStatus(syncResultBo, refundOrder);
            try {
                // 状态不一致，执行退款单调整逻辑
                if (!statusSync) {
                    // 如果没有支付来源, 设置支付来源为同步
                    this.adjustHandler(syncResultBo, refundOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResultBo.setSyncStatus(RefundSyncResultEnum.SYNC_FAIL);
                this.saveRecord(refundOrder, syncResultBo, false);
                throw e;
            }
            // 同步成功记录日志
            this.saveRecord(refundOrder, syncResultBo, !statusSync);
            return new RefundSyncResult()
                    .setOrderStatus(refundOrder.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 检查状态是否一致
     * @see RefundSyncResultEnum 同步返回类型
     * @see RefundStatusEnum 退款单状态
     */
    private boolean checkStatus(RefundSyncResultBo syncResult, RefundOrder order){
        var syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();

        // 如果订单为退款中, 对状态进行比较
        if (Objects.equals(orderStatus, RefundStatusEnum.SUCCESS.getCode())){
            // 退款完成
            if (Objects.equals(syncStatus, RefundSyncResultEnum.SUCCESS)) {
                return true;
            }
            // 退款失败
            if (Objects.equals(syncStatus, RefundSyncResultEnum.FAIL)) {
                return true;
            }
            // 退款中
            if (Objects.equals(syncStatus, RefundSyncResultEnum.PROGRESS)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行退款订单和支付订单的调整
     */
    private void adjustHandler(RefundSyncResultBo syncResult, RefundOrder order){
        RefundSyncResultEnum syncStatusEnum = syncResult.getSyncStatus();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            case SUCCESS ->
                this.success(order, syncResult);
            case PROGRESS -> {}
            case SYNC_FAIL -> {
                log.error("退款同步失败, 退款单号:{}, 错误信息:{}", order.getRefundNo(), syncResult.getSyncErrorMsg());
            }
            case FAIL, CLOSE-> this.close(order);
            default -> log.error("退款同步结果未知, 退款单号:{}, 错误信息:{}", order.getRefundNo(), syncResult.getSyncErrorMsg());
        }
    }


    /**
     * 退款成功, 更新退款单和支付单
     */
    private void success(RefundOrder refundOrder,RefundSyncResultBo syncResult) {
        PayOrder payOrder = payOrderService.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));

        // 订单相关状态
        PayRefundStatusEnum afterPayRefundStatus;

        // 判断订单全部退款还是部分退款
        if (BigDecimalUtil.isEqual(payOrder.getRefundableBalance(), BigDecimal.ZERO)) {
            afterPayRefundStatus = PayRefundStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setFinishTime(syncResult.getFinishTime());
        payOrder.setRefundStatus(afterPayRefundStatus.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);

        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);
        // 发送通知
        clientNoticeService.registerRefundNotice(refundOrder);
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private void close(RefundOrder refundOrder) {
        PayOrder payOrder = payOrderService.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));
        // 退款失败返还后的余额
        var payOrderAmount =  refundOrder
                .getAmount()
                .add(payOrder.getRefundableBalance());
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (BigDecimalUtil.isEqual(payOrderAmount, payOrder.getAmount())) {
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        // 发送通知
        clientNoticeService.registerRefundNotice(refundOrder);
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
                .setOutTradeStatus(syncResult.getSyncStatus().getCode())
                .setType(TradeTypeEnum.REFUND.getCode())
                .setChannel(refundOrder.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setAdjust(adjust)
                .setErrorCode(syncResult.getSyncErrorCode())
                .setErrorMsg(syncResult.getSyncErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}
