package cn.daxpay.open.payment.common.context;

/// # 支付上下文 ThreadLocal 持有者
///
/// 纯 ThreadLocal 实现，替代原 Spring 自定义 Scope（PaymentScope）。
/// 由 Filter/Manager 在请求入口 bind，出口 unbind；MyBatis 拦截器通过 PaymentContext 门面间接访问。
public final class PaymentContextHolder {

    private PaymentContextHolder() {
    }

    private static final ThreadLocal<ContextData> HOLDER = new ThreadLocal<>();

    /// 绑定新上下文到当前线程
    public static void bind() {
        if (HOLDER.get() != null) {
            throw new IllegalStateException("PaymentContext already active on this thread");
        }
        HOLDER.set(new ContextData());
    }

    /// 解除当前线程的上下文绑定
    public static void unbind() {
        HOLDER.remove();
    }

    /// 当前线程是否已绑定上下文
    public static boolean isBound() {
        return HOLDER.get() != null;
    }

    /// 获取当前线程的交易信息
    public static TradeInfo getTradeInfo() {
        return current().tradeInfo;
    }

    /// 获取当前线程的回调信息
    public static CallbackInfo getCallbackInfo() {
        return current().callbackInfo;
    }

    /// 获取当前上下文数据（未绑定则抛异常）
    private static ContextData current() {
        ContextData data = HOLDER.get();
        if (data == null) {
            throw new IllegalStateException("PaymentContext not active on this thread");
        }
        return data;
    }

    /// 每次请求的上下文数据
    private static class ContextData {
        final TradeInfo tradeInfo = new TradeInfo();
        final CallbackInfo callbackInfo = new CallbackInfo();
    }
}
