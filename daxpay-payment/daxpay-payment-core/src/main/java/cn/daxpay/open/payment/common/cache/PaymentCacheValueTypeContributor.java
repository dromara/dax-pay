package cn.daxpay.open.payment.common.cache;

import cn.daxpay.open.payment.route.service.model.PayRouteBundle;
import cn.daxpay.open.platform.capability.cache.core.CacheValueTypeContributor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.TypeFactory;

import java.util.Map;

/// # 支付域 POJO 缓存值类型注册
///
/// 向缓存层声明本模块 POJO 缓存的 cacheName → 值类型映射,
/// L2 Redis 据此构造定型序列化器, 反序列化还原真实类型(而非 LinkedHashMap)。
///
/// 约定: 新增 POJO/复杂类型缓存时必须在此登记, 漏登记会退回 Object.class 反序列化,
/// L2 命中后按方法返回类型使用时抛 ClassCastException(字符串/Boolean/List&lt;String&gt; 等简单类型无需登记)。
/// 泛型容器(如 List&lt;Xxx&gt;)须用 [TypeFactory] 构造携带元素类型的 [JavaType] 登记。
@Component
public class PaymentCacheValueTypeContributor implements CacheValueTypeContributor {

    @Override
    public Map<String, JavaType> getValueTypes(TypeFactory typeFactory) {
        return Map.of(
                // 路由数据包(策略+基础/场景配置聚合)
                "payment:route-bundle", typeFactory.constructType(PayRouteBundle.class));
    }
}
