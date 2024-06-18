package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 对账失败
 * @author xxm
 * @since 2024/6/17
 */
public class ReconciliationFailException extends PayFailureException{

    public ReconciliationFailException(String message) {
        super(DaxPayCommonErrorCode.RECONCILE_FAIL,message);
    }

    public ReconciliationFailException() {
        super(DaxPayCommonErrorCode.RECONCILE_FAIL,"对账失败");
    }
}
