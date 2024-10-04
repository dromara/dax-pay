package org.dromara.daxpay.service.service.notice;

import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.service.notice.callback.MerchantCallbackTaskService;
import org.dromara.daxpay.service.service.notice.notify.MerchantNotifyTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户通知服务
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeService {
    private final MerchantNotifyTaskService merchantNotifyService;

    private final MerchantCallbackTaskService merchantCallbackService;

    /**
     * 注册支付通知, 在事务执行成功后创建
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerPayNotice(PayOrder order) {
        merchantNotifyService.registerPayNotice(order);
        merchantCallbackService.registerPayNotice(order);
    }

    /**
     * 注册退款通知
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerRefundNotice(RefundOrder order) {
        merchantNotifyService.registerRefundNotice(order);
        merchantCallbackService.registerRefundNotice(order);
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        merchantNotifyService.registerTransferNotice(order);
        merchantCallbackService.registerTransferNotice(order);
    }
}
