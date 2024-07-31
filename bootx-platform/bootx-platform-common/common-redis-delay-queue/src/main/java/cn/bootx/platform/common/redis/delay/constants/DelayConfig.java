package cn.bootx.platform.common.redis.delay.constants;

/**
 *
 * @author daify
 * @date 2019-07-26 15:12
 **/
public class DelayConfig {
    /**
     * 睡眠时间
      */
    public static Long SLEEP_TIME = 1000L;

    /**
     * 重试次数
     */
    public static Integer RETRY_COUNT = 5;

    /**
     * 默认超时时间
     */
    public static Long PROCESS_TIME = 5000L;
}
