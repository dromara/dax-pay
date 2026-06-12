package org.dromara.daxpay.platform.capability.audit.log.service.log;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.capability.audit.log.convert.LogConvert;
import org.dromara.daxpay.platform.capability.audit.log.dao.LoginLogDbManager;
import org.dromara.daxpay.platform.capability.audit.log.entity.LoginLogDb;
import org.dromara.daxpay.platform.capability.audit.log.param.LoginLogParam;
import org.dromara.daxpay.platform.capability.audit.log.param.LoginLogQuery;
import org.dromara.daxpay.platform.capability.audit.log.result.LoginLogResult;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/// # 登录日志服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogService {

    /// 队列容量默认值
    private static final int DEFAULT_QUEUE_CAPACITY = 5000;

    /// 单次最大批量保存条数
    private static final int BATCH_SIZE = 200;

    private final LoginLogDbManager loginLogManager;

    /// 内存缓冲队列
    private final BlockingQueue<LoginLogParam> bufferQueue = new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);

    /// 添加日志
    /// 入缓冲队列
    public void add(LoginLogParam loginLog) {
        if (loginLog == null) {
            return;
        }

        boolean success = bufferQueue.offer(loginLog);
        if (!success) {
            log.warn("登录日志缓冲队列已满，尝试同步写入: {}", loginLog.getAccount());
            try {
                loginLogManager.save(LogConvert.CONVERT.convert(loginLog));
            } catch (Exception e) {
                log.error("登录日志同步写入失败: {}", loginLog.getAccount(), e);
            }
        }
    }

    /// 定时批量保存（每1秒执行一次）
    @Scheduled(fixedDelay = 1000)
    public void flushBuffer() {
        if (bufferQueue.isEmpty()) {
            return;
        }

        List<LoginLogParam> batch = new ArrayList<>(BATCH_SIZE);
        bufferQueue.drainTo(batch, BATCH_SIZE);

        if (batch.isEmpty()) {
            return;
        }

        try {
            List<LoginLogDb> entities = batch.stream()
                    .map(LogConvert.CONVERT::convert)
                    .toList();
            loginLogManager.saveAll(entities);
            log.debug("登录日志批量保存成功，数量: {}", entities.size());
        } catch (Exception e) {
            log.error("登录日志批量保存失败，待处理条数: {}", batch.size(), e);
            // 失败时回退逐条写入
            for (LoginLogParam param : batch) {
                try {
                    loginLogManager.save(LogConvert.CONVERT.convert(param));
                } catch (Exception ex) {
                    log.error("登录日志逐条写入失败: {}", param.getAccount(), ex);
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
        log.info("正在刷空登录日志缓冲队列...");
        List<LoginLogParam> remaining = new ArrayList<>();
        bufferQueue.drainTo(remaining);
        if (!remaining.isEmpty()) {
            try {
                List<LoginLogDb> entities = remaining.stream()
                        .map(LogConvert.CONVERT::convert)
                        .toList();
                loginLogManager.saveAll(entities);
                log.info("应用关闭前保存剩余登录日志: {} 条", entities.size());
            } catch (Exception e) {
                log.error("应用关闭前保存剩余登录日志失败", e);
            }
        }
    }

    /// 获取
    public LoginLogResult findById(Long id) {
        return loginLogManager.findById(id).map(LoginLogDb::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 分页
    public PageResult<LoginLogResult> page(PageParam pageParam, LoginLogQuery query) {
        return MpUtil.toPageResult(loginLogManager.page(pageParam, query));
    }

    /// 删除
    public void deleteByDay(int deleteDay) {
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        loginLogManager.deleteByOffset(offset);
    }
}

