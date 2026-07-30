package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayPayConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 网关支付配置解析(码牌/聚合共用)
///
/// 读应用级网关支付配置(AUTO/METHOD/DIRECT), 输出 method / channelMchNo / capability。
/// 码牌与聚合扫码共用同一份配置, 入参为 (appId, clientEnv, payForm)。
/// DIRECT: 与路由直接指定对齐——method 空时由 channelMch+capability 反推, 禁止静默默认。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayConfigResolveService {

    private final GatewayPayConfigManager configManager;
    private final GatewayPayClientEnvManager clientEnvManager;
    private final PayRouteService payRouteService;

    /// 解析结果
    public record Resolved(String method, String channelMchNo, String capability) {}

    /// 按应用配置解析(无配置 → AUTO)
    ///
    /// @param appId     应用号
    /// @param clientEnv 客户端环境
    /// @param payForm   支付形态(h5/mini)
    public Resolved resolve(String appId, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        CodePayFormEnum form = payForm == null ? CodePayFormEnum.H5 : payForm;
        GatewayPayConfig config = configManager.findByAppId(appId).orElse(null);
        AggregateConfigLevelEnum level = config == null
                ? AggregateConfigLevelEnum.AUTO
                : AggregateConfigLevelEnum.findByCode(config.getLevel());

        return switch (level) {
            case AUTO -> new Resolved(form.defaultMethodCode(clientEnv), null, null);
            case METHOD -> {
                GatewayPayClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getMethod())) {
                    // 网关: 该客户端环境与支付形态未配置支付方式
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvNotConfigured");
                }
                yield new Resolved(envConfig.getMethod(), null, null);
            }
            case DIRECT -> {
                GatewayPayClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getChannelMchNo()) || StrUtil.isBlank(envConfig.getCapability())) {
                    // 网关: 该客户端环境与支付形态未配置支付方式
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvNotConfigured");
                }
                String method = resolveDirectMethod(
                        envConfig.getMethod(), envConfig.getChannelMchNo(), envConfig.getCapability());
                yield new Resolved(method, envConfig.getChannelMchNo(), envConfig.getCapability());
            }
        };
    }

    /// 必须已有网关支付配置
    public Resolved resolveRequired(String appId, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        if (configManager.findByAppId(appId).isEmpty()) {
            // 网关: 应用未配置网关支付策略
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.payConfigMissing");
        }
        return resolve(appId, clientEnv, payForm);
    }

    /// DIRECT: 优先用已配 method；否则按通道商户+能力反推
    private String resolveDirectMethod(String configuredMethod, String channelMchNo, String capability) {
        if (StrUtil.isNotBlank(configuredMethod)) {
            return configuredMethod;
        }
        String inferred = payRouteService.inferMethodForCapability(channelMchNo, capability);
        if (StrUtil.isBlank(inferred)) {
            PayCapabilityEnum capEnum = PayCapabilityEnum.findByCode(capability);
            String capLabel = capEnum != null ? I18nUtil.getEnumName(capEnum) : capability;
            // 路由: 支付能力与通道商户不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.directCapabilityChannelMchMismatch", capLabel, channelMchNo);
        }
        return inferred;
    }

    /// 获取网关支付的客户端环境与支付形态子表, 不存在则抛异常
    private GatewayPayClientEnv requireEnvConfig(
            GatewayPayConfig config, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        if (config == null) {
            // 网关: 应用未配置网关支付策略
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.payConfigMissing");
        }
        GatewayPayClientEnv envConfig = clientEnvManager.findByConfigIdAndClientEnvAndPayForm(
                config.getId(), clientEnv.getCode(), payForm.getCode());
        if (envConfig == null) {
            // 网关: 该客户端环境与支付形态未配置支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotConfigured");
        }
        return envConfig;
    }
}
