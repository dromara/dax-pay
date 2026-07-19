package cn.daxpay.open.payment.admin.service.masterdata.product;

import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.payment.masterdata.param.product.PayProductConfigParam;
import cn.daxpay.open.payment.masterdata.result.product.PayProductConfigResult;
import cn.daxpay.open.payment.masterdata.service.product.PayProductService;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 支付产品配置服务
///
/// 生效环境(activeEnv)的唯一可切换入口。
/// 通道商户的 sandbox 字段在创建时按当时产品 activeEnv 固化写入, 之后不再随产品切换而改变;
/// 切换产品环境仅影响「新建商户的默认环境」与「路由目标环境」, 不级联回写存量商户。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayProductConfigService {

    private final PayProductConfigManager payProductConfigManager;

    private final PayProductService payProductService;

    private final PlatformConfigProperties platformConfigProperties;

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

    /// 切换产品的生效环境(唯一入口)
    ///
    /// 仅更新 pay_md_product_config.activeEnv, 不级联回写通道商户 sandbox。
    /// 切换后: 已存在的通道商户保持各自固化环境; 新建商户的默认环境与路由目标环境随之变更。
    @Transactional(rollbackFor = Exception.class)
    public void switchEnv(String product, boolean sandbox) {
        // 全局沙箱开关校验
        if (sandbox && !platformConfigProperties.isSandboxEnabled()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.env.sandboxDisabled");
        }

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
        // 全局沙箱开关校验
        if (PayEnvEnum.SANDBOX.getCode().equals(param.getActiveEnv())
                && !platformConfigProperties.isSandboxEnabled()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.env.sandboxDisabled");
        }

        PayProductConfig config = payProductConfigManager.findByProduct(param.getProduct())
                .orElseGet(PayProductConfig::new);

        config.setProduct(param.getProduct());
        config.setChannel(param.getChannel());
        config.setActiveEnv(param.getActiveEnv() != null ? param.getActiveEnv() : PayEnvEnum.PROD.getCode());
        config.setRemark(param.getRemark());
        payProductConfigManager.saveOrUpdate(config);
    }

    /// 应用启动后检查: 沙箱被全局禁用时, 将所有 activeEnv=sandbox 的产品强制重置为 prod
    ///
    /// 注意: 通道商户的 sandbox 字段固化为创建时的快照, 此处不级联回写。
    /// 重置后若存量 sandbox=true 的商户与产品当前 prod 环境不匹配, 路由层会按环境过滤将其排除。
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void checkSandboxOnStartup() {
        if (platformConfigProperties.isSandboxEnabled()) {
            return;
        }
        List<PayProductConfig> sandboxConfigs = payProductConfigManager.lambdaQuery()
                .eq(PayProductConfig::getActiveEnv, PayEnvEnum.SANDBOX.getCode())
                .list();
        for (PayProductConfig config : sandboxConfigs) {
            config.setActiveEnv(PayEnvEnum.PROD.getCode());
            payProductConfigManager.updateById(config);
        }
        if (!sandboxConfigs.isEmpty()) {
            log.info("沙箱环境已禁用(daxpay.platform.config.sandbox-enabled=false), {} 个产品从 sandbox 重置为 prod",
                    sandboxConfigs.size());
        }
    }

    /// PayProduct + 库表 + 策略合并为配置结果
    private PayProductConfigResult toConfigResult(PayProduct payProduct, Map<String, PayProductConfig> configMap) {
        var result = new PayProductConfigResult()
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
        } else {
            result.setActiveEnv(PayEnvEnum.PROD.getCode());
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
