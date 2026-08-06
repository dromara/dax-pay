package cn.daxpay.open.platform.system.entity.config.platform.security;

import cn.daxpay.open.platform.system.enums.PayRiskOpenIdLevelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付安全配置
///
/// 控制支付交易链路的风控开关。风控检查器实现见
/// [cn.daxpay.open.platform.plugin.risk.strategy.DefaultPayRiskChecker]，
/// 契约见 [cn.daxpay.open.payment.strategy.risk.PayRiskChecker]。
///
/// - 风控总开关: 关闭后支付主链路跳过所有风控检查
/// - 黑名单拦截: IP / 用户标识黑名单检查开关（第一层）
/// - 命中阻断: 支付前命中黑名单是否拒绝下单（关闭则仅记录不拦截）
/// - 事后补录: 支付成功后是否补充记录命中（用于事后分析，不阻断资金态）
/// - 用户标识拦截级别: 是否对 H5 / 主扫等免用户标识方式强制 OAuth, 见 [PayRiskOpenIdLevelEnum]
/// - 海外 IP 拦截: 拦截境外 IP 发起的支付请求（第二层）
/// - 省级地区拦截: 根据 IP 归属省份匹配省级黑名单（第二层）
/// - 市级地区拦截: 根据 IP 归属城市匹配市级黑名单（第二层, 与省级开关独立, 省命中后不执行）
/// - 地理围栏: 围栏功能全局开关 + 全局策略(strict/balanced/loose), 开启后各商户 opt-in 才生效（第三层）
@Data
@Accessors(chain = true)
public class PlatformPaySecurityConfig {

    /// 风控总开关（关闭后所有风控检查跳过）
    private Boolean riskEnabled = Boolean.TRUE;

    /// 黑名单拦截开关（IP / 用户标识, 默认开启保持历史行为）
    private Boolean blacklistEnabled = Boolean.TRUE;

    /// 命中黑名单后是否阻断下单（false=仅记录不拦截）
    private Boolean riskBlockBeforePay = Boolean.TRUE;

    /// 支付成功后是否补录命中（用于事后分析）
    private Boolean riskCheckAfterPay = Boolean.TRUE;

    /// 用户标识拦截级别（默认增强拦截, 保持历史行为）
    /// @see PayRiskOpenIdLevelEnum
    private String riskOpenIdLevel = PayRiskOpenIdLevelEnum.ENHANCED.getCode();

    /// 海外 IP 拦截（默认关闭, 拦截境外 IP 支付请求）
    private Boolean blockOverseasIp = Boolean.FALSE;

    /// 省级地区拦截（默认关闭, 开启后根据 IP 归属省份匹配省级黑名单）
    private Boolean provinceBlacklistEnabled = Boolean.FALSE;

    /// 市级地区拦截（默认关闭, 开启后根据 IP 归属城市匹配市级黑名单; 与省级开关独立, 省命中后不执行）
    private Boolean cityBlacklistEnabled = Boolean.FALSE;

    /// 地理围栏全局开关（默认关闭, 开启后各商户 mch_risk_config.geoFenceEnabled opt-in 才生效）
    private Boolean geoFenceEnabled = Boolean.FALSE;

    /// 地理围栏全局策略（strict 严格 / balanced 平衡 / loose 宽松, 默认 balanced）
    ///
    /// 平台统一策略, 全商户共用; 非法值由 Service 兜底回退 balanced。
    private String geoFenceStrategy = "balanced";
}
