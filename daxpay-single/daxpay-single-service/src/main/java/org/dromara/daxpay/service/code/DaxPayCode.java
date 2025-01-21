package org.dromara.daxpay.service.code;

/**
 * 系统编码
 * @author xxm
 * @since 2024/8/2
 */
public interface DaxPayCode {

    /**
     * 消息通知相关常量
     */
    interface Event {
        /** 商户任务通知 */
        String MERCHANT_NOTIFY_SENDER = "MerchantNotifySender";

        /** 商户回调通知 */
        String MERCHANT_CALLBACK_SENDER = "CallbackSender";

        /** 支付订单超时关闭 */
        String ORDER_PAY_TIMEOUT = "PayTimeout";

        /** 退款任务同步 默认两分钟后查询 */
        String ORDER_REFUND_SYNC = "RefundSync";

        /** 转账任务同步 默认两分钟后查询 */
        String ORDER_TRANSFER_SYNC = "TransferSync";

        /** 自动分账 */
        String ORDER_ALLOC_START = "AllocStart";

        /** 分账同步 */
        String ORDER_ALLOC_SYNC = "AllocSync";

        /** 分账完结 */
        String ORDER_ALLOC_FINISH = "AllocFinish";
    }
}
