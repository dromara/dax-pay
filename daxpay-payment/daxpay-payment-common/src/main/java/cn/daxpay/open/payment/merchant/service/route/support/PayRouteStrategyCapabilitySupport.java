package cn.daxpay.open.payment.merchant.service.route.support;

import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchItem;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.entity.PayCapability;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductCapability;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductCapabilityService;
import cn.daxpay.open.payment.masterdata.constants.provider.dao.PayProviderMethodManager;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.payment.old.pay.support.ProductStrategySupport;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.model.PayProviderMethodEntry;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 通道路由：策略方式→通道商户与能力候选
///
/// 统一场景模式通道商户/能力候选及保存校验。
/// 通道商户候选数据源为商户已开通的 {@link ChannelMerchant}(其 product 须支持对应目录支付方式)；
/// 能力候选数据源为 {@link AbsProductStrategy#methodCapabilityMapping()}(由通道商户绑定的产品决定)。
@Service
@RequiredArgsConstructor
public class PayRouteStrategyCapabilitySupport {

    private final PayProviderMethodService payProviderMethodService;
    private final PayProductCapabilityService payProductCapabilityService;
    private final PayProductCapabilityManager payProductCapabilityManager;
    private final PayCapabilityManager payCapabilityManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 通道路由白名单目录下全部 (provider,method) 的通道商户候选（单次请求聚合）
    ///
    /// 优化：目录、策略、产品能力在循环外一次性预加载，循环内仅做内存集合求交，
    /// 避免逐 (目录项 × 通道商户) 重复查库与扫描 Spring 容器。
    public Map<String, List<LabelValue>> listSceneChannelMchCandidatesBatch(String mchNo) {
        List<PayProviderMethodEntry> entries = payProviderMethodService.listDirectoryEntries();
        RouteBatchContext ctx = buildRouteBatchContext(entries);
        List<ChannelMerchant> mchants = channelMerchantManager.findAllByMchNo(mchNo);
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        for (PayProviderMethodEntry entry : entries) {
            if (!PayRouteConfigProviders.contains(entry.getProviderCode())) {
                continue;
            }
            String key = PayProviderMethodManager.pairKey(entry.getProviderCode(), entry.getMethodCode());
            index.put(key, filterChannelMchForDirectory(ctx, mchants, entry.getProviderCode(), entry.getMethodCode()));
        }
        return index;
    }

    /// 按目录项+通道商户批量返回支付能力候选
    ///
    /// 优化：目录、策略、产品能力及 channelMchNo→product 映射在循环外一次性预加载，
    /// 循环内仅做内存集合求交，避免逐 item 重复查库与扫描 Spring 容器。
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(List<PayRouteSceneCapabilityBatchItem> items) {
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        if (CollUtil.isEmpty(items)) {
            return index;
        }
        RouteBatchContext ctx = buildRouteBatchContext(payProviderMethodService.listDirectoryEntries());
        // 批量预加载 channelMchNo → product，替代逐次 productOfChannelMchNo 查库
        Set<String> channelMchNos = new HashSet<>();
        for (PayRouteSceneCapabilityBatchItem item : items) {
            if (item != null && StrUtil.isNotBlank(item.getChannelMchNo())) {
                channelMchNos.add(item.getChannelMchNo());
            }
        }
        Map<String, String> productByChannelMchNo = channelMerchantManager.lambdaQuery()
                .select(ChannelMerchant::getChannelMchNo, ChannelMerchant::getProduct)
                .in(ChannelMerchant::getChannelMchNo, channelMchNos)
                .list()
                .stream()
                .collect(Collectors.toMap(ChannelMerchant::getChannelMchNo, ChannelMerchant::getProduct, (a, b) -> a));
        for (PayRouteSceneCapabilityBatchItem item : items) {
            if (item == null || StrUtil.hasBlank(item.getProvider(), item.getMethod(), item.getChannelMchNo())) {
                continue;
            }
            String key = capabilityBatchKey(item.getProvider(), item.getMethod(), item.getChannelMchNo());
            if (!ctx.directoryPairKeys().contains(PayProviderMethodManager.pairKey(item.getProvider(), item.getMethod()))) {
                index.put(key, List.of());
                continue;
            }
            String product = productByChannelMchNo.get(item.getChannelMchNo());
            if (StrUtil.isBlank(product)) {
                index.put(key, List.of());
                continue;
            }
            index.put(key, capabilitiesForMethodWithCtx(ctx, product, PayMethodEnum.findByCode(item.getMethod())));
        }
        return index;
    }

