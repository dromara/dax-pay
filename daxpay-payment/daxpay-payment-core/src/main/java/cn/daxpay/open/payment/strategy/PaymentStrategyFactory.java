package cn.daxpay.open.payment.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/// # 策略工厂工具类
///
@UtilityClass
public class PaymentStrategyFactory {

    /// 根据产品编码获取策略
    /// @param product 产品编码
    /// @param clazz 策略类型
    /// @return 策略类
    /// @param <T> 需要为 PaymentStrategy 的子类
    public <T extends PaymentStrategy> T createByProduct(String product, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getProduct().getCode().equals(product))
                .findFirst()
                // 不支持的能力: {0}
                .orElseThrow(() -> new UnsupportedAbilityException("pay.error.unsupportedAbilityWithDetail", product));
    }

    /// 软查找: 未匹配返回 [Optional#empty], 不抛异常
    ///
    /// 用于"未实现策略的 product 静默跳过"场景(如通道商户清理 SPI,
    /// 未实现清理策略的通道留孤儿数据,业务无影响)。
    public <T extends PaymentStrategy> Optional<T> findOptionallyByProduct(String product, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getProduct().getCode().equals(product))
                .findFirst();
    }

    /// 判断传入产品的策略是否存在
    public <T extends PaymentStrategy> boolean existsByProduct(String product, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream().anyMatch(strategy -> strategy.getProduct().getCode().equals(product));
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
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return new ArrayList<>(beansOfType.values());
    }
}

