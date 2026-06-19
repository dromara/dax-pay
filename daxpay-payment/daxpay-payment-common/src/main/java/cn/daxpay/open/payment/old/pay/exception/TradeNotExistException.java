package cn.daxpay.open.payment.old.pay.exception;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.PayFailureException;

/// # 交易不存在
///
public class TradeNotExistException extends PayFailureException {

    /// 使用默认错误码与 i18n messageKey（勿传已翻译中文）
    public TradeNotExistException(String messageKey) {
        super(DaxPayErrorCode.TRADE_NOT_EXIST, messageKey);
    }

    /// 使用默认错误码、messageKey 与 MessageFormat 占位参数
    public TradeNotExistException(String messageKey, Object... args) {
        super(DaxPayErrorCode.TRADE_NOT_EXIST, messageKey, args);
    }

    public TradeNotExistException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }

    public TradeNotExistException() {
        // 交易不存在
        super(DaxPayErrorCode.TRADE_NOT_EXIST,"交易不存在");
        initMessageKey("pay.error.notExists");
    }
}
