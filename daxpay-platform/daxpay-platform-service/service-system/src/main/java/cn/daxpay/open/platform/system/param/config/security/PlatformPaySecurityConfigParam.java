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

    @Schema(description = "黑名单拦截开关（IP / 用户标识）")
    @NotNull(message = "{validation.field.blacklistEnabled.notNull}")
    private Boolean blacklistEnabled;

    @Schema(description = "命中黑名单后是否阻断下单（false=仅记录不拦截）")
    @NotNull(message = "{validation.field.riskBlockBeforePay.notNull}")
    private Boolean riskBlockBeforePay;

    @Schema(description = "支付成功后是否补录命中（用于事后分析）")
    @NotNull(message = "{validation.field.riskCheckAfterPay.notNull}")
    private Boolean riskCheckAfterPay;

    @Schema(description = "用户标识拦截级别（normal=正常拦截 / enhanced=增强拦截）")
    @NotNull(message = "{validation.field.riskOpenIdLevel.notNull}")
    private String riskOpenIdLevel;

    @Schema(description = "海外 IP 拦截（默认关闭, 拦截境外 IP 支付请求）")
    @NotNull(message = "{validation.field.blockOverseasIp.notNull}")
    private Boolean blockOverseasIp;

    @Schema(description = "省级地区拦截（默认关闭, 开启后根据 IP 归属省份匹配省级黑名单）")
    @NotNull(message = "{validation.field.provinceBlacklistEnabled.notNull}")
    private Boolean provinceBlacklistEnabled;

    @Schema(description = "市级地区拦截（默认关闭, 开启后根据 IP 归属城市匹配市级黑名单; 与省级开关独立）")
    @NotNull(message = "{validation.field.cityBlacklistEnabled.notNull}")
    private Boolean cityBlacklistEnabled;

    @Schema(description = "地理围栏全局开关（默认关闭, 开启后各商户围栏 opt-in 才生效）")
    @NotNull(message = "{validation.field.geoFenceEnabled.notNull}")
    private Boolean geoFenceEnabled;

    @Schema(description = "地理围栏全局策略（strict 严格 / balanced 平衡 / loose 宽松）")
    @NotNull(message = "{validation.field.geoFenceStrategy.notNull}")
    private String geoFenceStrategy;
}
