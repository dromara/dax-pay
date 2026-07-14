package cn.daxpay.open.payment.route.service.runtime;

import cn.daxpay.open.payment.masterdata.dao.capability.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.dao.capability.PayProductCapabilityManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.route.dao.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.route.dao.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.service.model.PayRouteBundle;
import cn.daxpay.open.payment.route.service.model.RouteHit;
import cn.daxpay.open.payment.route.service.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 支付通道路由服务
///
/// 供支付流程（NormalPayService / GatewayPayHandleService）调用，唯一运行时入口 [PayRouteService#resolve]。
/// 两种路径：
/// 1. 直接指定：已传 channelMchNo（+ capability），跳过应用路由策略，由 channelMchNo 推导产品；capability 必填，method 未传时由能力反推。
/// 2. 跟随通道路由：未传 channelMchNo，按 appId 加载策略，经基础/场景匹配后回填 product/channelMchNo/capability。
/// 调用方需保证 appId 已解析完毕。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteSceneConfigManager sceneConfigManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductCapabilityManager payProductCapabilityManager;
    private final PayCapabilityManager payCapabilityManager;

    /// 实付路由解析：直接指定优先，否则跟随通道路由匹配
    public void resolve(NormalPayParam payParam) {
        // 直接指定：已传通道商户号，跳过应用路由策略
        if (StrUtil.isNotBlank(payParam.getChannelMchNo())) {
            resolveDirect(payParam);
            return;
        }
        // 跟随通道路由: 支付方式必填(Bean Validation 已放宽以兼容直接指定可空, 此处显式校验)
        if (StrUtil.isBlank(payParam.getMethod())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.methodRequired");
        }
        String appId = payParam.getAppId();
        var bundle = loadBundle(appId);
        if (bundle == null || bundle.getStrategy() == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyNotFound");
        }
        PayRouteStrategy strategy = bundle.getStrategy();
        RouteHit hit = matchByMode(bundle, payParam, strategy.getMode());
        if (hit == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.noMatch");
        }
        fillPayParam(payParam, hit);
    }

    // --- 直接指定 ---

    /// 直接指定：capability 必填，由通道商户推导产品；method 未传时由(通道商户, 能力)反推回填
    private void resolveDirect(NormalPayParam payParam) {
        String channelMchNo = payParam.getChannelMchNo();
        String capability = payParam.getCapability();
        // 直接指定: 支付能力必填
        if (StrUtil.isBlank(capability)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityRequired");
        }
        String product = channelMerchantManager.requireProductByChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        String method = payParam.getMethod();
        if (StrUtil.isBlank(method)) {
            // 直接指定: 由(通道商户, 支付能力)反推支付方式, 供下游通道策略使用
            String inferred = inferMethodForCapability(channelMchNo, capability);
            if (inferred == null) {
                // 支付能力[{0}]与通道商户[{1}]不匹配
                PayCapabilityEnum capEnum = PayCapabilityEnum.findByCode(capability);
                String capLabel = capEnum != null ? I18nUtil.getEnumName(capEnum) : capability;
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.directCapabilityChannelMchMismatch", capLabel, channelMchNo);
            }
            payParam.setMethod(inferred);
        } else {
            // 已传支付方式: 校验(产品, 方式, 能力)一致
            validateCapability(product, PayMethodEnum.findByCode(method), capability);
        }
        payParam.setProduct(product);
    }

    /// 直接指定: 由(通道商户, 支付能力)反推支付方式编码(策略 Map + DB 启用, 无则 null)
    /// 查询次数与迁前一致：1 次通道商户 + 能力挂载/主数据检查
    private String inferMethodForCapability(String channelMchNo, String capabilityCode) {
        String product = channelMerchantManager.findProductByChannelMchNo(channelMchNo);
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

    /// 产品是否挂载该能力且主数据存在（与迁前 productCapabilityEnabled 等价）
    private boolean productCapabilityEnabled(String productCode, String capabilityCode) {
        if (!payProductCapabilityManager.exists(productCode, capabilityCode)) {
            return false;
        }
        return payCapabilityManager.findByCode(capabilityCode).isPresent();
    }

    // --- 路由模式 ---

    /// 按应用号从库加载路由数据包（无 Redis 缓存）
    private PayRouteBundle loadBundle(String appId) {
        var strategyOpt = strategyManager.findByAppId(appId);
        if (strategyOpt.isEmpty()) {
            return null;
        }
        var strategy = strategyOpt.get();
        return new PayRouteBundle()
                .setStrategy(strategy)
                .setBasicConfigs(basicConfigManager.findByStrategyId(strategy.getId()))
                .setSceneConfigs(sceneConfigManager.findByStrategyId(strategy.getId()));
    }

    /// 按路由模式匹配；未识别模式按场景模式处理
    private RouteHit matchByMode(PayRouteBundle bundle, NormalPayParam payParam, String mode) {
        if (Objects.equals(mode, PayRouteModeEnum.BASIC.getCode())) {
            return matchBasic(bundle.getBasicConfigs(), payParam);
        }
        return matchScene(bundle.getSceneConfigs(), payParam);
    }

    /// 基础模式：按「支付渠道 → 通道商户」配置，结合支付方式解析通道商户、产品与能力
    private RouteHit matchBasic(List<PayRouteBasicConfig> basicConfigs, NormalPayParam payParam) {
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 场景模式下须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        // 支付方式自带渠道属性(OTHER 等无归属时报错)
        PayProviderEnum provider = methodEnum.getProvider();
        if (provider == null) {
            // 未指定支付产品时，支付渠道不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequired");
        }
        String channelMchNo = findConfiguredChannelMchNo(basicConfigs, provider.getCode());
        if (StrUtil.isBlank(channelMchNo)) {
            // 支付渠道[{0}]未配置通道商户，请在通道路由基础模式中完成配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicChannelMchNotConfigured", PayRouteI18nHelper.provider(provider.getCode()));
        }
        String product = channelMerchantManager.requireProductByChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        if (!PaymentStrategyFactory.productSupportsProvider(product, provider)) {
            // 支付渠道[{0}]下无可用支付产品
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", PayRouteI18nHelper.provider(provider.getCode()));
        }
        // 基础模式不存能力，由 (产品, 支付方式) 派生
        String capability = resolveCapabilityFromProduct(product, methodEnum);
        return new RouteHit(product, channelMchNo, capability);
    }

    /// 从基础配置中取指定支付渠道已绑定的通道商户号
    private String findConfiguredChannelMchNo(List<PayRouteBasicConfig> basicConfigs, String providerCode) {
        if (basicConfigs == null) {
            return null;
        }
        return basicConfigs.stream()
                .filter(config -> Objects.equals(config.getProvider(), providerCode))
                .map(PayRouteBasicConfig::getChannelMchNo)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    /// 基础模式支付能力由产品策略声明的(方式→能力)映射取首个
    private String resolveCapabilityFromProduct(String product, PayMethodEnum method) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        List<PayCapabilityEnum> capabilities = ProductStrategySupport.capabilitiesForMethod(strategy, method);
        return capabilities.isEmpty() ? null : capabilities.getFirst().getCode();
    }

    /// 场景模式：按 method 精确命中唯一配置行
    private RouteHit matchScene(List<PayRouteSceneConfig> configs, NormalPayParam payParam) {
        if (CollUtil.isEmpty(configs)) {
            return null;
        }
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 场景模式下须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        String method = payParam.getMethod();
        List<PayRouteSceneConfig> candidates = configs.stream()
                .filter(config -> Objects.equals(config.getMethod(), method))
                .toList();
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() > 1) {
            // 场景模式下同一支付方式存在多条配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.duplicateSceneMethod",
                    method);
        }
        return RouteHit.fromScene(candidates.getFirst().getChannelMchNo(), candidates.getFirst().getCapability());
    }

    /// 将命中结果写入 NormalPayParam：回填 product(由通道商户号派生)、通道商户号、支付能力
    private void fillPayParam(NormalPayParam payParam, RouteHit hit) {
        String product = StrUtil.isNotBlank(hit.product())
                ? hit.product()
                : channelMerchantManager.requireProductByChannelMchNo(hit.channelMchNo());
        payParam.setProduct(product);
        if (StrUtil.isNotBlank(hit.channelMchNo())) {
            payParam.setChannelMchNo(hit.channelMchNo());
        }
        if (StrUtil.isBlank(payParam.getCapability()) && StrUtil.isNotBlank(hit.capability())) {
            payParam.setCapability(hit.capability());
        }
    }

    /// 校验指定能力属于该(产品, 支付方式)的策略声明候选
    private void validateCapability(String product, PayMethodEnum method, String capability) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(capability);
        boolean matched = capabilityEnum != null
                && ProductStrategySupport.capabilitiesForMethod(strategy, method).contains(capabilityEnum);
        if (!matched) {
            // 支付能力[{0}]与产品[{1}]、支付方式[{2}]不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityProductMismatch", capability, product, method.getCode());
        }
    }
}
