package cn.daxpay.single.result.sync;

import cn.daxpay.single.code.TransferStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账同步结果
 * @author xxm
 * @since 2024/6/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账同步结果")
public class TransferSyncResult  extends PaymentCommonResult {

    /**
     * 转账状态
     * @see TransferStatusEnum
     */
    @Schema(description = "转账状态")
    private String status;
}
