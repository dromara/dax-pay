package cn.daxpay.single.core.exception;

import cn.daxpay.single.core.code.DaxPayCommonErrorCode;

/**
 * 对账文件获取失败
 * @author xxm
 * @since 2024/6/17
 */
public class ReconcileGetFailedException extends PayFailureException{

    public ReconcileGetFailedException(String message) {
        super(DaxPayCommonErrorCode.RECONCILE_GET_FAILED,message);
    }

    public ReconcileGetFailedException() {
        super(DaxPayCommonErrorCode.RECONCILE_GET_FAILED,"对账文件获取失败");
    }
}
