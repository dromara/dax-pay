package cn.daxpay.single.result.pay;

import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.code.RefundSyncStatusEnum;
import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static cn.daxpay.single.code.PaySyncStatusEnum.FAIL;

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
     * @see AllocOrderStatusEnum
     */
    @Schema(description = "支付网关同步状态")
    private String status = FAIL.getCode();

}
