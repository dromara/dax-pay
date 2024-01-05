package cn.bootx.platform.daxpay.service.core.timeout.dao;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 支付单过期处理
 *
 * @author xxm
 * @since 2022/7/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayExpiredTimeRepository {

    private static final String KEY = "payment:pay:expiredtime";

    private final RedisClient redisClient;

    /**
     * 根据 token 存储对应的 ExpiredTokenKey
     */
    public void store(Long paymentId, LocalDateTime expiredTime) {
        long time = LocalDateTimeUtil.timestamp(expiredTime);
        redisClient.zadd(KEY, String.valueOf(paymentId), time);
    }

    /**
     * 获取所有已过期的订单ID
     */
    public Set<String> getExpiredKeys(LocalDateTime expiredTime) {
        long time = LocalDateTimeUtil.timestamp(expiredTime);
        return redisClient.zrangeByScore(KEY, 0L, time);
    }

    /**
     * 获取所有未过期的订单ID. (7天内的订单)
     */
    public Set<String> getNormalKeysBy30Day(){
        LocalDateTime now = LocalDateTime.now();
        long start = LocalDateTimeUtil.timestamp(now);
        long end = LocalDateTimeUtil.timestamp(now.plusDays(30));
        return redisClient.zrangeByScore(KEY, start, end);
    }

    /**
     * 支付完成或者取消支付后, 需要调用这个方法来清除列表里的订单ID
     */
    public void removeKeys(String... keys) {
        if (keys != null && keys.length > 0) {
            redisClient.zremByMembers(KEY, keys);
        }
    }

}
