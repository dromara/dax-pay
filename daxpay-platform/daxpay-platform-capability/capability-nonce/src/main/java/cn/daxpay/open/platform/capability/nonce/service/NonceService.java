package cn.daxpay.open.platform.capability.nonce.service;

import cn.daxpay.open.platform.capability.nonce.config.NonceVerificationConfigProvider;
import cn.daxpay.open.platform.capability.nonce.result.NonceResult;
import cn.daxpay.open.platform.core.exception.NonceInvalidException;
import cn.daxpay.open.platform.core.exception.TimestampExpiredException;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # Nonce生成与验证服务
///
/// 配置来源（依赖倒置）: 若容器中注册了 [NonceVerificationConfigProvider] 实现，
/// 则从其读取 nonce 有效期等参数；否则使用默认值 300 秒，保持向后兼容。
@Slf4j
@Service
public class NonceService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectProvider<NonceVerificationConfigProvider> configProviderProvider;

    private static final String NONCE_PREFIX = "nonce:";
    private static final int DEFAULT_TIMEOUT_SECONDS = 300;

    public NonceService(StringRedisTemplate stringRedisTemplate,
                        ObjectProvider<NonceVerificationConfigProvider> configProviderProvider) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.configProviderProvider = configProviderProvider;
    }

    /// 从配置提供者读取 nonce 有效期，无实现时回退默认值
    private int resolveTimeout() {
        NonceVerificationConfigProvider provider = configProviderProvider.getIfAvailable();
        if (provider != null) {
            int timeout = provider.getNonceTimeoutSeconds();
            if (timeout > 0) {
                return timeout;
            }
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }

    /// 生成Nonce并存入Redis，有效期从配置读取
    public NonceResult generate() {
        return generate(resolveTimeout());
    }

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
