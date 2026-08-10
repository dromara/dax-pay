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

    @Schema(description = "黑名单拦截开关（IP / 用户标识）")
    private Boolean blacklistEnabled;

    @Schema(description = "命中黑名单后是否阻断下单（false=仅记录不拦截）")
    private Boolean riskBlockBeforePay;

    @Schema(description = "支付成功后是否补录命中（用于事后分析）")
    private Boolean riskCheckAfterPay;

    @Schema(description = "用户标识拦截级别（normal=正常拦截 / enhanced=增强拦截）")
    private String riskOpenIdLevel;

    @Schema(description = "海外 IP 拦截（默认关闭, 拦截境外 IP 支付请求）")
    private Boolean blockOverseasIp;

    @Schema(description = "地区拦截（默认关闭, 开启后根据 IP 归属地匹配省级与市级黑名单; 省级命中后不执行市级检查）")
    private Boolean regionBlacklistEnabled;

    @Schema(description = "IPv6 地区匹配开关（默认关闭, 开启后地域检查对 IPv6 执行匹配; 离线数据精度有限）")
    private Boolean ipv6MatchEnabled;

    @Schema(description = "地理围栏全局开关（默认关闭, 开启后各商户围栏 opt-in 才生效）")
    private Boolean geoFenceEnabled;

    @Schema(description = "地理围栏全局策略（strict 严格 / balanced 平衡 / loose 宽松）")
    private String geoFenceStrategy;
}
