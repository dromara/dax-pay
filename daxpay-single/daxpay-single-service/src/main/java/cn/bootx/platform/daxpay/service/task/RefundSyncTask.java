package cn.bootx.platform.daxpay.service.task;

import cn.bootx.platform.daxpay.service.task.service.RefundSyncTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * 退款定时同步任务 一分钟一次, 查询退款中的订单进行同步
 * @author xxm
 * @since 2024/3/12
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class RefundSyncTask implements Job {

    private final RefundSyncTaskService refundSyncTaskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        refundSyncTaskService.syncTask();
    }
}
