package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeConfig;
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

/// # 码牌支付策略解析
///
/// 仅读 [GatewayCodeConfig] 系列表; **不回落**聚合配置。
/// METHOD/DIRECT 使用子表配置字面值, **不做** jsapi→mini 隐式升级。
/// DIRECT: 与路由直接指定对齐——method 空时由 channelMch+capability 反推，禁止静默默认 JSAPI。
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayResolveService {

    private final GatewayCodeConfigManager configManager;
    private final GatewayCodeClientEnvManager clientEnvManager;
    private final PayRouteService payRouteService;

    /// 解析结果
    public record Resolved(String method, String channelMchNo, String capability) {}

    /// 必须已有码牌配置; 按 clientEnv + payForm 解析
    public Resolved resolveRequired(String appId, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        GatewayCodeConfig config = configManager.findByAppId(appId)
                // 码牌: 应用未配置码牌支付策略
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.codeConfigMissing"));
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(config.getLevel());
        CodePayFormEnum form = payForm == null ? CodePayFormEnum.H5 : payForm;

        return switch (level) {
            case AUTO -> new Resolved(form.defaultMethodCode(clientEnv), null, null);
            case METHOD -> {
                GatewayCodeClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getMethod())) {
                    // 码牌: 该客户端环境与支付形态未配置支付方式
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvNotConfigured");
                }
                yield new Resolved(envConfig.getMethod(), null, null);
            }
            case DIRECT -> {
                GatewayCodeClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getChannelMchNo()) || StrUtil.isBlank(envConfig.getCapability())) {
                    // 码牌: 该客户端环境与支付形态未配置支付方式
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvNotConfigured");
                }
                String method = resolveDirectMethod(
                        envConfig.getMethod(), envConfig.getChannelMchNo(), envConfig.getCapability());
                yield new Resolved(method, envConfig.getChannelMchNo(), envConfig.getCapability());
            }
        };
    }

    /// DIRECT: 优先用已配 method；否则按通道商户+能力反推（与 [PayRouteService#inferMethodForCapability] 一致）
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

    /// 获取码牌配置的客户端环境与支付形态子表, 不存在则抛异常
    private GatewayCodeClientEnv requireEnvConfig(
            GatewayCodeConfig config, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        GatewayCodeClientEnv envConfig = clientEnvManager.findByConfigIdAndClientEnvAndPayForm(
                config.getId(), clientEnv.getCode(), payForm.getCode());
        if (envConfig == null) {
            // 码牌: 该客户端环境与支付形态未配置支付方式
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeClientEnvNotConfigured");
        }
        return envConfig;
    }
}
