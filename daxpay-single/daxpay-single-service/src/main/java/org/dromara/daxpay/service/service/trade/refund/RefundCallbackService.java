package org.dromara.daxpay.service.service.trade.refund;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final PayOrderManager payOrderManager;
    private final MerchantNoticeService merchantNoticeService;

    /**
     * 退款回调统一处理
     */
    public void refundCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:refund:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取退款单
            RefundOrder refundOrder = refundOrderManager.findByRefundNo(callbackInfo.getTradeNo()).orElse(null);
            // 退款单不存在,记录回调记录
            if (Objects.isNull(refundOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg("退款单不存在,记录回调记录");
                return;
            }
            // 退款单已经被处理, 记录回调记录
            if (!Objects.equals(RefundStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("退款单状态已处理,记录回调记录");
                return;
            }

            // 退款成功
            if (Objects.equals(RefundStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                this.success(refundOrder);
            }
            // 退款失败
            if (Objects.equals(RefundStatusEnum.FAIL.getCode(), callbackInfo.getTradeStatus())){
                this.close(refundOrder);
            }
            // 退款异常
            if (Objects.equals(RefundStatusEnum.CLOSE.getCode(), callbackInfo.getTradeStatus())){
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
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));

        // 订单相关状态
        PayRefundStatusEnum payRefundStatusEnum;

        // 判断订单全部退款还是部分退款
        if (BigDecimalUtil.isEqual(payOrder.getRefundableBalance(), BigDecimal.ZERO)) {
            payRefundStatusEnum = PayRefundStatusEnum.REFUNDED;
        } else {
            payRefundStatusEnum = PayRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setFinishTime(callbackInfo.getFinishTime());
        payOrder.setRefundStatus(payRefundStatusEnum.getCode());

        // 更新订单和退款相关订单
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);

        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
    }


    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    private void close(RefundOrder refundOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();

        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));
        // 退款失败返还后的余额
        var payOrderAmount = refundOrder.getAmount().add(payOrder.getRefundableBalance());
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为支付成功状态
        if (BigDecimalUtil.isEqual(payOrderAmount, payOrder.getAmount())) {
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode());
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode())
                .setErrorMsg(callbackInfo.getTradeErrorMsg());

        // 更新订单和退款相关订单
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
    }
}
