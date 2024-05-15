package cn.bootx.platform.common.redis.code;

/**
 * redis常量
 *
 * @author xxm
 * @since 2022/5/7
 */
public interface RedisCode {

    /** 发布订阅主题前缀 */
    String TOPIC_PREFIX = "bootx:redis:topic:";

    /** 监听的消息订阅内容 */
    String TOPIC_PATTERN_TOPIC = "bootx:redis:topic:**";

}
