package cn.bootx.platform.daxpay.common.context;

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

    /** 支付接口信息 */
    private final ApiInfoLocal apiInfo = new ApiInfoLocal();;

    /** 平台全局配置 */
    private final PlatformLocal platform = new PlatformLocal();

    /** 异步支付相关信息 */
    private final AsyncPayLocal asyncPayInfo = new AsyncPayLocal();

    /** 退款相关信息 */
    private final AsyncRefundLocal refundInfo = new AsyncRefundLocal();

    /** 消息通知相关信息 */
    private final NoticeLocal noticeInfo = new NoticeLocal();

    /** 支付请求相关信息 */
    private final RequestLocal request = new RequestLocal();

}
