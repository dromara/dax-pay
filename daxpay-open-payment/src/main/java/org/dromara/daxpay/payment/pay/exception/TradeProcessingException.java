package org.dromara.daxpay.payment.pay.exception;

import org.dromara.daxpay.payment.common.code.DaxPayErrorCode;
import org.dromara.daxpay.payment.common.exception.PayFailureException;

/**
 * 交易处理中, 请勿重复操作
 * @author xxm
 * @since 2024/6/17
 */
public class TradeProcessingException extends PayFailureException {

    public TradeProcessingException(String message) {
        super(DaxPayErrorCode.TRADE_PROCESSING,message);
    }

    public TradeProcessingException() {
        super(DaxPayErrorCode.TRADE_PROCESSING,"交易处理中，请勿重复操作");
    }
}
