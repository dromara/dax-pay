package org.dromara.daxpay.service.common.local;

import org.dromara.daxpay.service.common.context.PaymentContext;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * 支付上下文线程变量
 * @author xxm
 * @since 2023/12/22
 */
@UtilityClass
public final class PaymentContextLocal {

    private static final ThreadLocal<PaymentContext> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取
     */
    public PaymentContext get(){
        PaymentContext paymentContext = THREAD_LOCAL.get();
        if (Objects.isNull(paymentContext)){
            paymentContext = new PaymentContext();
            THREAD_LOCAL.set(paymentContext);
        }
        return paymentContext;
    }

    /**
     * 清除
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
