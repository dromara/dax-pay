package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;

/// # 支付失败异常
///
/// 支付域通用失败异常，默认未分类错误码为 `DaxPayErrorCode.UNCLASSIFIED_ERROR`。
public class PayFailureException extends BizException {

    /// 指定错误码与 messageKey
    public PayFailureException(int code, String messageKey) {
        super(code, messageKey);
    }

    /// 使用未分类支付错误码与 messageKey
    public PayFailureException(String messageKey) {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, messageKey);
    }

    /// 无参构造，使用 key `pay.error.payFailure`
    public PayFailureException() {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, "pay.error.payFailure");
        initMessageKey("pay.error.payFailure");
    }

    /// 指定错误码、messageKey 与占位符参数
    public PayFailureException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
