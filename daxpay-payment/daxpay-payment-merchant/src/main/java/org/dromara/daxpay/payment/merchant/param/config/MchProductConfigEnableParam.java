package org.dromara.daxpay.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户产品配置启用状态参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户产品配置启用状态参数")
public class MchProductConfigEnableParam {

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    @Schema(description = "产品编码")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    @Schema(description = "通道编码")
    @NotBlank(message = "{validation.field.channel.notBlank}")
    private String channel;

    @Schema(description = "是否启用")
    private Boolean enable;
}
