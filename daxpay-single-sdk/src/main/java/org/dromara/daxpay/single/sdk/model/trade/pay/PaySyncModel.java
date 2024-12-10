package org.dromara.daxpay.single.sdk.model.trade.pay;

import lombok.Data;
import org.dromara.daxpay.single.sdk.code.PayStatusEnum;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class PaySyncModel{

    /**
     * 退款订单同步后的状态状态
     * @see PayStatusEnum
     */
    private String orderStatus;

    /**
     * 是否触发了调整
     */
    private boolean adjust;

}
