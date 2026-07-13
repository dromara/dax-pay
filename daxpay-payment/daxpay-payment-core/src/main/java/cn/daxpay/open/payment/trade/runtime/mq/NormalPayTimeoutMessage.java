package cn.daxpay.open.payment.trade.runtime.mq;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 普通支付超时关单消息体
///
/// 下单时由 [PayAssistService] 通过
/// `ArtemisTemplateService.sendDelayAt(...)` 按订单过期时间定时投递到
/// [PayArtemisConstants#NORMAL_TIMEOUT_QUEUE]，到期后由
/// [NormalPayTimeoutConsumer] 消费触发自动关单。
///
/// 消费端 **只信任 tradeNo 重新查库做幂等校验**，不信任消息体里的状态字段，
/// 防止延时消息重复投递或多路径并发触发导致重复关单。
@Data
@Accessors(chain = true)
public class NormalPayTimeoutMessage {

    /// 平台支付交易号(对应 pay_trade.trade_no)
    private String tradeNo;

    /// 商户业务单号(仅用于日志追踪，不参与业务定位)
    private String bizOrderNo;
}
