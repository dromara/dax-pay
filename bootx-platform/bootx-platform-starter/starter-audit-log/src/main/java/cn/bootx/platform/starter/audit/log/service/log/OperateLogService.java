package cn.bootx.platform.starter.audit.log.service.log;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.audit.log.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.dao.OperateLogDbManager;
import cn.bootx.platform.starter.audit.log.entity.OperateLogDb;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.bootx.platform.starter.audit.log.result.OperateLogResult;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class OperateLogService {

    private final OperateLogDbManager operateLogManager;

    /**
     * 添加
     */
    @Async
    public void add(OperateLogParam operateLog) {
        operateLogManager.save(LogConvert.CONVERT.convert(operateLog));
    }

    /**
     * 获取
     */
    public OperateLogResult findById(Long id) {
        return operateLogManager.findById(id).map(OperateLogDb::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    public PageResult<OperateLogResult> page(PageParam pageParam, OperateLogParam operateLogParam) {
        return MpUtil.toPageResult(operateLogManager.page(pageParam, operateLogParam));
    }

    /**
     * 删除
     */
    public void deleteByDay(int deleteDay) {
        // 计算出来指定天数的日期
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        operateLogManager.deleteByOffset(offset);
    }

}
