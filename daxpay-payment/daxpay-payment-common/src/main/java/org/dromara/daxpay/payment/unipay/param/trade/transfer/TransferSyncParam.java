package org.dromara.daxpay.payment.unipay.param.trade.transfer;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 转账状态同步参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "转账状态同步参数")
public class TransferSyncParam extends MerchantPaymentCommonParam {

    /// 商户转账号
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账号
    @Size(max = 32, message = "{validation.field.transferNo.size}")
    @Schema(description = "转账号")
    private String transferNo;
}
