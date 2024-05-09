package cn.bootx.platform.starter.auth.redis;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sa-Token持久层接口 Redis版 配置
 *
 * @author xxm
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SaTokenRedisProperties.class)
public class SaTokenRedisConfiguration {

    private final SaTokenRedisProperties saTokenDaoRedisProperties;

    private final RedisConnectionFactory connectionFactory;

    @Resource
    private ObjectMapper typeObjectMapper;

    /**
     * 配置信息的前缀
     */
    private static final String ALONE_PREFIX = "sa-token.plugins.redis.alone-redis";

    /**
     * redis连接方式
     */
    protected RedisConnectionFactory redisConnectionFactory() {
        boolean alone = saTokenDaoRedisProperties.isAlone();
        // 获取自定义Redis对象
        RedisProperties aloneRedis = saTokenDaoRedisProperties.getAloneRedis();
        if (alone) {
            RedisConfiguration redisConfig = new RedisStandaloneConfiguration();
            // 单机模式/集群模式
            if (aloneRedis.getCluster() == null || CollUtil.isEmpty(aloneRedis.getCluster().getNodes())) {

                RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
                standaloneConfiguration.setHostName(aloneRedis.getHost());
                standaloneConfiguration.setDatabase(aloneRedis.getDatabase());
                standaloneConfiguration.setPort(aloneRedis.getPort());
                standaloneConfiguration.setPassword(aloneRedis.getPassword());
                redisConfig = standaloneConfiguration;
            }
            else {
                RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
                RedisProperties.Cluster cluster = aloneRedis.getCluster();
                List<RedisNode> redisNodes = cluster.getNodes().stream().map(node -> {
                    List<String> hostAndPort = StrUtil.split(node, ':');
                    if (hostAndPort.size() != 2) {
                        throw new FatalException(1, "Redis Cluster集群配置错误, 无法启动");
                    }
                    return new RedisNode(hostAndPort.get(0), Integer.parseInt(hostAndPort.get(1)));
                }).collect(Collectors.toList());
                clusterConfiguration.setClusterNodes(redisNodes);
                clusterConfiguration.setPassword(aloneRedis.getPassword());
                clusterConfiguration.setMaxRedirects(cluster.getMaxRedirects());
                redisConfig = clusterConfiguration;
            }
            // 2. 连接池配置
            GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
            // pool配置
            RedisProperties.Lettuce lettuce = aloneRedis.getLettuce();
            if (lettuce.getPool() != null) {
                RedisProperties.Pool pool = aloneRedis.getLettuce().getPool();
                // 连接池最大连接数
                poolConfig.setMaxTotal(pool.getMaxActive());
                // 连接池中的最大空闲连接
                poolConfig.setMaxIdle(pool.getMaxIdle());
                // 连接池中的最小空闲连接
                poolConfig.setMinIdle(pool.getMinIdle());
                // 连接池最大阻塞等待时间（使用负值表示没有限制）
                poolConfig.setMaxWait(pool.getMaxWait());
            }
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
                .builder();
            // timeout
            if (aloneRedis.getTimeout() != null) {
                builder.commandTimeout(aloneRedis.getTimeout());
            }
            // shutdownTimeout
            if (lettuce.getShutdownTimeout() != null) {
                builder.shutdownTimeout(lettuce.getShutdownTimeout());
            }
            // 创建Factory对象
            LettuceClientConfiguration clientConfig = builder.poolConfig(poolConfig).build();
            LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
            factory.afterPropertiesSet();
            return factory;
        }
        else {
            // 使用默认的
            return connectionFactory;
        }
    }

    /**
     * string专用
     */
    protected StringRedisTemplate stringRedisTemplate() {
        // 构建StringRedisTemplate
        StringRedisTemplate stringTemplate = new StringRedisTemplate();
        stringTemplate.setConnectionFactory(redisConnectionFactory());
        stringTemplate.afterPropertiesSet();
        return stringTemplate;
    }

    /**
     * object专用
     */
    protected RedisTemplate<String, Object> objectRedisTemplate() {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer(typeObjectMapper);

        // 构建RedisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Sa-Token持久层接口
     */
    @Bean
    @ConditionalOnMissingBean(SaTokenDao.class)
    public SaTokenDao saTokenDaoRedis() {
        SaTokenDaoRedis saTokenDaoRedis = new SaTokenDaoRedis();
        saTokenDaoRedis.setObjectRedisTemplate(objectRedisTemplate());
        saTokenDaoRedis.setStringRedisTemplate(stringRedisTemplate());
        return saTokenDaoRedis;
    }

    /**
     * 骗过编辑器，增加配置文件代码提示
     * @return 配置对象
     */
    @ConfigurationProperties(prefix = ALONE_PREFIX)
    public RedisProperties getSaAloneRedisConfig() {
        return new RedisProperties();
    }

}
