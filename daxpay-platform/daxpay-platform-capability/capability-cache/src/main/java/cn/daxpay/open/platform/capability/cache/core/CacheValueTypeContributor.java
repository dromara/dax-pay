package cn.daxpay.open.platform.capability.cache.core;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.TypeFactory;

import java.util.Map;

/// # 缓存值类型贡献者
///
/// 业务模块实现此接口, 向缓存层声明 POJO 缓存的 cacheName → 值类型映射。
/// L2 Redis 为已注册的缓存名构造定型序列化器
/// ([org.springframework.data.redis.serializer.JacksonJsonRedisSerializer]),
/// 反序列化时按声明的类型还原真实对象, 而非 LinkedHashMap。
///
/// ## 背景
///
/// 默认值序列化器 [cn.daxpay.open.platform.common.redis.serializer.JacksonRedisSerializer]
/// 反序列化固定读 `Object.class`, POJO 缓存从 L2 命中会得到 LinkedHashMap,
/// 被 Spring 缓存代理按方法返回类型使用时抛 ClassCastException
/// (2026-07-30 f400b164c 曾因此整体移除实体缓存注解, 本接口是治本方案)。
///
/// ## 使用约束
///
/// - 仅 POJO/复杂类型缓存需要注册; 字符串/Boolean/List&lt;String&gt; 等简单类型无需注册
/// - 同一 cacheName 的值类型必须全局唯一, 重复注册会在启动期快速失败
/// - 泛型容器须用 [TypeFactory] 构造携带元素类型的 [JavaType] 注册,
///   直接注册 `List.class` 会因类型擦除退化为 List&lt;LinkedHashMap&gt;
///   (列表本身不炸, 遍历元素时才 ClassCastException, 比纯 POJO 更隐蔽)
public interface CacheValueTypeContributor {

    /// 声明 cacheName → 缓存值类型映射
    ///
    /// @param typeFactory 平台 ObjectMapper 的类型工厂, 由装配方传入(勿自行静态获取, 保证与运行时 mapper 同源)
    Map<String, JavaType> getValueTypes(TypeFactory typeFactory);
}
