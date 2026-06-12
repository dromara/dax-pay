package org.dromara.daxpay.payment.unipay.param.trade.transfer;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 转账订单查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账订单查询参数")
public class QueryTransferParam extends MerchantPaymentCommonParam {

    /// 商户转账号
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账号
    @Size(max = 32, message = "{validation.field.transferNo.size}")
    @Schema(description = "转账号")
    private String transferNo;
}
