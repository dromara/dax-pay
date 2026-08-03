package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeRecordManager;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.payment.trade.notice.payload.NoticePayloadBuilder;
import cn.daxpay.open.payment.trade.notice.transport.NoticeSendResult;
import cn.daxpay.open.payment.trade.notice.transport.NoticeTransportSender;
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
/// 唯一负责: 选 PayloadBuilder(按 format) 组装信封 → 选 TransportSender(按 transport) 投递 → 写流水 → 更新任务 → 排期重试
@Slf4j
@Service
public class NoticeSendEngine {

    private final MchNoticeTaskManager taskManager;
    private final MchNoticeRecordManager recordManager;
    private final NoticeRetryPolicy retryPolicy;
    private final NoticeTaskScheduleService scheduleService;
    private final PaymentContext paymentContext;
    private final Map<String, NoticePayloadBuilder> payloadBuilderMap;
    private final Map<String, NoticeTransportSender> transportSenderMap;

    public NoticeSendEngine(MchNoticeTaskManager taskManager,
                            MchNoticeRecordManager recordManager,
                            NoticeRetryPolicy retryPolicy,
                            NoticeTaskScheduleService scheduleService,
                            PaymentContext paymentContext,
                            List<NoticePayloadBuilder> payloadBuilders,
                            List<NoticeTransportSender> transportSenders) {
        this.taskManager = taskManager;
        this.recordManager = recordManager;
        this.retryPolicy = retryPolicy;
        this.scheduleService = scheduleService;
        this.paymentContext = paymentContext;
        this.payloadBuilderMap = payloadBuilders.stream()
                .collect(Collectors.toMap(NoticePayloadBuilder::format, Function.identity(), (a, b) -> {
                    log.warn("NoticePayloadBuilder format 冲突, 保留前者: {}", a.format());
                    return a;
                }));
        this.transportSenderMap = transportSenders.stream()
                .collect(Collectors.toMap(NoticeTransportSender::transport, Function.identity(), (a, b) -> {
                    log.warn("NoticeTransportSender transport 冲突, 保留前者: {}", a.transport());
                    return a;
                }));
    }

    /// 自动发送（消费端入口）
    ///
    /// MQ 线程无商户登录上下文，需先用 NotTenant 引导读任务获取 mchNo，
    /// 再装载 PaymentContext 后走租户内发送（与 PayCloseService#closeForTimeout 范式一致）。
    public void sendAuto(Long taskId) {
        MchNoticeTask boot = taskManager.findByIdNotTenant(taskId).orElse(null);
        if (boot == null) {
            log.warn("出站通知任务不存在: taskId={}", taskId);
            return;
        }
        if (StrUtil.isBlank(boot.getMchNo())) {
            log.error("出站通知任务缺少 mchNo, 跳过: taskId={}", taskId);
            return;
        }
        // 仅 setMchNo，不校验商户启用（与超时关单一致）
        paymentContext.runAs(() -> {
            paymentContext.setMchNo(boot.getMchNo());
            send(taskId, true);
        });
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
            // autoSend(MQ重投)幂等保护: 已成功任务自动消费时跳过;
            // 手动重发(autoSend=false)不跳过, 允许重发已成功任务
            if (autoSend) {
                log.info("出站通知任务已成功, 自动消费跳过: taskId={}", taskId);
                return;
            }
            log.info("手动重发已成功任务: taskId={}", taskId);
        }
        OffsetDateTime sendTime = OffsetDateTime.now(ZoneOffset.UTC);
        int reqCount = (task.getSendCount() == null ? 0 : task.getSendCount()) + 1;
        MchNoticeRecord record = new MchNoticeRecord();
        // 运营端写 MchBaseEntity 必须显式 mchNo
        record.setMchNo(task.getMchNo());
        record.setTaskId(task.getId())
                .setReqCount(reqCount)
                .setSendType(autoSend ? NoticeSendTypeEnum.AUTO.getCode() : NoticeSendTypeEnum.MANUAL.getCode());

        NoticePayloadBuilder payloadBuilder = payloadBuilderMap.get(task.getFormat());
        if (payloadBuilder == null) {
            log.error("未找到通知报文构建器: format={}, taskId={}", task.getFormat(), taskId);
            record.setSuccess(false).setErrorMsg("payload builder not found: " + task.getFormat());
            failUpdate(task, sendTime, autoSend, record);
            return;
        }
        NoticeTransportSender transportSender = transportSenderMap.get(task.getTransport());
        if (transportSender == null) {
            log.error("未找到通知传输发送器: transport={}, taskId={}", task.getTransport(), taskId);
            record.setSuccess(false).setErrorMsg("transport sender not found: " + task.getTransport());
            failUpdate(task, sendTime, autoSend, record);
            return;
        }

        // 先组装信封, 再投递 (format 与 transport 正交)
        NoticeEnvelope envelope;
        try {
            envelope = payloadBuilder.build(task);
        } catch (Exception e) {
            log.error("出站通知报文组装异常: taskId={}", taskId, e);
            record.setSuccess(false).setErrorMsg(e.getMessage());
            failUpdate(task, sendTime, autoSend, record);
            return;
        }

        NoticeSendResult sendResult;
        try {
            sendResult = transportSender.send(task, envelope);
        } catch (Exception e) {
            log.error("出站通知投递异常: taskId={}", taskId, e);
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
            record.setSuccess(true);
            taskManager.updateById(task);
            recordManager.save(record);
            return;
        }

        record.setSuccess(false).setErrorMsg(sendResult.getErrorMsg());
        failUpdate(task, sendTime, autoSend, record);
    }

    /// 失败：更新任务并按需排期重试 (重试节奏按 transport 区分)
    private void failUpdate(MchNoticeTask task, OffsetDateTime sendTime, boolean autoSend, MchNoticeRecord record) {
        int reqCount = record.getReqCount() == null ? 1 : record.getReqCount();
        task.setSendCount(reqCount).setLatestTime(sendTime);
        if (StrUtil.isNotBlank(record.getErrorMsg())) {
            task.setErrorMsg(record.getErrorMsg());
        }
        // 手动重发失败时(含重发已成功任务)需将 success 置回 false, 与实际发送结果一致
        if (!autoSend) {
            task.setSuccess(false);
        }
        String transport = task.getTransport();
        if (autoSend && !task.isSuccess()) {
            int delayCount = task.getDelayCount() == null ? 0 : task.getDelayCount();
            if (retryPolicy.canRetry(transport, delayCount)) {
                int next = delayCount + 1;
                task.setDelayCount(next);
                int delaySeconds = retryPolicy.nextDelaySeconds(transport, next);
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
