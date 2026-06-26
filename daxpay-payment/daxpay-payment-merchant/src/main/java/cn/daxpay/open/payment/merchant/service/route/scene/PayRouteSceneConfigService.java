package cn.daxpay.open.payment.merchant.service.route.scene;

import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.route.scene.PayRouteSceneConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigItem;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.merchant.service.route.runtime.PayRouteProductResolver;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteConfigProviders;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteI18nHelper;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteStrategyCapabilitySupport;
import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
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
    private final PayRouteProductResolver productResolver;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteStrategyCapabilitySupport payRouteStrategyCapabilitySupport;
    private final PayRouteMethodValidator payRouteMethodValidator;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProviderMethodService payProviderMethodService;

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
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        PayRouteStrategy strategy = requireStrategy(param.getAppId());
        validateSceneConfigUnique(param.getItems());
        validateSceneChannelMchCapabilityPairing(param.getItems());
        sceneConfigManager.deleteByStrategyId(strategy.getId());
        List<PayRouteSceneConfig> configs = new ArrayList<>();
        for (PayRouteSceneConfigItem item : param.getItems()) {
            if (StrUtil.isBlank(item.getMethod()) || !PayRouteConfigProviders.contains(item.getProvider())) {
                continue;
            }
            if (isSceneRowEmpty(item)) {
                continue;
            }
            payRouteMethodValidator.validateSceneConfigItem(item.getProvider(), item.getMethod());
            // 校验通道商户属本商户且启用
            ChannelMerchant mch = channelMerchantManager
                    .findByMchNoAndChannelMchNo(mchNo, item.getChannelMchNo())
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.route.error.channelMchNotExist", item.getChannelMchNo()));
            if (!Boolean.TRUE.equals(mch.getEnable())) {
                // 通道商户[{0}]未启用
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.channelMchDisabled", item.getChannelMchNo());
            }
            String product = mch.getProduct();
            // 校验产品支持该(provider, method)
            if (!payRouteStrategyCapabilitySupport.routeProductSupportsMethod(
                    product, item.getProvider(), item.getMethod())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.sceneMethodProductMismatch",
                        PayRouteI18nHelper.payMethod(item.getMethod()), PayRouteI18nHelper.product(product));
            }
            // 校验支付能力在候选集合内
            payRouteStrategyCapabilitySupport.validateSceneCapability(
                    item.getProvider(), item.getMethod(), item.getChannelMchNo(), item.getCapability());
            // 通道编码由产品派生
            String channel = productResolver.channelOfProduct(product);
            PayRouteSceneConfig config = new PayRouteSceneConfig();
            config.setStrategyId(strategy.getId());
            config.setProvider(item.getProvider());
            config.setChannel(channel);
            config.setMethod(item.getMethod());
            config.setChannelMchNo(item.getChannelMchNo());
            config.setCapability(item.getCapability());
            configs.add(config);
        }
        for (PayRouteSceneConfig config : configs) {
            sceneConfigManager.save(config);
        }
    }

    /// 通道路由白名单目录下全部 (provider,method) 的通道商户候选（批量）
    public Map<String, List<LabelValue>> listSceneChannelMchCandidatesBatch(String appId) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.listSceneChannelMchCandidatesBatch(mchNo);
    }

    /// 按目录项+通道商户批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidatesBatch(mchNo, param.getItems());
    }

    /// 按目录项（支付渠道+支付方式）筛选商户已开通的通道商户候选
    public List<LabelValue> listSceneChannelMchCandidatesForMethod(String appId, String provider, String method) {
        if (StrUtil.isBlank(method) || !payProviderMethodService.contains(provider, method)) {
            return List.of();
        }
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        return payRouteStrategyCapabilitySupport.listSceneChannelMchCandidates(mchNo, provider, method);
    }

    /// 按目录项与通道商户筛选支付能力候选（策略 Map ∩ DB，不落库）
    public List<LabelValue> listSceneCapabilityCandidatesForMethod(
            String appId, String provider, String method, String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return List.of();
        }
        return payRouteStrategyCapabilitySupport.listSceneCapabilityCandidates(provider, method, channelMchNo);
    }

    /// 回显推断支付能力（候选唯一时返回编码）
    public String inferSceneCapability(String appId, String provider, String method, String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return null;
        }
        return payRouteStrategyCapabilitySupport.inferSceneCapability(provider, method, channelMchNo);
    }

    /// 校验批量保存项：支付方式全局唯一
    private void validateSceneConfigUnique(List<PayRouteSceneConfigItem> items) {
        Set<String> methodKeys = new HashSet<>();
        for (PayRouteSceneConfigItem item : items) {
            if (StrUtil.isBlank(item.getMethod())) {
                continue;
            }
            if (!methodKeys.add(item.getMethod())) {
                // 场景模式下同一支付方式存在多条配置
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
            // 支付方式[{0}]须同时选择通道商户与支付能力，或同时留空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.sceneChannelMchCapabilityPair", PayRouteI18nHelper.payMethod(item.getMethod()));
        }
    }

    /// 目录行未配置（通道商户与能力均为空）
    private boolean isSceneRowEmpty(PayRouteSceneConfigItem item) {
        return StrUtil.isBlank(item.getChannelMchNo()) && StrUtil.isBlank(item.getCapability());
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}
