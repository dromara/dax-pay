package cn.daxpay.single.service.util;

import cn.daxpay.single.core.exception.UnsupportedAbilityException;
import cn.daxpay.single.service.func.PayStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * 策略工厂工具类
 * @author xxm
 * @since 2024/6/7
 */
@UtilityClass
public class PayStrategyFactory {

    /**
     * 获取策略
     * @param channel 通道编码
     * @param clazz 策略类型
     * @return 策略类
     * @param <T> 需要为  PayStrategy 的子类
     */
    public <T extends PayStrategy> T create(String channel, Class<T> clazz) {
        val beansOfType = SpringUtil.getBeansOfType(clazz);
        return beansOfType.values().stream()
                .filter(strategy -> strategy.getChannel().equals(channel))
                .findFirst()
                .orElseThrow(() -> new UnsupportedAbilityException("不支持的能力"));
    }
}
