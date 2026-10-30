package cn.daxpay.open.device.support;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/// # 周期计数服务实现
///
/// 经 [StringRedisTemplate] 执行 Lua 脚本（见 `lua/period_counter.lua`），
/// 保证 INCRBY 与 EXPIRE 原子执行；计数器属于简单字符串值，
/// 按平台 Redis 使用约束优先使用 StringRedisTemplate。
/// Redis 异常不做捕获，向上抛出由调用方决定 fail-open 或降级策略。
///
/// 自 plus common-redis 内嵌移植(开源版 common-redis 无该基建, 设备域自持一份)。
@Component
@RequiredArgsConstructor
public class PeriodCounterServiceImpl implements PeriodCounterService {

    /// 自增 + 首次创建设 TTL 的原子脚本
    private static final RedisScript<Long> INCR_SCRIPT =
            RedisScript.of(new ClassPathResource("lua/period_counter.lua"), Long.class);

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public long increment(String key, Duration ttl) {
        return this.incrementBy(key, 1L, ttl);
    }

    @Override
    public long incrementBy(String key, long delta, Duration ttl) {
        Long result = stringRedisTemplate.execute(
                INCR_SCRIPT,
                List.of(key),
                String.valueOf(delta),
                String.valueOf(ttl.toSeconds()));
        // 集群/管道等场景理论上不会返回 null, 防御性兜底为 0
        return result == null ? 0L : result;
    }

    @Override
    public Long get(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value == null ? null : Long.parseLong(value);
    }
}
