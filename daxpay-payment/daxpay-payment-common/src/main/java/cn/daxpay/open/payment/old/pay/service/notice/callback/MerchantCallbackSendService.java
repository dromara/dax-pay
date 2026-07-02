package cn.daxpay.open.payment.old.pay.service.notice.callback;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.daxpay.open.platform.core.code.DaxPayCode;
import cn.daxpay.open.payment.common.result.DaxNoticeResult;
import cn.daxpay.open.payment.common.util.JsonSignStrUtil;
import cn.daxpay.open.payment.old.pay.dao.notice.callback.MerchantCallbackRecordManager;
import cn.daxpay.open.payment.old.pay.dao.notice.callback.MerchantCallbackTaskManager;
import cn.daxpay.open.payment.old.pay.entity.notice.callback.MerchantCallbackRecord;
import cn.daxpay.open.payment.old.pay.entity.notice.callback.MerchantCallbackTask;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSendTypeEnum;
import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.common.service.PaySignService;
import cn.daxpay.open.payment.old.pay.service.notice.MerchantNoticeAssistService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static cn.daxpay.open.platform.core.code.CommonCode.SUCCESS_CODE;
import static cn.daxpay.open.platform.core.code.CommonCode.SUCCESS_MSG;

/// # 客户回调信息发送服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackSendService {
    private final MerchantNoticeAssistService merchantNoticeAssistService;

    private final MerchantCallbackRecordManager recordManager;

    private final MerchantCallbackTaskManager taskManager;

    private final MerchantContextLoader merchantContextLoader;

    private final PaySignService paySignService;

//    private final DelayJobService delayJobService;

    /// 发送通知数据, 如果失败, 注册下次重发的任务
    public void sendData(MerchantCallbackTask task, boolean autoSend){
        this.sendDataBySystem(task, autoSend);
    }

    /// 发送通知数据, 如果失败, 注册下次重发的任务
    /// @param task 发送任务
    /// @param autoSend 是否为自动发送
    public void sendDataBySystem(MerchantCallbackTask task, boolean autoSend){
        merchantContextLoader.initMch(task.getMchNo());
        OffsetDateTime sendTime = OffsetDateTime.now(ZoneOffset.UTC);
        // 创建发送记录
        MerchantCallbackRecord record = new MerchantCallbackRecord()
                .setTaskId(task.getId())
                .setSendType(autoSend?NoticeSendTypeEnum.AUTO.getCode():NoticeSendTypeEnum.MANUAL.getCode())
                .setReqCount(task.getSendCount()+1);
        String body = null;
        try {
            // 构造通知消息并签名
            var daxResult = new DaxNoticeResult<>(SUCCESS_CODE, JsonSignStrUtil.buildSortedMap(task.getContent()), SUCCESS_MSG)
                    .setMchNo(task.getMchNo())
                    .setAppId(task.getAppId());
            // 设置响应时间并签名
            daxResult.setResTime(OffsetDateTime.now(ZoneOffset.UTC));
            paySignService.sign(daxResult);
            HttpResponse execute = HttpUtil.createPost(task.getUrl())
                    .body(JsonUtil.toJsonStr(daxResult), ContentType.JSON.getValue())
                    .timeout(15000)
                    .execute();
            body = execute.body();
        } catch (Exception e) {
            log.error("发送通知失败，数据错误，任务ID：{}",task.getTradeId(),e);
            record.setErrorMsg(e.getMessage());
        }
        // 如果响应值等于SUCCESS, 说明发送成功, 进行成功处理
        if (StrUtil.equalsIgnoreCase(body, "SUCCESS")){
            task.setSendCount(task.getSendCount() + 1)
                    .setLatestTime(sendTime)
                    .setSuccess(true);
            record.setSuccess(true);
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
        // 更新任务信息
        taskManager.updateById(task);
        // 保存请求记录更新任务
        recordManager.save(record);
    }

    /// 失败处理, 首先发送次数+1, 然后注册后推指定时间的重试任务
    private void failHandler(MerchantCallbackTask task, OffsetDateTime sendTime, boolean autoSend){
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
        // 如果延迟次数为空, 先设置为-1
        if (autoSend && Objects.isNull(task.getDelayCount())){
            task.setDelayCount(-1);
        }
        // 判断延迟次数是否未超过15次, 注册任务到redis中
        if (autoSend && task.getDelayCount() < 16){
            // 添加延迟次数
            task.setDelayCount(task.getDelayCount() + 1);
            // 下次偏移毫秒数
            int delay = merchantNoticeAssistService.getDelayTime(task.getDelayCount()+1);
            // 根据当前延迟次数和计算出下次执行时间
            task.setNextTime(sendTime.plusSeconds(delay/1000L));
            // 注册延时任务
//            delayJobService.register(task.getId(), DaxPayCode.Event.MERCHANT_CALLBACK_SENDER, delay);
        }
    }

    /// 手动发送
    public void send(Long taskId){
        var taskOpt = taskManager.findById(taskId);
        if (taskOpt.isPresent()){
            var task = taskOpt.get();
            merchantContextLoader.initMch(task.getMchNo());
            this.sendData(task,false);
        } else {
            log.error("发送任务不存在，任务ID：{}",taskId);
        }
    }
}

