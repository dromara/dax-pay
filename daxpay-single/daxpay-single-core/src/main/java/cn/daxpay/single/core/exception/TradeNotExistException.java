package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 交易不存在
 * @author xxm
 * @since 2024/6/17
 */
public class TradeNotExistException extends PayFailureException{

    public TradeNotExistException(String message) {
        super(DaxPayCommonErrorCode.TRADE_NOT_EXIST,message);
    }

    public TradeNotExistException() {
        super(DaxPayCommonErrorCode.TRADE_NOT_EXIST,"交易不存在");
    }
}
