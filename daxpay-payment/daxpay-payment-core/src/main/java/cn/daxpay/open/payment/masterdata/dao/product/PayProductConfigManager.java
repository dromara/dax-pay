package cn.daxpay.open.payment.masterdata.dao.product;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// # 支付产品配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayProductConfigManager extends BaseManager<PayProductConfigMapper, PayProductConfig> {

    /// 根据产品编码查询
    public Optional<PayProductConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(PayProductConfig::getProduct, product)
                .oneOpt();
    }

    /// 批量查询产品 → activeEnv 映射(缺省记录不在 map 中, 调用方按 prod 处理)
    public Map<String, String> mapActiveEnvByProducts(Collection<String> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyMap();
        }
        return lambdaQuery()
                .in(PayProductConfig::getProduct, products)
                .list()
                .stream()
                .collect(Collectors.toMap(PayProductConfig::getProduct, PayProductConfig::getActiveEnv, (a, b) -> a));
    }

    /// 读取产品当前是否沙箱(无配置视为生产)
    ///
    /// 缓存说明: 路由环境一致性校验高频调用, 配置低频修改。写侧通过 @CacheEvict 失效。
    @Cacheable(value = "payment:product-sandbox", key = "#product")
    public boolean isSandboxActive(String product) {
        return findByProduct(product)
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
    }
}
