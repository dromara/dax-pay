package cn.daxpay.open.device.virtual.client.service;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/// # 虚拟设备在线追踪(Redis ZSET 在线窗口 + 节流落库锁 + ack nonce 防重放)
///
/// 虚拟设备运行态的 Redis 触点汇聚:
/// - 在线判定: 单一 ZSET `daxpay:device:online`(member=deviceNo, score=最后一次合法 ack 的 epoch 秒),
///   score ≥ now-900s 视为在线(= 3 个心跳周期, 容忍丢 1~2 次心跳); 虚拟设备独立台账单表独用;
/// - 节流落库: `daxpay:device:online:lastwrite:{deviceNo}`(NX EX 1800)成功才回写台账 `last_online_time`,
///   DB 列非实时, 只做 Redis 冷启兜底与排序(展示取 max(DB 值, Redis score), 见 [#resolveLastOnlineTime]);
/// - nonce 防重放: `daxpay:device:ack:nonce:{nonce}`(NX EX 300), 重复消费即重放请求, 抛错拒绝。
///
/// Redis key 前缀统一 `daxpay:device:`(开源版命名空间, 与面板 MQTT 批 `daxpay.device.#` 矩阵同源)。
/// 已知口径误差(设计留档): 解绑设备残留成员在 15 分钟窗口内会使 ZCOUNT 在线数虚高, 窗口后自然出窗;
/// ZSET 成员清理由凭证回收汇聚点(VirtualDeviceCredentialService)顺带 ZREM, 泄漏上限=历史激活过的设备总数, 量级可控。
@Slf4j
@Component
@RequiredArgsConstructor
public class VirtualOnlineTracker {

    /// 在线判定 ZSET key(开源版 daxpay: 前缀规范)
    public static final String ONLINE_KEY = "daxpay:device:online";

    /// 在线判定窗口(秒, = 3 个心跳周期, 容忍丢 1~2 次心跳)
    public static final long ONLINE_WINDOW_SECONDS = 900;

    /// 节流落库锁 key 前缀(命中锁才回写台账 last_online_time)
    public static final String LASTWRITE_KEY_PREFIX = "daxpay:device:online:lastwrite:";

    /// 节流落库周期(秒, 台账列 30 分钟精度, 非实时)
    public static final long LASTWRITE_INTERVAL_SECONDS = 1800;

    /// ack nonce 防重放 key 前缀(有效期与回执时间窗 ±5 分钟一致)
    public static final String ACK_NONCE_KEY_PREFIX = "daxpay:device:ack:nonce:";

    /// nonce 占用有效期(秒, 与回执 ts 窗口同宽, 窗口外重放已被时间窗拦截)
    public static final long ACK_NONCE_TTL_SECONDS = 300;

    /// ZCOUNT 上界(epoch 秒 2100-01-01, score 均为当前 epoch 秒, 有限上界代替 +inf)
    private static final double ONLINE_SCORE_MAX = 4102444800d;

    private final StringRedisTemplate stringRedisTemplate;

    /// 刷新在线(ZADD, score=当前 epoch 秒; 心跳与回执验签通过后调用)
    public void refreshOnline(String deviceNo) {
        stringRedisTemplate.opsForZSet().add(ONLINE_KEY, deviceNo, (double) epochNow());
    }

    /// 读取最后在线 score(epoch 秒; 从未上线/已清理返回空)
    public Optional<Long> getOnlineScore(String deviceNo) {
        Double score = stringRedisTemplate.opsForZSet().score(ONLINE_KEY, deviceNo);
        return score == null ? Optional.empty() : Optional.of(score.longValue());
    }

