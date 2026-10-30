package cn.daxpay.open.device.support;

import java.time.Duration;

/// # 周期计数服务
///
/// 供设备域频率限制(激活限流等)使用的自增计数器。
/// Lua 脚本保证 INCR 与 EXPIRE 原子执行（仅在首次创建时设置 TTL），
/// 防止"先 INCR 再判断是否补 EXPIRE"在并发下漏设 TTL 导致 key 永久残留。
///
/// key 的周期标签（分钟 {@code yyyyMMddHHmm} / 日 {@code yyyyMMdd}）由调用方拼入 key，
/// TTL 仅作兜底清理。Redis 异常向上抛出，由调用方决定 fail-open 还是拒绝。
///
/// 自 plus common-redis 内嵌移植(开源版 common-redis 无该基建, 设备域自持一份)。
public interface PeriodCounterService {

    /// 自增 1 并在首次创建时设置 TTL，返回自增后的值
    ///
    /// @param key 计数 key（须已含周期标签）
    /// @param ttl 首次创建时设置的过期时间（兜底清理用）
    /// @return 自增后的计数值
    long increment(String key, Duration ttl);

    /// 自增 delta（金额累计等）并在首次创建时设置 TTL，返回自增后的值
    ///
    /// @param key   计数 key（须已含周期标签）
    /// @param delta 增量（正数）
    /// @param ttl   首次创建时设置的过期时间（兜底清理用）
    /// @return 自增后的计数值
    long incrementBy(String key, long delta, Duration ttl);

    /// 读取当前计数值
    ///
    /// @param key 计数 key
    /// @return 当前值；key 不存在时返回 null
    Long get(String key);
}
