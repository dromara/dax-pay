package cn.bootx.platform.daxpay.service.code;

/**
 * 微信参数
 *
 * @author xxm
 * @since 2021/6/21
 */
public interface WeChatPayCode {

    // 版本
    String API_V2 = "apiV2";

    String API_V3 = "apiV3";

    // 请求参数
    /** jsapi发起获取AuthCode时的重定向参数 */
    String JSAPI_REDIRECT_URL = "JsapiRedirectUrl";

    // 返回参数
    /** 二维码链接 */
    String CODE_URL = "code_url";

    /** 支付跳转链接 */
    String MWEB_URL = "mweb_url";

    /** 预支付交易会话ID */
    String PREPAY_ID = "prepay_id";

    /** 返回状态码 */
    String RETURN_CODE = "return_code";

    /** 返回信息 */
    String RETURN_MSG = "return_msg";

    /** 返回错误代码(例如付款码返回的支付中状态就在这里面) */
    String ERR_CODE = "err_code";

    /** 返回错误信息 */
    String ERR_CODE_DES = "err_code_des";

    /** 业务结果(部分结果不在这个参数里, 例如付款码的响应码) */
    String RESULT_CODE = "result_code";

    /** 交易类型 */
    String TRADE_TYPE = "trade_type";

    /** appid */
    String APPID = "appid";

    /** 交易状态 */
    String TRADE_STATE = "trade_state";

    /** 商户订单号 */
    String OUT_TRADE_NO = "out_trade_no";

    /** 微信交易单号 */
    String TRANSACTION_ID = "transaction_id";

    /** 支付完成时间 */
    String TIME_END = "time_end";

    // 交易状态
    /** 支付成功 */
    String TRADE_SUCCESS = "SUCCESS";

    /** 支付失败 */
    String TRADE_FAIL = "FAIL";

    /** 退款 */
    String TRADE_REFUND = "REFUND";

    /** 未支付 */
    String TRADE_NOTPAY = "NOTPAY";

    /** 已关闭 */
    String TRADE_CLOSED = "CLOSED";

    /** 已接收，等待扣款 */
    String TRADE_ACCEPT = "ACCEPT";

    /** 已撤销(刷卡支付) */
    String TRADE_REVOKED = "REVOKED";

    /** 用户支付中(刷卡支付) */
    String TRADE_USERPAYING = "USERPAYING";

    /** 支付失败(刷卡支付) */
    String TRADE_PAYERROR = "PAYERROR";

    /** 退款总金额(各退款单的退款金额累加) */
    String REFUND_FEE = "refund_fee";

    /** 当前返回退款笔数 */
    String REFUND_COUNT = "refund_count";

    /** 订单总退款次数 */
    String TOTAL_REFUND_COUNT = "total_refund_count";

}
