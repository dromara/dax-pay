package org.dromara.daxpay.platform.capability.nonce.service;

import org.dromara.daxpay.platform.capability.nonce.result.NonceResult;
import org.dromara.daxpay.platform.core.exception.NonceInvalidException;
import org.dromara.daxpay.platform.core.exception.TimestampExpiredException;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # Nonce生成与验证服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class NonceService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String NONCE_PREFIX = "nonce:";

    /// 生成Nonce并存入Redis
    /// @param timeout nonce有效期（秒）
    public NonceResult generate(int timeout) {
        String nonce = UUID.fastUUID().toString(true);
        long timestamp = System.currentTimeMillis();
        stringRedisTemplate.opsForValue().set(NONCE_PREFIX + nonce, "1", Duration.ofSeconds(timeout));
        return new NonceResult()
                .setNonce(nonce)
                .setTimestamp(timestamp);
    }

    /// 生成Nonce，使用默认5分钟有效期
    public NonceResult generate() {
        return generate(300);
    }

    /// 验证Nonce和时间戳
    /// @param nonce nonce值
    /// @param timestamp 请求时间戳（毫秒）
    /// @param timestampTolerance 时间戳允许偏差（秒）
    public void verify(String nonce, long timestamp, int timestampTolerance) {
        // 校验时间戳偏差
        long now = System.currentTimeMillis();
        long diffMillis = Math.abs(now - timestamp);
        long toleranceMillis = timestampTolerance * 1000L;
        if (diffMillis > toleranceMillis) {
            log.warn("请求时间戳超出允许范围, nonce: {}, 偏差: {}ms, 允许: {}ms", nonce, diffMillis, toleranceMillis);
            throw new TimestampExpiredException();
        }

        // 校验nonce是否存在（一次性消费）
        String key = NONCE_PREFIX + nonce;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            log.warn("Nonce无效或已过期, nonce: {}", nonce);
            throw new NonceInvalidException();
        }

        // 删除nonce，确保一次性消费
        stringRedisTemplate.delete(key);
    }

}

