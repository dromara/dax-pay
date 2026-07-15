package cn.daxpay.open.payment.merchant.service.gateway;

import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeClientEnvManager;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCodeConfigManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeConfig;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 码牌支付策略解析
///
/// 仅读 [GatewayCodeConfig] 系列表; **不回落**聚合配置。
/// METHOD/DIRECT 使用子表配置字面值, **不做** jsapi→mini 隐式升级。
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayResolveService {

    private final GatewayCodeConfigManager configManager;
    private final GatewayCodeClientEnvManager clientEnvManager;

    /// 解析结果
    public record Resolved(String method, String channelMchNo, String capability) {}

    /// 必须已有码牌配置; 按 clientEnv + payForm 解析
    public Resolved resolveRequired(String appId, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        GatewayCodeConfig config = configManager.findByAppId(appId)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.codeConfigMissing"));
        AggregateConfigLevelEnum level = AggregateConfigLevelEnum.findByCode(config.getLevel());
        CodePayFormEnum form = payForm == null ? CodePayFormEnum.H5 : payForm;

        return switch (level) {
            case AUTO -> new Resolved(form.defaultMethodCode(clientEnv), null, null);
            case METHOD -> {
                GatewayCodeClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getMethod())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvNotConfigured");
                }
                yield new Resolved(envConfig.getMethod(), null, null);
            }
            case DIRECT -> {
                GatewayCodeClientEnv envConfig = requireEnvConfig(config, clientEnv, form);
                if (StrUtil.isBlank(envConfig.getChannelMchNo())) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.gateway.codeClientEnvNotConfigured");
                }
                String method = StrUtil.isNotBlank(envConfig.getMethod())
                        ? envConfig.getMethod()
                        : form.defaultMethodCode(clientEnv);
                yield new Resolved(method, envConfig.getChannelMchNo(), envConfig.getCapability());
            }
        };
    }

    private GatewayCodeClientEnv requireEnvConfig(
            GatewayCodeConfig config, ClientEnvEnum clientEnv, CodePayFormEnum payForm) {
        GatewayCodeClientEnv envConfig = clientEnvManager.findByConfigIdAndClientEnvAndPayForm(
                config.getId(), clientEnv.getCode(), payForm.getCode());
        if (envConfig == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeClientEnvNotConfigured");
        }
        return envConfig;
    }
}
