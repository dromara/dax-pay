package cn.daxpay.open.platform.common.redis.configuration;

import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/// # 定时任务分布式锁配置(ShedLock)
///
/// 基于 Redis 实现, 与 [SpringAutoConfiguration] 上的 `@EnableScheduling` 配合,
/// 通过 `@SchedulerLock` 注解保证多节点部署时同一任务仅单节点执行。
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
public class ShedLockConfiguration {

    /// 锁 key 前缀环境名, 用于多应用共用 Redis 时隔离
    private static final String ENVIRONMENT = "daxpay";

    @Bean
    public RedisLockProvider lockProvider(RedisConnectionFactory connectionFactory) {
        return new RedisLockProvider(connectionFactory, ENVIRONMENT);
    }
}
