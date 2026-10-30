package cn.daxpay.open.device.audio.service;

import cn.daxpay.open.device.comm.DeviceArtemisConstants;
import cn.daxpay.open.device.comm.DevicePaySuccessMessage;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.notice.util.NoticeTxUtil;
import cn.daxpay.open.payment.strategy.device.PaySuccessDeviceHook;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # 设备通知服务(支付成功旁路生产者: 播报 + 小票)
///
/// [PaySuccessDeviceHook] SPI 的设备域实现(A 波接口, 本类为唯一实现):
/// 唯一挂点由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService#afterSuccess] 调用,
/// 事务提交后仅投递一条 Artemis 消息(只带 tradeNo), 查库/渲染/推送全部在
/// [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer] 消费侧执行。
/// 旁路语义: 不建通知任务表; 投递失败(broker 故障)记 ERROR 丢弃一条, 宁丢不反压支付主链路;
/// broker 故障期间消息在队列堆积、恢复后自动补推, 重复投递由 APP 端 msgId 去重承接。
/// 事件范围 V1 仅普通支付与网关支付成功(退款/关闭不播)。
///
/// 移植自 plus `AudioBroadcastService`(快照 773134af), 无业务改动。
@Slf4j
@RequiredArgsConstructor
@Service
public class AudioBroadcastService implements PaySuccessDeviceHook {

    /// 触发设备通知的交易形态(V1 仅普通支付与网关支付)
    private static final Set<String> NOTIFY_TRADE_TYPES = Set.of(
            PayTradeTypeEnum.NORMAL.getCode(), PayTradeTypeEnum.GATEWAY.getCode());

    private final ArtemisTemplateService artemisTemplateService;

    /// 支付成功后投递设备通知消息(事务提交后发送, 消费侧完成查库与推送)
    @Override
    public void broadcast(PayTrade trade) {
        if (!NOTIFY_TRADE_TYPES.contains(trade.getTradeType())) {
            return;
        }
        NoticeTxUtil.afterCommit(() -> {
            try {
                artemisTemplateService.send(DeviceArtemisConstants.PAY_SUCCESS_QUEUE,
                        JacksonUtil.toJson(new DevicePaySuccessMessage().setTradeNo(trade.getTradeNo())));
            } catch (Exception e) {
                // 设备: 通知消息投递失败(broker 故障), 丢弃本次推送
                log.error("设备通知消息投递失败, 丢弃: tradeNo={}", trade.getTradeNo(), e);
            }
        });
    }
}
