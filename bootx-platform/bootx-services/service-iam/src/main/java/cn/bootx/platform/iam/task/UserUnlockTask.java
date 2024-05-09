package cn.bootx.platform.iam.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * 用户解除锁定任务
 * @author xxm
 * @since 2023/11/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUnlockTask implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
