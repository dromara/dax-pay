package cn.daxpay.open.platform.common.redis.lock;

/// # 尝试抢锁执行结果
///
/// 区分「未抢到锁」与「抢到锁但业务返回 null」，避免 [java.util.Optional] 语义混淆。
///
/// @param acquired 是否成功获取锁
/// @param value 业务返回值（仅 [acquired] 为 true 时有意义，可为 null）
public record TryLockResult<T>(boolean acquired, T value) {

    /// 未抢到锁
    public static <T> TryLockResult<T> notAcquired() {
        return new TryLockResult<>(false, null);
    }

    /// 已持锁并执行完毕（value 允许为 null）
    public static <T> TryLockResult<T> of(T value) {
        return new TryLockResult<>(true, value);
    }
}
