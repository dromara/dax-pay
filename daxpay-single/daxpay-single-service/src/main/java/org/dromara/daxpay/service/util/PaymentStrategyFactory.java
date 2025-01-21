package org.dromara.daxpay.service.util;

import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.service.strategy.PaymentStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略工厂工具类
 * @author xxm
 * @since 2024/6/7
 */
@UtilityClass
public class PaymentStrategyFactory {

    /**
     * 获取策略
     * @param channel 通道编码
     * @param clazz 策略类型
     * @return 策略类
     * @param <T> 需要为  PaymentStrategy 的子类
     */
    public <T extends PaymentStrategy> T create(String channel, Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getChannel().equals(channel))
                .findFirst()
                .orElseThrow(() -> new UnsupportedAbilityException("不支持的能力"));
    }

    /**
     * 根据指定类型获取策略组
     * @param clazz 策略类型
     * @return 策略类列表
     * @param <T> 需要为  PaymentStrategy 的子类
     */
    public <T extends PaymentStrategy> List<T> createGroup(Class<T> clazz) {
        var beansOfType = SpringUtil.getBeansOfType(clazz);
        return new ArrayList<>(beansOfType.values());
    }


}
