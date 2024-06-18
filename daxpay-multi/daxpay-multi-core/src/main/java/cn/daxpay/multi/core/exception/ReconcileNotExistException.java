package cn.daxpay.multi.core.exception;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 * 对账交易账单不存在
 * @author xxm
 * @since 2024/6/17
 */
public class ReconcileNotExistException extends PayFailureException{

    public ReconcileNotExistException(String message) {
        super(DaxPayCommonErrorCode.RECONCILE_NOT_EXIST,message);
    }

    public ReconcileNotExistException() {
        super(DaxPayCommonErrorCode.RECONCILE_NOT_EXIST,"对账交易账单不存在");
    }
}
