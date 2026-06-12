package org.dromara.daxpay.payment.pay.service.trade;

import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.util.BigDecimalUtil;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayRefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.payment.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.payment.pay.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.payment.pay.service.assist.PayPluginAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/// # 交易统一处理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeUniHandleService {

    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;
    private final TradeFlowRecordService tradeFlowRecordService;
    private final MerchantNoticeService merchantNoticeService;
    private final RefundOrderManager refundOrderManager;
    private final PayPluginAssistService payPluginAssistService;

    /// 支付成功发起后处理
    public void payAfterHandel(PayOrder payOrder, PayOrderExpand orderExpand){
        // 订单更新
        payOrderManager.updateById(payOrder);
        payOrderExpandManager.updateById(orderExpand);
        // 如果支付完成进行统一处理相关逻辑
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            // 相关操作
            tradeFlowRecordService.savePay(payOrder);
            merchantNoticeService.registerPayNotice(payOrder);
            // 处理插件策略
            payPluginAssistService.paySuccess(payOrder, orderExpand);
        }

    }

    /// 支付失败处理
    @IgnoreTenant
    public void payFail(PayOrder payOrder, String errMsg){
        payOrder.setStatus(PayStatusEnum.FAIL.getCode())
                .setErrorMsg(errMsg)
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(payOrder);
        merchantNoticeService.registerPayNotice(payOrder);
        // 处理插件策略
        payPluginAssistService.payFail(payOrder);
    }

    /// 支付关闭处理
    @IgnoreTenant
    public void payClose(PayOrder order, PayStatusEnum payStatusEnum){
        order.setStatus(payStatusEnum.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(order);
        // 发送通知
        merchantNoticeService.registerPayNotice(order);
        // 处理插件策略
        payPluginAssistService.payClose(order);
    }

    /// 退款成功处理
    public void refundSuccess(PayOrder payOrder,RefundOrder refundOrder){
        // 判断订单全部退款还是部分退款
        if (BigDecimalUtil.isEqual(payOrder.getRefundableBalance(), BigDecimal.ZERO)) {
            payOrder.setRefundStatus(PayRefundStatusEnum.REFUNDED.getCode());
        } else {
            payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
        }
        // 更新状态
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                        .setErrorMsg(null);
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
        // 处理插件策略
        payPluginAssistService.refundSuccess(payOrder, refundOrder);
    }

    /// 退款关闭处理
    public void refundClose(PayOrder payOrder, RefundOrder refundOrder){
        // 退款失败返还后的余额
        var payOrderAmount =  refundOrder
                .getAmount()
                .add(payOrder.getRefundableBalance());
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为未退款状态
        if (BigDecimalUtil.isEqual(payOrderAmount, payOrder.getAmount())) {
            payOrder.setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode());
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode());

        // 更新订单和退款相关订单
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
        // 处理插件策略
        payPluginAssistService.refundClose(payOrder, refundOrder);
    }

}
