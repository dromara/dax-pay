package org.dromara.daxpay.payment.admin.controller.demo.store;

import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.payment.admin.controller.demo.result.DemoMessageResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/// # Artemis 演示消息内存存储
///
/// 演示场景下的消费记录暂存地，进程内存、非持久化。
///
/// 设计取舍：
/// - 仅用于 demo 演示，不做持久化（重启即清空）
/// - 有界容量 100 条，超出后丢弃最旧记录，避免内存膨胀
/// - 使用 {@link ConcurrentLinkedDeque} 保证生产/消费线程安全
@Slf4j
@Component
public class DemoMessageStore {

    /// 最大保留条数
    private static final int MAX_SIZE = 100;

    private final ConcurrentLinkedDeque<DemoMessageResult> deque = new ConcurrentLinkedDeque<>();

    /// 追加一条消费记录，超出容量时淘汰最旧记录
    public void add(DemoMessageResult result) {
        deque.offerFirst(result);
        while (deque.size() > MAX_SIZE) {
            // 超出容量，移除最旧（队尾）
            DemoMessageResult removed = deque.pollLast();
            if (removed == null) {
                break;
            }
        }
    }

    /// 返回全部记录（最新的在前）
    public List<DemoMessageResult> list() {
        return new ArrayList<>(deque);
    }

    /// 清空全部记录
    public void clear() {
        deque.clear();
        log.info("演示消息记录已清空");
    }
}
