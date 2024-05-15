package cn.bootx.platform.common.redis.listener;

/**
 * redis订阅消息
 *
 * @author xxm
 * @since 2022/5/7
 */
public interface RedisTopicListener<T> {

    /**
     * 订阅主题名称
     */
    String getTopic();

    /**
     * 消息处理
     * @param obj 消息对象
     */
    void onMessage(T obj);

}
