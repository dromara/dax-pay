package cn.bootx.platform.daxpay.code.pay;

/**
 * 支付状态
 *
 * @author xxm
 * @date 2021/3/1
 */
public interface PayStatusCode {

    // 支付状态
    /** 未知状态 */
    String TRADE_UNKNOWN = "trade_unknown";

    /** 支付中 */
    String TRADE_PROGRESS = "trade_progress";

    /** 成功 */
    String TRADE_SUCCESS = "trade_success";

    /** 失败 */
    String TRADE_FAIL = "trade_fail";

    /** 支付取消(超时/手动取消/订单已经关闭,撤销支付单) */
    String TRADE_CANCEL = "trade_cancel";

    /** 退款中 */
    String TRADE_REFUNDING = "trade_refunding";

    /** 已退款 */
    String TRADE_REFUNDED = "trade_refunded";

    // 回调信息支付状态
    /** 失败 */
    String NOTIFY_TRADE_FAIL = "notify_trade_fail";

    /** 成功 */
    String NOTIFY_TRADE_SUCCESS = "notify_trade_success";

    // 回调处理状态
    /** 失败 */
    String NOTIFY_PROCESS_FAIL = "notify_process_fail";

    /** 成功 */
    String NOTIFY_PROCESS_SUCCESS = "notify_process_success";

    /** 忽略 */
    String NOTIFY_PROCESS_IGNORE = "notify_process_ignore";

    // 退款处理状态
    /** 失败 */
    String REFUND_PROCESS_FAIL = "refund_process_fail";

    /** 成功 */
    String REFUND_PROCESS_SUCCESS = "refund_process_success";

}
