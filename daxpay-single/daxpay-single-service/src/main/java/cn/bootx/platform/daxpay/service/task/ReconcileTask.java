package cn.bootx.platform.daxpay.service.task;

import cn.bootx.platform.daxpay.service.task.service.ReconcileTaskService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 对账任务下载和保存定时任务
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class ReconcileTask implements Job {
    private final ReconcileTaskService reconcileTaskService;

    /**
     * 要同步的通道
     */
    @Setter
    private String channel;

    /**
     * 同步账单规则是 T+N 天, 例如T+1就是同步昨天的账单, T+2就是同步前天的账单
     */
    @Setter
    private Integer n;

    /**
     * 任务实现
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 日期, 如果未配置T+N规则, 默认为T+1
        LocalDate date = LocalDate.now();
        if (Objects.nonNull(n)){
            date = date.minusDays(n);
        } else {
            date = date.minusDays(1);
        }
        reconcileTaskService.reconcileTask(date,channel);
    }
}
