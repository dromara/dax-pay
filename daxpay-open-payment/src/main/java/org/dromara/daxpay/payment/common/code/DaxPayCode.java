package org.dromara.daxpay.payment.common.code;

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

        /** 商户回调通知 */
        String MERCHANT_CALLBACK_SENDER = "CallbackSender";

        /** 支付任务超时 */
        String ORDER_PAY_TIMEOUT = "PayTimeout";

        /** 退款任务同步 默认两分钟后查询 */
        String ORDER_REFUND_SYNC = "RefundSync";

        /** 转账任务同步 默认两分钟后查询 */
        String ORDER_TRANSFER_SYNC = "TransferSync";

    }

    /**
     * 终端类型
     */
    interface Client {
        /** 运营端 */
        String ADMIN = "dax-pay-admin";
        /** 商户端 */
        String MERCHANT = "dax-pay-merchant";

    }

    /**
     * 相关角色
     */
    interface Role {
        /** 商户管理员 */
        String MERCHANT_ADMIN = "merchant_admin";
        /** 运营商管理员 */
        String ISV_ADMIN = "daxpay_admin";
    }
}
