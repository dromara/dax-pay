package cn.bootx.platform.daxpay.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

/**
 * 超时支付单任务撤销(2-5秒轮训一次)
 *
 * @author xxm
 * @since 2022/7/12
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class PayExpiredTimeTask implements Job {

    private final PayExpiredTimeTaskService payExpiredTimeTaskService;

    @Override
    public void execute(JobExecutionContext context) {
        payExpiredTimeTaskService.sync();
    }

}
