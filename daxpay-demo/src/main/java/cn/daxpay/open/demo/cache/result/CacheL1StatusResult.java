package cn.daxpay.open.demo.cache.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # L1 本地缓存状态(演示)
///
/// 展示某缓存名在本节点 L1(Caffeine) 中当前的 key 集合,
/// 用于观察失效广播到达前后本机 L1 的变化(条目消失 = 通知生效)。
@Data
@Accessors(chain = true)
public class CacheL1StatusResult {

    /// 缓存名
    private String cacheName;

    /// L1 当前 key 列表(标准化字符串 key, 与广播消息中的 key 一致)
    private List<String> keys;

    /// L1 当前条目数
    private long size;
}
