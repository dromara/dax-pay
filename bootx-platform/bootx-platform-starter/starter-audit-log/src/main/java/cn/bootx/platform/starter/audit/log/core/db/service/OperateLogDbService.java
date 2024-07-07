package cn.bootx.platform.starter.audit.log.core.db.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.core.db.dao.OperateLogDbManager;
import cn.bootx.platform.starter.audit.log.core.db.entity.OperateLogDb;
import cn.bootx.platform.starter.audit.log.dto.OperateLogDto;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.bootx.platform.starter.audit.log.service.OperateLogService;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbService implements OperateLogService {

    private final OperateLogDbManager operateLogManager;

    /**
     * 添加
     */
    @Async("asyncExecutor")
    @Override
    public void add(OperateLogParam operateLog) {
        operateLogManager.save(LogConvert.CONVERT.convert(operateLog));
    }

    /**
     * 获取
     */
    @Override
    public OperateLogDto findById(Long id) {
        return operateLogManager.findById(id).map(OperateLogDb::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<OperateLogDto> page(PageParam pageParam, OperateLogParam operateLogParam) {
        return MpUtil.toPageResult(operateLogManager.page(pageParam, operateLogParam));
    }

    /**
     * 删除
     */
    @Override
    public void deleteByDay(int deleteDay) {
        // 计算出来指定天数的日期
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        operateLogManager.deleteByOffset(offset);
    }

}
