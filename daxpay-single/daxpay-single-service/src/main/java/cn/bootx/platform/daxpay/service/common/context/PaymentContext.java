package cn.bootx.platform.daxpay.service.common.context;

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
    private final PlatformLocal platformInfo = new PlatformLocal();

    /** 异步支付相关信息 */
    private final AsyncPayLocal asyncPayInfo = new AsyncPayLocal();

    /** 退款相关信息 */
    private final RefundLocal refundInfo = new RefundLocal();

    /** 消息通知(主动发起)相关信息 */
    private final NoticeLocal noticeInfo = new NoticeLocal();

    /** 回调相关信息 */
    private final CallbackLocal callbackInfo = new CallbackLocal();

    /** 请求相关信息 */
    private final RequestLocal requestInfo = new RequestLocal();

    /** 支付同步相关信息 */
    private final PaySyncLocal paySyncInfo = new PaySyncLocal();

    /** 支付修复相关 */
    private final RepairLocal repairInfo = new RepairLocal();

    /** 对账相关信息 */
    private final ReconcileLocal reconcileInfo = new ReconcileLocal();

}
