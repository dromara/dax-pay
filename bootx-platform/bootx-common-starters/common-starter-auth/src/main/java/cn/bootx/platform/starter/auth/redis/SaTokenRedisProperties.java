package cn.bootx.platform.starter.auth.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * sa-token Redis 插件配置
 *
 * @author xxm
 * @since 2021/8/2
 */
@ConfigurationProperties(prefix = "sa-token.plugins.redis")
public class SaTokenRedisProperties {

    /**
     * 是否开启独立redis配置
     */
    private boolean alone;

    /**
     * 独立redis配置
     */
    private RedisProperties aloneRedis;

    public boolean isAlone() {
        return alone;
    }

    public void setAlone(boolean alone) {
        this.alone = alone;
    }

    public RedisProperties getAloneRedis() {
        return aloneRedis;
    }

    public void setAloneRedis(RedisProperties aloneRedis) {
        this.aloneRedis = aloneRedis;
    }

}
