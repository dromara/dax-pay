package cn.bootx.platform.starter.quartz.core.service;

import cn.bootx.platform.starter.quartz.param.QuartzJobLogQuery;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.quartz.core.dao.QuartzJobLogManager;
import cn.bootx.platform.starter.quartz.core.entity.QuartzJobLog;
import cn.bootx.platform.starter.quartz.dto.QuartzJobLogDto;
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
    @Async("asyncExecutor")
    public void add(QuartzJobLog quartzJobLog) {
        quartzJobLog.setCreateTime(LocalDateTime.now());
        quartzJobLogManager.save(quartzJobLog);
    }

    /**
     * 分页
     */
    public PageResult<QuartzJobLogDto> page(PageParam pageParam, QuartzJobLogQuery query) {
        return MpUtil.convert2DtoPageResult(quartzJobLogManager.page(pageParam, query));
    }

    /**
     * 单条
     */
    public QuartzJobLogDto findById(Long id) {
        return quartzJobLogManager.findById(id).map(QuartzJobLog::toDto).orElseThrow(DataNotExistException::new);
    }

}
