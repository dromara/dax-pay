package cn.daxpay.open.payment.merchant.service.route.scene;

import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigItem;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.merchant.service.route.basic.PayRouteBasicConfigService;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteConfigProviders;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.merchant.service.route.runtime.PayRouteProductResolver;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteCapabilityService;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/// # 通道路由场景模式配置
///
/// 按渠道支付方式目录为每个 (provider, method) 绑定支付产品 product；
/// 批量保存要求通道路由白名单内目录项均已配置支付产品，唯一键为 strategy + provider + method。
@Service
@RequiredArgsConstructor
public class PayRouteSceneConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteSceneConfigManager sceneConfigManager;
    private final PayRouteProductResolver productResolver;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
    private final PayRouteSceneRouteResolver sceneRouteResolver;
    private final PayRouteBasicConfigService basicConfigService;
    private final PayRouteCapabilityService payRouteCapabilityService;
    private final PayProviderMethodService payProviderMethodService;
    private final PayRouteMethodValidator payRouteMethodValidator;

    /// 查询场景模式配置列表
    public List<PayRouteSceneConfigResult> listSceneByAppId(String appId) {
        PayRouteStrategy strategy = requireStrategy(appId);
        return sceneConfigManager.findByStrategyId(strategy.getId()).stream()
                .map(PayRouteSceneConfig::toResult)
                .toList();
    }

    /// 批量保存场景模式配置（全量覆盖：目录完整、provider+method 唯一、产品能力校验）
    @Transactional(rollbackFor = Exception.class)
    public void saveSceneBatch(PayRouteSceneConfigBatchParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        PayRouteStrategy strategy = requireStrategy(param.getAppId());
        validateSceneConfigUnique(param.getItems());
        validateSceneProductCapabilityPairing(param.getItems());
        sceneConfigManager.deleteByStrategyId(strategy.getId());
        List<PayRouteSceneConfig> configs = new ArrayList<>();
        for (PayRouteSceneConfigItem item : param.getItems()) {
            String channel;
            String method;
            String product;
            if (StrUtil.isNotBlank(item.getProvider())) {
                if (!PayRouteConfigProviders.contains(item.getProvider())) {
                    continue;
                }
                if (StrUtil.isBlank(item.getMethod())) {
                    // 能力: 支付方式不能为空
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.payment.capability.methodRequiredWithPayProvider");
                }
                if (isSceneDirectoryRowEmpty(item)) {
                    continue;
                }
                payRouteMethodValidator.validateSceneConfigItem(item.getProvider(), item.getMethod());
                validateSceneMchProduct(mchNo, item.getProduct(), item.getProvider());
                if (!payRouteCapabilityService.productSupportsMethod(item.getProduct(), item.getProvider(), item.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.route.error.sceneMethodProductMismatch",
                            PayRouteI18nHelper.payMethod(item.getMethod()), PayRouteI18nHelper.product(item.getProduct()));
                }
                payRouteStrategyCapabilitySupport.validateSceneCapability(
                        item.getProvider(), item.getMethod(), item.getProduct(), item.getCapability());
                var route = sceneRouteResolver.resolve(item.getProduct(), item.getProvider(), item.getMethod());
                channel = route.channel();
                method = route.method();
                product = route.product();
            } else {
                if (StrUtil.isBlank(item.getChannel()) || StrUtil.isBlank(item.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.route.error.sceneChannelMethodRequired");
                }
                payRouteMethodValidator.validateSceneConfigItem(item.getProvider(), item.getMethod());
                channel = item.getChannel();
                method = item.getMethod();
                product = productResolver.resolveAndFill(mchNo, channel, method, item.getProduct());
            }
            validateRouteItem(mchNo, channel, method);
            PayRouteSceneConfig config = new PayRouteSceneConfig();
            config.setStrategyId(strategy.getId());
            config.setProvider(StrUtil.blankToDefault(item.getProvider(), null));
            config.setChannel(channel);
            config.setMethod(method);
            config.setProduct(product);
            configs.add(config);
        }
        for (PayRouteSceneConfig config : configs) {
            sceneConfigManager.save(config);
        }
    }


    /// 通道路由白名单目录下全部 (provider,method) 的产品候选（批量）
    public Map<String, List<LabelValue>> listSceneProductCandidatesBatch(String appId) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.listSceneProductCandidatesBatch(mchNo);
    }

    /// 按目录项+产品批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidatesBatch(mchNo, param.getItems());
    }

    /// 按目录项（支付渠道+支付方式）筛选商户已开通且能力匹配的产品候选
    public List<LabelValue> listSceneProductCandidatesForMethod(String appId, String provider, String method) {
        if (StrUtil.isBlank(method) || !payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.listSceneProductCandidates(provider, method);
    }

    /// 按目录项与支付产品筛选支付能力候选（策略 Map ∩ DB，不落库）
    public List<LabelValue> listSceneCapabilityCandidatesForMethod(
            String appId, String provider, String method, String product) {
        if (StrUtil.isBlank(product)) {
            return List.of();
        }
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidates(provider, method, product);
    }

    /// 回显推断支付能力（候选唯一时返回编码）
    public String inferSceneCapability(String appId, String provider, String method, String product) {
        if (StrUtil.isBlank(product)) {
            return null;
        }
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.inferSceneCapability(provider, method, product);
    }

    /// 校验批量保存项：provider + method 在策略内唯一
    private void validateSceneConfigUnique(List<PayRouteSceneConfigItem> items) {
        Set<String> pairKeys = new HashSet<>();
        for (PayRouteSceneConfigItem item : items) {
            if (StrUtil.isBlank(item.getProvider())) {
                continue;
            }
            String key = item.getProvider() + "|" + StrUtil.blankToDefault(item.getMethod(), "");
            if (!pairKeys.add(key)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.duplicatePayProviderMethod",
                        PayRouteI18nHelper.provider(item.getProvider()), PayRouteI18nHelper.payMethod(item.getMethod()));
            }
        }
    }

    /// 校验目录行：支付产品与支付能力须同时为空或同时有值，不可只填其一
    private void validateSceneProductCapabilityPairing(List<PayRouteSceneConfigItem> items) {
        for (PayRouteSceneConfigItem item : items) {
            if (StrUtil.isBlank(item.getProvider())) {
                continue;
            }
            boolean hasProduct = StrUtil.isNotBlank(item.getProduct());
            boolean hasCapability = StrUtil.isNotBlank(item.getCapability());
            if (hasProduct == hasCapability) {
                continue;
            }
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneProductCapabilityPair",
                    PayRouteI18nHelper.provider(item.getProvider()),
                    PayRouteI18nHelper.payMethod(item.getMethod()));
        }
    }

    /// 目录行未配置（产品与能力均为空）
    private boolean isSceneDirectoryRowEmpty(PayRouteSceneConfigItem item) {
        return StrUtil.isBlank(item.getProduct()) && StrUtil.isBlank(item.getCapability());
    }

    /// 场景配置所选产品须在商户侧可用且支持该支付渠道
    private void validateSceneMchProduct(String mchNo, String product, String providerCode) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.basicProviderInvalid");
        }
        basicConfigService.validateBasicProduct(product, provider);
    }

    /// 校验通道+方式可解析为商户产品且存在对应产品策略
    private void validateRouteItem(String mchNo, String channel, String method) {
        PayMethodEnum.findByCode(method);
        String product = productResolver.resolve(mchNo, channel, method);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}
