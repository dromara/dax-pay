package cn.daxpay.open.payment.common.context;

import org.springframework.stereotype.Component;

/// # 统一支付上下文（门面）
///
/// 保持 @Component 单例，对 17 个注入点透明。实际数据由 [PaymentContextHolder] 通过 ThreadLocal 管理。
/// MyBatis 拦截器（租户隔离、自动填充）通过此门面获取当前线程的商户/应用信息。
@Component
public class PaymentContext {

    /// 交易信息(商户/应用/合作方)
    public TradeInfo getTradeInfo() {
        return PaymentContextHolder.getTradeInfo();
    }

    /// 回调相关信息
    public CallbackInfo getCallbackInfo() {
        return PaymentContextHolder.getCallbackInfo();
    }
}
