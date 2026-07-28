package cn.daxpay.open.payment.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 策略工厂工具类
///
/// 内部维护按策略类型（Class）分组的 `productCode → bean` 索引，懒加载且仅构建一次。
/// Strategy Bean 是 Spring 单例，启动后固定不变，缓存线程安全。
/// 改造前每次调用都走 `SpringUtil.getBeansOfType` 全量扫描；改造后降为 O(1) Map 查找。
@UtilityClass
public class PaymentStrategyFactory {

    /// 按 Class 维度的索引缓存：首次访问时通过 computeIfAbsent 原子构建
    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, ? extends PaymentStrategy>> INDEX = new ConcurrentHashMap<>();

    /// 构建指定 Class 的 (productCode → bean) 索引，仅执行一次 getBeansOfType
    @SuppressWarnings("unchecked")
    private static <T extends PaymentStrategy> ConcurrentHashMap<String, T> index(Class<T> clazz) {
        return (ConcurrentHashMap<String, T>) INDEX.computeIfAbsent(clazz, c -> {
            // 显式 raw cast 使 stream 能推断出 PaymentStrategy 类型
            Map<String, ? extends PaymentStrategy> beans =
                    SpringUtil.getBeansOfType((Class<? extends PaymentStrategy>) c);
            return beans.values().stream()
                    .collect(Collectors.toMap(
                            s -> s.getProduct().getCode(),
                            Function.identity(),
                            (a, b) -> a,
                            ConcurrentHashMap::new));
        });
    }

    /// 根据产品编码获取策略
    /// @param product 产品编码
    /// @param clazz 策略类型
    /// @return 策略类
    /// @param <T> 需要为 PaymentStrategy 的子类
    public <T extends PaymentStrategy> T createByProduct(String product, Class<T> clazz) {
        T strategy = index(clazz).get(product);
        if (strategy == null) {
            // 不支持的能力: {0}
            throw new UnsupportedAbilityException("pay.error.unsupportedAbilityWithDetail", product);
        }
        return strategy;
    }

    /// 软查找: 未匹配返回 [Optional#empty], 不抛异常
    ///
    /// 用于"未实现策略的 product 静默跳过"场景(如通道商户清理 SPI,
    /// 未实现清理策略的通道留孤儿数据,业务无影响)。
    public <T extends PaymentStrategy> Optional<T> findOptionallyByProduct(String product, Class<T> clazz) {
        return Optional.ofNullable(index(clazz).get(product));
    }

    /// 判断传入产品的策略是否存在
    public <T extends PaymentStrategy> boolean existsByProduct(String product, Class<T> clazz) {
        return index(clazz).containsKey(product);
    }

    /// 判断产品是否支持指定支付渠道（微信 / 支付宝 / 银联）
    public boolean productSupportsProvider(String productCode, PayProviderEnum provider) {
        if (provider == null || !existsByProduct(productCode, AbsProductStrategy.class)) {
            return false;
        }
        return ProductStrategySupport.supportsPayProvider(
                createByProduct(productCode, AbsProductStrategy.class), provider);
    }

    /// 根据指定类型获取策略组
    /// @param clazz 策略类型
    /// @return 策略类列表
    /// @param <T> 需要为 PaymentStrategy 的子类
    public <T extends PaymentStrategy> List<T> createGroup(Class<T> clazz) {
        return new ArrayList<>(index(clazz).values());
    }
}