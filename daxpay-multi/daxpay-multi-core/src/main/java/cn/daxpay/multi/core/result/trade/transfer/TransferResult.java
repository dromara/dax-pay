package cn.daxpay.multi.core.result.trade.transfer;

import cn.daxpay.multi.core.enums.TransferStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 转账结果
 * @author xxm
 * @since 2024/6/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "转账结果")
public class TransferResult {

    /** 商户转账号 */
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @Schema(description = "转账号")
    private String transferNo;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private String errorMsg;
}
