package cn.bootx.platform.daxpay.service.common.context;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付上下文
 * @author xxm
 * @since 2023/12/22
 */
@Getter
@Accessors(chain = true)
public class PaymentContext {

    /** 支付接口信息 */
    private final ApiInfoLocal apiInfo = new ApiInfoLocal();;

    /** 平台全局配置 */
    private final PlatformLocal platform = new PlatformLocal();

    /** 异步支付相关信息, 不只局限在支付流程，同步、退款、回调中都会用到 */
    private final AsyncPayLocal asyncPayInfo = new AsyncPayLocal();

    /** 异步退款相关信息 */
    private final AsyncRefundLocal asyncRefundInfo = new AsyncRefundLocal();

    /** 消息通知相关信息 */
    private final NoticeLocal noticeInfo = new NoticeLocal();

    /** 回调参数内容 */
    private final Map<String, String> callbackParam = new HashMap<>();

    /** 支付请求相关信息 */
    private final RequestLocal request = new RequestLocal();

}
