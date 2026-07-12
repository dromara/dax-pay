package cn.daxpay.open.payment.core.trade.runtime.close.consumer;

import cn.daxpay.open.payment.core.trade.runtime.close.service.GatewayTimeoutService;
import cn.daxpay.open.payment.core.trade.runtime.mq.GatewayTimeoutMessage;
import cn.daxpay.open.payment.core.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 网关支付超时关单消费者
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayTimeoutConsumer {

    private final GatewayTimeoutService gatewayTimeoutService;

    @JmsListener(destination = PayArtemisConstants.GATEWAY_TIMEOUT_QUEUE)
    public void onMessage(String json) {
        GatewayTimeoutMessage message;
        try {
            message = JacksonUtil.toBean(json, GatewayTimeoutMessage.class);
        } catch (Exception e) {
            log.warn("网关超时关单消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        try {
            gatewayTimeoutService.closeForTimeout(message.getOrderNo());
        } catch (Exception e) {
            log.error("网关超时关单处理失败, orderNo={}, bizOrderNo={}",
                    message.getOrderNo(), message.getBizOrderNo(), e);
        }
    }
}
