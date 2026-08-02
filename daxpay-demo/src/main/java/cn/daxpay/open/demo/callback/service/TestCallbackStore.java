package cn.daxpay.open.demo.callback.service;

import cn.daxpay.open.demo.callback.result.TestCallbackRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/// # 测试回调接收记录内存存储
///
/// 演示场景下的回调接收记录暂存地, 进程内存、非持久化。
///
/// 设计取舍:
/// - 仅用于 demo 联调, 不做持久化(重启即清空)
/// - 有界容量 100 条, 超出后丢弃最旧记录, 避免内存膨胀
/// - 使用 {@link ConcurrentLinkedDeque} 保证并发安全
@Slf4j
@Component
public class TestCallbackStore {

    /// 最大保留条数
    private static final int MAX_SIZE = 100;

    private final ConcurrentLinkedDeque<TestCallbackRecord> deque = new ConcurrentLinkedDeque<>();

    /// 追加一条接收记录, 超出容量时淘汰最旧记录
    public void add(TestCallbackRecord record) {
        deque.offerFirst(record);
        while (deque.size() > MAX_SIZE) {
            // 超出容量, 移除最旧(队尾)
            TestCallbackRecord removed = deque.pollLast();
            if (removed == null) {
                break;
            }
        }
    }

    /// 返回全部记录(最新的在前)
    public List<TestCallbackRecord> list() {
        return new ArrayList<>(deque);
    }

    /// 清空全部记录
    public void clear() {
        deque.clear();
        log.info("测试回调接收记录已清空");
    }
}
