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
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;

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

    private final CacheManager cacheManager;

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
    @CacheEvict(value = "payment:product-sandbox", key = "#product")
    public void switchEnv(String product, boolean sandbox) {
        // 仅沙箱产品(模拟通道)恒为 sandbox, 不允许切到生产
        if (!sandbox && isSandboxOnly(product)) {
            // 产品: 该产品仅支持沙箱环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.env.sandboxOnly");
        }
        // 全局沙箱开关校验
        if (sandbox && !platformConfigProperties.isSandboxEnabled()) {
            // 产品: 沙箱环境未开启
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

    /// 切换支付产品启停(2026-09-13 启停入口随配置页迁移, 启停逻辑本体在 [PayProductService#switchEnabled])
    public void switchEnabled(String product, boolean enabled) {
        payProductService.switchEnabled(product, enabled);
    }

    /// 保存或更新配置
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "payment:product-sandbox", key = "#param.product")
    public void saveOrUpdate(PayProductConfigParam param) {
        // 全局沙箱开关校验
        if (PayEnvEnum.SANDBOX.getCode().equals(param.getActiveEnv())
                && !platformConfigProperties.isSandboxEnabled()) {
            // 产品: 沙箱环境未开启
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.env.sandboxDisabled");
        }

        PayProductConfig config = payProductConfigManager.findByProduct(param.getProduct())
                .orElseGet(() -> createDefaultConfig(param.getProduct()));

        config.setProduct(param.getProduct());
        config.setChannel(param.getChannel());
        // 仅沙箱产品强制 sandbox, 忽略调用方传入的生产环境
        if (isSandboxOnly(param.getProduct())) {
            config.setActiveEnv(PayEnvEnum.SANDBOX.getCode());
        } else {
            config.setActiveEnv(Objects.nonNull(param.getActiveEnv()) ? param.getActiveEnv() : PayEnvEnum.PROD.getCode());
        }
        config.setRemark(param.getRemark());
        payProductConfigManager.saveOrUpdate(config);
    }

    /// 应用启动后检查: 沙箱被全局禁用时, 将所有 activeEnv=sandbox 的产品强制重置为 prod
    ///
    /// 注意: 通道商户的 sandbox 字段固化为创建时的快照, 此处不级联回写。
    /// 重置后若存量 sandbox=true 的商户与产品当前 prod 环境不匹配, 路由层会按环境过滤将其排除。
    ///
    /// 部署规范: 生产环境必须配置 daxpay.platform.config.sandbox-enabled=false;
    ///         测试/开发环境才允许 sandbox-enabled=true 用于沙箱联调。
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void checkSandboxOnStartup() {
        // 先补齐仅沙箱产品的配置行, 再按全局开关对齐
        ensureSandboxOnlyConfigs();
        if (platformConfigProperties.isSandboxEnabled()) {
            log.warn("⚠️ 沙箱环境已全局启用 (daxpay.platform.config.sandbox-enabled=true) → 当前为测试/开发环境, 切勿用于生产部署");
            return;
        }
        List<PayProductConfig> sandboxConfigs = payProductConfigManager.lambdaQuery()
                .eq(PayProductConfig::getActiveEnv, PayEnvEnum.SANDBOX.getCode())
                .list()
                .stream()
                // 仅沙箱产品(模拟通道)不参与重置: 恒沙箱是产品语义, 生产部署下该产品不装配、无实际支付能力
                .filter(config -> !isSandboxOnly(config.getProduct()))
                .toList();
        for (PayProductConfig config : sandboxConfigs) {
            config.setActiveEnv(PayEnvEnum.PROD.getCode());
            payProductConfigManager.updateById(config);
        }
        // 失效产品沙箱缓存: L2 Redis 跨重启存活, 上次运行写入的 sandbox=true 若不清除,
        // 路由环境一致性校验在 L2 TTL 窗口内仍按沙箱环境匹配(启用方判定与强置后的 DB 不一致)。
        // 本方法被事件监听自调用不走代理, 无法依赖 @CacheEvict, 故编程式 clear。
        cacheManager.getCache("payment:product-sandbox").clear();
        log.info("✓ 沙箱环境已全局禁用 (daxpay.platform.config.sandbox-enabled=false) → 生产模式启动, 数据保证为生产数据{}",
                sandboxConfigs.isEmpty() ? "" : ", 已将 " + sandboxConfigs.size() + " 个产品从 sandbox 强制重置为 prod");
    }

    /// PayProduct + 库表 + 策略合并为配置结果
    private PayProductConfigResult toConfigResult(PayProduct payProduct, Map<String, PayProductConfig> configMap) {
        var result = new PayProductConfigResult()
                .setProduct(payProduct.getCode())
                .setName(I18nUtil.getEnumName(ProductEnum.findByCode(payProduct.getCode())))
                .setChannel(payProduct.getChannel())
                .setChannelName(I18nUtil.getEnumName(ChannelEnum.findByCode(payProduct.getChannel())))
                .setEnabled(payProduct.isEnabled());

        AbsProductStrategy strategy = resolveStrategy(payProduct.getCode());
        // online = 通道策略存在且非预留产品(预留/无策略一律视为规划中)
        result.setOnline(strategy != null && !ProductEnum.isReserved(payProduct.getCode()));
        if (Objects.nonNull(strategy)) {
            result.setSandboxSupport(strategy.isSandbox());
            result.setSandboxOnly(strategy.isSandboxOnly());
            result.setIsv(strategy.isIsv());
        }

        PayProductConfig config = configMap.get(payProduct.getCode());
        if (Objects.nonNull(config)) {
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
        // 仅沙箱产品默认落 sandbox, 其余默认生产
        config.setActiveEnv(isSandboxOnly(product) ? PayEnvEnum.SANDBOX.getCode() : PayEnvEnum.PROD.getCode());
        payProductConfigManager.save(config);
        return config;
    }

    /// 补齐「仅沙箱产品」的配置行并纠正为沙箱生效
    ///
    /// pay_md_product_config 缺行时各读取方按生产环境处理(卡片页 activeEnv=prod / 路由 isSandboxActive=false),
    /// 与仅沙箱产品「恒沙箱」语义冲突: 卡片不显示沙箱金框, 且其通道商户(创建时固定 sandbox=true)
    /// 会在路由环境一致性校验处被判为环境不匹配。故启动时按策略层声明补齐/纠正, 幂等。
    private void ensureSandboxOnlyConfigs() {
        List<AbsProductStrategy> sandboxOnlyStrategies = PaymentStrategyFactory.createGroup(AbsProductStrategy.class)
                .stream()
                .filter(AbsProductStrategy::isSandboxOnly)
                .toList();
        for (AbsProductStrategy strategy : sandboxOnlyStrategies) {
            String product = strategy.getProduct().getCode();
            PayProductConfig config = payProductConfigManager.findByProduct(product).orElse(null);
            if (Objects.isNull(config)) {
                // 新建行默认即 sandbox(见 createDefaultConfig)
                createDefaultConfig(product);
                // 缺行期间可能已被读侧缓存为 false, 需失效
                cacheManager.getCache("payment:product-sandbox").evict(product);
                log.info("仅沙箱产品[{}]缺少产品配置行, 已补齐(activeEnv=sandbox)", product);
            } else if (!PayEnvEnum.SANDBOX.getCode().equals(config.getActiveEnv())) {
                config.setActiveEnv(PayEnvEnum.SANDBOX.getCode());
                payProductConfigManager.updateById(config);
                cacheManager.getCache("payment:product-sandbox").evict(product);
                log.info("仅沙箱产品[{}]配置行环境非沙箱, 已纠正为 sandbox", product);
            }
        }
    }

    /// 是否为仅沙箱产品(模拟通道等): 生效环境恒为 sandbox, 不允许切换到生产
    private boolean isSandboxOnly(String product) {
        AbsProductStrategy strategy = resolveStrategy(product);
        return Objects.nonNull(strategy) && strategy.isSandboxOnly();
    }

    private AbsProductStrategy resolveStrategy(String productCode) {
        if (!PaymentStrategyFactory.existsByProduct(productCode, AbsProductStrategy.class)) {
            return null;
        }
        return PaymentStrategyFactory.createByProduct(productCode, AbsProductStrategy.class);
    }
}
