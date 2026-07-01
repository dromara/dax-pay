package cn.daxpay.open.payment.common.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/// # 支付作用域管理器
///
/// 为HTTP请求、定时任务、MQ消费者等场景提供统一的上下文生命周期管理
@Component
@RequiredArgsConstructor
public class PaymentContextManager {

    private final PaymentAssistService paymentAssistService;

    /// 开启作用域(空上下文)
    public void start() {
        PaymentContextHolder.bind();
    }

    /// 开启作用域并初始化商户信息
    /// 用于定时任务/MQ消费者等非HTTP场景
    public void startWithMch(String mchNo, String appId) {
        PaymentContextHolder.bind();
        paymentAssistService.initMchAndApp(mchNo, appId);
    }

    /// 结束作用域
    public void end() {
        PaymentContextHolder.unbind();
    }

    /// 在作用域内执行(自动管理生命周期, 同步执行)
    /// Lambda是同步执行的, 不是异步, 异常正常向上传播
    /// 若当前线程已绑定上下文(如HTTP请求已被PaymentContextFilter绑定), 则复用之, 仅补初始化商户, 避免重复bind抛异常
    public <T> T executeWithScope(String mchNo, String appId, Supplier<T> action) {
        if (PaymentContextHolder.isBound()) {
            // 复用已有作用域, 仅初始化商户信息
            paymentAssistService.initMchAndApp(mchNo, appId);
            return action.get();
        }
        startWithMch(mchNo, appId);
        try {
            return action.get();
        } finally {
            end();
        }
    }

    /// 在作用域内执行(自动管理生命周期, 同步执行)
    /// Lambda是同步执行的, 不是异步, 异常正常向上传播
    /// 若当前线程已绑定上下文(如HTTP请求已被PaymentContextFilter绑定), 则复用之, 仅补初始化商户, 避免重复抛异常
    public void executeWithScope(String mchNo, String appId, Runnable action) {
        if (PaymentContextHolder.isBound()) {
            // 复用已有作用域, 仅初始化商户信息
            paymentAssistService.initMchAndApp(mchNo, appId);
            action.run();
            return;
        }
        startWithMch(mchNo, appId);
        try {
            action.run();
        } finally {
            end();
        }
    }
}
