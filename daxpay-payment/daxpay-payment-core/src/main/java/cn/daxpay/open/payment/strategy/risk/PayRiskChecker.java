package cn.daxpay.open.payment.strategy.risk;

/// # 支付风控检查器（可短路 SPI）
///
/// 实现放在可选插件（如 `daxpay-plugin-risk`）。无 Bean 时支付主链路视为放行。
/// 支付前命中应抛业务异常以拒绝下单；支付后默认只记命中，不阻断已成功资金态。
///
/// **范围**：仅覆盖支付发起（普通 / 网关）的事前拦截与成功后补录。
/// **不覆盖**退款、转账——黑名单语义是拦截付款发起；退款为资金回流、转账为出款，
/// 与拦截付款人下单不是同一产品决策，本期明确不接入，避免误伤合规退款。
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
