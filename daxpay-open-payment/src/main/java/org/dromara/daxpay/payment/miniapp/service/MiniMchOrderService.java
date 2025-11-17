package org.dromara.daxpay.payment.miniapp.service;

import org.dromara.daxpay.payment.pay.param.order.refund.RefundCreateParam;
import org.dromara.daxpay.payment.pay.service.order.pay.PayOrderService;
import org.dromara.daxpay.payment.pay.service.order.refund.RefundOrderService;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 小程序订单操作
 * @author xxm
 * @since 2025/4/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchOrderService {
    private final PayOrderService payOrderService;
    private final RefundOrderService refundOrderService;

    /**
     * 同步支付订单状态
     */
    public void syncPayOrder(Long id) {
        payOrderService.sync(id);
    }

    /**
     * 关闭支付订单
     */
    public void closePayOrder(Long id) {
        payOrderService.close(id);
    }

    /**
     * 撤销支付订单
     */
    public void cancelPayOrder(Long id) {
        payOrderService.cancel(id);
    }

    /**
     * 创建退款订单
     */
    public RefundResult createRefundOrder(RefundCreateParam param) {
        return refundOrderService.create(param);
    }

    /**
     * 同步退款订单状态
     */
    public void syncRefundOrder(Long id) {
        refundOrderService.sync(id);
    }

    /**
     * 退款重试
     */
    public void retryRefundOrder(Long id) {
        refundOrderService.retry(id);
    }

    /**
     * 退款关闭
     */
    public void closeRefundOrder(Long id) {
        refundOrderService.close(id);
    }


}
