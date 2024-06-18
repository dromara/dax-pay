package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 交易处理中, 请勿重复操作
 * @author xxm
 * @since 2024/6/17
 */
public class TradeProcessingException extends PayFailureException{

    public TradeProcessingException(String message) {
        super(DaxPayCommonErrorCode.TRADE_PROCESSING,message);
    }

    public TradeProcessingException() {
        super(DaxPayCommonErrorCode.TRADE_PROCESSING,"交易处理中，请勿重复操作");
    }
}
