package cn.bootx.platform.starter.quartz.task;

import cn.hutool.core.thread.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务
 * @author xxm
 * @since 2021/11/8
 */
@Setter
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class TestTask implements Job {

    /**
     * 若参数变量名修改 QuartzJobScheduler 中也需对应修改 需要给一个set方法, 让系统设置值
     */
    private String parameter;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("定时任务start");
        ThreadUtil.safeSleep(5000L);
        log.info("定时任务end");
        log.info("参数: {}", parameter);
    }

}
