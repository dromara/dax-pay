package cn.daxpay.multi.service.service.notice;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.service.notice.callback.MerchantCallbackTaskService;
import cn.daxpay.multi.service.service.notice.notify.MerchantNotifyTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    public void registerPayNotice(PayOrder order) {
        boolean isTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransaction) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    merchantNotifyService.registerPayNotice(order);
                    merchantCallbackService.registerPayNotice(order);
                }
            });
        } else {
            merchantNotifyService.registerPayNotice(order);
            merchantCallbackService.registerPayNotice(order);
        }
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        boolean isTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransaction) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    merchantNotifyService.registerRefundNotice(order);
                    merchantCallbackService.registerRefundNotice(order);
                }
            });
        } else {
            merchantNotifyService.registerRefundNotice(order);
            merchantCallbackService.registerRefundNotice(order);
        }
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        boolean isTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransaction) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    merchantNotifyService.registerTransferNotice(order);
                    merchantCallbackService.registerTransferNotice(order);
                }
            });
        } else {
            merchantNotifyService.registerTransferNotice(order);
            merchantCallbackService.registerTransferNotice(order);
        }
        merchantNotifyService.registerTransferNotice(order);
        merchantCallbackService.registerTransferNotice(order);
    }
}
