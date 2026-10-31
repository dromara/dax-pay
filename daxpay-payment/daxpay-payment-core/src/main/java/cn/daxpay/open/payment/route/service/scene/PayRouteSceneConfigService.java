package cn.daxpay.open.payment.route.service.scene;

import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.route.dao.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneConfigItem;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.route.service.support.PayRouteBundleCacheEvictor;
import cn.daxpay.open.payment.route.service.support.PayRouteConfigProviders;
import cn.daxpay.open.payment.route.service.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.route.service.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.masterdata.service.provider.PayProviderMethodService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
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
/// 配置粒度为「支付方式 → (通道商户, 支付能力)」，每支付方式唯一一行；
/// 通道商户唯一绑定支付产品，通道编码由产品派生。批量保存为全量覆盖。
@Service
@RequiredArgsConstructor
public class PayRouteSceneConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteSceneConfigManager sceneConfigManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProviderMethodService payProviderMethodService;
    private final PayRouteBundleCacheEvictor routeBundleCacheEvictor;

    /// 查询场景模式配置列表
    public List<PayRouteSceneConfigResult> listSceneByAppId(String appId) {
        PayRouteStrategy strategy = requireStrategy(appId);
        return sceneConfigManager.findByStrategyId(strategy.getId()).stream()
                .map(PayRouteSceneConfig::toResult)
                .toList();
    }

    /// 批量保存场景模式配置（全量覆盖：method 唯一、通道商户/能力校验）
    @Transactional(rollbackFor = Exception.class)
    public void saveSceneBatch(PayRouteSceneConfigBatchParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppId(param.getAppId());
        PayRouteStrategy strategy = requireStrategy(param.getAppId());
        validateSceneConfigUnique(param.getItems());
        validateSceneChannelMchCapabilityPairing(param.getItems());
        sceneConfigManager.deleteByStrategyId(strategy.getId());
        List<PayRouteSceneConfig> configs = new ArrayList<>();
        for (PayRouteSceneConfigItem item : param.getItems()) {
            if (StrUtil.isBlank(item.getMethod())) {
                continue;
            }
            // 未填写的目录行不落库(通道商户与能力都空 = 该支付方式不配置)
            if (isSceneRowEmpty(item)) {
                continue;
            }
            // 支付渠道由支付方式派生; 无渠道归属(OTHER)或非通道路由白名单渠道不允许配置。
            // 此处必须显式拒绝: 静默跳过会变成「保存成功但配置不生效」, 用户无从发现
            PayProviderEnum provider = PayMethodEnum.findByCode(item.getMethod()).getProvider();
            if (Objects.isNull(provider) || !PayRouteConfigProviders.contains(provider.getCode())) {
                // 路由: 支付方式所属支付渠道不支持通道路由配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.sceneProviderNotInRouteScope", PayRouteI18nHelper.payMethod(item.getMethod()));
            }
            String providerCode = provider.getCode();
            validateSceneConfigItem(providerCode, item.getMethod());
            // 校验通道商户属本商户且启用
            ChannelMerchant mch = channelMerchantManager
                    .findByMchNoAndChannelMchNo(mchNo, item.getChannelMchNo())
                    // 路由: 通道商户不存在
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.route.error.channelMchNotExist", item.getChannelMchNo()));
            if (!Boolean.TRUE.equals(mch.getEnable())) {
                // 路由: 通道商户未启用
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.channelMchDisabled", item.getChannelMchNo());
            }
            String product = mch.getProduct();
            // 校验产品支持该(provider, method)
            if (!payRouteStrategyCapabilitySupport.routeProductSupportsMethod(
                    product, providerCode, item.getMethod())) {
                // 路由: 支付方式与产品不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.sceneMethodProductMismatch",
                        PayRouteI18nHelper.payMethod(item.getMethod()), PayRouteI18nHelper.product(product));
            }
            // 校验支付能力在候选集合内
            payRouteStrategyCapabilitySupport.validateSceneCapability(
                    providerCode, item.getMethod(), item.getChannelMchNo(), item.getCapability());
            var config = new PayRouteSceneConfig();
            config.setStrategyId(strategy.getId());
            config.setMethod(item.getMethod());
            config.setChannelMchNo(item.getChannelMchNo());
            config.setCapability(item.getCapability());
            configs.add(config);
        }
        for (PayRouteSceneConfig config : configs) {
            sceneConfigManager.save(config);
        }
        // 缓存失效: 事务提交后执行(提交前失效会被并发回源回填旧配置)
        routeBundleCacheEvictor.evictAfterCommit(param.getAppId());
    }

    /// 通道路由白名单目录下全部 (provider,method) 的通道商户候选（批量）
    public Map<String, List<ChannelMchOption>> listSceneChannelMchCandidatesBatch(String appId) {
        String mchNo = mchAppInfoManager.requireMchNoByAppId(appId);
        return payRouteStrategyCapabilitySupport.listSceneChannelMchCandidatesBatch(mchNo);
    }

    /// 按目录项+通道商户批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        mchAppInfoManager.requireMchNoByAppId(param.getAppId());
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidatesBatch(param.getItems());
    }

    /// 按目录项（支付渠道+支付方式）筛选商户已开通的通道商户候选
    public List<ChannelMchOption> listSceneChannelMchCandidatesForMethod(String appId, String provider, String method) {
        if (StrUtil.isBlank(method) || !payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        String mchNo = mchAppInfoManager.requireMchNoByAppId(appId);
        return payRouteStrategyCapabilitySupport.listSceneChannelMchCandidates(mchNo, provider, method);
    }

    /// 按目录项与通道商户筛选支付能力候选（策略 Map ∩ DB，不落库）
    ///
    /// 与其余候选方法一致先校验 appId 归属：channelMchNo 由调用方传入，缺此校验等于放开跨商户探测入口
    /// (现状靠 channelMerchant 查询的租户隔离兜底，口径不应依赖底层实现)
    public List<LabelValue> listSceneCapabilityCandidatesForMethod(
            String appId, String provider, String method, String channelMchNo) {
        mchAppInfoManager.requireMchNoByAppId(appId);
        if (StrUtil.isBlank(channelMchNo)) {
            return List.of();
        }
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidates(provider, method, channelMchNo);
    }

    /// 校验批量保存项：支付方式全局唯一
    private void validateSceneConfigUnique(List<PayRouteSceneConfigItem> items) {
        Set<String> methodKeys = new HashSet<>();
        for (PayRouteSceneConfigItem item : items) {
            if (StrUtil.isBlank(item.getMethod())) {
                continue;
            }
            if (!methodKeys.add(item.getMethod())) {
                // 路由: 按支付方式配置下同一支付方式存在多条配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.duplicateSceneMethod", PayRouteI18nHelper.payMethod(item.getMethod()));
            }
        }
    }

    /// 校验目录行：通道商户号与支付能力须同时为空或同时有值，不可只填其一
    private void validateSceneChannelMchCapabilityPairing(List<PayRouteSceneConfigItem> items) {
        for (PayRouteSceneConfigItem item : items) {
            if (StrUtil.isBlank(item.getMethod())) {
                continue;
            }
            boolean hasMch = StrUtil.isNotBlank(item.getChannelMchNo());
            boolean hasCap = StrUtil.isNotBlank(item.getCapability());
            if (hasMch == hasCap) {
                continue;
            }
            // 路由: 须同时选择通道商户与支付能力或同时留空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneChannelMchCapabilityPair", PayRouteI18nHelper.payMethod(item.getMethod()));
        }
    }

    /// 目录行未配置（通道商户与能力均为空）
    private boolean isSceneRowEmpty(PayRouteSceneConfigItem item) {
        return StrUtil.isBlank(item.getChannelMchNo()) && StrUtil.isBlank(item.getCapability());
    }

    /// 校验场景配置项支付渠道与支付方式
    ///
    /// 调用方已按 [PayRouteConfigProviders] 过滤，provider 必非空且为白名单渠道，
    /// 此处只需校验支付方式落在已启用的渠道支付方式目录内。
    private void validateSceneConfigItem(String providerCode, String method) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (Objects.isNull(provider)) {
            // 路由: 支付渠道无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProviderInvalid");
        }
        // 支付方式须在合并且启用的渠道支付方式目录内
        if (!payProviderMethodService.contains(providerCode, method)) {
            // 路由: 支付方式不在渠道目录中
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.methodNotInDirectory",
                    PayRouteI18nHelper.payMethod(method), PayRouteI18nHelper.provider(providerCode));
        }
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                // 路由: 通道路由策略不存在
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}
