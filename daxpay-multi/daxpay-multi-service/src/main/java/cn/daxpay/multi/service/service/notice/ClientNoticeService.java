package cn.daxpay.multi.service.service.notice;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.service.notice.callback.ClientCallbackService;
import cn.daxpay.multi.service.service.notice.notify.ClientNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 客户通知服务
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeService {
    private final ClientNotifyService clientNotifyService;

    private final ClientCallbackService clientCallbackService;

    /**
     * 注册支付通知
     */
    public void registerPayNotice(PayOrder order) {
        clientNotifyService.registerPayNotice(order);
        clientCallbackService.registerPayNotice(order);
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        clientNotifyService.registerRefundNotice(order);
        clientCallbackService.registerRefundNotice(order);
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        clientNotifyService.registerTransferNotice(order);
        clientCallbackService.registerTransferNotice(order);
    }
}
