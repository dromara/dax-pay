package cn.bootx.platform.common.redis.redisson;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 项目启动时Redisson替换RedissonClient的实现, Redisson连接失败是会导致项目无法启动, 选择改为项目启动成功后, 替换掉原有的Bean
 *
 * @author xxm
 * @since 2022/11/30
 */
@Component
@ConditionalOnBean(name = "org.redisson.Redisson")
@RequiredArgsConstructor
public class RedissonLoadListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ConfigurableApplicationContext configurableApplicationContext;

    private final RedisProperties redisProperties;

    private final String REDIS_PREFIX = "redis://";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 单机/集群
        Config config = new Config();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (cluster == null || CollUtil.isEmpty(cluster.getNodes())) {
            initSingleConfig(config.useSingleServer());
        }
        else {
            initClusterConfig(config.useClusterServers());
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Redisson.class);
        beanDefinitionBuilder.addConstructorArgValue(config);

        String redissonClientName = StrUtil.lowerFirst(RedissonClient.class.getSimpleName());

        // 创建一个Redisson对象, 替换掉原有的对象
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext;
        beanDefinitionRegistry.removeBeanDefinition(redissonClientName);
        beanDefinitionRegistry.registerBeanDefinition(redissonClientName, beanDefinitionBuilder.getBeanDefinition());

        // 这里相当于初始化加载使用
        configurableApplicationContext.getBean(redissonClientName);
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
