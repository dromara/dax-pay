package cn.bootx.platform.daxpay.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.RefundSyncParam;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairWayEnum;
import cn.bootx.platform.daxpay.service.common.context.RepairLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RefundRepairResult;
import cn.bootx.platform.daxpay.service.core.payment.repair.service.RefundRepairService;
import cn.bootx.platform.daxpay.service.core.payment.sync.factory.RefundSyncStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.core.record.sync.service.PaySyncRecordService;
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

    private final PaySyncRecordService paySyncRecordService;

    private final RefundRepairService repairService;

    private final LockTemplate lockTemplate;

    /**
     * 退款同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SyncResult sync(RefundSyncParam param){
        // 先获取退款单
        PayRefundOrder refundOrder;
        if (Objects.nonNull(param.getRefundId())){
            refundOrder = refundOrderManager.findById(param.getRefundId())
                    .orElseThrow(() -> new PayFailureException("未查询到退款订单"));
        } else {
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new PayFailureException("未查询到退款订单"));
        }
        // 如果不是异步支付, 直接返回返回
        if (!refundOrder.isAsyncPay()){
            return new SyncResult().setSuccess(false).setRepair(false).setErrorMsg("订单没有异步通道的退款，不需要同步");
        }
        // 如果订单已经关闭, 直接返回失败
        if (Objects.equals(refundOrder.getStatus(), PayRefundStatusEnum.CLOSE.getCode())){
            return new SyncResult().setSuccess(false).setRepair(false).setErrorMsg("订单已经关闭，不需要同步");
        }
        return this.syncRefundOrder(refundOrder);
    }

    /**
     * 退款订单信息同步
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SyncResult syncRefundOrder(PayRefundOrder refundOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:refund:" + refundOrder.getId());
        if (Objects.isNull(lock)) {
            throw new RepetitiveOperationException("退款同步处理中，请勿重复操作");
        }
        try {
            // 获取支付同步策略类
            AbsRefundSyncStrategy syncPayStrategy = RefundSyncStrategyFactory.create(refundOrder.getAsyncChannel());
            syncPayStrategy.initRefundParam(refundOrder);
            // 同步前处理, 主要预防请求过于迅速
            syncPayStrategy.doBeforeHandler();
            // 执行操作, 获取支付网关同步的结果
            RefundGatewaySyncResult syncResult = syncPayStrategy.doSyncStatus();

            // 判断是否同步成功
            if (Objects.equals(syncResult.getSyncStatus(), PayRefundSyncStatusEnum.FAIL)) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                return new SyncResult().setErrorMsg(syncResult.getErrorMsg());
            }
            // 支付订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(syncResult.getGatewayOrderNo(), refundOrder.getGatewayOrderNo())){
                refundOrder.setGatewayOrderNo(syncResult.getGatewayOrderNo());
                refundOrderManager.updateById(refundOrder);
            }
            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态
            boolean statusSync = this.checkSyncStatus(syncResult, refundOrder);
            RefundRepairResult repairResult = new RefundRepairResult();
            try {
                // 状态不一致，执行退款单修复逻辑
                if (!statusSync) {
                    // 如果没有支付来源, 设置支付来源为同步
                    RepairLocal repairInfo = PaymentContextLocal.get().getRepairInfo();
                    if (Objects.isNull(repairInfo.getSource())){
                        repairInfo.setSource(PayRepairSourceEnum.SYNC);
                    }
                    repairInfo.setFinishTime(syncResult.getRefundTime());
                    repairResult = this.repairHandler(syncResult, refundOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncStatus(PayRefundSyncStatusEnum.FAIL);
                this.saveRecord(refundOrder, syncResult, false, null, e.getMessage());
                return new SyncResult().setErrorMsg(e.getMessage());
            }
            // 同步成功记录日志
            this.saveRecord(refundOrder, syncResult, !statusSync, repairResult.getRepairId(), null);
            return new SyncResult()
                    .setGatewayStatus(syncResult.getSyncStatus().getCode())
                    .setSuccess(true)
                    .setRepair(!statusSync)
                    .setRepairId(repairResult.getRepairId());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 检查状态是否一致
     * @see PayRefundSyncStatusEnum 同步返回类型
     * @see PayRefundStatusEnum 退款单状态
     */
    private boolean checkSyncStatus(RefundGatewaySyncResult syncResult, PayRefundOrder order){
        PayRefundSyncStatusEnum syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 退款完成
        if (Objects.equals(syncStatus, PayRefundSyncStatusEnum.SUCCESS)&&
                Objects.equals(orderStatus, PayRefundStatusEnum.SUCCESS.getCode())) {
            return true;
        }

        // 退款失败
        if (Objects.equals(syncStatus, PayRefundSyncStatusEnum.FAIL)&&
                Objects.equals(orderStatus, PayRefundStatusEnum.FAIL.getCode())) {
            return true;
        }
        // 退款中
        if (Objects.equals(syncStatus, PayRefundSyncStatusEnum.REFUNDING)&&
                Objects.equals(orderStatus, PayRefundStatusEnum.PROGRESS.getCode())) {
            return true;
        }
        return false;
    }

    /**
     * 进行退款订单和支付订单的补偿
     */
    private RefundRepairResult repairHandler(RefundGatewaySyncResult syncResult, PayRefundOrder order){
        PayRefundSyncStatusEnum syncStatusEnum = syncResult.getSyncStatus();
        RefundRepairResult repair = new RefundRepairResult();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            // 调用出错
            case SUCCESS:
                repair = repairService.repair(order, RefundRepairWayEnum.SUCCESS);
                break;
            case REFUNDING:
                // 不进行处理 TODO 添加重试
                log.warn("退款状态同步接口调用出错");
                break;
            case FAIL: {
                repair = repairService.repair(order, RefundRepairWayEnum.FAIL);
                break;
            }
            default: {
                throw new BizException("代码有问题");
            }
        }
        return repair;
    }

    /**
     * 保存同步记录
     * @param refundOrder 支付单
     * @param syncResult 同步结果
     * @param repair 是否修复
     * @param errorMsg 错误信息
     */
    private void saveRecord(PayRefundOrder refundOrder, RefundGatewaySyncResult syncResult, boolean repair, Long repairOrderId, String errorMsg){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setOrderId(refundOrder.getId())
                .setOrderNo(refundOrder.getRefundNo())
                .setSyncType(PaymentTypeEnum.REFUND.getCode())
                .setAsyncChannel(refundOrder.getAsyncChannel())
                .setGatewayOrderNo(syncResult.getGatewayOrderNo())
                .setSyncInfo(syncResult.getSyncInfo())
                .setGatewayStatus(syncResult.getSyncStatus().getCode())
                .setRepairOrder(repair)
                .setRepairOrderId(repairOrderId)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getRequestInfo().getClientIp())
                .setReqId(PaymentContextLocal.get().getRequestInfo().getReqId());
        paySyncRecordService.saveRecord(paySyncRecord);
    }
}
