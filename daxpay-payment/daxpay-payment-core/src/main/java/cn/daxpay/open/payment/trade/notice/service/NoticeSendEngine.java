package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.trade.notice.dao.MchNoticeRecordManager;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.protocol.NoticeProtocolSender;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSendTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 商户出站通知发送引擎
///
/// 唯一负责：选 Sender、写流水、更新任务、排期重试
@Slf4j
@Service
public class NoticeSendEngine {

    private final MchNoticeTaskManager taskManager;
    private final MchNoticeRecordManager recordManager;
    private final NoticeRetryPolicy retryPolicy;
    private final NoticeTaskScheduleService scheduleService;
    private final Map<String, NoticeProtocolSender> senderMap;

    public NoticeSendEngine(MchNoticeTaskManager taskManager,
                            MchNoticeRecordManager recordManager,
                            NoticeRetryPolicy retryPolicy,
                            NoticeTaskScheduleService scheduleService,
                            List<NoticeProtocolSender> senders) {
        this.taskManager = taskManager;
        this.recordManager = recordManager;
        this.retryPolicy = retryPolicy;
        this.scheduleService = scheduleService;
        this.senderMap = senders.stream()
                .collect(Collectors.toMap(NoticeProtocolSender::protocol, Function.identity(), (a, b) -> a));
    }

    /// 自动发送（消费端入口）
    public void sendAuto(Long taskId) {
        send(taskId, true);
    }

    /// 手动重发
    public void sendManual(Long taskId) {
        send(taskId, false);
    }

    /// 执行发送
    public void send(Long taskId, boolean autoSend) {
        MchNoticeTask task = taskManager.findById(taskId).orElse(null);
        if (task == null) {
            log.warn("出站通知任务不存在: taskId={}", taskId);
            return;
        }
        if (task.isSuccess()) {
            log.info("出站通知任务已成功, 跳过: taskId={}", taskId);
            return;
        }
        NoticeProtocolSender sender = senderMap.get(task.getProtocol());
        OffsetDateTime sendTime = OffsetDateTime.now(ZoneOffset.UTC);
        int reqCount = (task.getSendCount() == null ? 0 : task.getSendCount()) + 1;
        MchNoticeRecord record = new MchNoticeRecord();
        // 运营端写 MchBaseEntity 必须显式 mchNo
        record.setMchNo(task.getMchNo());
        record.setTaskId(task.getId())
                .setReqCount(reqCount)
                .setSendType(autoSend ? NoticeSendTypeEnum.AUTO.getCode() : NoticeSendTypeEnum.MANUAL.getCode());

        if (sender == null) {
            log.error("未找到通知协议 Sender: protocol={}, taskId={}", task.getProtocol(), taskId);
            record.setSuccess(false).setErrorMsg("protocol sender not found: " + task.getProtocol());
            failUpdate(task, sendTime, autoSend, record);
            return;
        }

        NoticeProtocolSender.NoticeSendResult sendResult;
        try {
            sendResult = sender.send(task);
        } catch (Exception e) {
            log.error("出站通知 Sender 异常: taskId={}", taskId, e);
            record.setSuccess(false).setErrorMsg(e.getMessage());
            failUpdate(task, sendTime, autoSend, record);
            return;
        }

        record.setHttpStatus(sendResult.getHttpStatus())
                .setRequestDigest(sendResult.getRequestDigest());
        if (sendResult.isSuccess()) {
            task.setSendCount(reqCount)
                    .setLatestTime(sendTime)
                    .setSuccess(true)
                    .setErrorMsg(null)
                    .setNextTime(null);
            if (autoSend && task.getDelayCount() != null) {
                // 自动路径成功时同步累计 delay 计数（与商业版一致，便于观测）
                task.setDelayCount(task.getDelayCount());
            }
            record.setSuccess(true);
            taskManager.updateById(task);
            recordManager.save(record);
            return;
        }

        record.setSuccess(false).setErrorMsg(sendResult.getErrorMsg());
        failUpdate(task, sendTime, autoSend, record);
    }

    /// 失败：更新任务并按需排期重试
    private void failUpdate(MchNoticeTask task, OffsetDateTime sendTime, boolean autoSend, MchNoticeRecord record) {
        int reqCount = record.getReqCount() == null ? 1 : record.getReqCount();
        task.setSendCount(reqCount).setLatestTime(sendTime);
        if (StrUtil.isNotBlank(record.getErrorMsg())) {
            task.setErrorMsg(record.getErrorMsg());
        }
        if (autoSend && !task.isSuccess()) {
            int delayCount = task.getDelayCount() == null ? 0 : task.getDelayCount();
            if (retryPolicy.canRetry(delayCount)) {
                int next = delayCount + 1;
                task.setDelayCount(next);
                int delaySeconds = retryPolicy.nextDelaySeconds(next);
                task.setNextTime(sendTime.plusSeconds(delaySeconds));
                taskManager.updateById(task);
                recordManager.save(record);
                scheduleService.scheduleRetry(task, delaySeconds);
                return;
            }
        }
        taskManager.updateById(task);
        recordManager.save(record);
    }
}
