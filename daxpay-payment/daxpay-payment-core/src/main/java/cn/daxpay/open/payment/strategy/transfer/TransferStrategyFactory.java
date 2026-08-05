package cn.daxpay.open.payment.strategy.transfer;

import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 转账策略工厂工具类
///
/// 内部维护 `channel → AbsTransferStrategy bean` 索引，懒加载且仅构建一次。
/// 转账按通道（而非支付产品）选型，与支付域 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory]
/// 解耦，避免支付工厂按 product 索引扫描转账策略时报错。
@UtilityClass
public class TransferStrategyFactory {

    /// 通道 → 策略索引缓存：首次访问时原子构建
    private static final ConcurrentHashMap<String, AbsTransferStrategy> INDEX = new ConcurrentHashMap<>();

    private static Map<String, AbsTransferStrategy> buildIndex() {
        return SpringUtil.getBeansOfType(AbsTransferStrategy.class).values().stream()
                .collect(Collectors.toMap(
                        AbsTransferStrategy::getChannel,
                        Function.identity(),
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }

    /// 根据通道编码获取转账策略
    ///
    /// @param channel 通道编码
    /// @return 转账策略
    /// @throws UnsupportedAbilityException 通道未实现转账能力
    public static AbsTransferStrategy create(String channel) {
        AbsTransferStrategy strategy = INDEX.computeIfAbsent(channel, c -> buildIndex().get(c));
        if (strategy == null) {
            // 不支持的能力: {0}
            throw new UnsupportedAbilityException("pay.error.unsupportedAbilityWithDetail", channel);
        }
        return strategy;
    }

    /// 软查找: 未匹配返回 [Optional#empty], 不抛异常
    public static Optional<AbsTransferStrategy> findOptionally(String channel) {
        AbsTransferStrategy strategy = INDEX.computeIfAbsent(channel, c -> buildIndex().get(c));
        return Optional.ofNullable(strategy);
    }
}
