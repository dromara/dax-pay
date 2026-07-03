package cn.daxpay.open.payment.merchant.service.route.basic;

import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.param.route.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.basic.PayRouteBasicConfigItem;
import cn.daxpay.open.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteConfigProviders;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 通道路由基础模式配置
///
/// 按支付渠道配置默认通道商户(唯一绑定支付产品)，保存前校验通道商户归属与渠道支持。
@Service
@RequiredArgsConstructor
public class PayRouteBasicConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 查询基础模式面板数据（已保存通道商户号 + 各渠道可选通道商户列表）
    public List<PayRouteBasicConfigResult> listBasicByAppId(String appId) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(appId);
        PayRouteStrategy strategy = requireStrategy(appId);
        Map<String, String> mchMap = basicConfigManager.findByStrategyId(strategy.getId()).stream()
                .filter(config -> StrUtil.isNotBlank(config.getProvider()))
                .collect(Collectors.toMap(PayRouteBasicConfig::getProvider,
                        PayRouteBasicConfig::getChannelMchNo, (a, b) -> a));
        return PayRouteConfigProviders.enumsInWhitelistOrder().stream()
                .map(provider -> toPanelResult(mchNo, provider, mchMap.get(provider.getCode())))
                .toList();
    }

    /// 批量保存基础模式配置（先删后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBasicBatch(PayRouteBasicConfigBatchParam param) {
        String mchNo = mchAppInfoManager.requireMchNoByAppIdNotTenant(param.getAppId());
        PayRouteStrategy strategy = requireStrategy(param.getAppId());
        basicConfigManager.deleteByStrategyId(strategy.getId());
        for (PayRouteBasicConfigItem item : param.getItems()) {
            if (StrUtil.isBlank(item.getChannelMchNo())) {
                continue;
            }
            PayProviderEnum provider = validateBasicPayProviderCode(item.getProvider());
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
            if (!PaymentStrategyFactory.productSupportsProvider(mch.getProduct(), provider)) {
                // 支付渠道[{0}]下无可用支付产品
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.basicProductNotAvailable", provider.getCode());
            }
            if (!PaymentStrategyFactory.existsByProduct(mch.getProduct(), AbsProductStrategy.class)) {
                // 支付产品策略不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.productStrategyMissing");
            }
            PayRouteBasicConfig config = new PayRouteBasicConfig();
            config.setStrategyId(strategy.getId());
            config.setProvider(item.getProvider());
            config.setChannelMchNo(item.getChannelMchNo());
            basicConfigManager.save(config);
        }
    }

    /// 校验支付渠道编码合法且在通道路由白名单内
    private PayProviderEnum validateBasicPayProviderCode(String providerCode) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null || !PayRouteConfigProviders.contains(providerCode)) {
            // 支付渠道无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.basicProviderInvalid");
        }
        return provider;
    }

    /// 组装单渠道基础模式面板行：已保存通道商户号 + 该渠道下可选通道商户列表
    private PayRouteBasicConfigResult toPanelResult(String mchNo, PayProviderEnum provider, String savedChannelMchNo) {
        List<LabelValue> mchants = channelMerchantManager.findAllByMchNo(mchNo).stream()
                .filter(mch -> Boolean.TRUE.equals(mch.getEnable()))
                .filter(mch -> PaymentStrategyFactory.productSupportsProvider(mch.getProduct(), provider))
                .map(mch -> new LabelValue(
                        StrUtil.isNotBlank(mch.getChannelMerchantName())
                                ? mch.getChannelMerchantName() : mch.getChannelMchNo(),
                        mch.getChannelMchNo()))
                .toList();
        return new PayRouteBasicConfigResult()
                .setProvider(provider.getCode())
                .setChannelMchNo(savedChannelMchNo)
                .setChannelMchants(mchants);
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}
