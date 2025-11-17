package org.dromara.daxpay.payment.unipay.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理商接口调用
 * @author xxm
 * @since 2025/8/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "代理商接口调用公共参数")
public abstract class AgentPaymentCommonParam extends PaymentCommonParam {

    /** 商户号 */
    @Schema(description = "代理商号")
    @NotBlank(message = "代理商号不可为空")
    @Size(max = 32, message = "代理商号不可超过32位")
    private String agentNo;

}
