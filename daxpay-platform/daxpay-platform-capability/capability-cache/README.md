# 缓存模块 (capability-cache)

基于 L1 本地缓存 + L2 Redis 缓存的二级缓存架构，支持跨节点缓存一致性。

## 快速开始

### 基本使用

使用 Spring Cache 注解即可，无需额外配置：

```java
// 查询缓存
@Cacheable(value = "user:info", key = "#userId")
public UserInfo getUserInfo(Long userId) {
    return userMapper.selectById(userId);
}

// 删除缓存
@CacheEvict(value = "user:info", key = "#userId")
public void updateUser(UserInfo userInfo) {
    userMapper.updateById(userInfo);
}

// 清空缓存
@CacheEvict(value = "user:info", allEntries = true)
public void clearUserCache() {
}
```

### 配置项

```yaml
daxpay:
  platform:
    common:
      cache:
        l1:
          default-ttl: 60        # L1 本地缓存默认过期时间（秒）
          maximum-size: 10000    # L1 本地缓存最大容量
        l2:
          default-ttl: 1800      # L2 Redis 缓存默认过期时间（秒）
        secure-prefix: "secure:" # 敏感缓存名前缀，匹配的 L2 value 整包 AES-GCM 加密
        # secure-names: []       # 额外精确敏感 cacheName 列表（可选）
  # 敏感 L2 加密复用 DB 字段加密密钥（全进程同一 SecureAesGcmEncryptor）
  # platform.config.encrypt.enable / keys
```

## 架构设计

### 二级缓存架构

```
┌─────────────────────────────────────────────────────────────┐
│                      应用层 (@Cacheable)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              DaxpayMultiLevelCacheManager                    │
│                    (二级缓存管理器)                           │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
┌─────────────────────────┐     ┌─────────────────────────────┐
│   L1 本地缓存 (Caffeine)  │     │    L2 Redis 缓存            │
│   - 短 TTL (60s)         │     │    - 长 TTL (30min)         │
│   - 节点内生效           │     │    - 跨节点共享             │
│   - LocalCacheRegistry   │     │    - DaxpayRedisCacheManager│
└─────────────────────────┘     └─────────────────────────────┘
```

### 缓存读写流程

**读取流程：**
```
请求 → L1 本地缓存 → 命中返回
                    ↓ 未命中
              L2 Redis 缓存 → 命中 → 回填 L1 → 返回
                              ↓ 未命中
                          查询数据库 → 写入 L1 + L2 → 返回
```

**写入流程：**
```
写入请求 → 写入 L2 Redis → 写入 L1 本地缓存
```

**删除流程：**
```
删除请求 → 删除 L2 Redis → 删除本机 L1 → 发布 RocketMQ 广播 → 其他节点删除 L1
```

### 跨节点一致性

通过 RocketMQ 广播模式实现跨节点缓存同步：

```
┌──────────┐    evict(key)     ┌──────────┐
│  节点 A   │ ────────────────► │  节点 B   │
│          │                   │          │
│ 删除 L1  │ ◄──────────────── │ 删除 L1  │
│ 删除 L2  │    RocketMQ 广播   │          │
│ 发送消息 │                   │ 收到消息  │
└──────────┘                   └──────────┘
```

## 核心类说明

| 类名 | 包路径 | 职责 |
|------|--------|------|
| `DaxpayMultiLevelCache` | core | 二级缓存实现，组合 L1 + L2 |
| `DaxpayMultiLevelCacheManager` | core | 二级缓存管理器，创建 Cache 实例 |
| `DaxpayRedisCache` | core | Redis 缓存实现，支持 null 值 |
| `DaxpayRedisCacheManager` | core | Redis 缓存管理器 |
| `LocalCacheRegistry` | core | 本地缓存注册表，管理 Caffeine 实例 |
| `CacheInvalidationPublisher` | notify.publisher | 缓存失效消息发布者 |
| `CacheInvalidationConsumer` | notify.consumer | 缓存失效消息消费者 |
| `CachingConfiguration` | configuration | 缓存自动配置类 |

## 常见问题

### 1. 为什么需要二级缓存？

**问题**：单层 Redis 缓存在高并发场景下存在性能瓶颈。

**解决**：
- L1 本地缓存减少 Redis 访问，降低网络开销
- L2 Redis 保证跨节点数据一致性
- 短 TTL 的 L1 作为消息丢失时的兜底机制

### 2. 为什么删除操作要广播通知？

**问题**：节点 A 删除缓存后，节点 B 的本地缓存仍然存在。

**解决**：
- 删除操作通过 RocketMQ 广播到所有节点
- 各节点收到消息后删除本地 L1 缓存
- L1 短 TTL 作为消息丢失时的兜底

