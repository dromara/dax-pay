package org.dromara.daxpay.payment.unipay.result.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账结果
///
@Data
@Accessors(chain = true)
@Schema(title = "转账结果")
public class TransferResult {

    /// 商户转账号
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账号
    @Schema(description = "转账号")
    private String transferNo;

    /// 转账参数, 用于拉起转账确认(微信)
    @Schema(description = "转账参数, 用于拉起转账确认(微信)")
    private String transferBody;

    /// 状态
    /// @see TransferStatusEnum
    @Schema(description = "状态")
    private String status;

    /// 提示信息
    @Schema(description = "提示信息")
    private String errorMsg;
}

