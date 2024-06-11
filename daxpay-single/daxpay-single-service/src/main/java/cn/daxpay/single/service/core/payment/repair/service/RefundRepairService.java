package cn.daxpay.single.service.core.payment.repair.service;

import cn.daxpay.single.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.code.RefundRepairWayEnum;
import cn.daxpay.single.service.common.context.RepairLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.payment.repair.result.RefundRepairResult;
import cn.daxpay.single.service.core.record.flow.service.TradeFlowRecordService;
import cn.daxpay.single.service.core.record.repair.entity.PayRepairRecord;
import cn.daxpay.single.service.core.record.repair.service.PayRepairRecordService;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    private final ClientNoticeService clientNoticeService;

    private final RefundOrderManager refundOrderManager;

    private final PayRepairRecordService recordService;

    private final LockTemplate lockTemplate;
    private final TradeFlowRecordService tradeFlowRecordService;

    /**
     * 修复退款单
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundRepairResult repair(RefundOrder refundOrder, RefundRepairWayEnum repairType){
        // 添加分布式锁
        LockInfo lock = lockTemplate.lock("repair:refund:" + refundOrder.getId(), 10000, 200);
        if (Objects.isNull(lock)){
            log.warn("当前退款单正在修复中: {}", refundOrder.getId());
            return new RefundRepairResult();
        }
        try {
            // 获取关联支付单
            PayOrder payOrder = payOrderQueryService.findById(refundOrder.getOrderId())
                    .orElseThrow(() -> new RuntimeException("支付单不存在"));

            // 根据不同的类型执行对应的修复逻辑
            RefundRepairResult repairResult = new RefundRepairResult();
            if (Objects.requireNonNull(repairType) == RefundRepairWayEnum.REFUND_SUCCESS) {
                repairResult = this.success(refundOrder, payOrder);
            } else if (repairType == RefundRepairWayEnum.REFUND_FAIL) {
                repairResult = this.close(refundOrder, payOrder);
            } else {
                log.error("走到了理论上讲不会走到的分支");
            }

            // 设置修复ID并保存修复记录
            repairResult.setRepairNo(OrderNoGenerateUtil.repair());
            // 支付修复记录
            PayRepairRecord payRepairRecord = this.payRepairRecord(payOrder, repairType, repairResult);
            // 退款修复记录
            PayRepairRecord refundRepairRecord = this.refundRepairRecord(refundOrder, repairType, repairResult);

            // 发送通知
            clientNoticeService.registerRefundNotice(refundOrder);
            recordService.saveAllRecord(Arrays.asList(payRepairRecord, refundRepairRecord));
            return repairResult;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 退款成功, 更新退款单和支付单
     */
    private RefundRepairResult success(RefundOrder refundOrder, PayOrder payOrder) {
        RepairLocal repairInfo = PaymentContextLocal.get().getRepairInfo();
        // 订单相关状态
        PayOrderRefundStatusEnum beforePayStatus = PayOrderRefundStatusEnum.findByCode(payOrder.getRefundStatus());
        PayOrderRefundStatusEnum afterPayRefundStatus;
        RefundStatusEnum beforeRefundStatus = RefundStatusEnum.findByCode(refundOrder.getStatus());

        // 判断订单全部退款还是部分退款
        if (Objects.equals(payOrder.getRefundableBalance(),0)){
            afterPayRefundStatus = PayOrderRefundStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayOrderRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setFinishTime(repairInfo.getFinishTime());
        payOrder.setRefundStatus(afterPayRefundStatus.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);

        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);

        // 发送通知
        List<String> list = Arrays.asList(RefundStatusEnum.SUCCESS.getCode(), RefundStatusEnum.CLOSE.getCode(),  RefundStatusEnum.FAIL.getCode());
        if (list.contains(refundOrder.getStatus())){
            clientNoticeService.registerRefundNotice(refundOrder);
        }

        return new RefundRepairResult()
                .setBeforePayStatus(beforePayStatus)
                .setAfterPayStatus(afterPayRefundStatus)
                .setBeforeRefundStatus(beforeRefundStatus)
                .setAfterRefundStatus(RefundStatusEnum.SUCCESS);
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private RefundRepairResult close(RefundOrder refundOrder, PayOrder payOrder) {
        // 要返回的状态
        RefundRepairResult repairResult = new RefundRepairResult();

        // 订单修复前状态
        PayOrderRefundStatusEnum beforePayStatus = PayOrderRefundStatusEnum.findByCode(payOrder.getRefundStatus());
        RefundStatusEnum beforeRefundStatus = RefundStatusEnum.findByCode(refundOrder.getStatus());
        repairResult.setBeforePayStatus(beforePayStatus)
                .setBeforeRefundStatus(beforeRefundStatus);

        // 退款失败返还后的余额
        int payOrderAmount = refundOrder.getAmount() + payOrder.getRefundableBalance();
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (payOrderAmount == payOrder.getAmount()){
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
            repairResult.setAfterPayStatus(PayOrderRefundStatusEnum.NO_REFUND);
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayOrderRefundStatusEnum.PARTIAL_REFUND.getCode());
            repairResult.setAfterPayStatus(PayOrderRefundStatusEnum.PARTIAL_REFUND);
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        return repairResult;
    }

    /**
     * 支付订单的修复记录
     * 支付完成 -> 退款
     * 退款 -> 全部退款
     * @param repairType 支付订单修复方式状态编码跟退款修复的状态一致,
     */
    private PayRepairRecord payRepairRecord(PayOrder order, RefundRepairWayEnum repairType, RefundRepairResult repairResult){
        // 修复前的状态
        String beforeStatus = Optional.ofNullable(repairResult.getBeforePayStatus())
                .map(PayOrderRefundStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource().getCode();
        return new PayRepairRecord()
                .setTradeId(order.getId())
                .setTradeNo(order.getOrderNo())
                .setRepairNo(repairResult.getRepairNo())
                .setRepairType(PaymentTypeEnum.PAY.getCode())
                .setRepairSource(source)
                .setRepairWay(repairType.getCode())
                .setChannel(order.getChannel())
                .setBeforeStatus(beforeStatus)
                .setAfterStatus(repairResult.getAfterPayStatus().getCode());
    }

    /**
     * 退款订单的修复记录
     * 退款中 -> 退款成功
     */
    private PayRepairRecord refundRepairRecord(RefundOrder refundOrder, RefundRepairWayEnum repairType, RefundRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getAfterRefundStatus()).map(RefundStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource().getCode();
        return new PayRepairRecord()
                .setTradeId(refundOrder.getId())
                .setRepairNo(repairResult.getRepairNo())
                .setTradeNo(refundOrder.getRefundNo())
                .setRepairType(PaymentTypeEnum.REFUND.getCode())
                .setBeforeStatus(repairResult.getBeforeRefundStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairSource(source)
                .setRepairWay(repairType.getCode());
    }
}
