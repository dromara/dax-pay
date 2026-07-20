package cn.daxpay.open.payment.strategy.risk;

/// # 支付风控检查器（可短路 SPI）
///
/// 实现放在可选插件（如 `daxpay-plugin-risk`）。无 Bean 时支付主链路视为放行。
/// 支付前命中应抛业务异常以拒绝下单；支付后默认只记命中，不阻断已成功资金态。
///
/// **注意**：与 [cn.daxpay.open.payment.strategy.plugin.AbsPayPluginStrategy] 不同——
/// 后者为协议侧事后广播且吞异常；本接口异常须向上抛出以拒绝交易。
public interface PayRiskChecker {

    /// 支付前检查。命中应抛业务异常（如 BizInfoException）以拒绝下单。
    void checkBeforePay(PayRiskCheckContext ctx);

    /// 支付成功后检查（主扫等事后 buyerId 补洞）。默认只记命中，不抛错。
    default void checkAfterPay(PayRiskCheckContext ctx) {
    }

    /// 是否存在 openId 类型黑名单（供网关层决定是否触发强制 OAuth 取 openId）
    default boolean hasOpenIdBlacklist() {
        return false;
    }
}
