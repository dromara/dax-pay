package cn.daxpay.open.platform.common.redis.lock;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/// # 分布式锁执行器
///
/// 收敛 lock4j [LockTemplate] 的「抢锁 → 判空 → try/finally 释放」模板。
/// 失败语义由调用方控制：硬失败传 [onFail]；软失败用 [tryExecute]/[tryRun]。
///
/// 不绑定业务异常类型与 messageKey，避免 common 层反向依赖业务模块。
@Component
@RequiredArgsConstructor
public class LockExecutor {

    /// 默认锁持有时间（毫秒）
    public static final long DEFAULT_EXPIRE_MS = 10_000L;

    /// 默认抢锁等待时间（毫秒）
    public static final long DEFAULT_WAIT_MS = 200L;

    private final LockTemplate lockTemplate;

    /// 抢锁执行（默认超时）；失败抛 [onFail] 提供的异常
    public <T> T execute(String key, Supplier<T> action, Supplier<? extends RuntimeException> onFail) {
        return execute(key, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, action, onFail);
    }

    /// 抢锁执行；失败抛 [onFail] 提供的异常
    public <T> T execute(String key, long expireMs, long waitMs,
                         Supplier<T> action, Supplier<? extends RuntimeException> onFail) {
        LockInfo lock = lockTemplate.lock(key, expireMs, waitMs);
        if (lock == null) {
            throw onFail.get();
        }
        try {
            return action.get();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// void 版抢锁执行（默认超时）
    public void run(String key, Runnable action, Supplier<? extends RuntimeException> onFail) {
        run(key, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, action, onFail);
    }

    /// void 版抢锁执行
    public void run(String key, long expireMs, long waitMs,
                    Runnable action, Supplier<? extends RuntimeException> onFail) {
        execute(key, expireMs, waitMs, () -> {
            action.run();
            return null;
        }, onFail);
    }

    /// 尝试抢锁执行（默认超时）；未抢到锁时 [TryLockResult#acquired] 为 false
    public <T> TryLockResult<T> tryExecute(String key, Supplier<T> action) {
        return tryExecute(key, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, action);
    }

    /// 尝试抢锁执行；未抢到锁时 [TryLockResult#acquired] 为 false（业务返回 null 仍算已获取）
    public <T> TryLockResult<T> tryExecute(String key, long expireMs, long waitMs, Supplier<T> action) {
        LockInfo lock = lockTemplate.lock(key, expireMs, waitMs);
        if (lock == null) {
            return TryLockResult.notAcquired();
        }
        try {
            return TryLockResult.of(action.get());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 尝试抢锁执行 void（默认超时）；未抢到返回 false
    public boolean tryRun(String key, Runnable action) {
        return tryRun(key, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, action);
    }

    /// 尝试抢锁执行 void；未抢到返回 false
    public boolean tryRun(String key, long expireMs, long waitMs, Runnable action) {
        return tryExecute(key, expireMs, waitMs, () -> {
            action.run();
            return null;
        }).acquired();
    }
}
