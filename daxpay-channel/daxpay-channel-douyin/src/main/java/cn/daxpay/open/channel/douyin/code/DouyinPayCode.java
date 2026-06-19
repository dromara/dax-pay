package cn.daxpay.open.channel.douyin.code;

/// # 抖音支付常量
///
/// @author xxm
/// @since 2026/6/4
public interface DouyinPayCode {

    /** AES 加密 */
    String ENCRYPT_TYPE_AES = "AES";

    /** 扫码支付 */
    String TRADE_TYPE_NATIVE = "NATIVE";

    /** JSAPI支付 */
    String TRADE_TYPE_JSAPI = "JSAPI";

    /** H5支付 */
    String TRADE_TYPE_MWEB = "MWEB";

    /** APP支付 */
    String TRADE_TYPE_APP = "APP";

    /** 支付成功 */
    String TRADE_STATE_SUCCESS = "SUCCESS";

    /** 转入退款 */
    String TRADE_STATE_REFUND = "REFUND";

    /** 未支付 */
    String TRADE_STATE_NOTPAY = "NOTPAY";

    /** 用户支付中 */
    String TRADE_STATE_USERPAYING = "USERPAYING";

    /** 已关闭 */
    String TRADE_STATE_CLOSED = "CLOSED";

    /** 支付失败 */
    String TRADE_STATE_PAYERROR = "PAYERROR";

    /** 回调签名序列号 */
    String HEADER_SERIAL = "Douyinpay-Serial";

    /** 回调签名 */
    String HEADER_SIGNATURE = "Douyinpay-Signature";

    /** 回调时间戳 */
    String HEADER_TIMESTAMP = "Douyinpay-Timestamp";

    /** 回调随机串 */
    String HEADER_NONCE = "Douyinpay-Nonce";

    /** 回调成功应答 */
    String NOTIFY_SUCCESS = "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";

    /** 回调失败应答 */
    String NOTIFY_FAIL = "{\"code\":\"FAIL\",\"message\":\"失败\"}";

    /** 货币种类 */
    String CURRENCY_CNY = "CNY";

    /** 退款成功 */
    String REFUND_STATUS_SUCCESS = "SUCCESS";

    /** 退款关闭 */
    String REFUND_STATUS_CLOSED = "CLOSED";

    /** 退款处理中 */
    String REFUND_STATUS_PROCESSING = "PROCESSING";

    /** 退款异常 */
    String REFUND_STATUS_ABNORMAL = "ABNORMAL";

    /** 退款成功通知 */
    String EVENT_TYPE_REFUND_SUCCESS = "REFUND.SUCCESS";
}
