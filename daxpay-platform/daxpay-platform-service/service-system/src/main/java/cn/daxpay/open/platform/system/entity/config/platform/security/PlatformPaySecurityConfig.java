package cn.daxpay.open.platform.system.entity.config.platform.security;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付安全配置
///
/// 控制支付交易链路的风控开关。风控检查器实现见
/// [cn.daxpay.open.platform.plugin.risk.strategy.DefaultPayRiskChecker]，
/// 契约见 [cn.daxpay.open.payment.strategy.risk.PayRiskChecker]。
///
/// - 风控总开关: 关闭后支付主链路跳过所有风控检查
/// - 命中阻断: 支付前命中黑名单是否拒绝下单（关闭则仅记录不拦截）
/// - 事后补录: 支付成功后是否补充记录命中（用于事后分析，不阻断资金态）
@Data
@Accessors(chain = true)
public class PlatformPaySecurityConfig {

    /// 风控总开关（关闭后所有风控检查跳过）
    private Boolean riskEnabled = Boolean.TRUE;

    /// 命中黑名单后是否阻断下单（false=仅记录不拦截）
    private Boolean riskBlockBeforePay = Boolean.TRUE;

    /// 支付成功后是否补录命中（用于事后分析）
    private Boolean riskCheckAfterPay = Boolean.TRUE;
}
