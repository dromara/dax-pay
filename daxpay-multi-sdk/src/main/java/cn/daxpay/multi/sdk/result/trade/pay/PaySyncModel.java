package cn.daxpay.multi.sdk.result.trade.pay;

import cn.daxpay.multi.sdk.code.PaySyncStatusEnum;
import lombok.Data;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class PaySyncModel{

    /**
     * 同步结果
     * @see PaySyncStatusEnum
     */
    private String status;

}
