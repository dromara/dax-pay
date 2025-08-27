package org.dromara.daxpay.sdk.result.trade.transfer;

import org.dromara.daxpay.sdk.code.TransferStatusEnum;
import lombok.Data;

/**
 * 转账结果
 * @author xxm
 * @since 2024/6/19
 */
@Data
public class TransferResult {

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    private String status;


    /**
     * 提示信息
     */
    private String errorMsg;
}
