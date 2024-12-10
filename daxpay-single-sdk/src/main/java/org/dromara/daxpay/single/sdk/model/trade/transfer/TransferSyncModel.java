
package org.dromara.daxpay.single.sdk.model.trade.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.daxpay.single.sdk.code.RefundStatusEnum;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class TransferSyncModel {

    /**
     * 转账订单同步后的状态状态
     * @see RefundStatusEnum
     */
    @Schema(description = "同步状态")
    private String orderStatus;

    /**
     * 是否触发了调整
     */
    @Schema(description = "是否触发了调整")
    private boolean adjust;

}
