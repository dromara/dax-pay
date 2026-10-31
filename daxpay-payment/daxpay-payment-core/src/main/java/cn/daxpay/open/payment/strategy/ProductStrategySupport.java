package cn.daxpay.open.payment.strategy;

import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

/// # 产品策略能力求交工具
///
/// 产品策略以 {@link AbsProductStrategy#methodCapabilityMapping()} 声明方式→能力；与通道路由、目录求交逻辑集中在此。
/// 仅提供核心内存运算，无 DB；勿再叠 Code/Enum 多层同义包装。
@UtilityClass
public class ProductStrategySupport {

    /// 产品是否声明支持指定支付渠道（通道路由基础模式等）
    public boolean supportsPayProvider(AbsProductStrategy strategy, PayProviderEnum provider) {
        return Objects.nonNull(provider) && strategy.supportedPayProviders().contains(provider);
    }

    /// 产品支持的支付能力列表（由方式→能力 Map 派生，对齐 pay_md_product_capability 全集）
    ///
    /// 输出按 [PayMethodEnum] 声明序归并(声明序即渠道分组: 微信→支付宝→银联→抖音, 与 data.sql 的 sort_no 同序),
    /// 不直接透出 mapping 的迭代序: 各产品策略普遍用 Map.of/ofEntries 构造, 迭代序随哈希漂移,
    /// 透出到前端会导致支付能力候选列表乱序(如 wechat_h5 排在首项、union_barcode 夹在支付宝之间)。
    public List<PayCapabilityEnum> supportedPayCapabilities(AbsProductStrategy strategy) {
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (CollUtil.isEmpty(mapping)) {
            return List.of();
        }
        List<PayMethodEnum> methods = new ArrayList<>(mapping.keySet());
        methods.sort(Comparator.comparingInt(PayMethodEnum::ordinal));
        Set<PayCapabilityEnum> capabilities = new LinkedHashSet<>();
        for (PayMethodEnum method : methods) {
            List<PayCapabilityEnum> list = mapping.get(method);
            if (Objects.nonNull(list)) {
                capabilities.addAll(list);
            }
        }
        return List.copyOf(capabilities);
    }

    /// 指定目录支付方式下策略声明的支付能力列表
    public List<PayCapabilityEnum> capabilitiesForMethod(AbsProductStrategy strategy, PayMethodEnum method) {
        if (Objects.isNull(method)) {
            return List.of();
        }
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (Objects.isNull(mapping)) {
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
        if (Objects.isNull(capability)) {
            return null;
        }
        Map<PayMethodEnum, List<PayCapabilityEnum>> mapping = strategy.methodCapabilityMapping();
        if (CollUtil.isEmpty(mapping)) {
            return null;
        }
        for (Map.Entry<PayMethodEnum, List<PayCapabilityEnum>> entry : mapping.entrySet()) {
            if (Objects.nonNull(entry.getValue()) && entry.getValue().contains(capability)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
