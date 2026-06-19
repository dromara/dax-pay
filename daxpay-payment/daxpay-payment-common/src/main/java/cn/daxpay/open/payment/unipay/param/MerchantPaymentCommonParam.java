package cn.daxpay.open.payment.unipay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 商户支付公共参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "商户支付公共参数")
public abstract class MerchantPaymentCommonParam extends PaymentCommonParam{

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    /// 应用号
    @Schema(description = "应用号")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

}
