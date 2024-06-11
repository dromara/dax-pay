package cn.daxpay.single.service.common.context;

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

    /** 平台全局配置 */
    private final PlatformLocal platformInfo = new PlatformLocal();

    /** 请求终端信息 */
    private final ClientLocal clientInfo = new ClientLocal();

    /** 支付相关信息 */
    private final PayLocal payInfo = new PayLocal();

    /** 退款相关信息 */
    private final RefundLocal refundInfo = new RefundLocal();

    /** 回调相关信息 */
    private final CallbackLocal callbackInfo = new CallbackLocal();

    /** 修复相关信息 */
    private final RepairLocal repairInfo = new RepairLocal();

    /** 对账相关信息 */
    private final ReconcileLocal reconcileInfo = new ReconcileLocal();

    /** 分账相关信息 */
    private final AllocationLocal allocationInfo = new AllocationLocal();

}
