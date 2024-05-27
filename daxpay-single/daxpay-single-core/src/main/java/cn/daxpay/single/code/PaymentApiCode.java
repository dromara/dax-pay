package cn.daxpay.single.code;

/**
 * 支付接口编码清单
 * @author xxm
 * @since 2024/2/25
 */
public interface PaymentApiCode {
    /** 支付 */
    String PAY = "pay";
    /** 退款 */
    String REFUND = "refund";
    /** 关闭订单 */
    String CLOSE = "close";
    /** 分账 */
    String ALLOCATION = "allocation";
    /** 转账 */
    String TRANSFER = "transfer";
    /** 分账完结 */
    String ALLOCATION_FINISH = "allocationFinish";
    /** 支付同步 */
    String SYNC_PAY = "syncPay";
    /** 退款同步 */
    String SYNC_REFUND = "syncRefund";
    /** 分账同步 */
    String SYNC_ALLOCATION = "syncAllocation";
    /** 转账同步 */
    String SYNC_TRANSFER = "syncTransfer";
    /** 查询支付订单 */
    String QUERY_PAY_ORDER = "queryPayOrder";
    /** 查询退款订单 */
    String QUERY_REFUND_ORDER = "queryRefundOrder";
    /** 查询分账订单 */
    String QUERY_ALLOCATION_ORDER = "queryAllocationOrder";
    /** 查询转账订单 */
    String QUERY_TRANSFER_ORDER = "queryTransferOrder";
    /** 查询分账接收方 */
    String QUERY_ALLOCATION_RECEIVER = "queryAllocationReceiver";
    /** 获取微信授权链接 */
    String GET_WX_AUTH_URL = "getWxAuthUrl";
    /** 获取微信AccessToken */
    String GET_WX_ACCESS_TOKEN = "getWxAccessToken";
    /** 添加分账方接口 */
    String ALLOCATION_RECEIVER_ADD = "allocationReceiverAdd";
    /** 删除分账方 */
    String ALLOCATION_RECEIVER_REMOVE = "allocationReceiverRemove";

}
