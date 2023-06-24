package cn.bootx.platform.daxpay.code.paymodel;

/**
 * 支付宝支付参数
 *
 * @author xxm
 * @date 2021/2/27
 */
public interface AliPayCode {

    // 认证类型
    /** 公钥 */
    String AUTH_TYPE_KEY = "key";

    /** 证书 */
    String AUTH_TYPE_CART = "cart";

    // 渠道枚举
    /** 目前PC支付必填 */
    String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";

    /** WAP支付必填 手机网站支付产品 */
    String QUICK_WAP_PAY = "QUICK_WAP_WAY";

    /** APP支付必填 APP支付产品 */
    String QUICK_MSECURITY_PAY = "QUICK_MSECURITY_PAY";

    /** 付款码支付 */
    String BAR_CODE = "bar_code";

    // 响应字段
    /** 支付状态 */
    String TRADE_STATUS = "trade_status";

    /** 公用回传参数 */
    String PASS_BACK_PARAMS = "passback_params";

    /** 对交易或商品的描述(在没有公用回传参数的时候, 这个作为公用回传参数) */
    String BODY = "body";

    /** 外部订单号-paymentId */
    String OUT_TRADE_NO = "out_trade_no";

    /** 支付宝流水号 */
    String TRADE_NO = "trade_no";

    /** appId */
    String APP_ID = "app_id";

    // 交易状态说明
    /** 交易创建，等待买家付款 */
    String PAYMENT_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /** 未付款交易超时关闭，或支付完成后全额退款 */
    String PAYMENT_TRADE_CLOSED = "TRADE_CLOSED";

    /** 交易支付成功 */
    String PAYMENT_TRADE_SUCCESS = "TRADE_SUCCESS";

    /** 交易结束，不可退款 */
    String PAYMENT_TRADE_FINISHED = "TRADE_FINISHED";

    // 通知触发条件
    /** 交易完成 */
    String NOTIFY_TRADE_FINISHED = "TRADE_FINISHED";

    /** 支付成功 */
    String NOTIFY_TRADE_SUCCESS = "TRADE_SUCCESS";

    /** 交易创建,不触发通知 */
    String NOTIFY_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /** 交易关闭 */
    String NOTIFY_TRADE_CLOSED = "TRADE_CLOSED";

    // 错误提示
    /** 交易不存在 */
    String ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";

    // 网关返回码
    String SUCCESS = "10000";

    // 网关返回码 支付进行中 order success pay inprocess
    String INPROCESS = "10003";

}
