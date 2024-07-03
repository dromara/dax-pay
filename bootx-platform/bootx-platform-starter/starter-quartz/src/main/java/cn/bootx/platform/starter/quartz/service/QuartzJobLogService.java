package cn.bootx.platform.starter.quartz.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.quartz.dao.QuartzJobLogManager;
import cn.bootx.platform.starter.quartz.entity.QuartzJobLog;
import cn.bootx.platform.starter.quartz.result.QuartzJobLogResult;
import cn.bootx.platform.starter.quartz.param.QuartzJobLogQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 定时任务日志
 *
 * @author xxm
 * @since 2022/5/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobLogService {

    private final QuartzJobLogManager quartzJobLogManager;

    /**
     * 添加
     */
    @Async
    public void add(QuartzJobLog quartzJobLog) {
        quartzJobLog.setCreateTime(LocalDateTime.now());
        quartzJobLogManager.save(quartzJobLog);
    }

    /**
     * 分页
     */
    public PageResult<QuartzJobLogResult> page(PageParam pageParam, QuartzJobLogQuery query) {
        return MpUtil.toPageResult(quartzJobLogManager.page(pageParam, query));
    }

    /**
     * 单条
     */
    public QuartzJobLogResult findById(Long id) {
        return quartzJobLogManager.findById(id).map(QuartzJobLog::toResult).orElseThrow(DataNotExistException::new);
    }

}
