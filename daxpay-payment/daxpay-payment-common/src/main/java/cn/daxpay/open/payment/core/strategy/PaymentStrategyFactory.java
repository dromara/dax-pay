package cn.daxpay.open.payment.core.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.daxpay.open.payment.core.strategy.PaymentStrategy;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.core.strategy.ProductStrategySupport;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/// # 策略工厂工具类
///
@UtilityClass
public class PaymentStrategyFactory {

    /// 根据通道编码获取策略
    /// @param channel 通道编码
    /// @param clazz 策略类型
    /// @return 策略类
    /// @param <T> 需要为 PaymentStrategy 的子类
    public <T extends PaymentStrategy> T create(String channel, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getProduct().getChannel().equals(channel))
                .findFirst()
                // 不支持的能力: {0}
                .orElseThrow(() -> new UnsupportedAbilityException("pay.error.unsupportedAbilityWithDetail", channel));
    }

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

    /// 判断传入通道的策略是否存在
    public <T extends PaymentStrategy> boolean existsByChannel(String channel, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream().anyMatch(strategy -> strategy.getProduct().getChannel().equals(channel));
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

