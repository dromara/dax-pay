package cn.daxpay.open.payment.trade.notice.job;

import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.service.NoticeTaskScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 商户出站通知兜底重投任务
///
/// 扫描未成功且未排程下次重试的孤儿通知任务, 重新投递 MQ。
/// 覆盖 [cn.daxpay.open.payment.trade.notice.service.NoticeTaskScheduleService#scheduleImmediateAfterCommit]
/// 投递 MQ 失败(Artemis 故障)导致任务永久卡在 success=false、nextTime=null 的场景;
/// 全仓此前无任何扫描 success=false 任务的定时入口, 是通知链路的可用性缺口。
///
/// 全局开关: `daxpay.platform.config.notice-retry-enabled`(默认 true)。
/// 与 [cn.daxpay.open.payment.trade.runtime.job.TradeSyncJob] 一样使用 ShedLock 防多节点重复执行。
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "daxpay.platform.config",
        name = "notice-retry-enabled", havingValue = "true", matchIfMissing = true)
public class NoticeRetryJob {

    private final MchNoticeTaskManager mchNoticeTaskManager;
    private final NoticeTaskScheduleService noticeTaskScheduleService;

    /// 每 2 分钟扫描孤儿通知任务(未成功且 nextTime 为空或已到), 重投 MQ
    ///
    /// 单笔重投失败不阻断整批(MQ 仍不可用时下轮再试); 已成功任务由 NoticeSendEngine 内部幂等控制不重复发送。
    @Scheduled(cron = "0 */2 * * * ?")
    @SchedulerLock(name = "lock:noticeRetry", lockAtMostFor = "110s", lockAtLeastFor = "30s")
    public void retryStaleTasks() {
        List<MchNoticeTask> stale = mchNoticeTaskManager.findStaleUnsent(500);
        if (stale.isEmpty()) {
            return;
        }
        log.info("通知兜底扫描命中 {} 笔孤儿任务, 重投 MQ", stale.size());
        for (MchNoticeTask task : stale) {
            try {
                noticeTaskScheduleService.sendNow(task.getId());
            } catch (Exception e) {
                // 单笔重投失败不阻断整批(MQ 仍不可用时下轮再试)
                log.warn("通知兜底重投失败 taskId={}", task.getId(), e);
            }
        }
    }
}
