package cn.daxpay.single.result.sync;

import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.code.RefundSyncStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static cn.daxpay.single.code.PaySyncStatusEnum.FAIL;

/**
 * 支付同步结果
 * @author xxm
 * @since 2023/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付同步结果")
public class PaySyncResult extends PaymentCommonResult {

    /**
     * 支付网关同步状态
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     */
    @Schema(description = "同步状态")
    private String status = FAIL.getCode();

}
