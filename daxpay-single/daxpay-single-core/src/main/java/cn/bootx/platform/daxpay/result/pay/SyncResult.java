package cn.bootx.platform.daxpay.result.pay;

import cn.bootx.platform.daxpay.code.AllocationOrderStatusEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static cn.bootx.platform.daxpay.code.PaySyncStatusEnum.FAIL;

/**
 * 各种单据同步结果
 * @author xxm
 * @since 2023/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "同步结果")
public class SyncResult extends PaymentCommonResult {

    /**
     * 支付网关同步状态
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     * @see AllocationOrderStatusEnum
     */
    @Schema(description = "支付网关同步状态")
    private String gatewayStatus = FAIL.getCode();

    @Schema(description = "是否进行了修复")
    private Boolean repair;

    @Schema(description = "修复号")
    private String repairNo;

}
