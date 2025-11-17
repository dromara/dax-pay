package org.dromara.daxpay.payment.unipay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "商户支付公共参数")
public abstract class MerchantPaymentCommonParam extends PaymentCommonParam{

    /** 商户号 */
    @Schema(description = "商户号")
    @NotBlank(message = "商户号不可为空")
    @Size(max = 32, message = "商户号不可超过32位")
    private String mchNo;

    /** 应用号 */
    @Schema(description = "应用号")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;

}
