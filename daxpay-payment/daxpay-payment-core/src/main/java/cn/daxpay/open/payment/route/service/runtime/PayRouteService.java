package cn.daxpay.open.payment.route.service.runtime;

import cn.daxpay.open.payment.masterdata.dao.capability.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.dao.capability.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.route.entity.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.service.basic.PayRouteBasicConfigService;
import cn.daxpay.open.payment.route.service.model.PayRouteBundle;
import cn.daxpay.open.payment.route.service.model.RouteHit;
import cn.daxpay.open.payment.route.service.scene.PayRouteSceneConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayPayHandleService;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// # 支付通道路由服务
///
/// 供支付流程（[NormalPayService] / [GatewayPayHandleService]）调用，唯一运行时入口 [PayRouteService#resolve]。
/// 两种路径：
/// 1. 直接指定：已传 channelMchNo（+ capability），跳过应用路由策略，由 channelMchNo 推导产品；capability 必填，method 未传时由能力反推。
/// 2. 跟随通道路由：未传 channelMchNo，按 appId 加载策略，经基础/场景匹配后回填 product/channelMchNo/capability。
/// 调用方需保证 appId 已解析完毕。
///
/// 另提供两个只读探测入口 [PayRouteService#tryResolveRoute] / [PayRouteService#tryResolveDirect]，
/// 供收银台支付项可用性预检使用：与 resolve 同口径探测，失败返回 null 而不抛异常，不影响支付链路运行时行为。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteService {

    private final PayRouteBundleService routeBundleService;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductCapabilityManager payProductCapabilityManager;
    private final PayCapabilityManager payCapabilityManager;
    private final PayProductConfigManager payProductConfigManager;
    private final PayProductManager payProductManager;

    /// 实付路由解析：直接指定优先，否则跟随通道路由匹配
    public void resolve(NormalPayParam payParam) {
        // 直接指定：已传通道商户号，跳过应用路由策略
        if (StrUtil.isNotBlank(payParam.getChannelMchNo())) {
            resolveDirect(payParam);
            // 产品启停校验(直接指定路径)
            validateProductEnabled(payParam.getProduct());
            // 环境一致性校验(直接指定路径)
            validateChannelMchEnvMatch(payParam.getChannelMchNo(), payParam.getProduct());
            return;
        }
        // 跟随通道路由
        resolveByRoute(payParam);
    }

    /// 跟随通道路由路径：按应用路由策略匹配并回填 product/channelMchNo/capability
    ///
    /// 从 [PayRouteService#resolve] 提取的「跟随路由」分支体，供运行时解析与只读探测
    /// [PayRouteService#tryResolveRoute] 共用，保证两条链路口径一致。
    private void resolveByRoute(NormalPayParam payParam) {
        // 跟随通道路由: 支付方式必填(Bean Validation 已放宽以兼容直接指定可空, 此处显式校验)
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 路由: 通道路由下支付方式不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.methodRequired");
        }
        String appId = payParam.getAppId();
        var bundle = routeBundleService.loadBundle(appId);
        if (Objects.isNull(bundle) || Objects.isNull(bundle.getStrategy())) {
            // 路由: 应用未配置通道路由策略
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.strategyNotFound");
        }
        PayRouteStrategy strategy = bundle.getStrategy();
        RouteHit hit = matchByMode(bundle, payParam, strategy.getMode());
        if (Objects.isNull(hit)) {
            // 路由: 未匹配到可用支付产品
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.noMatch");
        }
        fillPayParam(payParam, hit);
        // 产品启停校验(跟随路由路径)
        validateProductEnabled(payParam.getProduct());
        // 环境一致性校验(跟随路由路径)
        validateChannelMchEnvMatch(payParam.getChannelMchNo(), payParam.getProduct());
    }

    /// 跟随通道路由的只读探测（收银台支付项可用性预检用）
    ///
    /// 构造临时参数走 [PayRouteService#resolveByRoute] 完整链路（含产品/通道商户校验），
    /// 与运行时解析同口径；任一环节失败（方式为空/应用未配置策略/未命中/产品停用/通道商户禁用或环境不一致）
    /// 均返回 null 而不抛异常。成功时 [RouteHit] 由回填后的临时参数组装（product/channelMchNo/capability）。
    ///
    /// 捕获 [BizException] 而非 [BizInfoException]：链路内 `PayMethodEnum#findByCode` 抛的是
    /// `UnsupportedAbilityException`（继承链为 PayFailureException → BizException），只catch BizInfoException
    /// 会让脏支付方式穿透到调用方；本方法语义是「探测」，一切业务异常都应转成不可用。
    public RouteHit tryResolveRoute(String appId, String method) {
        NormalPayParam probe = new NormalPayParam();
        probe.setAppId(appId);
        probe.setMethod(method);
        try {
            resolveByRoute(probe);
        } catch (BizException e) {
            return null;
        }
        return new RouteHit(probe.getProduct(), probe.getChannelMchNo(), probe.getCapability());
    }

    /// 直接指定的只读探测（收银台支付项可用性预检用）
    ///
    /// 构造临时参数走 [PayRouteService#resolveDirect]（含能力校验与 method 能力反推回填）
    /// 加产品/通道商户可用性校验，与运行时解析同口径；任一环节失败（能力缺失/产品策略不存在/
    /// 能力与通道商户不匹配/产品停用/通道商户禁用或环境不一致）均返回 null 而不抛异常。
    ///
    /// 与 [PayRouteService#tryResolveRoute] 同口径捕获 [BizException]：探测语义下一切业务异常都转成不可用，
    /// 防止 `UnsupportedAbilityException`（继承 PayFailureException → BizException）穿透到调用方。
    public RouteHit tryResolveDirect(String channelMchNo, String capability) {
        NormalPayParam probe = new NormalPayParam();
        probe.setChannelMchNo(channelMchNo);
        probe.setCapability(capability);
        try {
            resolveDirect(probe);
            // 产品启停校验(直接指定路径)
            validateProductEnabled(probe.getProduct());
            // 环境一致性校验(直接指定路径)
            validateChannelMchEnvMatch(probe.getChannelMchNo(), probe.getProduct());
        } catch (BizException e) {
            return null;
        }
        return new RouteHit(probe.getProduct(), probe.getChannelMchNo(), probe.getCapability());
    }

    /// 产品启停校验(运行时): 停用的支付产品不允许发起新支付
    ///
    /// 本方法只拦支付下单链路, 存量订单的退款/同步/回调不经过路由, 不受停用影响;
    /// 主数据无行时视为默认启用, 与 [cn.daxpay.open.payment.masterdata.service.product.PayProductService] 目录融合口径一致。
    private void validateProductEnabled(String product) {
        if (StrUtil.isBlank(product)) {
            return;
        }
        Optional<PayProduct> row = payProductManager.findByCode(product);
        if (row.isPresent() && !row.get().isEnabled()) {
            log.info("支付产品[{}]已被停用, 路由拒绝", product);
            // 路由: 支付产品已停用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productDisabled", PayRouteI18nHelper.product(product));
        }
    }

    /// 通道商户可用性校验(运行时)
    ///
    /// 1. 启停用校验: 禁用的通道商户不允许被路由命中, 与配置保存时([PayRouteBasicConfigService]/[PayRouteSceneConfigService])保持一致
    /// 2. 环境一致性校验: 通道商户固化的 sandbox 标识必须与产品当前生效环境(activeEnv)匹配。
    ///    沙箱产品 → 只能路由到 sandbox 通道商户; 生产产品 → 只能路由到 prod 通道商户。
    ///    不匹配抛 channelMchEnvMismatch(明确告知调用方通道商户与产品环境不一致, 便于排查配置问题)。
    ///    注意: 通道商户 sandbox 在创建时固化, 不随产品环境切换改变; 一个产品下可同时存在两种商户。
    private void validateChannelMchEnvMatch(String channelMchNo, String product) {
        if (StrUtil.isBlank(channelMchNo) || StrUtil.isBlank(product)) {
            return;
        }
        Optional<ChannelMerchant> mchOpt = channelMerchantManager.findByChannelMchNo(channelMchNo);
        // 启停用校验(运行时): 禁用的商户不允许被路由命中
        if (mchOpt.isPresent() && !Boolean.TRUE.equals(mchOpt.get().getEnable())) {
            log.info("通道商户[{}]已被禁用, 路由拒绝", channelMchNo);
            // 路由: 通道商户未启用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.channelMchDisabled", channelMchNo);
        }
        // 沙箱/生产环境一致性校验
        boolean mchSandbox = mchOpt.map(ChannelMerchant::isSandbox).orElse(false);
        boolean prodSandbox = payProductConfigManager.isSandboxActive(product);
        if (mchSandbox != prodSandbox) {
            log.info("通道商户[{}]固化环境(sandbox={})与产品[{}]当前生效环境(sandbox={})不匹配, 路由拒绝",
                    channelMchNo, mchSandbox, product, prodSandbox);
            // 路由: 通道商户环境与产品当前生效环境不一致(明确告知调用方, 便于排查配置问题)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.channelMchEnvMismatch", channelMchNo, product);
        }
    }

    // --- 直接指定 ---

    /// 直接指定：capability 必填，由通道商户推导产品；method 未传时由(通道商户, 能力)反推回填
    private void resolveDirect(NormalPayParam payParam) {
        String channelMchNo = payParam.getChannelMchNo();
        String capability = payParam.getCapability();
        // 直接指定: 支付能力必填
        if (StrUtil.isBlank(capability)) {
            // 路由: 按支付方式配置时须选择支付能力
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityRequired");
        }
        String product = channelMerchantManager.requireProductByChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 路由: 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        String method = payParam.getMethod();
        if (StrUtil.isBlank(method)) {
            // 直接指定: 由(通道商户, 支付能力)反推支付方式, 供下游通道策略使用
            String inferred = inferMethodForCapability(channelMchNo, capability);
            if (Objects.isNull(inferred)) {
                // 支付能力[{0}]与通道商户[{1}]不匹配
                PayCapabilityEnum capEnum = PayCapabilityEnum.findByCode(capability);
                String capLabel = Objects.nonNull(capEnum) ? I18nUtil.getEnumName(capEnum) : capability;
                // 路由: 支付能力与通道商户不匹配
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
    ///
    /// 供码牌/聚合 DIRECT 解析与路由直接指定共用，避免两处默认 method 语义漂移。
    /// 查询次数：1 次通道商户 + 能力挂载/主数据检查
    public String inferMethodForCapability(String channelMchNo, String capabilityCode) {
        String product = channelMerchantManager.findProductByChannelMchNo(channelMchNo);
        if (StrUtil.isBlank(product) || !PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            return null;
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayCapabilityEnum capability = PayCapabilityEnum.findByCode(capabilityCode);
        if (Objects.isNull(capability) || !productCapabilityEnabled(product, capabilityCode)) {
            return null;
        }
        PayMethodEnum method = ProductStrategySupport.methodForCapability(strategy, capability);
        return Objects.isNull(method) ? null : method.getCode();
    }

    /// 产品是否挂载该能力且主数据存在（与迁前 productCapabilityEnabled 等价）
    private boolean productCapabilityEnabled(String productCode, String capabilityCode) {
        if (!payProductCapabilityManager.exists(productCode, capabilityCode)) {
            return false;
        }
        return payCapabilityManager.findByCode(capabilityCode).isPresent();
    }

    // --- 路由模式 ---

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
            // 路由: 按支付方式配置时须选择支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.sceneMethodRequired");
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        // 支付方式自带渠道属性(OTHER 等无归属时报错)
        PayProviderEnum provider = methodEnum.getProvider();
        if (Objects.isNull(provider)) {
            // 路由: 未指定支付产品时支付渠道不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.providerRequired");
        }
        String channelMchNo = findConfiguredChannelMchNo(basicConfigs, provider.getCode());
        if (StrUtil.isBlank(channelMchNo)) {
            // 路由: 支付渠道未配置通道商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicChannelMchNotConfigured", PayRouteI18nHelper.provider(provider.getCode()));
        }
        String product = channelMerchantManager.requireProductByChannelMchNo(channelMchNo);
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 路由: 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        if (!PaymentStrategyFactory.productSupportsProvider(product, provider)) {
            // 路由: 支付渠道下无可用支付产品
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", PayRouteI18nHelper.provider(provider.getCode()));
        }
        // 基础模式不存能力，由 (产品, 支付方式) 派生
        String capability = resolveCapabilityFromProduct(product, methodEnum);
        if (StrUtil.isBlank(capability)) {
            // 产品未声明该支付方式的能力映射时能力留空(维持历史口径，不可在此硬拒):
            // 部分通道按 method 自行分派、并不读能力(如 ums_qrcode 处理 wechat_jsapi/cashier)，
            // 硬拒会把这类可用组合一并挡掉; 但不声明通常也意味着该通道商户做不到这个支付方式，
            // 留 warning 便于定位「路由通过、到通道才报不支持」的问题(如拉卡拉产品未映射 wechat_qr)
            log.warn("通道路由命中未声明能力映射的组合, 支付能力留空: provider={}, method={}, product={}, channelMchNo={}",
                    provider.getCode(), methodEnum.getCode(), product, channelMchNo);
        }
        return new RouteHit(product, channelMchNo, capability);
    }

    /// 从基础配置中取指定支付渠道已绑定的通道商户号
    private String findConfiguredChannelMchNo(List<PayRouteBasicConfig> basicConfigs, String providerCode) {
        if (Objects.isNull(basicConfigs)) {
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
            // 路由: 按支付方式配置时须选择支付方式
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
            // 路由: 按支付方式配置下同一支付方式存在多条配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.duplicateSceneMethod",
                    method);
        }
        return RouteHit.fromScene(candidates.getFirst().getChannelMchNo(), candidates.getFirst().getCapability());
    }

    /// 将命中结果写入 NormalPayParam：回填 product(由通道商户号派生)、通道商户号、支付能力
    ///
    /// 支付能力**以命中配置为准**：跟随通道路由时调用方自报的 capability 不得覆盖路由配置(场景模式)
    /// 或产品派生值(基础模式)，否则调用方可绕过路由配置自选能力且全程无校验；
    /// 仅在命中结果无能力时才保留调用方原值(历史脏配置容错)。
    private void fillPayParam(NormalPayParam payParam, RouteHit hit) {
        String product = StrUtil.isNotBlank(hit.product())
                ? hit.product()
                : channelMerchantManager.requireProductByChannelMchNo(hit.channelMchNo());
        payParam.setProduct(product);
        if (StrUtil.isNotBlank(hit.channelMchNo())) {
            payParam.setChannelMchNo(hit.channelMchNo());
        }
        if (StrUtil.isBlank(hit.capability())) {
            return;
        }
        // 防配置漂移: 通道商户换产品后旧配置能力可能已不在(产品, 支付方式)候选内
        validateHitCapability(product, payParam.getMethod(), hit.capability());
        if (StrUtil.isNotBlank(payParam.getCapability()) && !Objects.equals(payParam.getCapability(), hit.capability())) {
            log.info("路由能力覆盖调用方传值: method={}, 调用方={}, 路由配置={}",
                    payParam.getMethod(), payParam.getCapability(), hit.capability());
        }
        payParam.setCapability(hit.capability());
    }

    /// 校验路由命中的支付能力仍在(产品, 支付方式)策略声明候选内
    ///
    /// 配置态保存时已做同口径校验，但通道商户换产品后旧配置会失效；运行期不校验会把错误能力
    /// 透传到通道层，报错点离配置很远。策略索引由 [PaymentStrategyFactory] 缓存，本校验零查库。
    private void validateHitCapability(String product, String method, String capability) {
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            // 路由: 支付产品策略不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(capability);
        boolean matched = Objects.nonNull(capabilityEnum)
                && ProductStrategySupport.capabilitiesForMethod(strategy, PayMethodEnum.findByCode(method))
                        .contains(capabilityEnum);
        if (!matched) {
            String capLabel = Objects.nonNull(capabilityEnum) ? I18nUtil.getEnumName(capabilityEnum) : capability;
            // 路由: 路由配置的支付能力已失效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.routeCapabilityMismatch",
                    capLabel, PayRouteI18nHelper.product(product), PayRouteI18nHelper.payMethod(method));
        }
    }

    /// 校验指定能力属于该(产品, 支付方式)的策略声明候选
    private void validateCapability(String product, PayMethodEnum method, String capability) {
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(product, AbsProductStrategy.class);
        PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(capability);
        boolean matched = Objects.nonNull(capabilityEnum)
                && ProductStrategySupport.capabilitiesForMethod(strategy, method).contains(capabilityEnum);
        if (!matched) {
            // 路由: 支付能力与产品、支付方式不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneCapabilityProductMismatch", capability, product, method.getCode());
        }
    }
}
