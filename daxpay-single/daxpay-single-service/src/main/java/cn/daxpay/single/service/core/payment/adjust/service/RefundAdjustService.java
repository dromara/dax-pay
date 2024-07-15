package cn.daxpay.single.service.core.payment.adjust.service;

import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.exception.OperationProcessingException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.service.code.RefundAdjustWayEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.adjust.param.RefundAdjustParam;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.record.flow.service.TradeFlowRecordService;
import cn.daxpay.single.service.core.record.repair.entity.TradeAdjustRecord;
import cn.daxpay.single.service.core.record.repair.service.TradeAdjustRecordService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 退款订单修复, 只有存在异步支付的退款订单才存在修复
 * @author xxm
 * @since 2024/1/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundAdjustService {

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    private final ClientNoticeService clientNoticeService;

    private final RefundOrderManager refundOrderManager;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final TradeAdjustRecordService tradeAdjustRecordService;

    private final LockTemplate lockTemplate;

    /**
     * 调整退款单
     */
    @Transactional(rollbackFor = Exception.class)
    public String adjust(RefundAdjustParam param){
        RefundOrder refundOrder = param.getOrder();
        RefundAdjustWayEnum repairType = param.getAdjustWay();
        // 添加分布式锁
        LockInfo lock = lockTemplate.lock("repair:refund:" + refundOrder.getId(), 10000, 200);
        if (Objects.isNull(lock)){
            log.warn("当前退款订单正在调整中: {}", refundOrder.getId());
            throw new OperationProcessingException("当前退款订单正在调整中");
        }
        // 如果到达终态不能向前回滚
        if (Arrays.asList(RefundStatusEnum.SUCCESS.getCode(), RefundStatusEnum.CLOSE.getCode()).contains(refundOrder.getStatus())){
            throw new TradeStatusErrorException("当前退款订单已处理完成");
        }
        try {
            // 获取关联支付单
            PayOrder payOrder = payOrderQueryService.findById(refundOrder.getOrderId()).orElseThrow(() -> new RuntimeException("支付单不存在"));
            String adjustNo = null;
            // 根据不同的类型执行对应的修复逻辑
            if (repairType == RefundAdjustWayEnum.SUCCESS) {
                adjustNo = this.success(param, payOrder);
            } else if (repairType == RefundAdjustWayEnum.FAIL) {
                adjustNo = this.close(param, payOrder);
            } else {
                log.error("走到了理论上讲不会走到的分支");
            }

            // 发送通知
            clientNoticeService.registerRefundNotice(refundOrder);
            return adjustNo;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 退款成功, 更新退款单和支付单
     */
    private String success(RefundAdjustParam param, PayOrder payOrder) {
        RefundOrder refundOrder = param.getOrder();
        String beforeRefundStatus = refundOrder.getStatus();

        // 订单相关状态
        PayOrderRefundStatusEnum afterPayRefundStatus;

        // 判断订单全部退款还是部分退款
        if (Objects.equals(payOrder.getRefundableBalance(),0)){
            afterPayRefundStatus = PayOrderRefundStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayOrderRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setFinishTime(param.getFinishTime());
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
        // 保存调整记录
        TradeAdjustRecord adjustRecord = this.saveRecord(param, beforeRefundStatus);
        return adjustRecord.getAdjustNo();
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private String close(RefundAdjustParam param, PayOrder payOrder) {
        RefundOrder refundOrder = param.getOrder();
        String beforeRefundStatus = refundOrder.getStatus();

        // 退款失败返还后的余额
        int payOrderAmount = refundOrder.getAmount() + payOrder.getRefundableBalance();
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (payOrderAmount == payOrder.getAmount()){
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayOrderRefundStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        TradeAdjustRecord adjustRecord = this.saveRecord(param, beforeRefundStatus);
        return adjustRecord.getAdjustNo();
    }

    /**
     * 保存退款订单的调整记录
     */
    private TradeAdjustRecord saveRecord(RefundAdjustParam param, String beforeRefundStatus){
        RefundOrder refundOrder = param.getOrder();
        // 修复发起来源
        TradeAdjustRecord record = new TradeAdjustRecord()
                .setTradeId(refundOrder.getId())
                .setAdjustNo(TradeNoGenerateUtil.adjust())
                .setTradeNo(refundOrder.getRefundNo())
                .setChannel(refundOrder.getChannel())
                .setType(TradeTypeEnum.REFUND.getCode())
                .setBeforeStatus(beforeRefundStatus)
                .setAfterStatus(refundOrder.getStatus())
                .setSource(param.getSource().getCode())
                .setWay(param.getAdjustWay().getCode());
        tradeAdjustRecordService.saveRecord(record);
        return record;
    }
}
