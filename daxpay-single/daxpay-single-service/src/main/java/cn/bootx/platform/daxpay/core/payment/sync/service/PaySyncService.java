package cn.bootx.platform.daxpay.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.code.PayRepairTypeEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.sync.service.PaySyncOrderService;
import cn.bootx.platform.daxpay.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.core.payment.repair.service.PayRepairService;
import cn.bootx.platform.daxpay.core.payment.sync.factory.PaySyncStrategyFactory;
import cn.bootx.platform.daxpay.core.payment.sync.result.GatewaySyncResult;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.result.pay.PaySyncResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final PayOrderManager payOrderManager;

    private final PaySyncOrderService syncOrderService;

    private final PayRepairService repairService;

    /**
     * 支付同步
     */
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderManager.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderManager.findByBusinessNo(param.getBusinessNo())
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
     * 同步支付状态
     * 1. 如果状态一致, 不进行处理
     * 2. 如果状态不一致, 调用修复逻辑进行修复
     */
    public PaySyncResult syncPayOrder(PayOrder order) {
        // 获取同步策略类
        AbsPaySyncStrategy syncPayStrategy = PaySyncStrategyFactory.create(order.getAsyncPayChannel());
        syncPayStrategy.initPayParam(order);
        // 同步支付状态
        GatewaySyncResult syncResult = syncPayStrategy.doSyncStatus();

        // 判断网关状态是否和支付单一致
        boolean statusSync = this.isStatusSync(syncResult,order);
        // 状态不一致，执行支付单修复逻辑
        if (!statusSync){
            // 根据同步记录对支付单进行处理处理
            this.resultHandler(syncResult,order);
        }
        // 记录同步的结果
        syncOrderService.saveRecord(syncResult,order);
        return new PaySyncResult().setSuccess(true).setRepair(!statusSync).setSyncStatus(syncResult.getSyncStatus());
    }

    /**
     * 支付单和网关状态是否一致
     */
    public boolean isStatusSync(GatewaySyncResult syncResult, PayOrder order){
        String syncStatus = syncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 支付成功比对
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_SUCCESS.getCode())){
            return true;
        }
        // 待支付比对
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_WAIT.getCode())){
            return true;
        }

        // 支付关闭比对
        if (orderStatus.equals(PayStatusEnum.CLOSE.getCode()) && syncStatus.equals(PaySyncStatusEnum.CLOSED.getCode())){
            return true;
        }

        // 退款比对
        if (orderStatus.equals(PayStatusEnum.REFUNDED.getCode()) && syncStatus.equals(PaySyncStatusEnum.REFUND.getCode())){
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行处理
     */
    private void resultHandler(GatewaySyncResult syncResult, PayOrder payOrder){

        PaySyncStatusEnum syncStatusEnum = PaySyncStatusEnum.getByCode(syncResult.getSyncStatus());
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
            // 订单已经关闭超时关闭 和 网关没找到记录,
            case CLOSED:
            case NOT_FOUND: {
                // 判断下是否超时, 变更为关闭支付
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
            case FAIL: {
                // 不进行处理 TODO 添加重试
                log.warn("支付状态同步接口调用出错");
                break;
            }
            case NOT_SYNC:
            default: {
                throw new BizException("代码有问题");
            }
        }
    }
}
