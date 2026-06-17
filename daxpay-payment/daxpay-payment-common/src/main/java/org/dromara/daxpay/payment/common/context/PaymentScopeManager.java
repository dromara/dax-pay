package org.dromara.daxpay.payment.common.context;

import org.dromara.daxpay.payment.old.pay.service.assist.PaymentAssistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/// # 支付作用域管理器
///
/// 为HTTP请求、定时任务、MQ消费者等场景提供统一的上下文生命周期管理
@Component
@RequiredArgsConstructor
public class PaymentScopeManager {

    private final PaymentScope paymentScope;

    private final PaymentAssistService paymentAssistService;

    /// 开启作用域(空上下文)
    public void start() {
        paymentScope.start();
    }

    /// 开启作用域并初始化商户信息
    /// 用于定时任务/MQ消费者等非HTTP场景
    public void startWithMch(String mchNo, String appId) {
        paymentScope.start();
        paymentAssistService.initMchAndApp(mchNo, appId);
    }

    /// 结束作用域
    public void end() {
        paymentScope.end();
    }

    /// 在作用域内执行(自动管理生命周期, 同步执行)
    /// Lambda是同步执行的, 不是异步, 异常正常向上传播
    public <T> T executeWithScope(String mchNo, String appId, Supplier<T> action) {
        startWithMch(mchNo, appId);
        try {
            return action.get();
        } finally {
            end();
        }
    }

    /// 在作用域内执行(自动管理生命周期, 同步执行)
    /// Lambda是同步执行的, 不是异步, 异常正常向上传播
    public void executeWithScope(String mchNo, String appId, Runnable action) {
        startWithMch(mchNo, appId);
        try {
            action.run();
        } finally {
            end();
        }
    }
}

