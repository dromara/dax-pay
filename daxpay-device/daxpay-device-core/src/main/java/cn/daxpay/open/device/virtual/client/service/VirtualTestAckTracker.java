package cn.daxpay.open.device.virtual.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

/// # 试播/试打回执轻存(Redis, 零 DDL)
///
/// 台账的收款回执三列只承接 `pay.success`(见 [VirtualDeviceClientService#ack]), 试播/试打/纯打印
/// 的回执此前直接丢弃 —— 运营端点"下发"后只能确认"平台发了", 无法确认"设备播完/打完",
/// 而"端到端验证"的价值恰恰在后者。
///
/// 取舍: 不为一个调试态新增台账列(DDL + 迁移成本), 改存 Redis 哈希
/// `daxpay:device:testack:{deviceNo}`(msgId/cmd/ok/ts, TTL 1 小时)。语义明确:
/// **这是排障快照, 不是业务台账** —— 过期即无, 重启即无, 不参与任何统计与对账。
///
/// 写入失败只记日志不影响回执主流程(试打回执丢失不应让设备心跳失败)。
@Slf4j
@Component
@RequiredArgsConstructor
public class VirtualTestAckTracker {

    /// 哈希 key 前缀(开源版 daxpay:device:* 前缀规范)
    public static final String KEY_PREFIX = "daxpay:device:testack:";

    /// 快照有效期(小时; 排障窗口足够, 过期自然消失)
    public static final long TTL_HOURS = 1;

    private final StringRedisTemplate stringRedisTemplate;

    /// 记录一条试播/试打/纯打印回执
    public void record(String deviceNo, String msgId, String cmd, Boolean ok) {
        String key = KEY_PREFIX + deviceNo;
        try {
            stringRedisTemplate.opsForHash().putAll(key, Map.of(
                    "msgId", msgId,
                    "cmd", cmd == null ? "" : cmd,
                    "ok", Boolean.TRUE.equals(ok) ? "1" : "0",
                    "ts", String.valueOf(System.currentTimeMillis())));
            stringRedisTemplate.expire(key, Duration.ofHours(TTL_HOURS));
        } catch (RuntimeException e) {
            // 试播回执快照属排障辅助, Redis 异常不阻断设备回执主流程
            log.warn("试播/试打回执快照写入失败: deviceNo={}, msgId={}", deviceNo, msgId, e);
        }
    }

    /// 读取最近一条试播/试打回执(无记录返回空)
    public Optional<TestAck> load(String deviceNo) {
        try {
            Map<Object, Object> raw = stringRedisTemplate.opsForHash().entries(KEY_PREFIX + deviceNo);
            if (raw == null || raw.isEmpty()) {
                return Optional.empty();
            }
            String msgId = text(raw.get("msgId"));
            String ts = text(raw.get("ts"));
            if (msgId.isBlank() || ts.isBlank()) {
                return Optional.empty();
            }
            OffsetDateTime time = Instant.ofEpochMilli(Long.parseLong(ts)).atOffset(ZoneOffset.UTC);
            return Optional.of(new TestAck(msgId, text(raw.get("cmd")), "1".equals(text(raw.get("ok"))), time));
        } catch (RuntimeException e) {
            log.warn("试播/试打回执快照读取失败: deviceNo={}", deviceNo, e);
            return Optional.empty();
        }
    }

    private static String text(Object value) {
        return value == null ? "" : value.toString();
    }

    /// 试播/试打回执快照(msgId 供界面与下发返回比对, cmd 区分 audio.test / print.ticket)
    public record TestAck(String msgId, String cmd, Boolean ok, OffsetDateTime time) {
    }
}
