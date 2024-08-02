package cn.daxpay.multi.service.service.notice.callback;

import cn.bootx.platform.common.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.common.redis.delay.annotation.DelayJobEvent;
import cn.bootx.platform.common.redis.delay.service.DelayJobService;
import cn.bootx.platform.core.util.JsonUtil;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.dao.notice.callback.MerchantCallbackRecordManager;
import cn.daxpay.multi.service.dao.notice.callback.MerchantCallbackTaskManager;
import cn.daxpay.multi.service.entity.notice.callback.MerchantCallbackRecord;
import cn.daxpay.multi.service.entity.notice.callback.MerchantCallbackTask;
import cn.daxpay.multi.service.enums.SendTypeEnum;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.notice.MerchantNoticeAssistService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 客户回调信息发送服务类
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackSendService {
    private final MerchantNoticeAssistService merchantNoticeAssistService;

    private final MerchantCallbackRecordManager recordManager;

    private final MerchantCallbackTaskManager taskManager;

    private final PaymentAssistService paymentAssistService;
    private final DelayJobService delayJobService;


    /**
     * 发送通知数据, 如果失败, 注册下次重发的任务
     * @param task 发送任务
     * @param autoSend 是否为自动发送
     */
    public void sendData(MerchantCallbackTask task, boolean autoSend){
        LocalDateTime sendTime = LocalDateTime.now();
        // 创建发送记录
        MerchantCallbackRecord record = new MerchantCallbackRecord()
                .setTaskId(task.getId())
                .setSendType(SendTypeEnum.AUTO.getType())
                .setReqCount(task.getSendCount()+1);
        String body = null;
        try {
            // 构造通知消息并签名
            DaxResult<Map<String, Object>> daxResult = DaxRes.ok(JsonUtil.parseObj(task.getContent()));
            paymentAssistService.sign(daxResult);

            HttpResponse execute = HttpUtil.createPost(task.getUrl())
                    .body(JsonUtil.toJsonStr(daxResult), ContentType.JSON.getValue())
                    .timeout(5000)
                    .execute();
            body = execute.body();
        } catch (Exception e) {
            log.error("发送通知失败，数据错误，任务ID：{}",task.getTradeId(),e);
            record.setErrorMsg(e.getMessage());
        }
        // 如果响应值等于SUCCESS, 说明发送成功, 进行成功处理
        if (Objects.equals(body, "SUCCESS")){
            task.setSendCount(task.getSendCount() + 1)
                    .setLatestTime(sendTime)
                    .setSuccess(true);;
            record.setSuccess(true);
        } else {
            // 失败处理
            this.failHandler(task,sendTime,autoSend);
            // 如果响应地址不为空, 将错误响应写到记录中
            if (Objects.nonNull(body)){
                // 预防返回值过长, 只保留200位
                record.setErrorMsg(StrUtil.sub(body,0,200));
            }
        }
        // 保存请求记录更新任务
        recordManager.save(record);
        // 更新任务信息
        taskManager.updateById(task);
    }

    /**
     * 失败处理, 首先发送次数+1, 然后注册后推指定时间的重试任务
     */
    private void failHandler(MerchantCallbackTask task, LocalDateTime sendTime, boolean autoSend){
        // 为空不进行处理
        if (Objects.isNull(task)){
            return;
        }
        // 次数+1
        task.setSendCount(task.getSendCount() + 1).setLatestTime(sendTime);
        // 任务完成了也不进行处理
        if (task.isSuccess()){
            return;
        }

        // 判断延迟次数是否未超过15次, 注册任务到redis中
        if (autoSend && task.getDelayCount() < 16){
            // 根据当前延迟次数和计算出下次执行时间
            task.setNextTime(sendTime.plusSeconds(merchantNoticeAssistService.getDelayTime(task.getDelayCount())));
            // 下次偏移毫秒数
            int delay = merchantNoticeAssistService.getDelayTime(task.getDelayCount());
            // 注册延时任务
            delayJobService.register(task.getId(), DaxPayCode.Event.MERCHANT_CALLBACK_SENDER, delay);
        }
    }

    /**
     * 接受发送任务的延时消息
     */
    @DelayEventListener(DaxPayCode.Event.MERCHANT_CALLBACK_SENDER)
    public void receiveJob(DelayJobEvent<Long> event){
        // 获取任务
        Long taskId = event.getMessage();
        var taskOpt = taskManager.findById(taskId);
        if (taskOpt.isPresent()){
            var task = taskOpt.get();
            paymentAssistService.initMchAndApp(task.getMchNo(), task.getAppId());
            this.sendData(task,true);
        } else {
            log.error("发送任务不存在，任务ID：{}",taskId);
        }
    }
}
