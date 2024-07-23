package cn.daxpay.multi.sdk.result.trade.refund;

import cn.daxpay.multi.sdk.code.RefundSyncStatusEnum;
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
