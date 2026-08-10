package cn.daxpay.open.payment.strategy.alloc;

import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 分账策略工厂
///
/// 按 [AbsAllocStrategy#getChannel] 索引, 与 [cn.daxpay.open.payment.strategy.transfer.TransferStrategyFactory] 同模式。
/// 首次访问时原子构建索引缓存。
@UtilityClass
public class AllocStrategyFactory {

    private final ConcurrentHashMap<String, AbsAllocStrategy> INDEX = new ConcurrentHashMap<>();

    private static Map<String, AbsAllocStrategy> buildIndex() {
        return SpringUtil.getBeansOfType(AbsAllocStrategy.class).values().stream()
                .collect(Collectors.toMap(
                        AbsAllocStrategy::getChannel,
                        Function.identity(),
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }

    /// 按通道创建策略, 未实现抛 [UnsupportedAbilityException]
    public static AbsAllocStrategy create(String channel) {
        AbsAllocStrategy strategy = INDEX.computeIfAbsent(channel, c -> buildIndex().get(c));
        if (strategy == null) {
            // 该通道暂不支持分账
            throw new UnsupportedAbilityException("pay.error.alloc.channelNotSupport", channel);
        }
        return strategy;
    }

    /// 软查找(通道未实现分账时返回 empty, 不抛异常)
    public static Optional<AbsAllocStrategy> findOptionally(String channel) {
        return Optional.ofNullable(INDEX.computeIfAbsent(channel, c -> buildIndex().get(c)));
    }
}
