package cn.daxpay.open.payment.merchant.service.route.support;

import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchItem;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.provider.dao.PayProviderMethodManager;
import cn.daxpay.open.payment.masterdata.constants.capability.entity.PayCapability;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductCapabilityService;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.old.pay.support.ProductStrategySupport;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.model.PayProviderMethodEntry;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 通道路由：策略方式→能力与 DB 求交
///
/// 统一场景模式产品/能力候选及保存校验，数据源为 {@link AbsProductStrategy#methodCapabilityMapping()}。
@Service
@RequiredArgsConstructor
public class PayRouteStrategyCapabilitySupport {

    private final PayProviderMethodService payProviderMethodService;
    private final PayProductCapabilityService payProductCapabilityService;
    private final PayProductCapabilityManager payProductCapabilityManager;
    private final PayCapabilityManager payCapabilityManager;

    /// 通道路由白名单目录下全部 (provider,method) 的产品候选（单次请求聚合）
    public Map<String, List<LabelValue>> listSceneProductCandidatesBatch(String mchNo) {
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        for (PayProviderMethodEntry entry : payProviderMethodService.listDirectoryEntries()) {
            if (!PayRouteConfigProviders.contains(entry.getProviderCode())) {
                continue;
            }
            String provider = entry.getProviderCode();
            String method = entry.getMethodCode();
            String key = PayProviderMethodManager.pairKey(provider, method);
            index.put(key, listSceneProductCandidates(provider, method));
        }
        return index;
    }

    /// 按目录项+产品批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(
            String mchNo, List<PayRouteSceneCapabilityBatchItem> items) {
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        if (CollUtil.isEmpty(items)) {
            return index;
        }
        for (PayRouteSceneCapabilityBatchItem item : items) {
            if (item == null || StrUtil.hasBlank(item.getProvider(), item.getMethod(), item.getProduct())) {
                continue;
            }
            String key = capabilityBatchKey(item.getProvider(), item.getMethod(), item.getProduct());
            index.put(key, listSceneCapabilityCandidates(
                    item.getProvider(), item.getMethod(), item.getProduct()));
        }
        return index;
    }

    /// 能力批量候选 Map 的 key：provider|method|product
    public static String capabilityBatchKey(String provider, String method, String product) {
        return provider + "|" + method + "|" + product;
    }

    /// 目录项下可用支付产品候选
    public List<LabelValue> listSceneProductCandidates(String provider, String method) {
        if (!payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(method);
        List<LabelValue> results = new ArrayList<>();
        for (ProductEnum pe : ProductEnum.values()) {
            String product = pe.getCode();
            if (!routeProductSupportsMethod(product, methodEnum)) {
                continue;
            }
            String label = I18nUtil.getEnumName(pe);
            if (results.stream().noneMatch(item -> Objects.equals(item.getValue(), product))) {
                results.add(new LabelValue(label, product));
            }
        }
        return results;
    }

    /// 指定产品+目录项下支付能力候选（策略 Map ∩ DB ∩ 能力主数据启用）
    public List<LabelValue> listSceneCapabilityCandidates(String provider, String method, String product) {
        if (!payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return List.of();
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(method);
        return ProductStrategySupport.capabilitiesForMethod(strategy, methodEnum).stream()
                .filter(capability -> productCapabilityEnabled(product, capability.getCode()))
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

    /// 候选唯一时返回能力编码（仅供回显）
    public String inferSceneCapability(String provider, String method, String product) {
        List<LabelValue> candidates = listSceneCapabilityCandidates(provider, method, product);
        if (candidates.size() == 1) {
            return candidates.getFirst().getValue();
        }
        return null;
    }

    /// 校验场景配置项所选能力在候选集合内
    public void validateSceneCapability(String provider, String method, String product, String capability) {
        if (StrUtil.isBlank(capability)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityRequired");
        }
        boolean matched = listSceneCapabilityCandidates(provider, method, product).stream()
                .anyMatch(item -> Objects.equals(item.getValue(), capability));
        if (!matched) {
            PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(capability);
            String capabilityLabel = capabilityEnum != null ? I18nUtil.getEnumName(capabilityEnum) : capability;
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityProductMismatch",
                    capabilityLabel,
                    PayRouteI18nHelper.product(product),
                    PayRouteI18nHelper.payMethod(method));
        }
    }

    /// 通道路由：产品是否支持目录支付方式（策略 Map ∧ DB）
    public boolean routeProductSupportsMethod(String product, PayMethodEnum method) {
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

    private boolean productCapabilityEnabled(String productCode, String capabilityCode) {
        if (!payProductCapabilityManager.exists(productCode, capabilityCode)) {
            return false;
        }
        return payCapabilityManager.findByCode(capabilityCode).isPresent();
    }
}