package org.dromara.daxpay.service.event;

import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.notice.callback.MerchantCallbackTaskManager;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyTaskManager;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.MerchantNotifyConfigService;
import org.dromara.daxpay.service.service.notice.callback.MerchantCallbackSendService;
import org.dromara.daxpay.service.service.notice.notify.MerchantNotifySendService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商户通知事件服务类
 * @author xxm
 * @since 2024/8/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeEventService {

    private final MerchantNotifySendService merchantNotifySendService;
    private final MerchantCallbackSendService merchantCallbackSendService;
    private final MerchantNotifyTaskManager merchantNotifyTaskManager;
    private final MerchantCallbackTaskManager merchantCallbackTaskManager;
    private final PaymentAssistService paymentAssistService;
    private final MerchantNotifyConfigService merchantNotifyConfigService;

    /**
     * 接受商户通知发送任务的延时消息
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_NOTIFY_SENDER)
    public void NotifyTaskReceiveJob(DelayJobEvent<Long> event){
        // 获取任务
        Long taskId = event.getMessage();
        var taskOpt = merchantNotifyTaskManager.findById(taskId);
        if (taskOpt.isPresent()){
            var task = taskOpt.get();
            paymentAssistService.initMchApp(task.getAppId());
            MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
            // 判断通知方式是否为http并且订阅了该类型的通知
            boolean subscribe = merchantNotifyConfigService.getSubscribeByAppIdAndType(mchAppInfo.getAppId(), task.getNotifyType());
            if (Objects.equals(mchAppInfo.getNotifyType(), MerchantNotifyTypeEnum.HTTP.getCode()) && subscribe){
                merchantNotifySendService.sendData(task, mchAppInfo.getNotifyUrl(), LocalDateTime.now(), true);
            } else {
                log.info("商户消息通知未开启，任务ID：{}",taskId);
            }
        } else {
            log.error("商户消息通知发送任务不存在，任务ID：{}",taskId);
        }
    }


    /**
     * 接受商户回调消息发送任务的延时消息
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_CALLBACK_SENDER)
    public void callbackReceiveJob(DelayJobEvent<Long> event){
        // 获取任务
        Long taskId = event.getMessage();
        var taskOpt = merchantCallbackTaskManager.findById(taskId);
        if (taskOpt.isPresent()){
            var task = taskOpt.get();
            paymentAssistService.initMchApp(task.getAppId());
            merchantCallbackSendService.sendData(task,true);
        } else {
            log.error("商户回调发送任务不存在，任务ID：{}",taskId);
        }
    }

}
