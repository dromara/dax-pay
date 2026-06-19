package cn.daxpay.open.payment.common.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/// # 统一支付上下文
///
/// 合并原 PaymentContextLocal/MchContextLocal 两个ThreadLocal的信息
/// 使用自定义作用域 "payment", 底层基于ThreadLocal, 兼容HTTP/定时任务/MQ场景
@Component
@Scope(value = PaymentScope.SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
@Accessors(chain = true)
public class PaymentContext {

    // ===== 子对象 =====

    /// 交易信息(商户/应用/合作方)
    private final TradeInfo tradeInfo = new TradeInfo();

    /// 回调相关信息
    private final CallbackInfo callbackInfo = new CallbackInfo();
}
