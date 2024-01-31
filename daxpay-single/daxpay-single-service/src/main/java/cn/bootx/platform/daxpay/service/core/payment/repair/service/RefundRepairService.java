package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairWayEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.repair.factory.RefundRepairStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RefundRepairResult;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.core.record.repair.service.PayRepairRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundRepairStrategy;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 退款订单修复, 只有存在异步支付的退款订单才存在修复
 * @author xxm
 * @since 2024/1/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundRepairService {

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final PayRefundOrderManager refundOrderManager;

    private final PayRefundChannelOrderManager refundChannelOrderManager;

    private final PayRepairRecordService recordService;

    /**
     * 修复退款单
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundRepairResult repair(PayRefundOrder refundOrder, RefundRepairWayEnum repairType){

        // 获取关联支付单
        PayOrder payOrder = payOrderQueryService.findById(refundOrder.getPaymentId())
                .orElseThrow(() -> new RuntimeException("支付单不存在"));
        // 关联支付通道支付单
        Map<String, PayChannelOrder> payChannelOrderMap = payChannelOrderManager.findAllByPaymentId(refundOrder.getPaymentId())
                .stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        // 异步通道退款单
        Map<String, PayRefundChannelOrder> refundChannelOrderMap = refundChannelOrderManager.findAllByRefundId(refundOrder.getId())
                .stream()
                .collect(Collectors.toMap(PayRefundChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));

        // 2 初始化修复参数
        List<String> channels = new ArrayList<>(payChannelOrderMap.keySet());
        List<AbsRefundRepairStrategy> repairStrategies = RefundRepairStrategyFactory.createAsyncLast(channels);
        for (AbsRefundRepairStrategy repairStrategy : repairStrategies) {
            PayChannelOrder payChannelOrder = payChannelOrderMap.get(repairStrategy.getChannel().getCode());
            PayRefundChannelOrder payRefundChannelOrder = refundChannelOrderMap.get(repairStrategy.getChannel().getCode());
            repairStrategy.initRepairParam(refundOrder, payRefundChannelOrder, payOrder, payChannelOrder);
        }

        // 根据不同的类型执行对应的修复逻辑
        RefundRepairResult repairResult = new RefundRepairResult();
        if (Objects.requireNonNull(repairType) == RefundRepairWayEnum.SUCCESS) {
            repairResult = this.success(refundOrder,payOrder,repairStrategies);
        } else if (repairType == RefundRepairWayEnum.FAIL) {
            repairResult = this.close(refundOrder,payOrder,repairStrategies);
        } else {
            log.error("走到了理论上讲不会走到的分支");
        }

        // 设置修复ID并保存修复记录
        repairResult.setRepairId(IdUtil.getSnowflakeNextId());
        PayRepairRecord payRepairRecord = this.payRepairRecord(payOrder, repairType, repairResult);
        PayRepairRecord refundRepairRecord = this.refundRepairRecord(refundOrder, repairType, repairResult);
        recordService.saveAllRecord(Arrays.asList(payRepairRecord, refundRepairRecord));

        return repairResult;
    }

    /**
     * 退款成功, 更新退款单和支付单
     */
    private RefundRepairResult success(PayRefundOrder refundOrder, PayOrder payOrder, List<AbsRefundRepairStrategy> repairStrategies) {

        // 订单相关状态
        PayStatusEnum beforePayStatus = PayStatusEnum.findByCode(refundOrder.getStatus());
        PayStatusEnum afterPayRefundStatus;
        PayRefundStatusEnum beforeRefundStatus = PayRefundStatusEnum.findByCode(refundOrder.getStatus());

        // 判断订单全部退款还是部分退款
        if (Objects.equals(payOrder.getRefundableBalance(),0)){
            afterPayRefundStatus = PayStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态
        refundOrder.setStatus(PayRefundStatusEnum.SUCCESS.getCode());
        payOrder.setStatus(afterPayRefundStatus.getCode());

        // 执行退款成功逻辑
        repairStrategies.forEach(AbsRefundRepairStrategy::doSuccessHandler);
        // 获取要更新的数据
        List<PayChannelOrder> payChannelOrders = repairStrategies.stream()
                .map(AbsRefundRepairStrategy::getPayChannelOrder)
                .collect(Collectors.toList());
        List<PayRefundChannelOrder> refundChannelOrders = repairStrategies
                .stream()
                .map(AbsRefundRepairStrategy::getRefundChannelOrder)
                .collect(Collectors.toList());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        payChannelOrderManager.updateAllById(payChannelOrders);
        refundChannelOrderManager.updateAllById(refundChannelOrders);

        return new RefundRepairResult()
                .setBeforePayStatus(beforePayStatus)
                .setAfterPayStatus(afterPayRefundStatus)
                .setBeforeRefundStatus(beforeRefundStatus)
                .setAfterRefundStatus(PayRefundStatusEnum.SUCCESS);
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private RefundRepairResult close(PayRefundOrder refundOrder, PayOrder payOrder, List<AbsRefundRepairStrategy> repairStrategies) {
        // 要返回的状态
        RefundRepairResult repairResult = new RefundRepairResult();

        // 订单修复前状态
        PayStatusEnum beforePayStatus = PayStatusEnum.findByCode(refundOrder.getStatus());
        PayRefundStatusEnum beforeRefundStatus = PayRefundStatusEnum.findByCode(refundOrder.getStatus());
        repairResult.setBeforePayStatus(beforePayStatus)
                .setBeforeRefundStatus(beforeRefundStatus);

        // 退款失败返还后的余额
        int payOrderAmount = refundOrder.getAmount() + payOrder.getRefundableBalance();
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (payOrderAmount == payOrder.getAmount()){
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
            repairResult.setAfterPayStatus(PayStatusEnum.SUCCESS);
        } else {
            // 回归部分退款状态
            payOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            repairResult.setAfterPayStatus(PayStatusEnum.PARTIAL_REFUND);
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(PayRefundStatusEnum.CLOSE.getCode());

        // 执行关闭退款逻辑
        repairStrategies.forEach(AbsRefundRepairStrategy::doCloseHandler);

        // 获取要更新的数据
        List<PayChannelOrder> payChannelOrders = repairStrategies.stream()
                .map(AbsRefundRepairStrategy::getPayChannelOrder)
                .collect(Collectors.toList());
        List<PayRefundChannelOrder> refundChannelOrders = repairStrategies
                .stream()
                .map(AbsRefundRepairStrategy::getRefundChannelOrder)
                .collect(Collectors.toList());

        // 更新订单和退款相关订单
        payChannelOrderManager.updateAllById(payChannelOrders);
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        refundChannelOrderManager.updateAllById(refundChannelOrders);
        return repairResult;
    }

    /**
     * 支付订单的修复记录
     * 支付完成 -> 退款
     * 退款 -> 全部退款
     */
    private PayRepairRecord payRepairRecord(PayOrder order, RefundRepairWayEnum repairType, RefundRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getBeforePayStatus()).map(PayStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource().getCode();
        return new PayRepairRecord()
                .setRepairId(repairResult.getRepairId())
                .setOrderId(order.getId())
                .setRepairType(PaymentTypeEnum.PAY.getCode())
                .setRepairSource(source)
                .setRepairWay(repairType.getCode())
                .setAsyncChannel(order.getAsyncChannel())
                .setOrderNo(order.getBusinessNo())
                .setBeforeStatus(repairResult.getAfterPayStatus().getCode())
                .setAfterStatus(afterStatus);
    }

    /**
     * 退款订单的修复记录
     * 退款中 -> 退款成功
     */
    private PayRepairRecord refundRepairRecord(PayRefundOrder refundOrder, RefundRepairWayEnum repairType, RefundRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getAfterRefundStatus()).map(PayRefundStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource().getCode();
        return new PayRepairRecord()
                .setOrderId(refundOrder.getId())
                .setRepairId(repairResult.getRepairId())
                .setOrderNo(refundOrder.getRefundNo())
                .setRepairType(PaymentTypeEnum.REFUND.getCode())
                .setBeforeStatus(repairResult.getBeforeRefundStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairSource(source)
                .setRepairWay(repairType.getCode());
    }
}
