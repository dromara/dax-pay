package cn.bootx.platform.common.redis.listener;

/**
 * Key过期事件
 *
 * @author xxm
 * @since 2022/5/7
 */
public interface RedisKeyExpiredListener {

    /**
     * 要监听的key
     */
    String getPrefixKey();

    /**
     * 事件处理
     * @param key 去除key前缀后的过期key值
     */
    void onMessage(String key);

}
