package cn.daxpay.multi.service.service.notice;

import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.hutool.core.util.StrUtil;
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

    /**
     * 注册支付通知
     */
    public void registerPayNotice(PayOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("支付订单无需通知，订单号：{}",order.getOrderNo());
            return;
        }
        log.info("注册支付通知");
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("支付退款无需通知，订单号：{}",order.getRefundNo());
            return;
        }
        log.info("注册退款通知");
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("转账订单无需通知，订单号：{}",order.getTransferNo());
            return;
        }
        log.info("注册转账通知");
    }
}
