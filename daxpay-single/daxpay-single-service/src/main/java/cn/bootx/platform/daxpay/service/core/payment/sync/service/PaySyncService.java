package cn.bootx.platform.daxpay.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.result.pay.PaySyncResult;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.service.core.payment.repair.service.PayRepairService;
import cn.bootx.platform.daxpay.service.core.payment.sync.factory.PaySyncStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.record.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.core.record.sync.service.PaySyncRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPaySyncStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 支付同步服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {
    private final PayOrderService payOrderService;

    private final PaySyncRecordService paySyncRecordService;

    private final PayRepairService repairService;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderService.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderService.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        // 如果不是异步支付, 直接返回返回
        if (!payOrder.isAsyncPay()){
            return new PaySyncResult().setSuccess(true).setRepair(false);
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
    public PaySyncResult syncPayOrder(PayOrder order) {
        // 获取同步策略类
        AbsPaySyncStrategy syncPayStrategy = PaySyncStrategyFactory.create(order.getAsyncChannel());
        syncPayStrategy.initPayParam(order);
        // 记录支付单同步前后的状态
        String oldStatus = order.getStatus();
        String repairStatus = null;

        // 执行同步操作, 获取支付网关同步的结果
        GatewaySyncResult syncResult = syncPayStrategy.doSyncStatus();
        // 判断是否同步成功
        if (Objects.equals(syncResult.getSyncStatus(), PaySyncStatusEnum.FAIL)){
            // 同步失败, 返回失败响应, 同时记录失败的日志
            this.saveRecord(order, syncResult, true, oldStatus, null, syncResult.getErrorMsg());
            return new PaySyncResult().setErrorMsg(syncResult.getErrorMsg());
        }

        // 判断网关状态是否和支付单一致, 同时更新网关同步状态
        boolean statusSync = this.checkStatusSync(syncResult,order);
        try {
            // 状态不一致，执行支付单修复逻辑
            if (!statusSync){
                this.resultHandler(syncResult, order);
                repairStatus = order.getStatus();
            }
        } catch (Exception e) {
            // 同步失败, 返回失败响应, 同时记录失败的日志 TODO 后面异常范围能这么宽泛
            syncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
            this.saveRecord(order, syncResult, false, oldStatus, null, e.getMessage());
            return new PaySyncResult().setErrorMsg(e.getMessage());
        }

        // 同步成功记录日志
        this.saveRecord( order, syncResult, !statusSync, oldStatus, repairStatus, null);
        return new PaySyncResult()
                .setSuccess(true)
                .setRepair(!statusSync)
                .setRepairStatus(repairStatus)
                .setSyncStatus(syncResult.getSyncStatus().getCode());
    }

    /**
     * 支付单和网关状态是否一致, 同时待支付状态下, 处理支付超时情况
     */
    public boolean checkStatusSync(GatewaySyncResult syncResult, PayOrder order){
        PaySyncStatusEnum syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 支付成功比对
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_SUCCESS)){
            return true;
        }
        // 待支付比对 支付中都代表待支付, 需要处理订单超时的情况
        List<PaySyncStatusEnum> enums = Collections.singletonList(PaySyncStatusEnum.PAY_WAIT);
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && enums.contains(syncStatus)){
            // 判断支付单是否支付超时, 如果待支付状态下触发超时
            if (LocalDateTimeUtil.le(order.getExpiredTime(), LocalDateTime.now())){
                syncResult.setSyncStatus(PaySyncStatusEnum.TIMEOUT);
                return false;
            }
            return true;
        }

        // 支付关闭比对
        if (orderStatus.equals(PayStatusEnum.CLOSE.getCode()) && syncStatus.equals(PaySyncStatusEnum.CLOSED)){
            return true;
        }

        // 退款比对
        if (orderStatus.equals(PayStatusEnum.REFUNDED.getCode()) && syncStatus.equals(PaySyncStatusEnum.REFUND)){
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行处理
     */
    private void resultHandler(GatewaySyncResult syncResult, PayOrder payOrder){
        PaySyncStatusEnum syncStatusEnum = syncResult.getSyncStatus();
        PayRepairParam repairParam = new PayRepairParam().setRepairSource(PayRepairSourceEnum.SYNC);
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PAY_SUCCESS: {
                repairParam.setRepairType(PayRepairTypeEnum.SUCCESS);
                repairService.repair(payOrder,repairParam);
                break;
            }
            // 待付款/ 支付中
            case PAY_WAIT: {
                log.info("依然是付款状态");
                break;
            }
            // 订单已经超时关闭 和 网关没找到记录, 对订单进行关闭
            case CLOSED:
            case NOT_FOUND: {
                repairParam.setRepairType(PayRepairTypeEnum.CLOSE);
                repairService.repair(payOrder, repairParam);
                break;
            }
            // 交易退款 TODO 未实现
            case REFUND: {
                repairParam.setRepairType(PayRepairTypeEnum.REFUND);
                repairService.repair(payOrder, repairParam);
                break;
            }
            // 调用出错
            case TIMEOUT:
                repairParam.setRepairType(PayRepairTypeEnum.TIMEOUT);
                repairService.repair(payOrder, repairParam);
                break;
            case FAIL: {
                // 不进行处理 TODO 添加重试
                log.warn("支付状态同步接口调用出错");
                break;
            }
            default: {
                throw new BizException("代码有问题");
            }
        }
    }


    /**
     * 保存同步记录
     * @param payOrder 支付单
     * @param syncResult 同步结果
     * @param repair 是否修复
     * @param oldStatus 修复前的状态
     * @param repairStatus 修复后的状态
     * @param errorMsg 错误信息
     */
    private void saveRecord(PayOrder payOrder,GatewaySyncResult syncResult, boolean repair, String oldStatus, String repairStatus, String errorMsg){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setPaymentId(payOrder.getId())
                .setChannel(payOrder.getAsyncChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setSyncStatus(syncResult.getSyncStatus().getCode())
                .setRepairOrder(repair)
                .setOldStatus(oldStatus)
                .setRepairStatus(repairStatus)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getRequest().getClientIp())
                .setReqId(PaymentContextLocal.get().getRequest().getReqId());
        paySyncRecordService.saveRecord(paySyncRecord);
    }
}
