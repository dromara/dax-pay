package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 对账失败
 * @author xxm
 * @since 2024/6/17
 */
public class ReconciliationFailedException extends PayFailureException{

    public ReconciliationFailedException(String message) {
        super(DaxPayCommonErrorCode.RECONCILIATION_FAILED,message);
    }

    public ReconciliationFailedException() {
        super(DaxPayCommonErrorCode.RECONCILIATION_FAILED,"对账失败");
    }
}
