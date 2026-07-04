package cn.daxpay.open.platform.capability.audit.log.service.log;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.capability.audit.log.convert.LogConvert;
import cn.daxpay.open.platform.capability.audit.log.dao.LoginLogDbManager;
import cn.daxpay.open.platform.capability.audit.log.entity.LoginLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogParam;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.LoginLogResult;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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

    private final TransService transService;

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
        LoginLogResult result = loginLogManager.findById(id).map(LoginLogDb::toResult).orElseThrow(DataNotExistException::new);
        transService.translate(result);
        return result;
    }

    /// 分页
    public PageResult<LoginLogResult> page(PageParam pageParam, LoginLogQuery query) {
        PageResult<LoginLogResult> pageResult = MpUtil.toPageResult(loginLogManager.page(pageParam, query));
        transService.translate(pageResult);
        return pageResult;
    }

    /// 删除
    public void deleteByDay(int deleteDay) {
        OffsetDateTime offset = OffsetDateTime.now(ZoneOffset.UTC).minusDays(deleteDay);
        loginLogManager.deleteByOffset(offset);
    }
}

