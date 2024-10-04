package cn.bootx.platform.starter.quartz.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.quartz.code.QuartzJobCode;
import cn.bootx.platform.starter.quartz.dao.QuartzJobManager;
import cn.bootx.platform.starter.quartz.result.QuartzJobResult;
import cn.bootx.platform.starter.quartz.entity.QuartzJob;
import cn.bootx.platform.starter.quartz.handler.QuartzJobScheduler;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobService {

    private final QuartzJobScheduler jobScheduler;

    private final QuartzJobManager quartzJobManager;

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(QuartzJobParam param) {
        QuartzJob quartzJob = QuartzJob.init(param);
        quartzJob.setState(QuartzJobCode.STOP);
        quartzJobManager.save(quartzJob);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzJobParam param) {
        QuartzJob quartzJob = quartzJobManager.findById(param.getId()).orElseThrow(() -> new BizException("定时任务不存在"));
        BeanUtil.copyProperties(param, quartzJob, CopyOptions.create().ignoreNullValue());
        quartzJobManager.updateById(quartzJob);
        jobScheduler.delete(quartzJob.getId());
        if (Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
            jobScheduler.add(quartzJob);
        }
    }

    /**
     * 启动
     */
    @Transactional(rollbackFor = Exception.class)
    public void start(Long id) {
        QuartzJob quartzJob = quartzJobManager.findById(id).orElseThrow(() -> new BizException("定时任务不存在"));
        // 非运行才进行操作
        if (!Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
            quartzJob.setState(QuartzJobCode.RUNNING);
            quartzJobManager.updateById(quartzJob);
            jobScheduler.add(quartzJob);
        }
        else {
            throw new BizException("已经是启动状态");
        }
    }

    /**
     * 停止
     */
    @Transactional(rollbackFor = Exception.class)
    public void stop(Long id) {
        QuartzJob quartzJob = quartzJobManager.findById(id).orElseThrow(() -> new BizException("定时任务不存在"));
        if (!Objects.equals(quartzJob.getState(), QuartzJobCode.STOP)) {
            quartzJob.setState(QuartzJobCode.STOP);
            quartzJobManager.updateById(quartzJob);
            jobScheduler.delete(id);
        }
        else {
            throw new BizException("已经是停止状态");
        }
    }

    /**
     * 立即执行
     */
    public void execute(Long id) {
        QuartzJob quartzJob = quartzJobManager.findById(id).orElseThrow(() -> new BizException("定时任务不存在"));
        jobScheduler.execute(quartzJob.getJobClassName(), quartzJob.getParameter());
    }

    /**
     * 分页
     */
    public PageResult<QuartzJobResult> page(PageParam pageParam, QuartzJobParam param) {
        return MpUtil.toPageResult(quartzJobManager.page(pageParam, param));
    }

    /**
     * 获取单条
     */
    public QuartzJobResult findById(Long id) {
        return quartzJobManager.findById(id).map(QuartzJob::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        quartzJobManager.deleteById(id);
        jobScheduler.delete(id);
    }

    /**
     * 判断是否是定时任务类
     */
    public String judgeJobClass(String jobClassName) {
        try {
            jobScheduler.getJobClass(jobClassName);
        }
        catch (BizException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * 同步状态
     */
    public void syncJobStatus() {
        Map<String, QuartzJob> quartzJobMap = quartzJobManager.findRunning()
            .stream()
            .collect(Collectors.toMap(o -> o.getId().toString(), Function.identity()));

        List<Trigger> triggers = jobScheduler.findTriggers();

        // 将开始任务列表里没有的Trigger给删除. 将未启动的任务状态更新成停止
        for (Trigger trigger : triggers) {
            String triggerName = trigger.getKey().getName();
            if (!quartzJobMap.containsKey(triggerName)) {
                jobScheduler.delete(Long.valueOf(triggerName));
            }
            else {
                quartzJobMap.remove(triggerName);
            }
        }
        // 更新任务列表状态
        Collection<QuartzJob> quartzJobs = quartzJobMap.values();
        for (QuartzJob quartzJob : quartzJobs) {
            quartzJob.setState(QuartzJobCode.STOP);
        }
        if (CollUtil.isNotEmpty(quartzJobs)) {
            quartzJobManager.updateAllById(quartzJobs);
        }
    }

}
