package org.dromara.daxpay.payment.old.pay.event;

import org.dromara.daxpay.platform.core.code.DaxPayCode;
import org.dromara.daxpay.payment.old.pay.dao.notice.callback.MerchantCallbackTaskManager;
import org.dromara.daxpay.payment.old.pay.service.notice.callback.MerchantCallbackSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户通知事件服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeEventService {

    private final MerchantCallbackSendService merchantCallbackSendService;
    private final MerchantCallbackTaskManager merchantCallbackTaskManager;

//    /// //     * 接受商户回调消息发送任务的延时消息
/// //
//    @DelayEventListener(DaxPayCode.Event.MERCHANT_CALLBACK_SENDER)
//    public void callbackReceiveJob(DelayJobEvent<Long> event){
//        // 获取任务
//        Long taskId = event.getMessage();
//        log.info("商户回调处理，任务ID：{}",taskId);
//        var taskOpt = merchantCallbackTaskManager.findByIdNotTenant(taskId);
//        if (taskOpt.isPresent()){
//            var task = taskOpt.get();
//            merchantCallbackSendService.sendData(task,true);
//        } else {
//            log.error("商户回调发送任务不存在，任务ID：{}",taskId);
//        }
//    }

}
