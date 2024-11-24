package org.dromara.daxpay.single.sdk.model.trade.refund;

import org.dromara.daxpay.single.sdk.code.RefundSyncStatusEnum;
import lombok.Data;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class RefundSyncModel{

    /**
     * 同步结果
     * @see RefundSyncStatusEnum
     */
    private String status;

}
