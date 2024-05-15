package cn.bootx.platform.iam.task;

import cn.bootx.platform.iam.core.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * 密码过期检查任务
 * @author xxm
 * @since 2023/11/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckPasswordExpireTask implements Job {
    private final UserInfoService userInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
