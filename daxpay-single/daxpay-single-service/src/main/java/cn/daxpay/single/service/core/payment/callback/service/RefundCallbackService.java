package cn.daxpay.single.service.core.payment.callback.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.record.flow.service.TradeFlowRecordService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 退款回调
 * @author xxm
 * @since 2024/1/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {
    private final RefundOrderManager refundOrderManager;

    private final LockTemplate lockTemplate;
    private final TradeFlowRecordService tradeFlowRecordService;
    private final PayOrderService payOrderService;
    private final ClientNoticeService clientNoticeService;

    /**
     * 退款回调统一处理
     */
    public void refundCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:refund:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取退款单
            RefundOrder refundOrder = refundOrderManager.findByRefundNo(callbackInfo.getTradeNo()).orElse(null);
            // 退款单不存在,记录回调记录
            if (Objects.isNull(refundOrder)) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.NOT_FOUND).setErrorMsg("退款单不存在,记录回调记录");
                return;
            }
            // 退款单已经被处理, 记录回调记录
            if (!Objects.equals(RefundStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setErrorMsg("退款单状态已处理,记录回调记录");
                return;
            }

            // 退款成功还是失败
            if (Objects.equals(RefundStatusEnum.SUCCESS.getCode(), callbackInfo.getOutStatus())) {
                this.success(refundOrder);
            }  else {
                this.close(refundOrder);
            }

        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 退款成功, 更新退款单和支付单
     */
    private void success(RefundOrder refundOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        PayOrder payOrder = payOrderService.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));

        // 订单相关状态
        PayOrderRefundStatusEnum afterPayRefundStatus;

        // 判断订单全部退款还是部分退款
        if (Objects.equals(payOrder.getRefundableBalance(), 0)) {
            afterPayRefundStatus = PayOrderRefundStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayOrderRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setFinishTime(callbackInfo.getFinishTime());
        payOrder.setRefundStatus(afterPayRefundStatus.getCode());

        // 更新订单和退款相关订单
        payOrderService.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);

        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);
        // 发送通知
        clientNoticeService.registerRefundNotice(refundOrder);
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private void close(RefundOrder refundOrder) {
        PayOrder payOrder = payOrderService.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));
        // 退款失败返还后的余额
        int payOrderAmount = refundOrder.getAmount() + payOrder.getRefundableBalance();
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (payOrderAmount == payOrder.getAmount()) {
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
        // 发送通知
        clientNoticeService.registerRefundNotice(refundOrder);
    }
}
