package org.dromara.daxpay.core.context;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 支付上下文
 * @author xxm
 * @since 2023/12/22
 */
@Getter
@Accessors(chain = true)
public class PaymentContext {

    /**
     * 统一支付相关接口调用时，会进行初始化
     * 接收到回调时，会进行初始化
     * 接收到消息通知时是, 会进行初始化
     */
    private final MchAppLocal mchAppInfo = new MchAppLocal();

    /** 请求终端信息 */
    private final PaymentClientLocal clientInfo = new PaymentClientLocal();

    /** 回调相关信息 */
    private final CallbackLocal callbackInfo = new CallbackLocal();

}