    /// 用预加载上下文计算支付能力候选(策略声明能力 ∩ DB 挂载启用)，零查库
    private List<LabelValue> capabilitiesForMethodWithCtx(RouteBatchContext ctx, String product, PayMethodEnum method) {
        AbsProductStrategy strategy = ctx.strategyByProduct().get(product);
        if (strategy == null) {
            return List.of();
        }
        List<PayCapabilityEnum> declared = ProductStrategySupport.capabilitiesForMethod(strategy, method);
        if (CollUtil.isEmpty(declared)) {
            return List.of();
        }
        Set<String> mounted = ctx.capabilityCodesByProduct().getOrDefault(product, Set.of());
        return declared.stream()
                .filter(capability -> mounted.contains(capability.getCode()))
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

    /// 能力批量候选 Map 的 key：provider|method|channelMchNo
    public static String capabilityBatchKey(String provider, String method, String channelMchNo) {
        return provider + "|" + method + "|" + channelMchNo;
    }

    /// 目录项下商户已开通且其产品支持该(provider,method)的通道商户候选
    public List<LabelValue> listSceneChannelMchCandidates(String mchNo, String provider, String method) {
        if (!payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        // 单次请求也复用批量上下文，避免内层逐商户查库 / 扫容器
        RouteBatchContext ctx = buildRouteBatchContext(payProviderMethodService.listDirectoryEntries());
        return filterChannelMchForDirectory(ctx, channelMerchantManager.findAllByMchNo(mchNo), provider, method);
    }

    /// 从商户全部通道商户中筛出启用且其产品支持该(provider,method)的候选（用预加载上下文，零查库）
    private List<LabelValue> filterChannelMchForDirectory(
            RouteBatchContext ctx, List<ChannelMerchant> mchants, String provider, String method) {
        List<LabelValue> results = new ArrayList<>();
        // 目录键与支付方式枚举预解析，避免逐商户重复计算
        if (!ctx.directoryPairKeys().contains(PayProviderMethodManager.pairKey(provider, method))) {
            return results;
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(method);
        for (ChannelMerchant mch : mchants) {
            if (!Boolean.TRUE.equals(mch.getEnable())) {
                continue;
            }
            String product = mch.getProduct();
            if (!routeProductSupportsMethod(ctx, product, methodEnum)) {
                continue;
            }
            String label = StrUtil.isNotBlank(mch.getChannelMerchantName())
                    ? mch.getChannelMerchantName() : mch.getChannelMchNo();
            if (results.stream().noneMatch(item -> Objects.equals(item.getValue(), mch.getChannelMchNo()))) {
                results.add(new LabelValue(label, mch.getChannelMchNo()));
            }
        }
        return results;
    }

    /// 指定通道商户+目录项下支付能力候选（策略 Map ∩ DB ∩ 能力主数据启用）
    public List<LabelValue> listSceneCapabilityCandidates(String provider, String method, String channelMchNo) {
        if (!payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        String product = productOfChannelMchNo(channelMchNo);
        if (StrUtil.isBlank(product) || !PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return List.of();
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(method);
        // 预加载该产品已挂载且主数据存在的能力集合（替代逐能力查库）
        Set<String> enabledCodes = loadEnabledCapabilityCodes(product);
        return ProductStrategySupport.capabilitiesForMethod(strategy, methodEnum).stream()
                .filter(capability -> enabledCodes.contains(capability.getCode()))
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

    /// 传值模式: 商户全部启用通道商户候选(不按支付方式过滤)
    public List<LabelValue> listDirectChannelMchCandidates(String mchNo) {
        List<ChannelMerchant> mchants = channelMerchantManager.findAllByMchNo(mchNo);
        List<LabelValue> results = new ArrayList<>();
        for (ChannelMerchant mch : mchants) {
            if (!Boolean.TRUE.equals(mch.getEnable())) {
                continue;
            }
            String label = StrUtil.isNotBlank(mch.getChannelMerchantName())
                    ? mch.getChannelMerchantName() : mch.getChannelMchNo();
            if (results.stream().noneMatch(item -> Objects.equals(item.getValue(), mch.getChannelMchNo()))) {
                results.add(new LabelValue(label, mch.getChannelMchNo()));
            }
        }
        return results;
    }

    /// 传值模式: 按通道商户(产品)返回全部启用支付能力候选(不按支付方式过滤)
    public List<LabelValue> listDirectCapabilityCandidates(String channelMchNo) {
        String product = productOfChannelMchNo(channelMchNo);
        if (StrUtil.isBlank(product) || !PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return List.of();
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        // 预加载该产品已挂载且主数据存在的能力集合（替代逐能力查库）
        Set<String> enabledCodes = loadEnabledCapabilityCodes(product);
        return ProductStrategySupport.supportedPayCapabilities(strategy).stream()
                .filter(capability -> enabledCodes.contains(capability.getCode()))
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

    /// 传值模式: 由(通道商户, 支付能力)反推支付方式编码(策略 Map + DB 启用, 无则 null)
    public String inferMethodForCapability(String channelMchNo, String capabilityCode) {
        String product = productOfChannelMchNo(channelMchNo);
        if (StrUtil.isBlank(product) || !PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return null;
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayCapabilityEnum capability = PayCapabilityEnum.findByCode(capabilityCode);
        if (capability == null || !productCapabilityEnabled(product, capabilityCode)) {
            return null;
        }
        PayMethodEnum method = ProductStrategySupport.methodForCapability(strategy, capability);
        return method == null ? null : method.getCode();
    }

    /// 校验场景配置项所选能力在候选集合内
    public void validateSceneCapability(String provider, String method, String channelMchNo, String capability) {
        if (StrUtil.isBlank(capability)) {
            // 场景模式下须选择支付能力
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityRequired");
        }
        boolean matched = listSceneCapabilityCandidates(provider, method, channelMchNo).stream()
                .anyMatch(item -> Objects.equals(item.getValue(), capability));
        if (!matched) {
            PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(capability);
            String capabilityLabel = capabilityEnum != null ? I18nUtil.getEnumName(capabilityEnum) : capability;
            // 支付能力[{0}]与通道商户[{1}]、支付方式[{2}]不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityChannelMchMismatch",
                    capabilityLabel, channelMchNo, method);
        }
    }

    /// 通道路由：产品是否支持目录支付方式（策略 Map ∧ DB）
    private boolean routeProductSupportsMethod(String product, PayMethodEnum method) {
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return false;
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        if (!ProductStrategySupport.supportsDirectoryMethod(strategy, method)) {
            return false;
        }
        return payProductCapabilityService.productSupportsMethod(product, method.getCode());
    }

    /// 通道路由：产品是否支持目录 provider + method
    public boolean routeProductSupportsMethod(String product, String providerCode, String methodCode) {
        if (!payProviderMethodService.contains(providerCode, methodCode)) {
            return false;
        }
        return routeProductSupportsMethod(product, PayMethodEnum.findByCode(methodCode));
    }

    /// 批量判断上下文：循环外一次性预加载目录 / 策略 / 产品能力，循环内零查库
    private record RouteBatchContext(
            // 目录有效 (provider,method) 对键集合(替代逐次 contains 重查目录)
            Set<String> directoryPairKeys,
            // product → 策略实例(替代逐次 SpringUtil.getBeansOfType 扫描容器)
            Map<String, AbsProductStrategy> strategyByProduct,
            // product → 已挂载且启用能力编码集合(替代逐次 listByProduct + findByCode)
            Map<String, Set<String>> capabilityCodesByProduct) {
    }

    /// 构建批量判断上下文：目录键集合 + 策略索引 + 产品能力映射
    private RouteBatchContext buildRouteBatchContext(List<PayProviderMethodEntry> entries) {
        Set<String> pairKeys = new HashSet<>();
        for (PayProviderMethodEntry entry : entries) {
            pairKeys.add(PayProviderMethodManager.pairKey(entry.getProviderCode(), entry.getMethodCode()));
        }
        // 一次性获取全部产品策略 bean，建立 product 索引(替代逐次 getBeansOfType 扫描容器)
        Map<String, AbsProductStrategy> strategyByProduct = new HashMap<>();
        for (AbsProductStrategy strategy : PaymentStrategyFactory.createGroup(AbsProductStrategy.class)) {
            strategyByProduct.put(strategy.getProduct().getCode(), strategy);
        }
        // 预加载「产品 → 能力编码」(替代逐次 listByProduct + 逐能力 findByCode)
        Map<String, Set<String>> capabilityCodesByProduct = payProductCapabilityService.loadCapabilityCodesByProduct();
        return new RouteBatchContext(pairKeys, strategyByProduct, capabilityCodesByProduct);
    }

    /// 用预加载上下文判断产品是否支持目录支付方式(策略声明能力 ∩ DB 挂载启用)，零查库
    private boolean routeProductSupportsMethod(RouteBatchContext ctx, String product, PayMethodEnum method) {
        if (method == null || StrUtil.isBlank(product)) {
            return false;
        }
        AbsProductStrategy strategy = ctx.strategyByProduct().get(product);
        if (strategy == null) {
            return false;
        }
        List<PayCapabilityEnum> declared = ProductStrategySupport.capabilitiesForMethod(strategy, method);
        if (CollUtil.isEmpty(declared)) {
            return false;
        }
        Set<String> mounted = ctx.capabilityCodesByProduct().getOrDefault(product, Set.of());
        for (PayCapabilityEnum capability : declared) {
            if (mounted.contains(capability.getCode())) {
                return true;
            }
        }
        return false;
    }

    /// 通道商户号→产品编码(不存在返回 null)
    private String productOfChannelMchNo(String channelMchNo) {
        return channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(ChannelMerchant::getProduct)
                .orElse(null);
    }

    /// 预加载产品已挂载且主数据存在的能力编码集合（替代逐能力 productCapabilityEnabled 查库）
    private Set<String> loadEnabledCapabilityCodes(String product) {
        List<PayProductCapability> rels = payProductCapabilityManager.listByProduct(product);
        if (rels.isEmpty()) {
            return Set.of();
        }
        Set<String> capabilityCodes = rels.stream()
                .map(PayProductCapability::getCapabilityCode)
                .collect(Collectors.toSet());
        Set<String> validCodes = payCapabilityManager.listByCodes(capabilityCodes).stream()
                .map(PayCapability::getCode)
                .collect(Collectors.toSet());
        return rels.stream()
                .map(PayProductCapability::getCapabilityCode)
                .filter(validCodes::contains)
                .collect(Collectors.toSet());
    }

    private boolean productCapabilityEnabled(String productCode, String capabilityCode) {
        if (!payProductCapabilityManager.exists(productCode, capabilityCode)) {
            return false;
        }
        return payCapabilityManager.findByCode(capabilityCode).isPresent();
    }
}
