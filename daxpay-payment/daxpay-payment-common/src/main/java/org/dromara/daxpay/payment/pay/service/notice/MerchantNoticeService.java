package org.dromara.daxpay.payment.pay.service.notice;

import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.service.notice.callback.MerchantCallbackTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 客户通知服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeService {

    private final MerchantCallbackTaskService merchantCallbackService;

    /// 注册支付通知, 在事务执行成功后创建
    @Transactional(rollbackFor = Exception.class)
    public void registerPayNotice(PayOrder order) {
        merchantCallbackService.registerPayNotice(order);
    }

    /// 注册退款通知
    @Transactional(rollbackFor = Exception.class)
    public void registerRefundNotice(RefundOrder order) {
        merchantCallbackService.registerRefundNotice(order);
    }

    /// 注册转账通知
    @Transactional(rollbackFor = Exception.class)
    public void registerTransferNotice(TransferOrder order) {
        merchantCallbackService.registerTransferNotice(order);
    }
}
