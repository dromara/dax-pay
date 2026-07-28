package cn.daxpay.open.payment.auth.merchant;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 认证策略注册表（按 authType 索引，启动时构建）
///
/// 认证策略按 [ChannelAuthStrategy#getAuthType] 注册, 查找 O(1)。
/// 新增通道认证只需新增 [ChannelAuthStrategy] 实现, 无需改本类。
@Slf4j
@Service
public class ChannelAuthStrategyRegistry {

    private final Map<String, ChannelAuthStrategy> strategies;

    public ChannelAuthStrategyRegistry(List<ChannelAuthStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(s -> s.getAuthType().getCode(), Function.identity()));
    }

    /// 按 authType 查找认证策略
    public ChannelAuthStrategy findByAuthType(String authType) {
        ChannelAuthStrategy strategy = strategies.get(authType);
        if (strategy == null) {
            // 不支持的能力: {0}
            throw new UnsupportedAbilityException("pay.error.unsupportedAbilityWithDetail", authType);
        }
        return strategy;
    }
}