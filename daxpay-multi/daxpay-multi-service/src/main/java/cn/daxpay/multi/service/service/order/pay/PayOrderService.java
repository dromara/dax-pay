package cn.daxpay.multi.service.service.order.pay;

import cn.bootx.platform.common.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.common.redis.delay.annotation.DelayJobEvent;
import cn.bootx.platform.common.redis.delay.service.DelayJobService;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 支付订单服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {
    private final PayOrderManager payOrderManager;
    private final DelayJobService delayJobService;
    private final PaySyncService paySyncService;
    private final PaymentAssistService paymentAssistService;


    /**
     * 同步
     */
    public void sync(PayOrderSyncParam payOrder) {
    }

    /**
     * 注册超时时间
     */
    public void register(PayOrder payOrder) {
        delayJobService.registerByTransaction(payOrder.getId(), DaxPayCode.Event.MERCHANT_PAY_TIMEOUT, payOrder.getExpiredTime());
    }

    /**
     * 接收订单超时事件
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_PAY_TIMEOUT)
    public void payExpired(DelayJobEvent<Long> event) {
        Optional<PayOrder> orderOpt = payOrderManager.findById(event.getMessage());
        if (orderOpt.isPresent()) {
            PayOrder payOrder = orderOpt.get();
            // 不是支付中不需要进行同步
            if (payOrder.getStatus().equals(PayStatusEnum.PROGRESS.getCode())|| payOrder.getStatus().equals(PayStatusEnum.TIMEOUT.getCode())) {
                paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
                paySyncService.syncPayOrder(payOrder);
            }
        }
    }
}
