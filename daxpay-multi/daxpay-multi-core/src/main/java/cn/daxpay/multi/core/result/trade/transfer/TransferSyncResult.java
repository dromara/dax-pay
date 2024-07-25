package cn.daxpay.multi.core.result.trade.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 转账同步结果
 * @author xxm
 * @since 2024/6/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "转账同步结果")
public class TransferSyncResult {

    /**
     * 转账状态
     * @see cn.daxpay.multi.core.enums.TransferStatusEnum
     */
    @Schema(description = "转账状态")
    private String status;
}
