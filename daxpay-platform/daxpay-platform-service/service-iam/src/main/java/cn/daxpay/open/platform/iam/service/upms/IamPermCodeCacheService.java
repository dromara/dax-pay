package cn.daxpay.open.platform.iam.service.upms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/// # 权限码缓存失效服务
///
/// 统一管理 `iam:user-perm-codes` 多级缓存(L1 Caffeine + L2 Redis + Artemis 广播)的失效时机。
///
/// ## 为什么不用 @CacheEvict 注解
/// 注解 evict 在方法返回后、事务提交前触发: 失效瞬间其他节点可能从 DB 回填**未提交的旧数据**,
/// 脏缓存将存活至 L2 TTL 到期(默认 30 分钟)。本服务统一把失效注册到事务提交后(afterCommit),
/// 提交与失效严格定序, 消除脏回填窗口; 事务回滚时不失效(数据未变, 无需失效)。
/// evict/clear 经 [CacheManager] 走与注解完全相同的 [MultiLevelCache](org.springframework.cache.Cache) 路径, 广播语义不变。
@Slf4j
@Service
@RequiredArgsConstructor
public class IamPermCodeCacheService {

    /// 用户权限码缓存名, 与 [UserRolePremService#findAllCodesByUser] 的 @Cacheable 一致
    public static final String CACHE_NAME = "iam:user-perm-codes";

    private final CacheManager cacheManager;

    /// 事务提交后失效单个用户的权限码缓存(用户-角色精确变更用)
    public void evictUserAfterCommit(Long userId) {
        this.registerAfterCommit(() -> this.doEvictUser(userId));
    }

    /// 事务提交后全清权限码缓存(角色授权/权限码扫描等无法按 key 反查的批量变更用)
    public void evictAllAfterCommit() {
        this.registerAfterCommit(this::doEvictAll);
    }

    /// 注册提交后失效; 无事务上下文时立即失效兜底
    private void registerAfterCommit(Runnable evictor) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            evictor.run();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                evictor.run();
            }
        });
    }

    private void doEvictUser(Long userId) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.evict(userId);
        }
    }

    private void doEvictAll() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
        }
    }
}
