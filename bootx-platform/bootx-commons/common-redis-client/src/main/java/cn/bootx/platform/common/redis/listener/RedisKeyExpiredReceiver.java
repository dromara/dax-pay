package cn.bootx.platform.common.redis.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * key过期事件接收
 *
 * @author xxm
 * @since 2022/5/7
 */
@Slf4j
@Component
public class RedisKeyExpiredReceiver extends KeyExpirationEventMessageListener {

    private final List<RedisKeyExpiredListener> redisKeyExpiredListeners;

    public RedisKeyExpiredReceiver(RedisMessageListenerContainer listenerContainer,
            List<RedisKeyExpiredListener> redisKeyExpiredListeners) {
        super(listenerContainer);
        this.redisKeyExpiredListeners = redisKeyExpiredListeners;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        for (RedisKeyExpiredListener redisKeyExpiredListener : redisKeyExpiredListeners) {
            String prefixKey = redisKeyExpiredListener.getPrefixKey();
            if (StrUtil.startWith(expiredKey, prefixKey)) {
                // 去除key前缀
                redisKeyExpiredListener.onMessage(StrUtil.removePrefix(expiredKey, prefixKey));
            }
        }
    }

}
