package cn.daxpay.open.platform.capability.cache.core;

import cn.daxpay.open.platform.capability.cache.notify.publisher.CacheInvalidationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.concurrent.Callable;

/// # 二级缓存实现
///
/// 组合 L1 本地缓存（Caffeine）和 L2 Redis 缓存，实现多级缓存架构。
///
/// 设计要点：
/// - L2 Redis 是共享缓存主层，负责跨节点数据共享
/// - L1 Caffeine 是性能加速层，仅在本节点生效
/// - 缓存失效通过 Artemis 广播通知其他节点删除本地 L1
/// - 本地缓存 key 必须统一使用字符串形式，保证跨节点广播删除一致性
/// - 敏感缓存（secure:）在数据加密未启用时仅使用 L1，禁止明文写 Redis；读侧同样跳过 L2（防历史明文/密文脏数据），见 [#isL1Only]
/// - 两层开关：[#cacheEnabled] 总开关关 L1+L2 一并 NoOp；[#l1Enabled] 在总开关开启时可单独关 L1 留 L2
@Slf4j
public class MultiLevelCache implements Cache {

    private final String name;

    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache;

    private final Cache redisCache;

    private final CacheInvalidationPublisher publisher;

    /// 是否为敏感缓存名
    private final boolean secureCache;

    /// 敏感缓存是否允许写/读 L2（依赖数据加密已启用）
    private final boolean secureL2Enabled;

    /// 缓存总开关，false 时 L1+L2 一并 NoOp（直接穿透到方法）
    ///
    /// 由 [cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties.Cache#isEnabled] 控制。
    private final boolean cacheEnabled;

    /// L1 本地缓存单独开关，false 时跳过 L1 读写删，仅保留 L2 Redis（纯 Redis 模式）
    ///
    /// 由 [cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties.Cache.L1#isEnabled] 控制。
    /// 仅在 [#cacheEnabled] 为 true 时有意义；总开关关闭时 L1 随之关闭。
    private final boolean l1Enabled;

