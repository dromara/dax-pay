package cn.daxpay.open.demo.cache.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 缓存失效广播事件(演示)
///
/// 由 [cn.daxpay.open.demo.cache.listener.DemoCacheInvalidationListener] 订阅集群失效 Topic 记录,
/// 把「本节点收到了失效广播」可视化给前端, 与 L1 状态变化互相印证。
@Data
@Accessors(chain = true)
public class CacheInvalidationEventResult {

    /// 收到通知的时间
    private OffsetDateTime time;

    /// 失效类型: EVICT(按 key 删除) / CLEAR(整缓存清空)
    private String type;

    /// 缓存名
    private String cacheName;

    /// 缓存 key(CLEAR 时为空)
    private String key;
}
