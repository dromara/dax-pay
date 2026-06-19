package cn.daxpay.open.payment.common.context;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.concurrent.ConcurrentHashMap;

/// # 支付自定义作用域, 底层基于ThreadLocal实现
///
/// 兼容HTTP请求、定时任务、MQ消费者等场景
public class PaymentScope implements Scope, DisposableBean {

    public static final String SCOPE_NAME = "payment";

    private final ThreadLocal<PaymentContext> threadLocal = new ThreadLocal<>();

    private ObjectFactory<?> objectFactory;

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (this.objectFactory == null) {
            this.objectFactory = objectFactory;
        }
        PaymentContext context = threadLocal.get();
        if (context == null) {
            context = (PaymentContext) objectFactory.getObject();
            threadLocal.set(context);
        }
        return context;
    }

    @Override
    public Object remove(String name) {
        PaymentContext context = threadLocal.get();
        threadLocal.remove();
        return context;
    }

    /// 开启作用域, 创建PaymentContext并绑定到当前线程
    public void start() {
        if (threadLocal.get() != null) {
            throw new IllegalStateException("PaymentScope already active on this thread");
        }
        if (objectFactory != null) {
            get(SCOPE_NAME, objectFactory);
        } else {
            threadLocal.set(new PaymentContext());
        }
    }

    /// 结束作用域, 清除当前线程的PaymentContext
    public void end() {
        threadLocal.remove();
    }

    /// 获取当前上下文
    /// 用于非Spring管理的类或需要静态访问的场景
    public PaymentContext current() {
        PaymentContext context = threadLocal.get();
        if (context == null) {
            throw new IllegalStateException("PaymentScope not active on this thread");
        }
        return context;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return Thread.currentThread().getName();
    }

    @Override
    public void destroy() {
        threadLocal.remove();
    }
}

