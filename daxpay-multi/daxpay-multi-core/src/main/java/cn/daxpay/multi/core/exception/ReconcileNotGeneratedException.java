package cn.daxpay.multi.core.exception;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 * 对账交易账单未生成
 * @author xxm
 * @since 2024/6/17
 */
public class ReconcileNotGeneratedException extends PayFailureException{

    public ReconcileNotGeneratedException(String message) {
        super(DaxPayCommonErrorCode.RECONCILE_NOT_GENERATED,message);
    }

    public ReconcileNotGeneratedException() {
        super(DaxPayCommonErrorCode.RECONCILE_NOT_GENERATED,"对账交易账单未生成");
    }
}
