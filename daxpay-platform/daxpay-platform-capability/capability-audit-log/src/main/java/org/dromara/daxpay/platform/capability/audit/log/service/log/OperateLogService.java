package org.dromara.daxpay.platform.capability.audit.log.service.log;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.capability.audit.log.convert.LogConvert;
import org.dromara.daxpay.platform.capability.audit.log.dao.OperateLogDbManager;
import org.dromara.daxpay.platform.capability.audit.log.entity.OperateLogDb;
import org.dromara.daxpay.platform.capability.audit.log.param.OperateLogParam;
import org.dromara.daxpay.platform.capability.audit.log.param.OperateLogQuery;
import org.dromara.daxpay.platform.capability.audit.log.result.OperateLogResult;
import org.dromara.daxpay.platform.capability.audit.log.service.mask.AuditLogMaskService;
import cn.hutool.core.date.LocalDateTimeUtil;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/// # 操作日志服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogService {

    /// 队列容量默认值
    private static final int DEFAULT_QUEUE_CAPACITY = 5000;

    /// 单次最大批量保存条数
    private static final int BATCH_SIZE = 200;

    private final OperateLogDbManager operateLogManager;
    private final AuditLogMaskService maskService;

    /// 内存缓冲队列
    private final BlockingQueue<OperateLogParam> bufferQueue = new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);

    /// 添加日志
    /// 1. 脱敏处理
    /// 2. 截断处理
    /// 3. 入缓冲队列
    public void add(OperateLogParam operateLog) {
        if (operateLog == null) {
            return;
        }

        // 参数脱敏处理
        String paramJson = operateLog.getOperateParam();
        if (maskService.shouldProcess(operateLog.getSaveParam() != null && operateLog.getSaveParam(), paramJson)) {
            operateLog.setOperateParam(maskService.process(paramJson, 
                    Boolean.TRUE.equals(operateLog.getMaskParam()), 
                    operateLog.getFullMaskKeys(),
                    operateLog.getPartialMaskRules(),
                    operateLog.getPayloadMaxLength() != null ? operateLog.getPayloadMaxLength() : 20000));
        }

        // 返回值脱敏处理
        String returnJson = operateLog.getOperateReturn();
        if (maskService.shouldProcess(operateLog.getSaverReturn() != null && operateLog.getSaverReturn(), returnJson)) {
            operateLog.setOperateReturn(maskService.process(returnJson, 
                    Boolean.TRUE.equals(operateLog.getMaskReturn()), 
                    operateLog.getFullMaskKeys(),
                    operateLog.getPartialMaskRules(),
                    operateLog.getPayloadMaxLength() != null ? operateLog.getPayloadMaxLength() : 20000));
        }

        // 入队列
        boolean success = bufferQueue.offer(operateLog);
        if (!success) {
            log.warn("操作日志缓冲队列已满，尝试同步写入: {}", operateLog.getTitle());
            try {
                operateLogManager.save(LogConvert.CONVERT.convert(operateLog));
            } catch (Exception e) {
                log.error("操作日志同步写入失败: {}", operateLog.getTitle(), e);
            }
        }
    }

    /// 定时批量保存（每1秒执行一次）
    @Scheduled(fixedDelay = 1000)
    public void flushBuffer() {
        if (bufferQueue.isEmpty()) {
            return;
        }

        List<OperateLogParam> batch = new ArrayList<>(BATCH_SIZE);
        bufferQueue.drainTo(batch, BATCH_SIZE);

        if (batch.isEmpty()) {
            return;
        }

        try {
            List<OperateLogDb> entities = batch.stream()
                    .map(LogConvert.CONVERT::convert)
                    .toList();
            operateLogManager.saveAll(entities);
            log.debug("操作日志批量保存成功，数量: {}", entities.size());
        } catch (Exception e) {
            log.error("操作日志批量保存失败，待处理条数: {}", batch.size(), e);
            // 失败时回退逐条写入
            for (OperateLogParam param : batch) {
                try {
                    operateLogManager.save(LogConvert.CONVERT.convert(param));
                } catch (Exception ex) {
                    log.error("操作日志逐条写入失败: {}", param.getTitle(), ex);
                }
            }
        }
    }

    /// 获取队列当前大小（用于监控）
    public int getQueueSize() {
        return bufferQueue.size();
    }

    /// 应用关闭时刷空队列
    @PreDestroy
    public void shutdown() {
        log.info("正在刷空操作日志缓冲队列...");
        List<OperateLogParam> remaining = new ArrayList<>();
        bufferQueue.drainTo(remaining);
        if (!remaining.isEmpty()) {
            try {
                List<OperateLogDb> entities = remaining.stream()
                        .map(LogConvert.CONVERT::convert)
                        .toList();
                operateLogManager.saveAll(entities);
                log.info("应用关闭前保存剩余操作日志: {} 条", entities.size());
            } catch (Exception e) {
                log.error("应用关闭前保存剩余操作日志失败", e);
            }
        }
    }

    /// 获取
    public OperateLogResult findById(Long id) {
        return operateLogManager.findById(id).map(OperateLogDb::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 分页
    public PageResult<OperateLogResult> page(PageParam pageParam, OperateLogQuery operateLogParam) {
        return MpUtil.toPageResult(operateLogManager.page(pageParam, operateLogParam));
    }

    /// 删除
    public void deleteByDay(int deleteDay) {
        // 计算出来指定天数的日期
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        operateLogManager.deleteByOffset(offset);
    }

}

