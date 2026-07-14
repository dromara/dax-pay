package cn.daxpay.open.payment.strategy;

import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/// # 产品策略能力求交工具
///
/// 产品策略以 {@link AbsProductStrategy#methodCapabilityMapping()} 声明方式→能力；与通道路由、目录求交逻辑集中在此。
/// 仅提供核心内存运算，无 DB；勿再叠 Code/Enum 多层同义包装。
@UtilityClass
public class ProductStrategySupport {

    /// 产品是否声明支持指定支付渠道（通道路由基础模式等）
    public boolean supportsPayProvider(AbsProductStrategy strategy, PayProviderEnum provider) {
        return provider != null && strategy.supportedPayProviders().contains(provider);
    }

    /// 产品支持的支付能力列表（由方式→能力 Map 派生，对齐 pay_md_product_capability 全集）
    public List<PayCapabilityEnum> supportedPayCapabilities(AbsProductStrategy strategy) {
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (CollUtil.isEmpty(mapping)) {
            return List.of();
        }
        Set<PayCapabilityEnum> capabilities = new LinkedHashSet<>();
        for (List<PayCapabilityEnum> list : mapping.values()) {
            if (list != null) {
                capabilities.addAll(list);
            }
        }
        return List.copyOf(capabilities);
    }

    /// 指定目录支付方式下策略声明的支付能力列表
    public List<PayCapabilityEnum> capabilitiesForMethod(AbsProductStrategy strategy, PayMethodEnum method) {
        if (method == null) {
            return List.of();
        }
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (mapping == null) {
            return List.of();
        }
        List<PayCapabilityEnum> capabilities = mapping.get(method);
        if (CollUtil.isEmpty(capabilities)) {
            return List.of();
        }
        return List.copyOf(capabilities);
    }

    /// 反推: 给定策略与能力, 返回所属支付方式(多归属取首个, 无则 null)
    public PayMethodEnum methodForCapability(AbsProductStrategy strategy, PayCapabilityEnum capability) {
        if (capability == null) {
            return null;
        }
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (CollUtil.isEmpty(mapping)) {
            return null;
        }
        for (Map.Entry<PayMethodEnum, List<PayCapabilityEnum>> entry : mapping.entrySet()) {
            if (entry.getValue() != null && entry.getValue().contains(capability)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
