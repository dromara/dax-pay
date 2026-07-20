package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付安全配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付安全配置参数")
public class PlatformPaySecurityConfigParam {

    @Schema(description = "风控总开关（关闭后所有风控检查跳过）")
    @NotNull(message = "{validation.field.riskEnabled.notNull}")
    private Boolean riskEnabled;

    @Schema(description = "命中黑名单后是否阻断下单（false=仅记录不拦截）")
    @NotNull(message = "{validation.field.riskBlockBeforePay.notNull}")
    private Boolean riskBlockBeforePay;

    @Schema(description = "支付成功后是否补录命中（用于事后分析）")
    @NotNull(message = "{validation.field.riskCheckAfterPay.notNull}")
    private Boolean riskCheckAfterPay;

    @Schema(description = "用户标识拦截级别（normal=正常拦截 / enhanced=增强拦截）")
    @NotNull(message = "{validation.field.riskOpenIdLevel.notNull}")
    private String riskOpenIdLevel;

    @Schema(description = "海外 IP 拦截（占位字段, 默认关闭, 后续接入）")
    @NotNull(message = "{validation.field.blockOverseasIp.notNull}")
    private Boolean blockOverseasIp;
}
