package cn.bootx.platform.daxpay.code;

/**
 * 支付网关同步状态
 *
 * @author xxm
 * @since 2021/4/21
 */
public interface PaySyncStatus {

    /** 不需要同步 */
    String NOT_SYNC = "not_sync";

    /** 远程支付成功 */
    String TRADE_SUCCESS = "trade_success";

    /** 交易创建，等待买家付款 */
    String WAIT_BUYER_PAY = "wait_buyer_pay";

    /** 已关闭 */
    String TRADE_CLOSED = "trade_closed";

    /** 已退款 */
    String TRADE_REFUND = "trade_refund";

    /** 查询不到订单 */
    String NOT_FOUND = "not_found";

    /** 查询失败 */
    String FAIL = "fail";

}
