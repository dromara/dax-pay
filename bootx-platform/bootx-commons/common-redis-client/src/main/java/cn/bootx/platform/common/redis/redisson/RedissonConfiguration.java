package cn.bootx.platform.common.redis.redisson;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 自动配置
 *
 * @author xxm
 * @since 2022/12/19
 */
@Configuration
@ConditionalOnBean(name = "org.redisson.Redisson")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@AllArgsConstructor
public class RedissonConfiguration {

    private final RedisProperties redisProperties;

    private final String REDIS_PREFIX = "redis://";

    /**
     * 配置一个临时的对象到spring容器中，不使用
     * @return 一个RedissonClient的实现
     */
    @Bean
    public RedissonClient redissonClient() {
        return new RedissonClientTemporary();

    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redisson() {
        // 单机/集群
        Config config = new Config();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (cluster == null || CollUtil.isEmpty(cluster.getNodes())) {
            initSingleConfig(config.useSingleServer());
        }
        else {
            initClusterConfig(config.useClusterServers());
        }

        return Redisson.create(config);
    }

    /**
     * 单节点模式
     */
    private void initSingleConfig(SingleServerConfig singleServerConfig) {
        singleServerConfig.setAddress(REDIS_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort())
            .setDatabase(redisProperties.getDatabase())
            .setPassword(redisProperties.getPassword());
    }

    /**
     * 集群模式
     */
    private void initClusterConfig(ClusterServersConfig clusterServersConfig) {
        String[] nodes = redisProperties.getCluster()
            .getNodes()
            .stream()
            .map(node -> REDIS_PREFIX + node)
            .toArray(String[]::new);
        clusterServersConfig.setPassword(redisProperties.getPassword()).addNodeAddress(nodes);
    }

}
