package cn.daxpay.open.channel.ums.code;

/// # 银联商务支付常量
///
/// 银联商务回调为 form 参数, 验签后通过 errCode/success 响应。
/// 统一状态码 SUCCESS/PROGRESS/CLOSED 由子应用映射, 本类定义平台侧使用的常量。
public interface UmsCode {

    /// 银联商务请求成功标识(响应体 errCode 字段)
    String ERR_CODE_SUCCESS = "SUCCESS";

    /// 回调成功应答(银联商务要求返回 success 或 OK)
    String NOTIFY_SUCCESS = "success";

    /// 回调失败应答
    String NOTIFY_FAIL = "fail";

    /// 统一交易状态-支付成功
    String TRADE_STATUS_SUCCESS = "SUCCESS";

    /// 统一交易状态-进行中
    String TRADE_STATUS_PROGRESS = "PROGRESS";

    /// 统一交易状态-已关闭
    String TRADE_STATUS_CLOSED = "CLOSED";

    /// 退款状态-成功
    String REFUND_STATUS_SUCCESS = "SUCCESS";

    /// 退款状态-处理中
    String REFUND_STATUS_PROCESSING = "PROCESSING";

    /// 退款状态-失败
    String REFUND_STATUS_FAIL = "FAIL";

    /// 货币种类
    String CURRENCY_CNY = "CNY";
}
