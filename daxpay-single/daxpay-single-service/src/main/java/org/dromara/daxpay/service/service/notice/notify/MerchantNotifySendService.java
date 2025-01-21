package org.dromara.daxpay.service.service.notice.notify;

import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.bootx.platform.core.util.JsonUtil;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyRecordManager;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyTaskManager;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyRecord;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyTask;
import org.dromara.daxpay.service.enums.NoticeSendTypeEnum;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.MerchantNotifyConfigService;
import org.dromara.daxpay.service.service.notice.MerchantNoticeAssistService;
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
 * 商户订阅消息通知发送服务类
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifySendService {

    private final MerchantNoticeAssistService merchantNoticeAssistService;

    private final MerchantNotifyRecordManager recordManager;

    private final MerchantNotifyTaskManager taskManager;

    private final PaymentAssistService paymentAssistService;

    private final DelayJobService delayJobService;

    private final MerchantNotifyConfigService notifyConfigService;


    /**
     * 发送通知数据, 如果失败, 注册下次重发的任务
     * @param task 发送任务
     * @param sendTime 发送时间
     * @param url 消息接收地址
     * @param autoSend 是否为自动发送
     */
    public void sendData(MerchantNotifyTask task, String url, LocalDateTime sendTime, boolean autoSend){
        // 创建发送记录
        MerchantNotifyRecord record = new MerchantNotifyRecord()
                .setTaskId(task.getId())
                .setSendType(autoSend?NoticeSendTypeEnum.AUTO.getType():NoticeSendTypeEnum.MANUAL.getType())
                .setReqCount(task.getSendCount()+1);
        String body = null;
        try {
            // 构造通知消息并签名
            DaxResult<Map<String, Object>> daxResult = DaxRes.ok(JsonUtil.parseObj(task.getContent()));
            paymentAssistService.sign(daxResult);

            HttpResponse execute = HttpUtil.createPost(url)
                    .body(JsonUtil.toJsonStr(daxResult), ContentType.JSON.getValue())
                    .timeout(5000)
                    .execute();
            body = execute.body();
        } catch (Exception e) {
            log.error("发送通知失败，数据错误，任务ID：{}",task.getTradeId(),e);
            record.setErrorMsg(e.getMessage());
        }
        // 如果响应值等于SUCCESS, 说明发送成功, 进行成功处理
        if (StrUtil.equalsIgnoreCase(body, "SUCCESS")){
            record.setSuccess(true);
            task.setSendCount(task.getSendCount() + 1)
                    .setDelayCount(0)
                    .setLatestTime(sendTime)
                    .setSuccess(true);
            // 如果为自动发送且延迟次数, 延迟次数也+1
            if (autoSend && Objects.nonNull(task.getDelayCount())){
                task.setDelayCount(task.getDelayCount()+1);
            }
        } else {
            // 失败处理
            this.failHandler(task,sendTime,autoSend);
            // 如果响应地址不为空, 将错误响应写到记录中
            if (Objects.nonNull(body)){
                // 预防返回值过长, 只保留300位
                record.setErrorMsg(StrUtil.sub(body,0,300));
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
    private void failHandler(MerchantNotifyTask task, LocalDateTime sendTime, boolean autoSend){
        // 为空不进行处理
        if (Objects.isNull(task)){
            return;
        }
        // 发送次数+1
        task.setSendCount(task.getSendCount() + 1).setLatestTime(sendTime);
        // 任务完成了也不进行处理
        if (task.isSuccess()){
            return;
        }
        // 如果延迟次数为空, 先设置为-1
        if (autoSend && Objects.isNull(task.getDelayCount())){
            task.setDelayCount(0);
        }
        // 判断延迟次数是否未超过15次, 注册任务到redis中
        if (autoSend && task.getDelayCount() < 16){
            // 添加延迟次数
            task.setDelayCount(task.getDelayCount() + 1);
            // 下次偏移毫秒数
            int delay = merchantNoticeAssistService.getDelayTime(task.getDelayCount());
            // 根据当前延迟次数和计算出下次执行时间
            task.setNextTime(sendTime.plusSeconds(delay/1000));
            // 注册延时任务
            delayJobService.register(task.getId(), DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, delay);
        }
    }

    /**
     * 手动发送
     */
    public void send(Long taskId){
        var taskOpt = taskManager.findById(taskId);
        if (taskOpt.isPresent()){
            var task = taskOpt.get();
            paymentAssistService.initMchApp(task.getAppId());
            MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
            // 判断通知方式是否为http并且订阅了该类型的通知
            boolean subscribe = notifyConfigService.getSubscribeByAppIdAndType(mchAppInfo.getAppId(), task.getNotifyType());
            if (Objects.equals(mchAppInfo.getNotifyType(), MerchantNotifyTypeEnum.HTTP.getCode()) && subscribe){
                this.sendData(task, mchAppInfo.getNotifyUrl(), LocalDateTime.now(), false);
            } else {
                log.info("商户消息通知未开启，任务ID：{}",taskId);
            }
        } else {
            log.error("发送任务不存在，任务ID：{}",taskId);
        }
    }
}
