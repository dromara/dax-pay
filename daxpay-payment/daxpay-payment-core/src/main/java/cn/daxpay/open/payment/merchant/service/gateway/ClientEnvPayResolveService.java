package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayAggregateConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 聚合扫码支付方式解析
///
/// 读应用级**聚合**扫码配置(AUTO/METHOD/DIRECT), 输出 method / channelMchNo / capability。
/// 仅供 [cn.daxpay.open.payment.trade.runtime.service.pay.gateway.AggregatePayService] 使用。
/// 码牌支付请使用 [CodePayResolveService], 不再读本服务。
/// DIRECT: 与路由直接指定对齐——method 空时由 channelMch+capability 反推。
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientEnvPayResolveService {

    private final GatewayAggregateConfigManager configManager;
    private final GatewayAggregateClientEnvManager clientEnvManager;
    private final PayRouteService payRouteService;

    /// 解析结果
    public record Resolved(String method, String channelMchNo, String capability) {}

    /// 按应用聚合配置解析(无配置 → AUTO)
    ///
    /// @param appId     应用号
    /// @param clientEnv 客户端环境
    /// @param runtime   运行形态(默认 H5); mini 时 method 自动升级
    public Resolved resolve(String appId, ClientEnvEnum clientEnv, ClientRuntimeEnum runtime) {
        ClientRuntimeEnum rt = runtime == null ? ClientRuntimeEnum.H5 : runtime;
        GatewayAggregateConfig config = configManager.findByAppId(appId).orElse(null);
        AggregateConfigLevelEnum level = config == null
                ? AggregateConfigLevelEnum.AUTO
                : AggregateConfigLevelEnum.findByCode(config.getLevel());

        return switch (level) {
            case AUTO -> new Resolved(clientEnv.defaultMethodCode(rt), null, null);
            case METHOD -> {
                GatewayAggregateClientEnv envConfig = requireEnvConfig(config, clientEnv);
                if (StrUtil.isBlank(envConfig.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvNotConfigured");
                }
                String method = ClientEnvEnum.adaptMethodForRuntime(envConfig.getMethod(), rt);
                yield new Resolved(method, null, null);
            }
            case DIRECT -> {
                GatewayAggregateClientEnv envConfig = requireEnvConfig(config, clientEnv);
                if (StrUtil.isBlank(envConfig.getChannelMchNo()) || StrUtil.isBlank(envConfig.getCapability())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.clientEnvNotConfigured");
                }
                String method;
                if (StrUtil.isNotBlank(envConfig.getMethod())) {
                    method = ClientEnvEnum.adaptMethodForRuntime(envConfig.getMethod(), rt);
                }
                else {
                    // 与码牌/路由 DIRECT 一致：按能力反推，禁止静默默认 JSAPI
                    String inferred = payRouteService.inferMethodForCapability(
                            envConfig.getChannelMchNo(), envConfig.getCapability());
                    if (StrUtil.isBlank(inferred)) {
                        PayCapabilityEnum capEnum = PayCapabilityEnum.findByCode(envConfig.getCapability());
                        String capLabel = capEnum != null ? I18nUtil.getEnumName(capEnum) : envConfig.getCapability();
                        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                                "pay.route.error.directCapabilityChannelMchMismatch",
                                capLabel, envConfig.getChannelMchNo());
                    }
                    method = ClientEnvEnum.adaptMethodForRuntime(inferred, rt);
                }
                yield new Resolved(method, envConfig.getChannelMchNo(), envConfig.getCapability());
            }
        };
    }

    /// 聚合支付: 必须已有聚合配置
    public Resolved resolveRequired(String appId, ClientEnvEnum clientEnv, ClientRuntimeEnum runtime) {
        if (configManager.findByAppId(appId).isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.aggregateConfigMissing");
        }
        return resolve(appId, clientEnv, runtime);
    }

    private GatewayAggregateClientEnv requireEnvConfig(GatewayAggregateConfig config, ClientEnvEnum clientEnv) {
        if (config == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.aggregateConfigMissing");
        }
        GatewayAggregateClientEnv envConfig = clientEnvManager
                .findByConfigIdAndClientEnv(config.getId(), clientEnv.getCode());
        if (envConfig == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotConfigured");
        }
        return envConfig;
    }
}
