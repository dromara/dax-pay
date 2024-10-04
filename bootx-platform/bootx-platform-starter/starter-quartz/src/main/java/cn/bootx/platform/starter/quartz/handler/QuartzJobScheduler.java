package cn.bootx.platform.starter.quartz.handler;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.starter.quartz.entity.QuartzJob;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定时任务调度器
 *
 * @author xxm
 * @since 2021/11/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobScheduler {

    private final Scheduler scheduler;

    /** 立即执行的任务分组 */
    private static final String JOB_TEST_GROUP = "job_test_group";

    /** 参数 */
    private static final String PARAMETER = "parameter";

    /**
     * 添加定时任务
     */
    public void add(QuartzJob quartzJob) {
        try {
            String idStr = String.valueOf(quartzJob.getId());
            // 启动调度器
            scheduler.start();
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getJobClass(quartzJob.getJobClassName()))
                .withIdentity(idStr)
                .usingJobData(PARAMETER, quartzJob.getParameter())
                .build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCron());

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(idStr,quartzJob.getGroupName())
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        }
        catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new BizException("创建定时任务失败");
        }
    }

    /**
     * 删除定时任务
     */
    public void delete(Long id) {
        try {
            String idStr = String.valueOf(id);
            scheduler.pauseTrigger(TriggerKey.triggerKey(idStr));
            scheduler.unscheduleJob(TriggerKey.triggerKey(idStr));
            scheduler.deleteJob(JobKey.jobKey(idStr));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BizException("删除定时任务失败");
        }
    }

    /**
     * 立即执行
     * @param jobClassName 任务类名
     * @param parameter 参数
     */
    public void execute(String jobClassName, String parameter) {
        try {
            String identity = jobClassName + RandomUtil.randomString(8);

            // 定义一个Trigger
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(identity, JOB_TEST_GROUP)
                .startNow()
                .build();
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getJobClass(jobClassName))
                .withIdentity(identity)
                .usingJobData(PARAMETER, parameter)
                .build();
            // 将trigger和 jobDetail 加入这个调度
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动scheduler
            scheduler.start();
        }
        catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw new BizException("定时任务启动失败");
        }
    }

    /**
     * 获取定时任务列表
     */
    public List<Trigger> findTriggers() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            return jobKeys.stream()
                .map(this::getTriggersOfJob)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        }
        catch (SchedulerException e) {
            throw new BizException(e.getMessage());
        }
    }

    @SneakyThrows
    private List<? extends Trigger> getTriggersOfJob(JobKey jobKey) {
        return scheduler.getTriggersOfJob(jobKey);
    }

    /**
     * 获取任务对象
     */
    public Class<? extends Job> getJobClass(String classname) {
        Class<?> clazz;
        try {
            clazz = Class.forName(classname);
        }
        catch (ClassNotFoundException e) {
            throw new BizException("找不到该定时任务类名");
        }
        if (Job.class.isAssignableFrom(clazz)) {
            // noinspection unchecked
            return (Class<Job>) clazz;
        }
        throw new BizException("该类不是定时任务类");
    }

}
