package cn.daxpay.multi.service.code;

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

        /** 支付任务超时 */
        String MERCHANT_PAY_TIMEOUT = "PayTimeout";

    }
}