    public MultiLevelCache(String name,
                           LocalCacheRegistry localCacheRegistry,
                           Cache redisCache,
                           CacheInvalidationPublisher publisher,
                           boolean secureCache,
                           boolean secureL2Enabled,
                           boolean cacheEnabled,
                           boolean l1Enabled) {
        this.name = name;
        this.localCache = localCacheRegistry.getOrCreate(name);
        this.redisCache = redisCache;
        this.publisher = publisher;
        this.secureCache = secureCache;
        this.secureL2Enabled = secureL2Enabled;
        this.cacheEnabled = cacheEnabled;
        this.l1Enabled = l1Enabled;
        // 仅在缓存启用时才提示敏感缓存降级，关闭态不打扰日志
        if (cacheEnabled && secureCache && !secureL2Enabled) {
            log.warn("敏感缓存 [{}] 因未启用数据加密，仅使用 L1 本地缓存，不写 Redis", name);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.localCache;
    }

    /// 是否跳过 L2（敏感且未开加密）
    ///
    /// 注意这不是普通缓存的常规形态, 而是**敏感缓存的安全降级保护**:
    /// 缓存名命中敏感名单(secure: 前缀或 secureNames)且平台数据加密未启用时,
    /// 敏感数据被禁止明文写 Redis —— 写侧仅写 L1 本机内存([put] 内分支),
    /// 读侧连 L2 也不查([get] 内短路)。一旦启用数据加密即恢复 L1+L2 双层(仅 L2 value 为整包密文)。
    ///
    /// 普通缓存永远不进入本分支, 其读路径只有两种: L1+L2 双层 / 仅 L2(纯 Redis 模式, l1.enabled=false)。
    private boolean isL1Only() {
        return this.secureCache && !this.secureL2Enabled;
    }

    /// 读取缓存，优先从 L1 本地缓存读取，未命中则从 L2 Redis 读取并回填 L1
    ///
    /// 读取流程：
    /// - 总开关关闭时直接返回 null（穿透到方法）
    /// - L1 开启时查本地缓存，命中直接返回
    /// - L1 关闭或未命中则查 L2 Redis
    /// - L2 命中且 L1 开启则回填 L1
    /// - 全未命中返回 null
    ///
    /// 敏感缓存且未启用加密时跳过 L2，仅查 L1（L1 也关则穿透）
    @Override
    public ValueWrapper get(Object key) {
        // 缓存总开关关闭：直接穿透，不查任何一层
        if (!this.cacheEnabled) {
            return null;
        }
        String localKey = this.toLocalKey(key);
        // L1 单独开关：开启时先查本地缓存
        if (this.l1Enabled) {
            Object localValue = this.localCache.getIfPresent(localKey);
            if (localValue != null) {
                return () -> localValue;
            }
        }

        // 敏感缓存未开加密：禁止读 Redis —— L2 里可能是三类脏数据:
        // ① 曾启用加密后关闭残留的密文(当前无法解密) ② 加密启用前明文写入的历史数据(无保护) ③ 多节点密钥版本不一致的密文(解密失败)。
        // 宁可穿透到方法重查, 也不能把不对的数据当缓存值返回(fail-safe)
        if (this.isL1Only()) {
            log.debug("敏感缓存 L1-only 未命中: cacheName={}, key={}, l1Enabled={}", this.name, key, this.l1Enabled);
            return null;
        }

        ValueWrapper redisValue = this.redisCache.get(key);
        if (redisValue != null) {
            // L1 开启才回填本地，关闭时仅返回 L2 值
            if (this.l1Enabled) {
                this.localCache.put(localKey, Objects.requireNonNull(redisValue.get()));
            }
            return redisValue;
        }

        log.debug("缓存未命中: cacheName={}, key={}", this.name, key);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = this.get(key);
        if (wrapper != null) {
            Object value = wrapper.get();
            if (value != null && !type.isInstance(value)) {
                throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
            }
            return type.cast(value);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = this.get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        try {
            T value = valueLoader.call();
            this.put(key, value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load value for key: " + key, e);
        }
    }

    /// 写入缓存
    ///
    /// - L1 开启：写 L2 Redis + L1 本地
    /// - L1 关闭：仅写 L2 Redis（纯 Redis 模式）
    /// - 敏感缓存且未启用加密时仅写 L1（L1 也关则不缓存）
    @Override
    public void put(Object key, Object value) {
        // 缓存总开关关闭：直接穿透，不写任何一层
        if (!this.cacheEnabled) {
            return;
        }
        if (value == null) {
            log.debug("缓存值为空，跳过写入: cacheName={}, key={}", this.name, key);
            return;
        }
        String localKey = this.toLocalKey(key);
        if (this.isL1Only()) {
            // 敏感缓存禁 Redis(加密未启用, 禁止敏感数据明文落盘): 仅写 L1 本机内存; L1 也关则该缓存实质不缓存(每次全穿透)
            if (this.l1Enabled) {
                this.localCache.put(localKey, value);
                log.debug("写入 L1-only 敏感缓存: cacheName={}, key={}", this.name, key);
            } else {
                log.debug("敏感缓存 L1 已关闭且禁写 Redis，跳过写入: cacheName={}, key={}", this.name, key);
            }
            return;
        }
        this.redisCache.put(key, value);
        if (this.l1Enabled) {
            this.localCache.put(localKey, value);
        }
        log.debug("写入缓存: cacheName={}, key={}, l1Enabled={}", this.name, key, this.l1Enabled);
    }

    /// 删除缓存
    ///
    /// 删除流程：
    /// - 删除 L2 Redis（共享层）
    /// - L1 开启时删除本机 L1
    /// - 发布 Artemis 广播消息（始终发，其他节点可能 L1 开启）
    ///
    /// 注意：广播不因本机 L1 关闭而跳过——集群中其他节点可能 L1 开启，仍需通知其删除本地缓存
    @Override
    public void evict(Object key) {
        // 总开关仅控制本机读写穿透, 失效操作(L2 删除 + 集群广播)始终执行:
        // 避免总开关关闭期间写侧改配置后, Redis 残留旧值 + 其他节点 L1 未失效, 重新开启后读到脏数据。
        String localKey = this.toLocalKey(key);
        // 仍尝试删 Redis，清理可能存在的历史数据
        this.redisCache.evict(key);
        if (this.l1Enabled) {
            this.localCache.invalidate(localKey);
        }
        log.debug("删除缓存: cacheName={}, key={}, l1Enabled={}", this.name, key, this.l1Enabled);
        this.publisher.publishEvict(this.name, localKey);
    }

    /// 清空缓存
    ///
    /// 清空 L2 Redis；L1 开启时清空本机 L1；始终广播（其他节点可能 L1 开启）
    @Override
    public void clear() {
        // 总开关仅控制本机读写穿透, 失效操作(L2 清空 + 集群广播)始终执行(理由同 evict)
        this.redisCache.clear();
        if (this.l1Enabled) {
            this.localCache.invalidateAll();
        }
        log.debug("清空缓存: cacheName={}, l1Enabled={}", this.name, this.l1Enabled);
        this.publisher.publishClear(this.name);
    }

    /// 将原始 key 转换为本地缓存使用的标准化字符串 key
    ///
    /// 为什么必须统一使用字符串 key：
    /// - 本地缓存原始 key 可能是任意对象类型（Long、String、自定义对象等）
    /// - Artemis 广播消息中的 key 只能是字符串
    /// - 如果本地缓存使用原始对象作为 key，广播消息使用字符串，会导致跨节点删除失败
    /// - 例如：本机 key=Long(1)，广播 key="1"，远端无法匹配
    ///
    /// 因此本地缓存必须统一使用字符串 key，与广播消息保持一致。
    ///
    /// @param key 原始缓存 key
    /// @return 标准化后的字符串 key
    private String toLocalKey(Object key) {
        if (key == null) {
            return "null";
        }
        return String.valueOf(key);
    }
}
