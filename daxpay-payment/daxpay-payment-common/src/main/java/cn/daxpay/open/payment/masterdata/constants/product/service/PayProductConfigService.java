package cn.daxpay.open.payment.masterdata.constants.product.service;

import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProduct;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
import cn.daxpay.open.payment.masterdata.constants.product.param.PayProductConfigParam;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductConfigResult;
import cn.daxpay.open.payment.core.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 支付产品配置服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayProductConfigService {

    private final PayProductConfigManager payProductConfigManager;

    private final PayProductService payProductService;

    /// 查询全部产品配置列表（卡片页使用）
    /// 融合 PayProduct + pay_md_product_config 表 + 策略信息
    public List<PayProductConfigResult> listAll() {
        Map<String, PayProductConfig> configMap = payProductConfigManager.lambdaQuery().list().stream()
                .collect(Collectors.toMap(PayProductConfig::getProduct, c -> c, (a, b) -> a));

        return payProductService.listSortedProducts()
                .stream()
                .map(payProduct -> toConfigResult(payProduct, configMap))
                .toList();
    }

    /// 切换产品的生效环境
    @Transactional(rollbackFor = Exception.class)
    public void switchEnv(String product, boolean sandbox) {
        PayProductConfig config = payProductConfigManager.findByProduct(product)
                .orElseGet(() -> createDefaultConfig(product));

        if (sandbox) {
            config.setActiveEnv(PayEnvEnum.SANDBOX.getCode());
        } else {
            config.setActiveEnv(PayEnvEnum.PROD.getCode());
        }
        payProductConfigManager.saveOrUpdate(config);
    }

    /// 保存或更新配置
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(PayProductConfigParam param) {
        PayProductConfig config = payProductConfigManager.findByProduct(param.getProduct())
                .orElseGet(PayProductConfig::new);

        config.setProduct(param.getProduct());
        config.setChannel(param.getChannel());
        config.setActiveEnv(param.getActiveEnv() != null ? param.getActiveEnv() : PayEnvEnum.PROD.getCode());
        config.setConfigured(param.isConfigured());
        config.setRemark(param.getRemark());
        payProductConfigManager.saveOrUpdate(config);
    }

    /// PayProduct + 库表 + 策略合并为配置结果
    private PayProductConfigResult toConfigResult(PayProduct payProduct, Map<String, PayProductConfig> configMap) {
        PayProductConfigResult result = new PayProductConfigResult()
                .setProduct(payProduct.getCode())
                .setName(I18nUtil.getEnumName(ProductEnum.findByCode(payProduct.getCode())))
                .setChannel(payProduct.getChannel())
                .setChannelName(I18nUtil.getEnumName(ChannelEnum.findByCode(payProduct.getChannel())));

        AbsProductStrategy strategy = resolveStrategy(payProduct.getCode());
        if (strategy != null) {
            result.setSandboxSupport(strategy.isSandbox());
            result.setIsv(strategy.isIsv());
        }

        PayProductConfig config = configMap.get(payProduct.getCode());
        if (config != null) {
            result.setId(config.getId());
            result.setActiveEnv(config.getActiveEnv());
            result.setConfigured(config.isConfigured());
        } else {
            result.setActiveEnv(PayEnvEnum.PROD.getCode());
            result.setConfigured(false);
        }

        return result;
    }

    /// 创建默认配置记录
    private PayProductConfig createDefaultConfig(String product) {
        ProductEnum productEnum = ProductEnum.findByCode(product);

        PayProductConfig config = new PayProductConfig();
        config.setProduct(product);
        config.setChannel(productEnum.getChannel());
        config.setActiveEnv(PayEnvEnum.PROD.getCode());
        config.setConfigured(false);
        payProductConfigManager.save(config);
        return config;
    }

    private AbsProductStrategy resolveStrategy(String productCode) {
        if (!PaymentStrategyFactory.existsByProduct(productCode, AbsProductStrategy.class)) {
            return null;
        }
        return PaymentStrategyFactory.createByProduct(productCode, AbsProductStrategy.class);
    }
}
