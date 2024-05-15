package cn.daxpay.single.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.pay.PaySyncParam;
import cn.daxpay.single.result.pay.SyncResult;
import cn.daxpay.single.service.code.PayRepairSourceEnum;
import cn.daxpay.single.service.code.PayRepairWayEnum;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.common.context.RepairLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.repair.result.PayRepairResult;
import cn.daxpay.single.service.core.payment.repair.service.PayRepairService;
import cn.daxpay.single.service.core.payment.sync.factory.PaySyncStrategyFactory;
import cn.daxpay.single.service.core.payment.sync.result.PaySyncResult;
import cn.daxpay.single.service.core.record.sync.entity.PaySyncRecord;
import cn.daxpay.single.service.core.record.sync.service.PaySyncRecordService;
import cn.daxpay.single.service.func.AbsPaySyncStrategy;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.daxpay.single.code.PaySyncStatusEnum.*;

/**
 * 支付同步服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {
    private final PayOrderQueryService payOrderQueryService;

    private final PayOrderService payOrderService;

    private final PaySyncRecordService paySyncRecordService;

    private final PayRepairService repairService;

    private final LockTemplate lockTemplate;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SyncResult sync(PaySyncParam param) {
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new PayFailureException("支付订单不存在"));
        // 钱包支付钱包不需要
        if (PayChannelEnum.WALLET.getCode().equals(payOrder.getChannel())){
            throw new PayFailureException("订单没有异步支付方式，不需要同步");
        }
        // 执行订单同步逻辑
        return this.syncPayOrder(payOrder);
    }
    /**
     * 同步支付状态, 开启一个新的事务, 不受外部抛出异常的影响
     * 1. 如果状态一致, 不进行处理
     * 2. 如果状态不一致, 调用修复逻辑进行修复, 更新状态和完成时间
     * 3. 会更新关联网关订单号
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SyncResult syncPayOrder(PayOrder payOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:pay" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }

        try {
            // 获取支付同步策略类
            AbsPaySyncStrategy syncPayStrategy = PaySyncStrategyFactory.create(payOrder.getChannel());
            syncPayStrategy.initPayParam(payOrder);
            // 执行操作, 获取支付网关同步的结果
            PaySyncResult paySyncResult = syncPayStrategy.doSyncStatus();
            // 判断是否同步成功
            if (Objects.equals(paySyncResult.getSyncStatus(), PaySyncStatusEnum.FAIL)){
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(payOrder, paySyncResult, false, null, paySyncResult.getErrorMsg());
                throw new PayFailureException(paySyncResult.getErrorMsg());
            }
            // 支付订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(paySyncResult.getOutOrderNo(), payOrder.getOutOrderNo())){
                payOrder.setOutOrderNo(paySyncResult.getOutOrderNo());
                payOrderService.updateById(payOrder);
            }
            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态
            boolean statusSync = this.checkAndAdjustSyncStatus(paySyncResult,payOrder);
            PayRepairResult repairResult = new PayRepairResult();
            try {
                // 状态不一致，执行支付单修复逻辑
                if (!statusSync){
                    // 如果没有修复触发来源, 设置修复触发来源为同步
                    RepairLocal repairInfo = PaymentContextLocal.get().getRepairInfo();
                    if (Objects.isNull(repairInfo.getSource())){
                        repairInfo.setSource(PayRepairSourceEnum.SYNC);
                    }
                    // 设置支付单完成时间
                    repairInfo.setFinishTime(paySyncResult.getPayTime());
                    repairResult = this.repairHandler(paySyncResult, payOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                paySyncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
                this.saveRecord(payOrder, paySyncResult, false, null, e.getMessage());
                throw e;
            }

            // 同步成功记录日志
            this.saveRecord( payOrder, paySyncResult, !statusSync, repairResult.getRepairNo(), null);
            return new SyncResult().setStatus(paySyncResult.getSyncStatus().getCode());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 判断支付单和网关状态是否一致, 同时待支付状态下, 支付单支付超时进行状态的更改
     */
    private boolean checkAndAdjustSyncStatus(PaySyncResult syncResult, PayOrder order){
        PaySyncStatusEnum syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 本地支付成功/网关支付成功
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(SUCCESS)){
            return true;
        }

        /*
        本地支付中/网关支付中或者订单未找到(未知)  支付宝特殊情况，未找到订单可能是发起支付用户未操作、支付已关闭、交易未找到三种情况
        所以需要根据本地订单不同的状态进行特殊处理
         */
        List<PaySyncStatusEnum> syncWaitEnums = Arrays.asList(PROGRESS, NOT_FOUND_UNKNOWN);
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && syncWaitEnums.contains(syncStatus)){
            // 判断支付单是否支付超时, 如果待支付状态下触发超时
            if (LocalDateTimeUtil.le(order.getExpiredTime(), LocalDateTime.now())){
                // 将支付单同步状态状态调整为支付超时, 进行订单的关闭
                syncResult.setSyncStatus(PaySyncStatusEnum.TIMEOUT);
                return false;
            }
            return true;
        }
        /*
         关闭 /网关支付关闭、订单未找到、订单未找到(特殊)， 订单未找到(特殊)主要是支付宝特殊情况，
         未找到订单可能是发起支付用户未操作、支付已关闭、交易未找到三种情况
         所以需要根据本地订单不同的状态进行特殊处理, 此处视为支付已关闭、交易未找到这两种, 处理方式相同, 都作为支付关闭处理
         */
        List<String> payCloseEnums = Collections.singletonList(PayStatusEnum.CLOSE.getCode());
        List<PaySyncStatusEnum> syncClose = Arrays.asList(CLOSED, NOT_FOUND, NOT_FOUND_UNKNOWN);
        if (payCloseEnums.contains(orderStatus) && syncClose.contains(syncStatus)){
            return true;
        }

        // 退款比对状态不做额外处理, 需要通过退款接口进行处理
        List<String> orderClose = Arrays.asList(
                PayStatusEnum.REFUNDED.getCode(),
                PayStatusEnum.REFUNDING.getCode(),
                PayStatusEnum.PARTIAL_REFUND.getCode());
        if (orderClose.contains(orderStatus) || syncStatus.equals(PaySyncStatusEnum.REFUND)){
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行修复处理
     */
    private PayRepairResult repairHandler(PaySyncResult syncResult, PayOrder payOrder){
        PaySyncStatusEnum syncStatusEnum = syncResult.getSyncStatus();
        PayRepairResult repair = new PayRepairResult();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case SUCCESS: {
                repair = repairService.repair(payOrder, PayRepairWayEnum.PAY_SUCCESS);
                break;
            }
            // 待支付, 将订单状态重新设置为待支付
            case PROGRESS: {
                repair = repairService.repair(payOrder, PayRepairWayEnum.PROGRESS);
                break;
            }
            case REFUND:
                throw new PayFailureException("支付订单为退款状态，请通过执行对应的退款订单进行同步，来更新具体为什么类型退款状态");
            // 交易关闭和未找到, 都对本地支付订单进行关闭, 不需要再调用网关进行关闭
            case CLOSED:
            case NOT_FOUND: {
                repair = repairService.repair(payOrder, PayRepairWayEnum.CLOSE_LOCAL);
                break;
            }
            // 超时关闭和交易不存在(特殊) 关闭本地支付订单, 同时调用网关进行关闭, 确保后续这个订单不能被支付
            case TIMEOUT:
            case NOT_FOUND_UNKNOWN:{
                repair = repairService.repair(payOrder, PayRepairWayEnum.CLOSE_GATEWAY);
                break;
            }
            // 调用出错
            case FAIL: {
                // 不进行处理
                log.warn("支付状态同步接口调用出错");
                break;
            }
            default: {
                throw new PayFailureException("代码有问题");
            }
        }
        return repair;
    }


    /**
     * 保存同步记录
     * @param payOrder 支付单
     * @param syncResult 同步结果
     * @param repair 是否修复
     * @param repairOrderNo 修复号
     * @param errorMsg 错误信息
     */
    private void saveRecord(PayOrder payOrder, PaySyncResult syncResult, boolean repair, String repairOrderNo, String errorMsg){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setBizTradeNo(payOrder.getBizOrderNo())
                .setTradeNo(payOrder.getOrderNo())
                .setOutTradeNo(payOrder.getOutOrderNo())
                .setOutTradeStatus(syncResult.getSyncStatus().getCode())
                .setSyncType(PaymentTypeEnum.PAY.getCode())
                .setChannel(payOrder.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setRepair(repair)
                .setRepairNo(repairOrderNo)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getRequestInfo().getClientIp());
        paySyncRecordService.saveRecord(paySyncRecord);
    }
}
