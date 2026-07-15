package cn.daxpay.open.plugin.easypay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "易支付配置参数")
public class EasyPayConfigParam {

    @Schema(description = "主键")
    private Long id;

    @NotNull(message = "{validation.field.pid.notNull}")
    @Schema(description = "易支付商户号")
    private Integer pid;

    @Schema(description = "限制支付")
    private String limitPay;
}
