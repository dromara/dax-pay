package cn.daxpay.open.platform.capability.audit.log.service.log;

import cn.daxpay.open.platform.capability.audit.log.convert.LogConvert;
import cn.daxpay.open.platform.capability.audit.log.dao.UnipayApiLogDbManager;
import cn.daxpay.open.platform.capability.audit.log.entity.UnipayApiLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogParam;
import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.UnipayApiLogResult;
import cn.daxpay.open.platform.capability.audit.log.service.mask.AuditLogMaskService;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
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

/// # 统一支付接口审计日志服务
///
/// 写入策略与操作日志类似，但队列满时**丢弃**（禁止同步写库，避免拖慢支付）。
/// 请求/响应 body 强制脱敏并截断。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnipayApiLogService {

    /// 队列容量（高于操作日志，支付流量更大）
    private static final int DEFAULT_QUEUE_CAPACITY = 10000;

    /// 单次最大批量保存条数
    private static final int BATCH_SIZE = 200;

    /// body 最大长度（字符）
    private static final int PAYLOAD_MAX_LENGTH = 8192;

    /// 支付接口强制全量脱敏键（sign 非敏感密钥，不脱敏）
    private static final String[] UNIPAY_FULL_MASK_KEYS = {
            "password", "token", "secret", "credential", "accesstoken", "refreshtoken",
            "idcard", "phone", "mobile", "bankcard", "cvv", "ssn", "passport",
            "authcode", "paybody"
    };

    private final UnipayApiLogDbManager unipayApiLogManager;
    private final AuditLogMaskService maskService;

    /// 内存缓冲队列
    private final BlockingQueue<UnipayApiLogParam> bufferQueue = new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);

    /// 添加入队：强制脱敏 + 截断；队列满则丢弃
    public void add(UnipayApiLogParam param) {
        if (param == null) {
            return;
        }

        try {
            // 请求参数强制脱敏截断
            if (StrUtil.isNotEmpty(param.getReqParam())) {
                param.setReqParam(maskService.process(
                        param.getReqParam(),
                        true,
                        UNIPAY_FULL_MASK_KEYS,
                        null,
                        PAYLOAD_MAX_LENGTH));
            }
            // 响应体强制脱敏截断
            if (StrUtil.isNotEmpty(param.getResBody())) {
                param.setResBody(maskService.process(
                        param.getResBody(),
                        true,
                        UNIPAY_FULL_MASK_KEYS,
                        null,
                        PAYLOAD_MAX_LENGTH));
            }
            // 错误信息截断
            if (StrUtil.isNotEmpty(param.getErrorMsg()) && param.getErrorMsg().length() > 512) {
                param.setErrorMsg(param.getErrorMsg().substring(0, 512));
            }

            boolean offered = bufferQueue.offer(param);
            if (!offered) {
                // 队列满：丢弃，绝不同步写库拖慢支付
                log.warn("支付接口审计队列已满，丢弃日志 path={} mchNo={}", param.getApiPath(), param.getMchNo());
            }
        } catch (Exception e) {
            log.warn("支付接口审计入队失败 path={}: {}", param.getApiPath(), e.getMessage());
        }
    }

    /// 定时批量保存（每 1 秒）
    @Scheduled(fixedDelay = 1000)
    public void flushBuffer() {
        if (bufferQueue.isEmpty()) {
            return;
        }

        List<UnipayApiLogParam> batch = new ArrayList<>(BATCH_SIZE);
        bufferQueue.drainTo(batch, BATCH_SIZE);
        if (batch.isEmpty()) {
            return;
        }

        try {
            List<UnipayApiLogDb> entities = batch.stream()
                    .map(LogConvert.CONVERT::convert)
                    .toList();
            unipayApiLogManager.saveAll(entities);
            log.debug("支付接口审计批量保存成功，数量: {}", entities.size());
        } catch (Exception e) {
            log.error("支付接口审计批量保存失败，待处理条数: {}", batch.size(), e);
            for (UnipayApiLogParam item : batch) {
                try {
                    unipayApiLogManager.save(LogConvert.CONVERT.convert(item));
                } catch (Exception ex) {
                    log.error("支付接口审计逐条写入失败 path={}", item.getApiPath(), ex);
                }
            }
        }
    }

    /// 应用关闭时刷空队列
    @PreDestroy
    public void shutdown() {
        log.info("正在刷空支付接口审计缓冲队列...");
        List<UnipayApiLogParam> remaining = new ArrayList<>();
        bufferQueue.drainTo(remaining);
        if (remaining.isEmpty()) {
            return;
        }
        try {
            List<UnipayApiLogDb> entities = remaining.stream()
                    .map(LogConvert.CONVERT::convert)
                    .toList();
            unipayApiLogManager.saveAll(entities);
            log.info("应用关闭前保存剩余支付接口审计: {} 条", entities.size());
        } catch (Exception e) {
            log.error("应用关闭前保存剩余支付接口审计失败", e);
        }
    }

    /// 详情
    public UnipayApiLogResult findById(Long id) {
        return unipayApiLogManager.findById(id)
                .map(UnipayApiLogDb::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /// 分页
    public PageResult<UnipayApiLogResult> page(PageParam pageParam, UnipayApiLogQuery query) {
        return MpUtil.toPageResult(unipayApiLogManager.page(pageParam, query));
    }

    /// 按天数清理
    public void deleteByDay(int deleteDay) {
        OffsetDateTime offset = OffsetDateTime.now(ZoneOffset.UTC).minusDays(deleteDay);
        unipayApiLogManager.deleteByOffset(offset);
    }

    /// 当前队列大小（监控用）
    public int getQueueSize() {
        return bufferQueue.size();
    }
}
