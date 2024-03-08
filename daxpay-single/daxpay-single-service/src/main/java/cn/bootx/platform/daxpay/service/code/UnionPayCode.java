package cn.bootx.platform.daxpay.service.code;

/**
 * 云闪付常量
 * @author xxm
 * @since 2024/3/7
 */
public interface UnionPayCode {


    /** 成功状态 */
    String SUCCESS = "0";

    /** 状态 0表示成功 */
    String STATUS = "status";

    /** 业务结果 0表示成功，非0表示失败 */
    String RESULT_CODE = "result_code";

    /** 网关订单号 */
    String TRANSACTION_ID = "transaction_id";

    /** 第三方订单号 */
    String OUT_TRANSACTION_ID = "out_transaction_id";

    /** 退款ID */
    String REFUND_ID = "refund_id";


    /**
     * 支付完成时间
     * 格式: yyyyMMddHHmmss
     */
    String TIME_END = "time_end";

    /** 支付结果 */
    String PAY_RESULT = "pay_result";

    /** 总金额 */
    String TOTAL_FEE = "total_fee";

    /** 交易状态 */
    String TRADE_STATE = "trade_state";

    /** 支付成功 */
    String TRADE_SUCCESS = "SUCCESS";

    /** 转入退款 */
    String TRADE_REFUND = "REFUND";

    /** 未支付 */
    String TRADE_NOT_PAY = "NOTPAY";

    /** 已关闭 */
    String TRADE_CLOSED = "CLOSED";

    /** 支付失败(其他原因，如银行返回失败) */
    String TRADE_PAY_ERROR = "PAYERROR";

    /** 返回信息 */
    String MESSAGE = "message";

    /** 错误代码描述 */
    String ERR_MSG = "err_msg";


    /** 对账单下载类型编码 */
    String RECONCILE_BILL_TYPE = "00";

}
