package cn.daxpay.open.payment.unipay.param.trade.alloc;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 分账同步参数(对外签名)
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "分账同步参数")
public class AllocSyncParam extends MerchantPaymentCommonParam {

    /// 平台分账单号
    @Schema(description = "平台分账单号")
    @Size(max = 100, message = "{validation.field.allocNo.size}")
    private String allocNo;

    /// 商户分账单号
    @Schema(description = "商户分账单号")
    @Size(max = 100, message = "{validation.field.bizAllocNo.size}")
    private String bizAllocNo;
}
