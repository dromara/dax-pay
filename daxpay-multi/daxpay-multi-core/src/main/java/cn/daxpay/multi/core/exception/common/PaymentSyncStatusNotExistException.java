package cn.daxpay.multi.core.exception.common;

import cn.daxpay.multi.core.code.DaxPayCommonErrorCode;

/**
 *
 * @author xxm
 * @since 2024/6/17
 */
public class PaymentSyncStatusNotExistException extends PayFailureException{

    public PaymentSyncStatusNotExistException(String message) {
        super(DaxPayCommonErrorCode.PAYMENT_SYNC_STATUS_NOT_EXIST,message);
    }

    public PaymentSyncStatusNotExistException() {
        super(DaxPayCommonErrorCode.PAYMENT_SYNC_STATUS_NOT_EXIST,"不存在的支付通道");
    }
}
