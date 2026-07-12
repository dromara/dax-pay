package cn.daxpay.open.payment.gateway.param;

import cn.daxpay.open.payment.unipay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 网关订单查询参数
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "网关订单查询参数")
public class GatewayOrderQueryParam extends PaymentCommonParam {

    @Schema(description = "平台网关单号")
    @Size(max = 64, message = "{validation.field.orderNo.size}")
    private String orderNo;

    @Schema(description = "商户业务单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    @Schema(description = "应用号")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;
}
