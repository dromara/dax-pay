package cn.bootx.platform.daxpay.service.core.payment.callback.service;

import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付退款回调
 * @author xxm
 * @since 2024/1/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundCallbackService {

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    /**
     * 退款回调统一处理
     */
    public void refundCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:callback:" + callbackInfo.getOrderId());
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getOrderId());
            return;
        }

        try {

        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
