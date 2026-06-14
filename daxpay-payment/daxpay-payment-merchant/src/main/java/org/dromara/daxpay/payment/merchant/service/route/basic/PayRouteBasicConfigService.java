package org.dromara.daxpay.payment.merchant.service.route.basic;

import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.dao.route.basic.PayRouteBasicConfigManager;
import org.dromara.daxpay.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import org.dromara.daxpay.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import org.dromara.daxpay.payment.merchant.entity.route.strategy.PayRouteStrategy;
import org.dromara.daxpay.payment.merchant.param.route.basic.PayRouteBasicConfigBatchParam;
import org.dromara.daxpay.payment.merchant.param.route.basic.PayRouteBasicConfigItem;
import org.dromara.daxpay.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import org.dromara.daxpay.payment.merchant.service.route.support.PayRouteConfigProviders;
import org.dromara.daxpay.payment.pay.strategy.AbsProductStrategy;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 通道路由基础模式配置
///
/// 按支付渠道配置默认支付产品，保存前校验产品策略能力。
@Service
@RequiredArgsConstructor
public class PayRouteBasicConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final PayRouteBasicConfigManager basicConfigManager;
    private final MchAppInfoManager mchAppInfoManager;

    /// 查询基础模式面板数据（已保存 product + 各渠道可选 products）
    public List<PayRouteBasicConfigResult> listBasicByAppId(String appId) {
        PayRouteStrategy strategy = requireStrategy(appId);
        Map<String, String> productMap = basicConfigManager.findByStrategyId(strategy.getId()).stream()
                .filter(config -> StrUtil.isNotBlank(config.getProvider()))
                .collect(Collectors.toMap(PayRouteBasicConfig::getProvider, PayRouteBasicConfig::getProduct, (a, b) -> a));
        return PayRouteConfigProviders.enumsInWhitelistOrder().stream()
                .map(provider -> toPanelResult(provider, productMap.get(provider.getCode())))
                .toList();
    }

    /// 批量保存基础模式配置（先删后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBasicBatch(PayRouteBasicConfigBatchParam param) {
        PayRouteStrategy strategy = requireStrategy(param.getAppId());
        basicConfigManager.deleteByStrategyId(strategy.getId());
        for (PayRouteBasicConfigItem item : param.getItems()) {
            if (StrUtil.isBlank(item.getProduct())) {
                continue;
            }
            PayProviderEnum provider = validateBasicPayProviderCode(item.getProvider());
            validateBasicProduct(item.getProduct(), provider);
            PayRouteBasicConfig config = new PayRouteBasicConfig();
            config.setStrategyId(strategy.getId());
            config.setProvider(item.getProvider());
            config.setProduct(item.getProduct());
            basicConfigManager.save(config);
        }
    }

    /// 校验产品存在且支持指定支付渠道
    public void validateBasicProduct(String product, PayProviderEnum provider) {
        ProductEnum productEnum = ProductEnum.findByCode(product);
        if (productEnum == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productInvalid", product);
        }
        if (!PaymentStrategyFactory.productSupportsProvider(product, provider)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProductNotAvailable", provider.getCode());
        }
        if (!PaymentStrategyFactory.existsByProduct(product, AbsProductStrategy.class)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.productStrategyMissing");
        }
    }

    /// 校验支付渠道编码合法且在通道路由白名单内
    private PayProviderEnum validateBasicPayProviderCode(String providerCode) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null || !PayRouteConfigProviders.contains(providerCode)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.route.error.basicProviderInvalid");
        }
        return provider;
    }

    /// 组装单渠道基础模式面板行：已保存 product + 该渠道下可选 products
    private PayRouteBasicConfigResult toPanelResult(PayProviderEnum provider, String savedProduct) {
        List<String> products = productsForProvider(provider);
        return new PayRouteBasicConfigResult()
                .setProvider(provider.getCode())
                .setProduct(savedProduct)
                .setProducts(products);
    }

    /// 从产品枚举中筛出支持指定支付渠道的产品编码
    private List<String> productsForProvider(PayProviderEnum provider) {
        return Arrays.stream(ProductEnum.values())
                .map(ProductEnum::getCode)
                .filter(product -> PaymentStrategyFactory.productSupportsProvider(product, provider))
                .distinct()
                .toList();
    }

    /// 按应用号加载路由策略，不存在则抛业务异常
    private PayRouteStrategy requireStrategy(String appId) {
        return strategyManager.findByAppId(appId)
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
    }
}