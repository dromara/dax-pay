package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付安全配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付安全配置结果")
public class PlatformPaySecurityConfigResult extends BaseResult {

    @Schema(description = "风控总开关（关闭后所有风控检查跳过）")
    private Boolean riskEnabled;

    @Schema(description = "命中黑名单后是否阻断下单（false=仅记录不拦截）")
    private Boolean riskBlockBeforePay;

    @Schema(description = "支付成功后是否补录命中（用于事后分析）")
    private Boolean riskCheckAfterPay;

    @Schema(description = "openId 拦截级别（normal=正常拦截 / enhanced=增强拦截）")
    private String riskOpenIdLevel;

    @Schema(description = "海外 IP 拦截（占位字段, 默认关闭, 后续接入）")
    private Boolean blockOverseasIp;
}
