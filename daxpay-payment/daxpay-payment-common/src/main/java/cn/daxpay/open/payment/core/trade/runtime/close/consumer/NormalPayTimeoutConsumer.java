package cn.daxpay.open.payment.core.trade.runtime.close.consumer;

import cn.daxpay.open.payment.core.trade.runtime.close.service.PayCloseService;
import cn.daxpay.open.payment.core.trade.runtime.mq.NormalPayTimeoutMessage;
import cn.daxpay.open.payment.core.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/// # 普通支付超时关单消费者
///
/// 监听 [PayArtemisConstants#NORMAL_TIMEOUT_QUEUE]，消费由下单时
/// `sendDelayAt(...)` 按订单过期时间定时投递的超时关单消息。
///
/// 幂等保障：[PayCloseService#closeForTimeout] 内部基于资金状态前置校验 +
/// Redis 分布式锁保证同一订单只会被关闭一次，消费端无需额外去重。
///
/// 异常处理：消费失败只记 error 日志，不向上抛出，避免触发 JMS 默认重试风暴。
/// 漏单由 [NormalPayTimeoutJob] 兜底定时扫描补救。
@Slf4j
@Component
@RequiredArgsConstructor
public class NormalPayTimeoutConsumer {

    private final PayCloseService payCloseService;

    /// 消费超时关单消息
    ///
    /// @param json 消息体 JSON，见 [NormalPayTimeoutMessage]
    @JmsListener(destination = PayArtemisConstants.NORMAL_TIMEOUT_QUEUE)
    public void onMessage(String json) {
        NormalPayTimeoutMessage message;
        try {
            // 统一 Text 传输，消费端手动反序列化为目标类型
            message = JacksonUtil.toBean(json, NormalPayTimeoutMessage.class);
        } catch (Exception e) {
            // 消息体解析失败属于毒消息，直接丢弃避免无限重试
            log.warn("超时关单消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        String tradeNo = message.getTradeNo();
        try {
            payCloseService.closeForTimeout(tradeNo);
        } catch (Exception e) {
            // 不向上抛出，避免 JMS 重试风暴；漏单由定时任务兜底
            log.error("超时关单处理失败, tradeNo={}, bizOrderNo={}",
                    tradeNo, message.getBizOrderNo(), e);
        }
    }
}
