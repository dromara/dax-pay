package cn.bootx.platform.daxpay.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.result.pay.PaySyncResult;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RepairResult;
import cn.bootx.platform.daxpay.service.core.payment.repair.service.PayRepairService;
import cn.bootx.platform.daxpay.service.core.payment.sync.factory.PaySyncStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.core.record.sync.service.PaySyncRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPaySyncStrategy;
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

import static cn.bootx.platform.daxpay.code.PaySyncStatusEnum.*;

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

    private final PaySyncRecordService paySyncRecordService;

    private final PayRepairService repairService;

    private final LockTemplate lockTemplate;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderQueryService.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderQueryService.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        // 如果不是异步支付, 直接返回返回
        if (!payOrder.isAsyncPay()){
            return new PaySyncResult().setSuccess(false).setRepair(false).setErrorMsg("订单没有异步支付方式，不需要同步");
        }
        // 执行订单同步逻辑
        return this.syncPayOrder(payOrder);
    }
    /**
     * 同步支付状态, 开启一个新的事务, 不受外部抛出异常的影响
     * 1. 如果状态一致, 不进行处理
     * 2. 如果状态不一致, 调用修复逻辑进行修复
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaySyncResult syncPayOrder(PayOrder payOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + payOrder.getId());
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }

        try {
            // 获取同步策略类
            AbsPaySyncStrategy syncPayStrategy = PaySyncStrategyFactory.create(payOrder.getAsyncChannel());
            syncPayStrategy.initPayParam(payOrder);
            // 执行同步操作, 获取支付网关同步的结果
            GatewaySyncResult syncResult = syncPayStrategy.doSyncStatus();
            // 判断是否同步成功
            if (Objects.equals(syncResult.getSyncStatus(), PaySyncStatusEnum.FAIL)){
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(payOrder, syncResult, false, null, syncResult.getErrorMsg());
                return new PaySyncResult().setErrorMsg(syncResult.getErrorMsg());
            }

            // 判断网关状态是否和支付单一致, 同时更新网关同步状态
            boolean statusSync = this.checkAndAdjustSyncStatus(syncResult,payOrder);
            RepairResult repairResult = new RepairResult();
            try {
                // 状态不一致，执行支付单修复逻辑
                if (!statusSync){
                    repairResult = this.resultHandler(syncResult, payOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
                this.saveRecord(payOrder, syncResult, false, null, e.getMessage());
                return new PaySyncResult().setErrorMsg(e.getMessage());
            }

            // 同步成功记录日志
            this.saveRecord( payOrder, syncResult, !statusSync, repairResult.getId(), null);
            return new PaySyncResult()
                    .setGatewayStatus(syncResult.getSyncStatus().getCode())
                    .setSuccess(true)
                    .setRepair(!statusSync)
                    .setRepairId(repairResult.getId());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 判断支付单和网关状态是否一致, 同时待支付状态下, 支付单支付超时进行状态的更改
     */
    private boolean checkAndAdjustSyncStatus(GatewaySyncResult syncResult, PayOrder order){
        PaySyncStatusEnum syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 本地支付成功/网关支付成功
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(PAY_SUCCESS)){
            return true;
        }

        /*
        本地支付中/网关支付中或者订单未找到(未知)  支付宝特殊情况，未找到订单可能是发起支付用户未操作、支付已关闭、交易未找到三种情况
        所以需要根据本地订单不同的状态进行特殊处理
         */
        List<PaySyncStatusEnum> syncWaitEnums = Arrays.asList(PAY_WAIT, NOT_FOUND_UNKNOWN);
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

        // TODO 退款比对
        if (orderStatus.equals(PayStatusEnum.REFUNDED.getCode()) && syncStatus.equals(PaySyncStatusEnum.REFUND)){
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行处理
     */
    private RepairResult resultHandler(GatewaySyncResult syncResult, PayOrder payOrder){
        PaySyncStatusEnum syncStatusEnum = syncResult.getSyncStatus();
        PayRepairParam repairParam = new PayRepairParam().setRepairSource(PayRepairSourceEnum.SYNC);
        RepairResult repair = new RepairResult();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PAY_SUCCESS: {
                repairParam.setRepairType(PayRepairTypeEnum.SUCCESS);
                repair = repairService.repair(payOrder, repairParam);
                break;
            }
            // 待支付, 将订单状态重新设置为待支付
            case PAY_WAIT: {
                repairParam.setRepairType(PayRepairTypeEnum.WAIT);
                repair = repairService.repair(payOrder,repairParam);
                break;
            }
            // 交易关闭和未找到, 都对本地支付订单进行关闭, 不需要再调用网关进行关闭
            case CLOSED:
            case NOT_FOUND: {
                repairParam.setRepairType(PayRepairTypeEnum.CLOSE_LOCAL);
                repair = repairService.repair(payOrder, repairParam);
                break;
            }
            // 超时关闭和交易不存在(特殊) 关闭本地支付订单, 同时调用网关进行关闭, 确保后续这个订单不能被支付
            case TIMEOUT:
            case NOT_FOUND_UNKNOWN:{
                repairParam.setRepairType(PayRepairTypeEnum.CLOSE_GATEWAY);
                repair = repairService.repair(payOrder, repairParam);
                break;
            }
            // 交易退款 TODO 未实现
            case REFUND: {
                repairParam.setRepairType(PayRepairTypeEnum.REFUND);
                repair = repairService.repair(payOrder, repairParam);
                break;
            }
            // 调用出错
            case FAIL: {
                // 不进行处理 TODO 添加重试
                log.warn("支付状态同步接口调用出错");
                break;
            }
            default: {
                throw new BizException("代码有问题");
            }
        }
        return repair;
    }


    /**
     * 保存同步记录 TODO 目前出现一次请求多次与网关同步, 未全部记录
     * @param payOrder 支付单
     * @param syncResult 同步结果
     * @param repair 是否修复
     * @param errorMsg 错误信息
     */
    private void saveRecord(PayOrder payOrder,GatewaySyncResult syncResult, boolean repair, Long repairOrderId, String errorMsg){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setPaymentId(payOrder.getId())
                .setBusinessNo(payOrder.getBusinessNo())
                .setAsyncChannel(payOrder.getAsyncChannel())
                .setSyncInfo(syncResult.getSyncPayInfo())
                .setGatewayStatus(syncResult.getSyncStatus().getCode())
                .setRepairOrder(repair)
                .setRepairOrderId(repairOrderId)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getRequest().getClientIp())
                .setReqId(PaymentContextLocal.get().getRequest().getReqId());
        paySyncRecordService.saveRecord(paySyncRecord);
    }
}
