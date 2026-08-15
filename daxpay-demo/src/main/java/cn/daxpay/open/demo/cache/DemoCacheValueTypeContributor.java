package cn.daxpay.open.demo.cache;

import cn.daxpay.open.demo.cache.model.CacheDemoProduct;
import cn.daxpay.open.platform.capability.cache.core.CacheValueTypeContributor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.TypeFactory;

import java.util.List;
import java.util.Map;

/// # 缓存演示值类型注册
///
/// [CacheValueTypeContributor] 的最小使用示例:
/// 单对象用 [TypeFactory#constructType] 注册 Class;
/// 泛型容器 List&lt;POJO&gt; 用 [TypeFactory#constructCollectionType] 构造携带元素类型的 [JavaType] 注册
/// (直接注册 List.class 会因类型擦除退化为 List&lt;LinkedHashMap&gt;)。
///
/// 对照实验: 注释掉任一注册行重启后, L2 命中时元素类型会退化为 java.util.LinkedHashMap,
/// 演示页类型探针会显示红色告警 —— 即 2026-07-30 f400b164c 移除实体缓存注解时的缺陷现场。
@Component
public class DemoCacheValueTypeContributor implements CacheValueTypeContributor {

    @Override
    public Map<String, JavaType> getValueTypes(TypeFactory typeFactory) {
        return Map.of(
                // 单对象缓存: 直接注册实体类型
                "demo:cache-product", typeFactory.constructType(CacheDemoProduct.class),
                // 列表缓存: 泛型容器, 必须携带元素类型
                "demo:cache-product-list", typeFactory.constructCollectionType(List.class, CacheDemoProduct.class));
    }
}
