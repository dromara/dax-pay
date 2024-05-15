package cn.daxpay.single.code;

/**
 * 支付接口编码清单
 * @author xxm
 * @since 2024/2/25
 */
public interface PaymentApiCode {

    String PAY = "pay";

    String REFUND = "refund";

    String CLOSE = "close";

    String ALLOCATION = "allocation";

    String ALLOCATION_FINISH = "allocationFinish";

    String SYNC_PAY = "syncPay";

    String SYNC_REFUND = "syncRefund";

    String QUERY_PAY_ORDER = "queryPayOrder";

    String QUERY_REFUND_ORDER = "queryRefundOrder";

    String GET_WX_AUTH_URL = "getWxAuthUrl";

    String GET_WX_ACCESS_TOKEN = "getWxAccessToken";



}
