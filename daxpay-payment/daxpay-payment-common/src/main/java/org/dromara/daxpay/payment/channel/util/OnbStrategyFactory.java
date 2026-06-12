package org.dromara.daxpay.payment.channel.util;

import org.dromara.daxpay.platform.core.exception.business.UnsupportedAbilityException;
import org.dromara.daxpay.payment.channel.strategy.OnbStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

/// # 服务商策略工厂工具类
///
@UtilityClass
public class OnbStrategyFactory {

    /// 获取策略
    /// @param channel 通道编码
    /// @param clazz 策略类型
    /// @return 策略类
    /// @param <T> 需要为  PayStrategy 的子类
    public <T extends OnbStrategy> T create(String channel, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getProduct().getChannel().equals(channel))
                .findFirst()
                // 不支持该能力
                .orElseThrow(() -> new UnsupportedAbilityException("pay.error.unsupportedAbility"));
    }
}

