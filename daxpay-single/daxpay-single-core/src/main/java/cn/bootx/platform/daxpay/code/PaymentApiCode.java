package cn.bootx.platform.daxpay.code;

/**
 * 支付接口编码清单
 * @author xxm
 * @since 2024/2/25
 */
public interface PaymentApiCode {

    String PAY = "pay";

    String SIMPLE_PAY = "simplePay";

    String REFUND = "refund";

    String SIMPLE_REFUND = "simpleRefund";

    String CLOSE = "close";

    String SYNC_PAY = "syncPay";

    String SYNC_REFUND = "syncRefund";

    String QUERY_PAY_ORDER = "queryPayOrder";

    String QUERY_REFUND_ORDER = "queryRefundOrder";

    String GET_WX_AUTH_URL = "getWxAuthUrl";

    String GET_WX_ACCESS_TOKEN = "getWxAccessToken";



}
