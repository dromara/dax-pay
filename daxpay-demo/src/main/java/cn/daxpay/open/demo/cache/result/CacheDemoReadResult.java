package cn.daxpay.open.demo.cache.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 缓存读取演示结果
///
/// 携带类型探针与命中观测指标, 把缓存序列化行为直接暴露给前端:
/// - [elementType]: 缓存值的实际运行时类型全名。定型序列化生效时为真实实体类型;
///   未注册类型且经 L2 反序列化时为 java.util.LinkedHashMap(类型丢失缺陷现场)
/// - [methodLoads]: 被缓存方法体的真实执行次数(即未命中次数), 连续读取不涨 = 命中
/// - [costMillis]: 本次读取耗时, 未命中(含模拟慢查询 ~300ms)与命中(毫秒级)差异直观
@Data
@Accessors(chain = true)
public class CacheDemoReadResult {

    /// 缓存名
    private String cacheName;

    /// 缓存 key
    private String cacheKey;

    /// 缓存数据(单对象或列表)
    private Object data;

    /// 缓存值的实际运行时类型全名(类型探针核心字段)
    private String elementType;

    /// 期望的类型全名(注册的实体类型)
    private String expectedType;

    /// 实际类型是否与期望一致
    private boolean typeMatched;

    /// 被缓存方法体的真实执行次数(未命中次数)
    private int methodLoads;

    /// 本次读取耗时(毫秒)
    private long costMillis;
}