    /// 批量读取在线 score(列表页组装用, pipeline 一次往返; 不在 ZSET 的设备不在返回 Map 中)
    public Map<String, Long> getOnlineScores(Collection<String> deviceNos) {
        if (deviceNos == null || deviceNos.isEmpty()) {
            return Map.of();
        }
        List<String> members = List.copyOf(deviceNos);
        // pipeline 内逐台 ZSCORE, 结果顺序与入参一致
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<Object> scores = stringRedisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) {
                for (String member : members) {
                    operations.opsForZSet().score(ONLINE_KEY, member);
                }
                return null;
            }
        });
        Map<String, Long> result = new HashMap<>(members.size());
        for (int i = 0; i < members.size(); i++) {
            Object score = scores.get(i);
            if (score instanceof Number number) {
                result.put(members.get(i), number.longValue());
            }
        }
        return result;
    }

    /// 设备当前是否在线(score ≥ now-900s; 从未上线返回 false)
    public boolean isOnline(String deviceNo) {
        return getOnlineScore(deviceNo)
                .map(VirtualOnlineTracker::isOnlineScore)
                .orElse(false);
    }

    /// score 是否落在在线窗口(纯逻辑判定, 供批量 score 复用)
    public static boolean isOnlineScore(Long score) {
        return score != null && score >= epochNow() - ONLINE_WINDOW_SECONDS;
    }

    /// 当前在线设备数(ZCOUNT, score ∈ [now-900s, 上界 2100 年))
    public long countOnline() {
        Long count = stringRedisTemplate.opsForZSet()
                .count(ONLINE_KEY, (double) (epochNow() - ONLINE_WINDOW_SECONDS), ONLINE_SCORE_MAX);
        return count == null ? 0L : count;
    }

    /// 当前在线设备号名单(在线窗口内成员; 列表按在线/离线切分用, 与展示判定同一窗口)
    ///
    /// 名单规模即当前在线设备数(通常远小于台账总量), 供 SQL in/notIn 使用。
    public List<String> findOnlineDeviceNos() {
        Set<String> members = stringRedisTemplate.opsForZSet()
                .rangeByScore(ONLINE_KEY, (double) (epochNow() - ONLINE_WINDOW_SECONDS), ONLINE_SCORE_MAX);
        return members == null ? List.of() : List.copyOf(members);
    }

    /// 移除在线成员(凭证回收汇聚点顺带清理: 解绑/删除, 防残留成员虚高在线数)
    public void removeOnline(String deviceNo) {
        stringRedisTemplate.opsForZSet().remove(ONLINE_KEY, deviceNo);
    }

    /// 节流落库闸门(SET NX EX 1800; 返回 true 表示距上次落库超 30 分钟或首次, 本次应回写台账)
    public boolean tryBeginOnlineWrite(String deviceNo) {
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(LASTWRITE_KEY_PREFIX + deviceNo, "1", Duration.ofSeconds(LASTWRITE_INTERVAL_SECONDS));
        return Boolean.TRUE.equals(acquired);
    }

    /// 消费 ack nonce(SETNX NX EX 300; 重复 → 重放请求, 抛错拒绝)
    ///
    /// 不返回 revoked 而是抛业务异常: 防网络重发误踢真设备(APP 心跳下轮自愈);
    /// 签名不通过的请求不消耗 nonce 空间(防恶意占位), 调用方须在验签之后调用。
    public void consumeAckNonce(String nonce) {
        Boolean first = stringRedisTemplate.opsForValue()
                .setIfAbsent(ACK_NONCE_KEY_PREFIX + nonce, "1", Duration.ofSeconds(ACK_NONCE_TTL_SECONDS));
        if (!Boolean.TRUE.equals(first)) {
            // 设备: 回执重复, 已拒绝(防重放; 心跳下轮自愈)
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.device.virtual.ackDuplicate");
        }
    }

    /// 展示用最后在线时间取值(Redis 优先, DB 兜底: max(DB 值, Redis score); 双空返回 null)
    ///
    /// Redis 冷启丢数据后仍可显示 DB 节流值, DB 长期未落库时以 Redis 实时值为准。
    /// (开源版实体时间统一 OffsetDateTime, epoch 秒按 UTC 解释, 展示层按用户时区渲染)
    public OffsetDateTime resolveLastOnlineTime(OffsetDateTime dbValue, Long redisScore) {
        if (redisScore == null) {
            return dbValue;
        }
        OffsetDateTime redisTime = Instant.ofEpochSecond(redisScore).atOffset(ZoneOffset.UTC);
        if (dbValue == null || redisTime.isAfter(dbValue)) {
            return redisTime;
        }
        return dbValue;
    }

    /// 当前 epoch 秒
    private static long epochNow() {
        return Instant.now().getEpochSecond();
    }
}
