package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RefundRepairResult;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.core.record.repair.service.PayRepairRecordService;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 退款订单修复, 只有存在异步支付的退款订单才存在修复
 * @author xxm
 * @since 2024/1/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundRepairService {

    private final PayOrderManager payOrderManager;

    private final PayChannelOrderManager payChannelOrderManager;

    private final PayRefundOrderManager refundOrderManager;

    private final PayRefundChannelOrderManager refundChannelOrderManager;

    private final PayRepairRecordService recordService;

    /**
     * 修复支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundRepairResult repair(PayRefundOrder refundOrder, RefundRepairTypeEnum repairType){

        // 获取关联支付单
        PayOrder payOrder = payOrderManager.findById(refundOrder.getPaymentId())
                .orElseThrow(() -> new RuntimeException("支付单不存在"));
        // 关联异步支付通道支付单
        PayChannelOrder payChannelOrder = payChannelOrderManager.findByPaymentIdAndChannel(payOrder.getId(), payOrder.getAsyncChannel())
                .orElseThrow(DataNotExistException::new);
        // 异步通道退款单
        PayRefundChannelOrder refundChannelOrder = refundChannelOrderManager.findByPaymentIdAndChannel(refundOrder.getPaymentId(), payOrder.getAsyncChannel())
                .orElseThrow(DataNotExistException::new);

        // 根据不同的类型执行对应的修复逻辑
        RefundRepairResult repairResult = new RefundRepairResult();
        if (Objects.requireNonNull(repairType) == RefundRepairTypeEnum.SUCCESS) {
            repairResult = this.success(refundOrder,payOrder,refundChannelOrder,payChannelOrder);
        } else if (repairType == RefundRepairTypeEnum.FAIL) {
            repairResult = this.closeLocal(refundOrder,payOrder,refundChannelOrder,payChannelOrder);
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
    private RefundRepairResult success(PayRefundOrder refundOrder, PayOrder payOrder, PayRefundChannelOrder refundChannelOrder, PayChannelOrder payChannelOrder) {
        // 更新通道支付单全部退款还是部分退款
        if (Objects.equals(payChannelOrder.getRefundableBalance(),0)){
            payChannelOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            payChannelOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }

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
        refundChannelOrder.setStatus(PayRefundStatusEnum.SUCCESS.getCode());

        // 更新订单和退款相关订单
        payChannelOrderManager.updateById(payChannelOrder);
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        refundChannelOrderManager.updateById(refundChannelOrder);
        return new RefundRepairResult()
                .setBeforePayStatus(beforePayStatus)
                .setAfterPayStatus(afterPayRefundStatus)
                .setBeforeRefundStatus(beforeRefundStatus)
                .setAfterRefundStatus(PayRefundStatusEnum.SUCCESS);
    }


    /**
     * 退款失败, 将失败的退款金额归还回订单
     */
    private RefundRepairResult closeLocal(PayRefundOrder refundOrder, PayOrder payOrder, PayRefundChannelOrder refundChannelOrder, PayChannelOrder payChannelOrder) {

        // 更新通道支付单全部退款还是部分退款
        if (Objects.equals(payChannelOrder.getRefundableBalance(),0)){
            payChannelOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            payChannelOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 订单相关状态
        PayStatusEnum beforePayStatus = PayStatusEnum.findByCode(refundOrder.getStatus());
        PayStatusEnum afterPayRefundStatus;
        PayRefundStatusEnum beforeRefundStatus = PayRefundStatusEnum.findByCode(refundOrder.getStatus());

        // 判断订单是支付成功还是部分退款


        if (Objects.equals(payOrder.getRefundableBalance(),0)){
            afterPayRefundStatus = PayStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayStatusEnum.PARTIAL_REFUND;
        }

        // 设置退款为失败状态
        refundOrder.setStatus(PayRefundStatusEnum.FAIL.getCode());
        refundChannelOrder.setStatus(PayRefundStatusEnum.FAIL.getCode());

        // 更新订单和退款相关订单
        payChannelOrderManager.updateById(payChannelOrder);
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        refundChannelOrderManager.updateById(refundChannelOrder);
        return new RefundRepairResult()
                .setBeforePayStatus(beforePayStatus)
                .setAfterPayStatus(afterPayRefundStatus)
                .setBeforeRefundStatus(beforeRefundStatus)
                .setAfterRefundStatus(PayRefundStatusEnum.FAIL);
    }

    /**
     * 支付订单的修复记录
     */
    private PayRepairRecord payRepairRecord(PayOrder order, RefundRepairTypeEnum repairType, RefundRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getBeforePayStatus()).map(PayStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource();
        return new PayRepairRecord()
                .setOrderId(order.getId())
                .setAsyncChannel(order.getAsyncChannel())
                .setOrderNo(order.getBusinessNo())
                .setBeforeStatus(repairResult.getAfterPayStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairSource(source)
                .setRepairType(repairType.getCode());
    }

    /**
     * 退款订单的修复记录
     */
    private PayRepairRecord refundRepairRecord(PayRefundOrder refundOrder, RefundRepairTypeEnum repairType, RefundRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getAfterRefundStatus()).map(PayRefundStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource();
        return new PayRepairRecord()
                .setOrderId(refundOrder.getId())
                .setOrderNo(refundOrder.getRefundNo())
                .setBeforeStatus(repairResult.getBeforeRefundStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairSource(source)
                .setRepairType(repairType.getCode());
    }
}