### 3. 为什么写入操作不广播？

**原因**：
- 写入后，其他节点读取时会从 L2 加载最新值
- 写入广播会增加消息量，但收益不大
- 读取时自动回填 L1，保证数据新鲜度

### 4. 本地缓存 key 为什么必须是字符串？

**问题**：原始 key 可能是 Long、自定义对象等类型，广播消息只能传字符串。

**示例**：
```
本机: localCache.put(Long(1), value)  // key 是 Long 对象
广播: message.setKey("1")              // key 是字符串
远端: localCache.invalidate("1")       // 无法匹配 Long(1)！
```

**解决**：本地缓存统一使用 `String.valueOf(key)` 作为 key。

### 5. 如何批量清除缓存？

使用 `CacheClearService`：

```java
@Autowired
private CacheClearService cacheClearService;

public void clearUserCache() {
    // 清除以 "user:info:" 为前缀的所有缓存
    cacheClearService.clearCacheByPrefix(List.of("user:info:"));
}
```

### 6. 缓存 key 的命名规范？

建议使用 `模块:业务` 格式：

```java
@Cacheable(value = "payment:order", key = "#orderId")
@Cacheable(value = "user:info", key = "#userId")
@Cacheable(value = "merchant:config", key = "#mchNo")
```

**含密钥/证书等敏感数据的缓存名必须以 `secure:` 开头**（见下文「敏感缓存」）。

### 7. 如何处理缓存穿透？

当前实现不缓存 null 值（`disableCachingNullValues()`），如需防止缓存穿透：

```java
@Cacheable(value = "user:info", key = "#userId", unless = "#result == null")
public UserInfo getUserInfo(Long userId) {
    return userMapper.selectById(userId);
}
```

### 8. RocketMQ 消费失败怎么办？

- L1 本地缓存有短 TTL（默认 60s），消息丢失或消费失败时，脏数据会在 TTL 后自动过期
- 消费失败不影响主流程，只记录日志

### 9. 如何查看缓存命中情况？

L1 本地缓存开启了统计（`recordStats()`），可通过 JMX 或自定义接口查看：

```java
@Autowired
private LocalCacheRegistry localCacheRegistry;

public void printCacheStats(String cacheName) {
    var cache = localCacheRegistry.getOrCreate(cacheName);
    var stats = cache.stats();
    log.info("命中次数: {}, 未命中次数: {}", stats.hitCount(), stats.missCount());
}
```

## 敏感缓存（secure:）

通道密钥、商户对接密钥等敏感对象若使用 Spring Cache，**必须**使用 `secure:` 前缀的 cacheName，基础设施会自动对 L2 Redis value 做**整包 AES-GCM 加密**。

### 与 DB 字段加密的区别

| 层 | 粒度 | 说明 |
|----|------|------|
| DB（DataEncryptTypeHandler） | **字段级** | 仅 `privateKey` / `apiKey` 等列密文，其它列明文 |
| Redis L2（secure:） | **整包** | 整个缓存对象 JSON 一次加密，Redis 中不可见任何字段明文 |
| L1 本地 | 明文对象 | 进程内，与普通缓存一致 |

密钥管理复用 `daxpay.platform.config.encrypt`（与 DB 同一 `SecureAesGcmEncryptor` Bean）。

### 业务写法（与普通缓存相同）

```java
// 读：默认 CacheManager，仅 cacheName 用 secure: 前缀
@Cacheable(value = "secure:channel-key:adapay-direct",
           key = "#channelMchNo + ':' + #sandbox")
public AdapayDirectKeyConfig getForPay(String channelMchNo, boolean sandbox) {
    return manager.find(...).orElseThrow(...);
}

// 写：必须同名同 key 失效
@CacheEvict(value = "secure:channel-key:adapay-direct",
            key = "#param.channelMchNo + ':' + #param.sandbox")
public void saveConfig(AdapayDirectKeyConfigParam param) {
    // ...
}
```

命名建议：

```
secure:channel-key:wechat-isv
secure:channel-key:adapay-direct
secure:merchant-credential
secure:platform-encrypt-config
```

### 数据加密未启用时

若 `daxpay.platform.config.encrypt.enable=false`：

- `secure:*` **禁止写 Redis**（L1-only 降级），避免明文密钥落盘
- 启动日志会 warn 提示

### 禁止事项

- 禁止对含密钥的实体使用非 `secure:` 的 cacheName（会明文进 Redis）
- 禁止缓存完整 `*SdkCredential` 到普通 cacheName
- 不需要也不应指定第二个 `cacheManager`：统一用默认二级缓存即可
