package cn.daxpay.open.payment.merchant.service.route.support;

import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchItem;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public Map<String, List<LabelValue>> listSceneChannelMchCandidatesBatch(String mchNo) {
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        // 通道商户是商户级数据，目录循环外一次性加载
        List<ChannelMerchant> mchants = channelMerchantManager.findAllByMchNo(mchNo);
        for (PayProviderMethodEntry entry : payProviderMethodService.listDirectoryEntries()) {
            if (!PayRouteConfigProviders.contains(entry.getProviderCode())) {
                continue;
            }
            String key = PayProviderMethodManager.pairKey(entry.getProviderCode(), entry.getMethodCode());
            index.put(key, filterChannelMchForDirectory(mchants, entry.getProviderCode(), entry.getMethodCode()));
        }
        return index;
    }

    /// 按目录项+通道商户批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(
            String mchNo, List<PayRouteSceneCapabilityBatchItem> items) {
        Map<String, List<LabelValue>> index = new LinkedHashMap<>();
        if (CollUtil.isEmpty(items)) {
            return index;
        }
        for (PayRouteSceneCapabilityBatchItem item : items) {
            if (item == null || StrUtil.hasBlank(item.getProvider(), item.getMethod(), item.getChannelMchNo())) {
                continue;
            }
            String key = capabilityBatchKey(item.getProvider(), item.getMethod(), item.getChannelMchNo());
            index.put(key, listSceneCapabilityCandidates(item.getProvider(), item.getMethod(), item.getChannelMchNo()));
        }
        return index;
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
        return filterChannelMchForDirectory(channelMerchantManager.findAllByMchNo(mchNo), provider, method);
    }

    /// 从商户全部通道商户中筛出启用且其产品支持该(provider,method)的候选
    private List<LabelValue> filterChannelMchForDirectory(List<ChannelMerchant> mchants, String provider, String method) {
        List<LabelValue> results = new ArrayList<>();
        for (ChannelMerchant mch : mchants) {
            if (!Boolean.TRUE.equals(mch.getEnable())) {
                continue;
            }
            String product = mch.getProduct();
            if (!routeProductSupportsMethod(product, provider, method)) {
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
        return ProductStrategySupport.capabilitiesForMethod(strategy, methodEnum).stream()
                .filter(capability -> productCapabilityEnabled(product, capability.getCode()))
                .map(capability -> new LabelValue(I18nUtil.getEnumName(capability), capability.getCode()))
                .toList();
    }

    /// 候选唯一时返回能力编码（仅供回显）
    public String inferSceneCapability(String provider, String method, String channelMchNo) {
        List<LabelValue> candidates = listSceneCapabilityCandidates(provider, method, channelMchNo);
        if (candidates.size() == 1) {
            return candidates.getFirst().getValue();
        }
        return null;
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
        return ProductStrategySupport.supportedPayCapabilities(strategy).stream()
                .filter(capability -> productCapabilityEnabled(product, capability.getCode()))
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

    /// 通道商户号→产品编码(不存在返回 null)
    private String productOfChannelMchNo(String channelMchNo) {
        return channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(ChannelMerchant::getProduct)
                .orElse(null);
    }

    private boolean productCapabilityEnabled(String productCode, String capabilityCode) {
        if (!payProductCapabilityManager.exists(productCode, capabilityCode)) {
            return false;
        }
        return payCapabilityManager.findByCode(capabilityCode).isPresent();
    }
}
