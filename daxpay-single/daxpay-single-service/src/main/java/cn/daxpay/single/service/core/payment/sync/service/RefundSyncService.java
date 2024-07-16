package cn.daxpay.single.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.core.exception.PayFailureException;
import cn.daxpay.single.core.exception.TradeNotExistException;
import cn.daxpay.single.core.param.payment.refund.RefundSyncParam;
import cn.daxpay.single.core.result.sync.RefundSyncResult;
import cn.daxpay.single.service.code.RefundAdjustWayEnum;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.refund.service.RefundOrderQueryService;
import cn.daxpay.single.service.core.payment.adjust.param.RefundAdjustParam;
import cn.daxpay.single.service.core.payment.adjust.service.RefundAdjustService;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.core.record.sync.service.TradeSyncRecordService;
import cn.daxpay.single.service.func.AbsRefundSyncStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.core.util.StrUtil;
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
public class RefundSyncService {
    private final RefundOrderManager refundOrderManager;

    private final RefundOrderQueryService refundOrderQueryService;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final RefundAdjustService adjustService;

    private final LockTemplate lockTemplate;

    /**
     * 退款同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RefundSyncResult sync(RefundSyncParam param){
        // 先获取退款单
        RefundOrder refundOrder = refundOrderQueryService.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo())
                .orElseThrow(() -> new TradeNotExistException("未查询到退款订单"));
        // 如果订单已经关闭, 直接返回退款关闭
        if (Objects.equals(refundOrder.getStatus(), RefundStatusEnum.CLOSE.getCode())){
            return new RefundSyncResult().setStatus(RefundStatusEnum.CLOSE.getCode());
        }
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
        try {
            // 获取支付同步策略类
            AbsRefundSyncStrategy syncPayStrategy = PayStrategyFactory.create(refundOrder.getChannel(),AbsRefundSyncStrategy.class);
            syncPayStrategy.setRefundOrder(refundOrder);
            // 同步前处理, 主要预防请求过于迅速
            syncPayStrategy.doBeforeHandler();
            // 执行操作, 获取支付网关同步的结果
            RefundRemoteSyncResult refundRemoteSyncResult = syncPayStrategy.doSyncStatus();

            // 判断是否同步成功
            if (Objects.equals(refundRemoteSyncResult.getSyncStatus(), RefundSyncStatusEnum.FAIL)) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(refundOrder, refundRemoteSyncResult, null);
                throw new OperationFailException(refundRemoteSyncResult.getErrorMsg());
            }
            // 订单的通道交易号是否一致, 不一致进行更新
            if (Objects.nonNull(refundRemoteSyncResult.getOutRefundNo()) && !Objects.equals(refundRemoteSyncResult.getOutRefundNo(), refundOrder.getOutRefundNo())){
                refundOrder.setOutRefundNo(refundRemoteSyncResult.getOutRefundNo());
                refundOrderManager.updateById(refundOrder);
            }
            // 判断网关状态是否和支付单一致
            boolean statusSync = this.checkSyncStatus(refundRemoteSyncResult, refundOrder);
            String adjustNo = null;
            try {
                // 状态不一致，执行退款单调整逻辑
                if (!statusSync) {
                    // 如果没有支付来源, 设置支付来源为同步
                    adjustNo = this.adjustHandler(refundRemoteSyncResult, refundOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                refundRemoteSyncResult.setSyncStatus(RefundSyncStatusEnum.FAIL);
                this.saveRecord(refundOrder, refundRemoteSyncResult, null);
                throw e;
            }
            // 同步成功记录日志
            this.saveRecord(refundOrder, refundRemoteSyncResult, adjustNo);
            return new RefundSyncResult().setStatus(refundRemoteSyncResult.getSyncStatus().getCode());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 检查状态是否一致
     * @see RefundSyncStatusEnum 同步返回类型
     * @see RefundStatusEnum 退款单状态
     */
    private boolean checkSyncStatus(RefundRemoteSyncResult syncResult, RefundOrder order){
        RefundSyncStatusEnum syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 退款完成
        if (Objects.equals(syncStatus, RefundSyncStatusEnum.SUCCESS)&&
                Objects.equals(orderStatus, RefundStatusEnum.SUCCESS.getCode())) {
            return true;
        }

        // 退款失败
        if (Objects.equals(syncStatus, RefundSyncStatusEnum.FAIL)&&
                Objects.equals(orderStatus, RefundStatusEnum.FAIL.getCode())) {
            return true;
        }
        // 退款中
        if (Objects.equals(syncStatus, RefundSyncStatusEnum.PROGRESS)&&
                Objects.equals(orderStatus, RefundStatusEnum.PROGRESS.getCode())) {
            return true;
        }
        return false;
    }

    /**
     * 进行退款订单和支付订单的调整
     */
    private String adjustHandler(RefundRemoteSyncResult syncResult, RefundOrder order){
        RefundSyncStatusEnum syncStatusEnum = syncResult.getSyncStatus();
        RefundAdjustParam param = new RefundAdjustParam()
                .setOrder(order)
                .setOutTradeNo(syncResult.getOutRefundNo())
                .setSource(TradeAdjustSourceEnum.SYNC)
                .setFinishTime(syncResult.getFinishTime());

        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            case SUCCESS:
                param.setAdjustWay(RefundAdjustWayEnum.SUCCESS);
                return adjustService.adjust(param);
            case PROGRESS:
                // 不进行处理
                break;
            case FAIL: {
                param.setAdjustWay(RefundAdjustWayEnum.FAIL);
                return adjustService.adjust(param);
            }
            case NOT_FOUND:
                param.setAdjustWay(RefundAdjustWayEnum.FAIL);
                return adjustService.adjust(param);
            default: {
                throw new BizException("代码有问题");
            }
        }
        return null;
    }

    /**
     * 保存同步记录, 使用新事务进行保存
     * @param refundOrder 支付单
     * @param syncResult 同步结果
     * @param adjustNo 调整号
     */
    private void saveRecord(RefundOrder refundOrder, RefundRemoteSyncResult syncResult, String adjustNo){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setTradeNo(refundOrder.getRefundNo())
                .setBizTradeNo(refundOrder.getBizRefundNo())
                .setOutTradeNo(syncResult.getOutRefundNo())
                .setOutTradeStatus(syncResult.getSyncStatus().getCode())
                .setType(TradeTypeEnum.REFUND.getCode())
                .setChannel(refundOrder.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setAdjust(StrUtil.isNotBlank(adjustNo))
                .setAdjustNo(adjustNo)
                .setErrorCode(syncResult.getErrorCode())
                .setErrorMsg(syncResult.getErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}
