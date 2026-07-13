package cn.daxpay.open.payment.common.context;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

/// # 支付运行时上下文(线程级身份)
///
/// 三合一:ThreadLocal 仓库 + 读门面 + 生命周期编排。
/// 仅持有 [TradeActor](商户身份)。回调数据见 `common.callback.CallbackData`(函数传参,不进线程上下文)。
///
/// ## 生命周期
/// - HTTP 请求:由 `PaymentContextFilter` 在入口 open、出口 close
/// - 非 HTTP(MQ/定时/内部调用):用 `runAs` 自管理作用域
///
/// ## 读 API 分级
/// - `getActor` / `getMchNo`:业务代码用,未开启抛异常
/// - `currentActor`:MyBatis 拦截器等基础设施用,未开启返回 empty；**TenantLine 在 empty/空 mchNo 时 fail-closed**
///
/// ## 与隔离内核关系
/// 本类只存身份；`mch_no` SQL 隔离见 `common.tenant.MchNoTenantLineHandler`。
/// 身份装载矩阵见 `_doc/design/mch-no-tenant-isolation.md`。
@Component
public final class PaymentContext {

    private static final ThreadLocal<TradeActor> HOLDER = new ThreadLocal<>();

    /// 开启作用域(创建空身份),已开启则抛异常避免重复绑定
    public void open() {
        if (HOLDER.get() != null) {
            throw new IllegalStateException("PaymentContext already active on this thread");
        }
        HOLDER.set(new TradeActor());
    }

    /// 关闭作用域,释放线程绑定
    public void close() {
        HOLDER.remove();
    }

    /// 当前线程是否已开启作用域
    public boolean isOpen() {
        return HOLDER.get() != null;
    }

    /// 在作用域内执行(自动管理生命周期,同步)。
    /// 若当前线程已开启(HTTP 请求已被 Filter 开启),则复用之,仅执行 action;否则自行 open/close。
    public <T> T runAs(Supplier<T> action) {
        if (isOpen()) {
            return action.get();
        }
        open();
        try {
            return action.get();
        } finally {
            close();
        }
    }

    /// 在作用域内执行(无返回值),语义同 [#runAs]
    public void runAs(Runnable action) {
        if (isOpen()) {
            action.run();
            return;
        }
        open();
        try {
            action.run();
        } finally {
            close();
        }
    }

    /// 获取当前身份(未开启则抛异常),业务代码使用
    public TradeActor getActor() {
        return requireBound();
    }

    /// 获取当前身份(null-safe,未开启返回 empty),供 MyBatis 拦截器等基础设施降级
    public Optional<TradeActor> currentActor() {
        return Optional.ofNullable(HOLDER.get());
    }

    /// 当前商户号(未开启返回 null)
    public String getMchNo() {
        return currentActor().map(TradeActor::getMchNo).orElse(null);
    }

    /// 设置当前商户号(供 `MerchantContextLoader` 初始化身份使用)
    public void setMchNo(String mchNo) {
        requireBound().setMchNo(mchNo);
    }

    private TradeActor requireBound() {
        TradeActor actor = HOLDER.get();
        if (actor == null) {
            // 若为 MQ/定时任务等非 HTTP 场景,请通过 runAs 开启作用域
            throw new IllegalStateException("PaymentContext not active on this thread");
        }
        return actor;
    }
}
