package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.util.NoticeTxUtil;
import cn.daxpay.open.payment.trade.runtime.mq.MchNoticeSendMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户出站通知任务投递
///
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeTaskScheduleService {

    private final ArtemisTemplateService artemisTemplateService;

    /// 事务提交后立即投递（delay=0）
    // afterCommit 回调在事务提交后执行, 投递失败时事务已无法回滚(task 已落库),
    // 若不捕获, 异常会被 Spring 的 TransactionSynchronizationUtils 静默吞掉, 消息永久丢失。
    // 此处显式捕获并打明确日志, task 可由手动重发兜底。
    public void scheduleImmediateAfterCommit(Long taskId) {
        NoticeTxUtil.afterCommit(() -> {
            try {
                sendNow(taskId);
            } catch (Exception e) {
                log.error("出站通知MQ投递失败, 任务不会自动发送, 请手动重发: taskId={}", taskId, e);
            }
        });
    }

    /// 立即投递
    public void sendNow(Long taskId) {
        var msg = new MchNoticeSendMessage().setTaskId(taskId);
        artemisTemplateService.send(PayArtemisConstants.MCH_NOTICE_SEND, JacksonUtil.toJson(msg));
        log.debug("出站通知已投递: taskId={}", taskId);
    }

    /// 延时投递
    public void scheduleDelay(Long taskId, int delaySeconds) {
        var msg = new MchNoticeSendMessage().setTaskId(taskId);
        artemisTemplateService.sendDelay(PayArtemisConstants.MCH_NOTICE_SEND, JacksonUtil.toJson(msg), delaySeconds);
        log.info("出站通知延时重试已投递: taskId={}, delaySeconds={}", taskId, delaySeconds);
    }

    /// 根据任务排程下次重试
    public void scheduleRetry(MchNoticeTask task, int delaySeconds) {
        scheduleDelay(task.getId(), delaySeconds);
    }
}
